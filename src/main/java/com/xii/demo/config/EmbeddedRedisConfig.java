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
//         redisServer = new RedisServer(redisPort);
//         System.out.println("레디스 서버 start");
//         redisServer.start();
//     }

//     @PreDestroy
//     public void stopRedis() {
//         if (redisServer != null) {
//             redisServer.stop();
//         }
//     }
// }