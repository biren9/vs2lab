package vslab2.vslab2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vslab2.vslab2.config.properties.AuthProperties;
import vslab2.vslab2.dbLayer.BitterDB;

@Service
public class SocketSenderService {

    private final SimpMessagingTemplate template;
    private final BitterDB dao;
    private final AuthProperties authProperties;

    @Autowired
    public SocketSenderService(BitterDB dao, SimpMessagingTemplate template, AuthProperties authProperties) {
        this.template = template;
        this.dao = dao;
        this.authProperties = authProperties;
    }

    public void sendGlobalMessage(String message) {
        this.template.convertAndSend("/topic/global", message);
    }


}
