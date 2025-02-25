package com.zerobase.plistbackend.common.app.config;

import com.zerobase.plistbackend.module.websocket.service.RedisChatPubSubService;
import com.zerobase.plistbackend.module.websocket.service.RedisNewUserEnterService;
import com.zerobase.plistbackend.module.websocket.service.RedisVideoControlService;
import com.zerobase.plistbackend.module.websocket.service.RedisVideoSyncService;
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
  @Qualifier("chatListener")
  public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory, @Qualifier("chatListener") MessageListener messageListener) {

    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(messageListener, new PatternTopic("chat"));
    return container;
  }

  @Bean
  @Qualifier("chatListener")
  public MessageListener chatMessageListener(RedisChatPubSubService service) {
    return new MessageListenerAdapter(service, "onMessage");
  }

  @Bean
  @Qualifier("videoSyncListener")
  public RedisMessageListenerContainer redisVideoSyncListenerContainer(RedisConnectionFactory connectionFactory, @Qualifier("videoSyncListener") MessageListener messageListener) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(messageListener, new PatternTopic("videoSync"));
    return container;
  }

  @Bean
  @Qualifier("videoSyncListener")
  public MessageListener videoSyncMessageListener(RedisVideoSyncService service) {
    return new MessageListenerAdapter(service, "onMessage");
  }

  @Bean
  @Qualifier("newUserWelcomeListener")
  public RedisMessageListenerContainer redisNewUserWelcomeListener(RedisConnectionFactory connectionFactory,@Qualifier("newUserWelcomeListener") MessageListener messageListener) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(messageListener, new PatternTopic("newUserEnter"));
    return container;
  }

  @Bean
  @Qualifier("newUserWelcomeListener")
  public MessageListener newUserWelcomeListener(RedisNewUserEnterService service) {
    return new MessageListenerAdapter(service, "onMessage");
  }

  @Bean
  @Qualifier("videoControlListener")
  public RedisMessageListenerContainer redisVideoControlListener(RedisConnectionFactory connectionFactory,@Qualifier("videoControlListener") MessageListener messageListener) {
    RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.addMessageListener(messageListener, new PatternTopic("videoControl"));
    return container;
  }

  @Bean
  @Qualifier("videoControlListener")
  public MessageListener videoControlListener(RedisVideoControlService service) {
    return new MessageListenerAdapter(service, "onMessage");
  }
}
