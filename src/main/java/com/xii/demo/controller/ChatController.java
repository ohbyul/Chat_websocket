// package com.xii.demo.controller;

// import lombok.RequiredArgsConstructor;

// import com.xii.demo.redis.ChatRoomRepository;
// import com.xii.demo.redis.RedisPublisher;
// import com.xii.demo.vo.ChatMessage;
// import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.stereotype.Controller;

// @RequiredArgsConstructor
// @Controller
// public class ChatController {

//     private final RedisPublisher redisPublisher;
//     private final ChatRoomRepository chatRoomRepository;

//     /**
//      * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
//      */
//     @MessageMapping("/chat/message")
//     public void message(ChatMessage message) {
//         if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
//             chatRoomRepository.enterChatRoom(message.getRoomId());
//             message.setMessage(message.getSender() + "님이 입장하셨습니다.");
//         }
//         // Websocket에 발행된 메시지를 redis로 발행한다(publish)
//         //redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
//         redisPublisher.publish(chatRoomRepository.getTopic("f546f629-e327-4779-83ed-ea1cdc72b204"), message);
//         //"f546f629-e327-4779-83ed-ea1cdc72b204"
//     }
// }


// // @Controller
// // @Log4j2
// // public class ChatController {
    
// //     @GetMapping("/chat")
// //     public String chatGET(){

// //         System.out.println("@ChatController, chat GET()");
        
// //         return "chat";
// //     }
// // }
