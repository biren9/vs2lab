package vslab2.vslab2.controller;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vslab2.vslab2.entity.FollowRequestEntity;
import vslab2.vslab2.service.AuthenticationService;
import vslab2.vslab2.service.ManageUsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class FollowController {

    private final ManageUsersService service;
    private final AuthenticationService authService;
    private final Gson gson;

    @Autowired
    public FollowController(AuthenticationService authService, ManageUsersService service, Gson gson) {
        this.authService = authService;
        this.service = service;
        this.gson = gson;
    }

    @RequestMapping(value = "/api/follow/{username}", method = RequestMethod.POST)
    public void followUser(
            @PathVariable String username,
            @RequestBody FollowRequestEntity followRequest,
            HttpServletRequest req,
            HttpServletResponse res) {
        if (!authService.getAuthenticatedUserByRequest(req).equals(username)) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        service.addSub(username, followRequest.getFollow());
    }

    @RequestMapping(value = "/api/unfollow/{username}", method = RequestMethod.POST)
    public void unfollowUser(
            @PathVariable String username,
            @RequestBody FollowRequestEntity unfollowRequest,
            HttpServletRequest req,
            HttpServletResponse res) {
        if (!authService.getAuthenticatedUserByRequest(req).equals(username)) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        service.delSub(username, unfollowRequest.getFollow());
    }

    @RequestMapping(value = "api/subscriptions/{username}", method = RequestMethod.GET)
    @ResponseBody
    public String getSubs(@PathVariable String username) {
        return gson.toJson(service.getSubs(username));
    }
}
