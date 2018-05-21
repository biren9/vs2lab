package vslab2.vslab2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vslab2.vslab2.dbLayer.BitterDB;
import vslab2.vslab2.entity.MessageEntity;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@Service
public class ManageUsersService {

    @Autowired
    private BitterDB dao;

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void initIt() {
        dao.generateTestData();
    }

    public String registerUser(String username, String password) {
        dao.createUser(username, password);
        ObjectNode jsonResponse = objectMapper.createObjectNode();
        jsonResponse.put("result", "username" + username + "created");
        return jsonResponse.toString();
    }

    public List<String> getMessage(String username, long start, long stop) {
        return dao.getMessage(username, start, stop);
    }

    public void addMessage(String username, String text) {
        dao.addMessage(username, text);
    }

    public void addMessage(MessageEntity msg) {
        msg.setTimestamp(new Date());
        dao.addMessage(msg);
    }

}
