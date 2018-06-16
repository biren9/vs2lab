package vslab2.vslab2.service;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vslab2.vslab2.config.properties.AuthProperties;
import vslab2.vslab2.dbLayer.BitterDB;
import vslab2.vslab2.entity.MessageEntity;

@Service
public class SocketSenderService {

    private final SimpMessagingTemplate template;
    private final Gson gson;

    @Autowired
    public SocketSenderService(SimpMessagingTemplate template, Gson gson) {
        this.template = template;
        this.gson = gson;
    }

    /**
     * Send a Bitter message over socket connection
     * @param message Bitter message. Serialized json message.
     */
    public void sendSocketMessage(String message) {
        String topic = gson.fromJson(message, MessageEntity.class).getAuthor();
        this.template.convertAndSend("/topic/" + topic , message);
    }
}
