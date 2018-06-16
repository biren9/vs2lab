package vslab2.vslab2.config;

import jdk.jfr.events.SocketReadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import vslab2.vslab2.dbLayer.pubsub.MessagePublisher;
import vslab2.vslab2.service.SocketSenderService;
import vslab2.vslab2.service.pubsub.RedisTimelinePublisher;
import vslab2.vslab2.service.pubsub.RedisTimelineSubscriber;

@Configuration
public class RedisPubSubConfig {
    /**
     * Redis Pub/Sub settings.
     * -------------------------------------------------------------------------------------------------------------
     */
    private final RedisTemplate<String, Object> redisTemplate;
    private final JedisConnectionFactory jedisConnectionFactory;
    private final SocketSenderService socketSenderService;

    @Autowired
    public RedisPubSubConfig(RedisTemplate<String, Object> redisTemplate, JedisConnectionFactory jedisConnectionFactory, SocketSenderService socketSenderService) {
        this.redisTemplate = redisTemplate;
        this.jedisConnectionFactory = jedisConnectionFactory;
        this.socketSenderService = socketSenderService;
    }

    @Bean
    MessageListenerAdapter messageListener()
    {
        return new MessageListenerAdapter(new RedisTimelineSubscriber(socketSenderService));
    }

    @Bean
    MessagePublisher redisPublisher() {
        return new RedisTimelinePublisher(redisTemplate, topic());
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(messageListener(), topic());
        return container;
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("messageQueue");
    }
}
