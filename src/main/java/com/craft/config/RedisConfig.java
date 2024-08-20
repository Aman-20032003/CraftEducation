package com.craft.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
@Configuration
@EnableCaching
public class RedisConfig {
	
	@Bean
	@Primary
	ReactiveRedisConnectionFactory connectionFactory() {
		return new LettuceConnectionFactory();
	}
	
	
	@Bean
 RedisTemplate<String,Object>redisTemplate(){
		RedisTemplate<String, Object>redisTemplate= new RedisTemplate<>();
		redisTemplate.setConnectionFactory((RedisConnectionFactory) connectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		return redisTemplate;
		
	}

}
