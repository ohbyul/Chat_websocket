package com.xii.demo.controller;


import lombok.RequiredArgsConstructor;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.xii.demo.redis.ChatRoomRepository;
import com.xii.demo.vo.ChatRoom;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    // @GetMapping("/room")
    // public String rooms(Model model) {
    //     return "/chat/room";
    // }

    @GetMapping("/rooms")
    public List<ChatRoom> room() {
        return chatRoomRepository.findAllRoom();
    }

    // @PostMapping("/room")
    // // @ResponseBody
    // public ChatRoom createRoom(@RequestBody String name) {
    //     System.out.println(name);
    //     ChatRoom chatRoom = chatRoomRepository.createChatRoom(name);
    //     return chatRoom;
    // }
    @GetMapping("/room")
    public ChatRoom createRoom(@RequestParam String name) {
        System.out.println("CHAT ROOM 생성");
        return chatRoomRepository.createChatRoom(name);
    }


    // @GetMapping("/room/enter/{roomId}")
    // public String roomDetail(Model model, @PathVariable String roomId) {
    //     model.addAttribute("roomId", roomId);
    //     return "/chat/roomdetail";
    // }

    @GetMapping("/room/{roomId}")
    // @GetMapping("/roomInfo")
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }
}