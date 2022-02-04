package com.xii.demo.config;


import lombok.RequiredArgsConstructor;


import org.json.JSONObject;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xii.demo.redis.ChatRoomRepository;
import com.xii.demo.redis.RedisPublisher;
import com.xii.demo.vo.ChatMessage;
import com.xii.demo.vo.ChatMessage.MessageType;

@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler implements MessageListener{

    private static List<WebSocketSession> list = new ArrayList<>();
    // private Map<String, Object> channelMap = new HashMap<>();
    // private Map<String, Object> userMap = new HashMap<>();
    List<HashMap<String, Object>> roomSessionList = new ArrayList<>(); //웹소켓 세션을 담아둘 리스트 ---roomListSessions
    private final RedisPublisher redisPublisher;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;
    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
    private final RedisMessageListenerContainer redisMessageListener;
    private Map<String, ChannelTopic> topics = new HashMap<>();
    private final ObjectMapper objectMapper;
    private static final String FILE_UPLOAD_PATH = "C:/test/websocket/";
    private static int fileUploadIdx = 0;
    private static WebSocketSession fileUploadSession = null;

    /**
     * 텍스트 메세지 보낼때
     * TODO : 파일 보내기 실패 ^^ 
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();  //JSON형태의 String메시지
        System.out.println("payload : " + payload);

        JSONObject jObject = new JSONObject(payload);   //JSON데이터를 JSONObject로 파싱
        String type = jObject.get("type").toString();

        if(("INIT").equals(type)){
            //입장메세지
            ChatMessage enterMessage = new ChatMessage();
            enterMessage.setMessage(jObject.get("userName").toString()+" 님이 입장 하셨습니다.");
            enterMessage.setRoomId(jObject.get("roomId").toString());
            enterMessage.setSender(jObject.get("userName").toString());
            enterMessage.setType(MessageType.ENTER);
            redisPublisher.publish(getTopic(jObject.get("roomId").toString()), enterMessage);
            System.out.println("-----------");
        }else if (type.equals(("BYE"))) {
            // list = channelMap.get(roomId);
            // list.remove(roomId);            
        }else if (type.equals(("TALK"))) {
            ChatMessage talkMessage = new ChatMessage();
            talkMessage.setMessage(jObject.get("text").toString());
            talkMessage.setRoomId(jObject.get("roomId").toString());
            talkMessage.setSender(jObject.get("userName").toString());
            talkMessage.setType(MessageType.TALK);
            redisPublisher.publish(getTopic(jObject.get("roomId").toString()), talkMessage);
        }else if (type.equals(("FILE"))) {
            System.out.println("i'm in!");
        }

    }

    /*
    @Override
    public void handleBinaryMessage(WebSocketSession session, BinaryMessage message){
        //바이너리 메시지 발송
		ByteBuffer byteBuffer = message.getPayload();
        String fileName = "temp.jpg";

        File dir = new File(FILE_UPLOAD_PATH);
        if(!dir.exists()){
            dir.mkdirs();
        }

        File file = new File(FILE_UPLOAD_PATH, fileName);
        FileOutputStream pos = null;
		FileChannel fc = null;

        try{
            byteBuffer.flip(); //byteBuffer를 읽기 위해 세팅
			pos = new FileOutputStream(file, true); //생성을 위해 OutputStream을 연다.
			fc = pos.getChannel(); //채널을 열고
			byteBuffer.compact(); //파일을 복사한다.
			fc.write(byteBuffer); //파일을 쓴다.
        }catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pos != null) {
					pos.close();
				}
				if(fc != null) {
					fc.close();
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
        byteBuffer.position(0); //파일을 저장하면서 position값이 변경되었으므로 0으로 초기화한다.
		//파일쓰기가 끝나면 이미지를 발송한다.

        int fileUploadIdx = 0;
		HashMap<String, Object> temp = userMap.get(fileUploadIdx);
		for(String k : temp.keySet()) {
			if(k.equals("roomNumber")) {
				continue;
			}
			WebSocketSession wss = (WebSocketSession) temp.get(k);
			try {
				wss.sendMessage(new BinaryMessage(byteBuffer)); //초기화된 버퍼를 발송한다.
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
*/

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // redis에서 발행된 데이터를 받아 deserialize
            System.out.println("onMessage : " + message);
            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
            System.out.println("publishMessage : " + publishMessage);

            // ChatMessage 객채로 맵핑
            ChatMessage roomMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
            System.out.println("roomMessage : " + roomMessage.toString());

            String roomId = roomMessage.getRoomId();
            String type = roomMessage.getType().toString();
            String userId = roomMessage.getSender();

            HashMap<String, Object> temp = new HashMap<String, Object>();
            if(roomSessionList.size() > 0) {

                for(int i=0; i<roomSessionList.size(); i++) {
                    String roomName = (String) roomSessionList.get(i).get("roomId"); //세션리스트의 저장된 방ID를 가져와서
                    if(roomName.equals(roomId)) { //같은값의 방이 존재한다면
                        temp = roomSessionList.get(i); //해당 방번호의 세션리스트의 존재하는 모든 object값을 가져온다.
                        fileUploadIdx = i;
                        fileUploadSession = (WebSocketSession) temp.get(userId);
                        break;
                    }
                }

                if(!type.equals("FILE")) { //메시지의 타입이 파일업로드가 아닐때만 전송한다.
                    //해당 방의 세션들만 찾아서 메시지를 발송해준다.
                    for(String k : temp.keySet()) { 
                        if(k.equals("roomId")) { //다만 방번호일 경우에는 건너뛴다.
                            continue;
                        }
                        
                        WebSocketSession wss = (WebSocketSession) temp.get(k);
                        if(wss != null) {
                            try {
                                wss.sendMessage(new TextMessage(publishMessage));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

        
        } catch (Exception e) {
            // log.error(e.getMessage());
        }
    }

    /* Client 가 접속 시 호출되는 메서드*/
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String roomId = session.getUri().toString().split("roomId=")[1];
        roomId = roomId.split("&userId=")[0];
        String userId = session.getUri().toString().split("userId=")[1];

        System.out.println("[url] room ID : " + roomId + " / userId : " + userId );

        boolean flag = false;

        //subscriber...redis 구독..
        enterChatRoom(roomId);
        
        //check
        int roomIdx =roomSessionList.size(); //방의 사이즈를 조사한다.
        //방 번호 check
		if(roomSessionList.size() > 0) {
			for(int i=0; i<roomSessionList.size(); i++) {
				String roomName = (String) roomSessionList.get(i).get("roomId");
				if(roomName.equals(roomId)) {
					flag = true;
					roomIdx = i;
					break;
				}
			}
		}        
        if(flag) { //존재하는 방이라면 세션만 추가한다.
			HashMap<String, Object> map = roomSessionList.get(roomIdx);
			map.put(userId, session);
		}else { //최초 생성하는 방이라면 방ID와 세션을 추가한다.
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("roomId", roomId);
			map.put(userId, session);
			roomSessionList.add(map);
		}
		
        //세션등록이 끝나면 발급받은 세션ID값의 메시지를 발송한다.
		JSONObject obj = new JSONObject();
		obj.put("type", "INIT");
		obj.put("sessionId", session.getId());
        obj.put("userId", userId);
		session.sendMessage(new TextMessage( obj.toString() ));
      

    }

    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null) {
            topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(this, topic);
            topics.put(roomId, topic);
        }
    }


    /* Client가 접속 해제 시 호출되는 메서드드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        System.out.println(session + " 클라이언트 접속 해제");
        // list.remove(session);
        // for(int i = 0 ; i<list.size() ;i++){
        //     System.out.println("session [ " + i + " ]" + list.get(i));
        // }
        //소켓 종료
        HashMap<String, Object> map = new HashMap<String, Object>();
        String key = "";
		if(roomSessionList.size() > 0) { //소켓이 종료되면 해당 세션값들을 찾아서 지운다.
			for(int i=0; i<roomSessionList.size(); i++) {
				// roomSessionList.get(i).remove(session.getId());
                map = roomSessionList.get(i);
                Iterator<String> iterator = map.keySet().iterator();
                while(iterator.hasNext()){
                    key = (String)iterator.next(); // 키 = userID 얻기 
                    if(key.equals("roomId")) { //방번호일 경우에는 건너뛴다.
                        continue;
                    }
                    WebSocketSession userSess = (WebSocketSession) map.get(key);
                    if(userSess.equals(session)){
                        //session 제거
                        map.remove(key); 
                        break;
                    }
                }
                break;
            }
		}
        //TODO: clsoe시, 오류 Message will not be sent because the WebSocket session has been closed 
        //세션제거전 접속 user에게 BYE메세지를 보낸다.
        JSONObject obj = new JSONObject();
        obj.put("type", "BYE");
        obj.put("session", session);
        obj.put("userId", key);
        // session.sendMessage(new TextMessage( obj.toString() ));  
        System.out.println(session + " 클라이언트 세션 remove");    

    }




}