package com.zerobase.plistbackend.common.app.config;

import com.zerobase.plistbackend.module.websocket.service.RedisChatPubSubService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  @Value("${spring.data.redis.password}")
  private String password;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(host);
    redisStandaloneConfiguration.setPort(port);
    redisStandaloneConfiguration.setPassword(password);
    return new LettuceConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean
  public RedisTemplate<?, ?> redisTemplate() {
    RedisTemplate<?, ?> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory());

    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());

    return template;
  }

  @Bean
  public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory, MessageListener messageListener) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(messageListener, new PatternTopic("chat"));
    return container;
  }

  @Bean
  public MessageListener messageListener(RedisChatPubSubService service) {
    return new MessageListenerAdapter(service, "onMessage");
  }
}
