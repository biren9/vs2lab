package vslab2.vslab2.service.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import vslab2.vslab2.dbLayer.pubsub.MessagePublisher;

@Service
public class RedisTimelinePublisher implements MessagePublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    @Autowired
    public RedisTimelinePublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(String message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}

