package vslab2.vslab2.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vslab2.vslab2.entity.MessageEntity;
import vslab2.vslab2.service.ManageUsersService;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TweetWallController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final int messageCount = 5;
    private Gson gson = new Gson();

    @Autowired
    ManageUsersService service;

    @RequestMapping(value = "/tweetWall/{username}")
    public String greetingSubmit(HttpSession session, @PathVariable String username, Model model) {
        List<MessageEntity> messageEntities = new ArrayList<>();
        for (String message : service.getMessage(username, 0, messageCount)) {
            messageEntities.add(gson.fromJson(message,MessageEntity.class));
        }
        model.addAttribute("messages", messageEntities );
        log.info("session att: " + session.getAttribute("test"));
        return  "tweetWall";
    }

    @RequestMapping(value = "/tweetWall/{username}/{page}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, method = RequestMethod.GET)
    @ResponseBody
    public String getTweets(@PathVariable String username, @PathVariable int page) {
        List<String> messages = service.getMessage(username, page*messageCount+1, (page+1)*messageCount);
        return new Gson().toJson(messages);
    }
}