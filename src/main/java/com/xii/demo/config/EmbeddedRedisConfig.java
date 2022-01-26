// package com.xii.demo.config;

// import javax.annotation.PostConstruct;
// import javax.annotation.PreDestroy;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Profile;

// import redis.embedded.RedisServer;

// /**
//  * 로컬 환경일경우 내장 레디스가 실행됩니다.
//  */
// @Profile("local")
// @Configuration
// public class EmbeddedRedisConfig {

//     @Value("${spring.redis.port}")
//     private int redisPort;

//     private RedisServer redisServer;

//     @PostConstruct
//     public void redisServer() {

//         if(redisServer.ports().size()==0){
//             redisServer = new RedisServer(redisPort);
//             redisServer.start();
//             System.out.println("redis server start ! port : [" +redisPort+ "]");
//         }else{
//             System.out.println(redisServer.ports().get(0) + "already redis server on!");
//         }
        
//     }

//     @PreDestroy
//     public void stopRedis() {
//         if (redisServer != null) {
//             redisServer.stop();
//         }
//     }
// }