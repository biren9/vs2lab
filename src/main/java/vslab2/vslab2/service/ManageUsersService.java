package vslab2.vslab2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vslab2.vslab2.dbLayer.BitterDB;

import java.util.List;

@Service
public class ManageUsersService {

    @Autowired
    private BitterDB dao;

    private ObjectMapper objectMapper = new ObjectMapper();

    public String registerUser(String username, String password) {
        dao.createUser(username, password);
        ObjectNode jsonResponse = objectMapper.createObjectNode();
        jsonResponse.put("result", "username" + username + "created");
        return jsonResponse.toString();
    }

    public List<String> getMessage(String username, long start, long stop) {
        return dao.getMessage(username, start, stop);
    }

}
