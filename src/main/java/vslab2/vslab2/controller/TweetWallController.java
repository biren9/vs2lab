package vslab2.vslab2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TweetWallController {
    @RequestMapping(value = "/tweetWall")
    public String greetingSubmit() {
        return  "tweetWall";
    }
}