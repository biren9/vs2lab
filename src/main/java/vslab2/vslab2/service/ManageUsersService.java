package vslab2.vslab2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vslab2.vslab2.config.properties.AuthProperties;
import vslab2.vslab2.dbLayer.BitterDB;
import vslab2.vslab2.entity.MessageEntity;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ManageUsersService {

    private final BitterDB dao;

    private final AuthProperties authProperties;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ManageUsersService(BitterDB dao, AuthProperties authProperties) {
        this.dao = dao;
        this.authProperties = authProperties;
    }

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

    public List<String> getTimelineMessages(String username, long start, long stop) {
        return dao.getTimelineMessages(username, start, stop);
    }
    public List<String> getGlobalTimelineMessages(long start, long stop) {
        return dao.getGlobalTimelineMessages(start, stop);
    }

    public void addMessage(String username, String text) {
        dao.addMessage(username, text);
    }

    public void addMessage(MessageEntity msg) {
        msg.setTimestamp(new Date());
        dao.addMessage(msg);
    }

    public void addSub(String username, String subscribedUser) {
        dao.addSub(username, subscribedUser);
    }

    public void delSub(String username, String subscribedUser) {
        dao.removeSub(username, subscribedUser);
    }

    public Set<String> getSubs(String username) {
        return dao.getSubs(username);
    }

    public Set<String> getFollowers(String username) {
        return dao.getFollowers(username);
    }

    public void deleteSession(String sessionToken) {
        dao.deleteSession(sessionToken);
    }

    /**
     * see {@link vslab2.vslab2.dbLayer.BitterDBImpl#getUsersPageMatchingPattern(String, int)}
     */
    public Set<String> getUsersPageMatchingPattern(String pattern, int pageSize) {
        return dao.getUsersPageMatchingPattern(pattern, pageSize);
    }
}
