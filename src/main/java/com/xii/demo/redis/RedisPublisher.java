package com.xii.demo.redis;

import com.xii.demo.vo.ChatMessage;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}

/**
 * Redis 발행 서비스 구현
    채팅방에 입장하여 메시지를 작성하면 해당 메시지를 Redis Topic에 발행하는 기능의 서비스입니다. 
    이 서비스를 통해 메시지를 발행하면 대기하고 있던 redis 구독 서비스가 메시지를 처리합니다.
 */