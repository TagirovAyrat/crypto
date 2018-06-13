package ru.airiva.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.telegram.telegrambots.ApiContext;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import ru.airiva.bot.KryptoPrideLongPolingBot;

@Configuration
@PropertySource(value = "classpath:bot.config.properties", encoding = "UTF-8")
@ComponentScan(value = {"ru.airiva"})
public class SubscriberAppConfig {
    @Bean
    KryptoPrideLongPolingBot kryptoPrideLongPolingBot() {
        return new KryptoPrideLongPolingBot(ApiContext.getInstance(DefaultBotOptions.class));
    }
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration("localhost", 6379);
        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration);
        return factory;
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
