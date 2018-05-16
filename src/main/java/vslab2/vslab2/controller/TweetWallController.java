package vslab2.vslab2.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TweetWallController {

    ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/tweetWall")
    public String greetingSubmit() {
        return  "tweetWall";
    }

    @RequestMapping(value = "/tweet", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, method = RequestMethod.POST)
    @ResponseBody
    public String getTweets() {
        JsonNode node = mapper.createObjectNode();
        return node.toString();
    }
}