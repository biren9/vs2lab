package vslab2.vslab2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import vslab2.vslab2.service.AuthenticationService;
import vslab2.vslab2.service.ManageUsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
public class AuthenticationController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ManageUsersService service;

    private final AuthenticationService authService;

    @Autowired
    public AuthenticationController(ManageUsersService service, AuthenticationService authService) {
        this.service = service;
        this.authService = authService;
    }

    @RequestMapping(value = "/")
    private String authenticate() {
        return "login";
    }

    @RequestMapping(value = "/login", method=RequestMethod.POST)
    private String register(@RequestBody MultiValueMap<String,String> formData, HttpServletRequest request,
                            HttpServletResponse response) {

        boolean isLogOnAction = formData.get("loginBtn") != null;
        boolean isRegisterAction = formData.get("registerBtn") != null;
        if (!(isLogOnAction || isRegisterAction)){
            return "error";
        }

        String username = formData.get("email").get(0);
        String password = formData.get("password").get(0);

        if (isRegisterAction) {
            if ((formData.get("email") == null) || (formData.get("password") == null)) {
                return "error";
            }
            if (username == null || password == null) {
                return "error";
            }
            service.registerUser(username, password);
            authService.handleLoginRequest(request, response, username, password);
            log.info("registered user: " + username +" with password: " + password);
            return "redirect:tweetWall/" + username;
        }
        //Login
        if (authService.handleLoginRequest(request, response, username, password)) {
            return "redirect:tweetWall/" + username;
        }
        return "login";
    }
}
