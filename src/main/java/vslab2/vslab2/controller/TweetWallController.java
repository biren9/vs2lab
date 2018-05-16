package vslab2.vslab2.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import vslab2.vslab2.entity.MessageEntity;
import vslab2.vslab2.service.ManageUsersService;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TweetWallController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Gson gson = new Gson();

    @Autowired
    ManageUsersService service;

    @RequestMapping(value = "/tweetWall/{username}")
    public String greetingSubmit(HttpSession session, @PathVariable String username, Model model) {
        List<MessageEntity> messageEntities = new ArrayList<>();
        for (String message : service.getMessage(username, 0, 20)) {
            messageEntities.add(gson.fromJson(message,MessageEntity.class));
        }
        model.addAttribute("messages", messageEntities );
        log.info((String) session.getAttribute("test"));
        return  "tweetWall";
    }

    @RequestMapping(value = "/tweet", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, method = RequestMethod.POST)
    @ResponseBody
    public String getTweets() {
        List<String> messages = service.getMessage("pknp", 0, 10);
        return new Gson().toJson(messages);
    }
}