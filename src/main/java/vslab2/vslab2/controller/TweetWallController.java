package vslab2.vslab2.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vslab2.vslab2.service.ManageUsersService;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TweetWallController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final int messageCount = 20;

    @Autowired
    ManageUsersService service;

    @RequestMapping(value = "/tweetWall/{username}")
    public String greetingSubmit(HttpSession session, @PathVariable String username, Model model) {
        model.addAttribute("messages", service.getMessage(username, 0, messageCount));
        log.info((String) session.getAttribute("test"));
        return  "tweetWall";
    }

    @RequestMapping(value = "/tweet", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, method = RequestMethod.POST)
    @ResponseBody
    public String getTweets(@RequestParam("page") int page) {
        List<String> messages = service.getMessage("pknp", page*messageCount+1, (page+1)*messageCount);
        return new Gson().toJson(messages);
    }
}