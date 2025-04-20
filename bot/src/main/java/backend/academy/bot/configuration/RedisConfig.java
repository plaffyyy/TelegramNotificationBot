package backend.academy.bot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    // Step 1: Create Redis connection configuration
    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        return config;
    }

    // Step 2: Create Redis connection factory
    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisStandaloneConfiguration config) {
        return new LettuceConnectionFactory(config);
    }

    // Step 3: Create RedisTemplate with proper serialization
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        
        // Set the connection factory
        template.setConnectionFactory(connectionFactory);
        
        // Configure key serializer (String)
        template.setKeySerializer(new StringRedisSerializer());
        
        // Configure value serializer (JSON)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        // Configure hash key serializer (String)
        template.setHashKeySerializer(new StringRedisSerializer());
        
        // Configure hash value serializer (JSON)
        // используется для того, чтобы хранить доп информацию о кэше, пока не используется
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        // Initialize the template
        template.afterPropertiesSet();
        
        return template;
    }
}