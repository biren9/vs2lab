package vslab2.vslab2.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vslab2.vslab2.service.ManageUsersService;

@Controller
public class UserListController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final int USERS_SEARCH_PAGE_SIZE = 10;
    public static final int USERS_LIST_PAGE_SIZE = 12;

    private final ManageUsersService service;
    private final Gson gson;

    @Autowired
    public UserListController(ManageUsersService service, Gson gson) {
        this.service = service;
        this.gson = gson;
    }

    @RequestMapping(value = "/userlist/{pageNumber}")
    public String getUserList(Model model, @PathVariable("pageNumber") int pageNumber) {
        model.addAttribute("users", service.getUsersPageMatchingPattern(null, USERS_LIST_PAGE_SIZE, pageNumber).toArray());
        model.addAttribute("pageNumber", pageNumber);
        return  "userlist";
    }

    @RequestMapping(value = "/api/usersearch/{searchPattern}")
    @ResponseBody
    public String searchForUsers(@PathVariable("searchPattern") String searchPattern) {
        searchPattern = searchPattern.toLowerCase();
        return gson.toJson(service.getUsersPageMatchingPattern(searchPattern, USERS_SEARCH_PAGE_SIZE, 0).toArray());
    }
}