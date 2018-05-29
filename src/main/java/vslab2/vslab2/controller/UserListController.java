package vslab2.vslab2.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vslab2.vslab2.service.ManageUsersService;

@Controller
public class UserListController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final int USERS_SEACH_PAGE_SIZE = 10;

    private final ManageUsersService service;
    private final Gson gson;

    @Autowired
    public UserListController(ManageUsersService service, Gson gson) {
        this.service = service;
        this.gson = gson;
    }

    @RequestMapping(value = "/userlist")
    public String getUserList() {
        return  "userlist";
    }

    @RequestMapping(value = "/api/usersearch/{searchPattern}")
    @ResponseBody
    public String searchForUsers(@PathVariable("searchPattern") String searchPattern) {
        log.info("????");
        log.info(gson.toJson(service.getUsersPageMatchingPattern(searchPattern, USERS_SEACH_PAGE_SIZE).toArray()));
        return gson.toJson(service.getUsersPageMatchingPattern(searchPattern, USERS_SEACH_PAGE_SIZE).toArray());
    }
}