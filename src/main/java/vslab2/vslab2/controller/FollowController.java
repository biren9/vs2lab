package vslab2.vslab2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import vslab2.vslab2.entity.FollowRequestEntity;
import vslab2.vslab2.service.AuthenticationService;
import vslab2.vslab2.service.ManageUsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class FollowController {

    private final ManageUsersService service;
    private final AuthenticationService authService;

    @Autowired
    public FollowController(AuthenticationService authService, ManageUsersService service) {
        this.authService = authService;
        this.service = service;
    }

    @RequestMapping(value = "/follow/{username}", method = RequestMethod.POST)
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
}
