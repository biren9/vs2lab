package vslab2.vslab2.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vslab2.vslab2.service.ManageUsersService;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TweetWallController {

    @Autowired
    ManageUsersService service;
    ObjectMapper mapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/tweetWall")
    public String greetingSubmit(HttpSession session) {
        log.info((String) session.getAttribute("test"));
        return  "tweetWall";
    }

    @RequestMapping(value = "/tweet", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, method = RequestMethod.POST)
    @ResponseBody
    public String getTweets() {
        JsonNode node = mapper.createObjectNode();
        List<String> messages = service.getMessage("pknp", 0, 10);
        for(String message : messages) {
            ((ObjectNode) node).put(message, message);
        }
        return node.toString();
    }
}