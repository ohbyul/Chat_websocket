package com.xii.demo.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
 
    @GetMapping("/test")
    public Map<String, Object> enter() {
    //https://1nnovator.tistory.com/48?category=784056
        System.out.println("================call api");
        Map<String, Object> map = new HashMap<>();
        map.put("resultData", "aa");
        map.put("resultCode", 200);
        return map;
    }

}