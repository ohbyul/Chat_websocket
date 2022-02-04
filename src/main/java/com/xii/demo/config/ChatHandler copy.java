// package com.xii.demo.config;


// import lombok.RequiredArgsConstructor;


// import org.json.JSONObject;
// import org.springframework.data.redis.connection.Message;
// import org.springframework.data.redis.connection.MessageListener;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.listener.ChannelTopic;
// import org.springframework.data.redis.listener.RedisMessageListenerContainer;
// import org.springframework.messaging.simp.SimpMessageSendingOperations;
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.BinaryMessage;
// import org.springframework.web.socket.CloseStatus;
// import org.springframework.web.socket.TextMessage;
// import org.springframework.web.socket.WebSocketSession;
// import org.springframework.web.socket.handler.TextWebSocketHandler;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import java.io.File;
// import java.io.FileOutputStream;
// import java.io.IOException;
// import java.nio.ByteBuffer;
// import java.nio.channels.FileChannel;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Iterator;
// import java.util.List;
// import java.util.Map;
// import java.util.Map.Entry;

// import com.xii.demo.redis.ChatRoomRepository;
// import com.xii.demo.redis.RedisPublisher;
// import com.xii.demo.vo.ChatMessage;
// import com.xii.demo.vo.ChatMessage.MessageType;

// @Component
// @RequiredArgsConstructor
// public class ChatHandler extends TextWebSocketHandler implements MessageListener{

//     private static List<WebSocketSession> list = new ArrayList<>();
//     // private Map<String, List<WebSocketSession>> channelMap = new HashMap<>();
//     private Map<String, Object> channelMap = new HashMap<>();
//     private Map<String, Object> userMap = new HashMap<>();
//     private final RedisPublisher redisPublisher;
//     private final RedisTemplate redisTemplate;
//     private final SimpMessageSendingOperations messagingTemplate;
//     // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
//     private final RedisMessageListenerContainer redisMessageListener;
//     private Map<String, ChannelTopic> topics = new HashMap<>();
//     private final ObjectMapper objectMapper;
//     private static final String FILE_UPLOAD_PATH = "C:/test/websocket/";
//     /**
//      * 텍스트 메세지 보낼때
//      */
//     @Override
//     protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//         String payload = message.getPayload();
//         System.out.println("payload : " + payload);

//         JSONObject jObject = new JSONObject(payload);
//         String type = jObject.get("type").toString();
//         String roomId = jObject.get("roomId").toString();
//         if(("INIT").equals(type)){
//             // if (channelMap.get(roomId) == null) {
//             //     list = new ArrayList<>();
//             // }
//             // else {
//             //     list = channelMap.get(roomId);
//             // }
//             //list.add(session);
//             // channelMap.put(roomId, list);
//             //입장메세지
//             ChatMessage enterMessage = new ChatMessage();
//             enterMessage.setMessage(jObject.get("userName").toString()+" 님이 입장 하셨습니다.");
//             enterMessage.setRoomId(jObject.get("roomId").toString());
//             enterMessage.setSender(jObject.get("userName").toString());
//             enterMessage.setType(MessageType.ENTER);
//             redisPublisher.publish(getTopic(jObject.get("roomId").toString()), enterMessage);
//             System.out.println("-----------");
//         }else if (type.equals(("BYE"))) {
//             // list = channelMap.get(roomId);
//             // list.remove(roomId);            
//         }else {
//             //메세지 보내기 type = TALK 
//             ChatMessage talkMessage = new ChatMessage();
//             talkMessage.setMessage(jObject.get("text").toString());
//             talkMessage.setRoomId(jObject.get("roomId").toString());
//             talkMessage.setSender(jObject.get("userName").toString());
//             talkMessage.setType(MessageType.TALK);
//             redisPublisher.publish(getTopic(jObject.get("roomId").toString()), talkMessage);
            
//         }
        
//         // for(WebSocketSession sess: list) {
//         //     sess.sendMessage(message);
//         // }
//     }

//     /*
//     @Override
//     public void handleBinaryMessage(WebSocketSession session, BinaryMessage message){
//         //바이너리 메시지 발송
// 		ByteBuffer byteBuffer = message.getPayload();
//         String fileName = "temp.jpg";

//         File dir = new File(FILE_UPLOAD_PATH);
//         if(!dir.exists()){
//             dir.mkdirs();
//         }

//         File file = new File(FILE_UPLOAD_PATH, fileName);
//         FileOutputStream pos = null;
// 		FileChannel fc = null;

//         try{
//             byteBuffer.flip(); //byteBuffer를 읽기 위해 세팅
// 			pos = new FileOutputStream(file, true); //생성을 위해 OutputStream을 연다.
// 			fc = pos.getChannel(); //채널을 열고
// 			byteBuffer.compact(); //파일을 복사한다.
// 			fc.write(byteBuffer); //파일을 쓴다.
//         }catch(Exception e) {
// 			e.printStackTrace();
// 		}finally {
// 			try {
// 				if(pos != null) {
// 					pos.close();
// 				}
// 				if(fc != null) {
// 					fc.close();
// 				}
// 			}catch (IOException e) {
// 				e.printStackTrace();
// 			}
// 		}
//         byteBuffer.position(0); //파일을 저장하면서 position값이 변경되었으므로 0으로 초기화한다.
// 		//파일쓰기가 끝나면 이미지를 발송한다.

