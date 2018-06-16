package vslab2.vslab2.service.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import vslab2.vslab2.service.SocketSenderService;

@Service
public class RedisTimelineSubscriber implements MessageListener {

    private static final int REDIS_PUB_SUB_PREFIX_END = 7;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final SocketSenderService socketSender;

    @Autowired
    public RedisTimelineSubscriber(SocketSenderService socketSender) {
        this.socketSender = socketSender;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String msg = message.toString().substring(REDIS_PUB_SUB_PREFIX_END);
        log.info(msg);
        socketSender.sendSocketMessage(msg);
    }
}
