package com.xii.demo.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xii.demo.redis.ChatRoomRepository;
import com.xii.demo.redis.RedisPublisher;
@Component
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private static List<WebSocketSession> list = new ArrayList<>();
    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("payload : " + payload);
        chatRoomRepository.enterChatRoom("f546f629-e327-4779-83ed-ea1cdc72b204");
        redisPublisher.publish(chatRoomRepository.getTopic("f546f629-e327-4779-83ed-ea1cdc72b204"), message);
/*
        JSONObject jObject  = new JSONObject(payload);
        String userName = jObject .getString("userName");
        int roomNo = jObject .getInt("roomNo");
        String text = jObject .getString("text");

        //TODO : 스트링으로 리턴 불가.
        String returnMsg = "[" + userName + "] " + text ; 
*/      
        for(WebSocketSession sess: list) {
            sess.sendMessage(message);
        }
    }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String,Object> map = new HashMap<>();
        
        list.add(session);

        System.out.println(session + " 클라이언트 접속");
        for(int i = 0 ; i<list.size() ;i++){
            System.out.println("session [ " + i + " ]" + list.get(i));
        }
        
        // List<String> userList = new ArrayList<>();
        // for(int i = 0 ; i < list.size() ; i++ ){
        //     userList.add(list.get(i).getId());
        // }
        // System.out.println("userList 접 목록" + userList);
        // TextMessage textMessage = new TextMessage(userList.toString());

        // handleMessage(session, textMessage);
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        System.out.println(session + " 클라이언트 접속 해제");
        list.remove(session);
        for(int i = 0 ; i<list.size() ;i++){
            System.out.println("session [ " + i + " ]" + list.get(i));
        }
    }
}