//         int fileUploadIdx = 0;
// 		HashMap<String, Object> temp = userMap.get(fileUploadIdx);
// 		for(String k : temp.keySet()) {
// 			if(k.equals("roomNumber")) {
// 				continue;
// 			}
// 			WebSocketSession wss = (WebSocketSession) temp.get(k);
// 			try {
// 				wss.sendMessage(new BinaryMessage(byteBuffer)); //초기화된 버퍼를 발송한다.
// 			} catch (IOException e) {
// 				e.printStackTrace();
// 			}
// 		}
//     }
// */

//     public ChannelTopic getTopic(String roomId) {
//         return topics.get(roomId);
//     }


//     @Override
//     public void onMessage(Message message, byte[] pattern) {
//         try {
//             // redis에서 발행된 데이터를 받아 deserialize
//             System.out.println("onMessage : " + message);
//             String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
//             System.out.println("publishMessage : " + publishMessage);

//             // ChatMessage 객채로 맵핑
//             ChatMessage roomMessage = objectMapper.readValue(publishMessage, ChatMessage.class);
//             System.out.println("roomMessage : " + roomMessage.toString());

//             String roomId = roomMessage.getRoomId();
//             Map<String, Object> userList = (Map<String, Object>) channelMap.get(roomId);
//             list = new ArrayList<>();

//             Iterator<String> iterator = userList.keySet().iterator();
//             while(iterator.hasNext()){
//                 String key = (String)iterator.next(); // 키 = userID 얻기
//                 WebSocketSession userSess = (WebSocketSession) userList.get(key);
//                 list.add(userSess);
//             }

//             for(WebSocketSession sess: list) {
//                 sess.sendMessage( new TextMessage(publishMessage));
//             }


//             // Websocket 구독자에게 채팅 메시지 Send
//             // messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getRoomId(), roomMessage);
//             // messagingTemplate.convertAndSend("/sub/chat/room/" + "ROOMID", "ROOMMESSAGE");
//         } catch (Exception e) {
//             //log.error(e.getMessage());
//         }
//     }

//     /* Client 가 접속 시 호출되는 메서드*/
//     @Override
//     public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//         String roomId = session.getUri().toString().split("roomId=")[1];
//         roomId = roomId.split("&userId=")[0];
//         String userId = session.getUri().toString().split("userId=")[1];

//         System.out.println("url...room ID : " + roomId + " / userId : " + userId );

//         //subscriber...redis 구독..
//         enterChatRoom(roomId);
        
        
//         //check
//         System.out.println("[세션 확인 전] channelMap size : " + channelMap.size());
//         System.out.println("[세션 확인 전] userMap size : " + userMap.size());
//         int idx = 0 ; //채팅 채널의 접속 세션 사이즈

//         userMap = new HashMap<>();

//         if (channelMap.get(roomId) != null) {
//             userMap = (Map<String, Object>) channelMap.get(roomId);
//             //동일여부 체크
//             if(userMap.get(userId) != null){
//                 if(userMap.get(userId).equals(session)){
//                     System.out.println("같은세션 존재");       
//                 }else{
//                     System.out.println("다른세션 존재 / 새로고침이나 채널이동 등으로 발생");
//                 }
//             }
//         }

//         userMap.put(userId, session);
//         channelMap.put(roomId, userMap);
//         idx = userMap.size();
//         System.out.println("[세션 확인 후] userMap size : " + userMap.size());

//         System.out.println("[세션 확인 후] channelMap size : " + channelMap.size());








//         // list.add(session);

//         // System.out.println(session + " 클라이언트 접속");
//         // for(int i = 0 ; i<list.size() ;i++){
//         //     System.out.println("session [ " + i + " ]" + list.get(i));
//         // }
        
//         //...여기다 sub하고 싶은데..

//         // List<String> userList = new ArrayList<>();
//         // for(int i = 0 ; i < list.size() ; i++ ){
//         //     userList.add(list.get(i).getId());
//         // }
//         // System.out.println("userList 접 목록" + userList);
//         // TextMessage textMessage = new TextMessage(userList.toString());

//         // handleMessage(session, textMessage);
//     }

//     public void enterChatRoom(String roomId) {
//         ChannelTopic topic = topics.get(roomId);
//         if (topic == null) {
//             topic = new ChannelTopic(roomId);
//             redisMessageListener.addMessageListener(this, topic);
//             topics.put(roomId, topic);
//         }
//     }


//     /* Client가 접속 해제 시 호출되는 메서드드 */
//     @Override
//     public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

//         System.out.println(session + " 클라이언트 접속 해제");
//         list.remove(session);
//         for(int i = 0 ; i<list.size() ;i++){
//             System.out.println("session [ " + i + " ]" + list.get(i));
//         }
//     }
// }