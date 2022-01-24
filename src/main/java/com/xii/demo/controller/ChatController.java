package com.xii.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class ChatController {
    
    @GetMapping("/chat")
    public String chatGET(){

        System.out.println("@ChatController, chat GET()");
        
        return "chat";
    }
}