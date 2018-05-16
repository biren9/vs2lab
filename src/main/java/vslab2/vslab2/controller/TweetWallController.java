package vslab2.vslab2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class TweetWallController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @RequestMapping(value = "/tweetWall")
    public String greetingSubmit(HttpSession session) {
        log.info((String) session.getAttribute("test"));
        return  "tweetWall";
    }
}