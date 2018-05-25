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
import vslab2.vslab2.entity.MessagePageEntity;
import vslab2.vslab2.service.AuthenticationService;
import vslab2.vslab2.service.ManageUsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class TweetWallController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final int messageCount = 5;

    private final Gson gson;

    private final ManageUsersService service;

    private final AuthenticationService authService;

    @Autowired
    public TweetWallController(Gson gson, ManageUsersService service, AuthenticationService authenticationService) {
        this.gson = gson;
        this.service = service;
        this.authService = authenticationService;
    }


    @RequestMapping(value = "/tweetWall/{username}", method = RequestMethod.GET)
    public String greetingSubmit(
            HttpSession session,
            @PathVariable String username,
            Model model,
            HttpServletRequest req)
    {
        List<MessageEntity> messageEntities = new ArrayList<>();
        List<String> messageSource;
        if (username.equals("global")) {
            messageSource = service.getGlobalTimelineMessages(0, messageCount);
        } else {
            messageSource = service.getTimelineMessages(username, 0, messageCount);
        }
        for (String message : messageSource) {
            messageEntities.add(gson.fromJson(message,MessageEntity.class));
        }
        model.addAttribute("messages", messageEntities );
        model.addAttribute("tweetWallOwner", username);
        return  "tweetWall";
    }

    @RequestMapping(value = "/api/messages/{username}", method = RequestMethod.POST)
    public void createTweet(@PathVariable String username, HttpServletResponse res, HttpServletRequest req, @RequestBody MessageEntity mess, Model model) {
        if (!authService.getAuthenticatedUserByRequest(req).equals(mess.getAuthor())) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        service.addMessage(mess);
        log.info("created tweet: " + mess);
    }

    @RequestMapping(value = "/api/messages/{username}/{page}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, method = RequestMethod.GET)
    @ResponseBody
    public String getTweets(@PathVariable String username, @PathVariable int page, HttpServletRequest req) {
        List<String> messages;
        Set<String> subscriptions = service.getSubs(authService.getAuthenticatedUserByRequest(req));
        if (username.equals("global")) {
            messages = service.getGlobalTimelineMessages(page*messageCount+1, (page+1)*messageCount);
        } else {

            messages = service.getTimelineMessages(username, page*messageCount+1, (page+1)*messageCount);

        }

        return gson.toJson(new MessagePageEntity(subscriptions, messages));
    }
}