package vslab2.vslab2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import vslab2.vslab2.service.ManageUsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
public class AuthenticationController {
    private static final String SUBTLE_ERROR_MESSAGE = "either our server is on fire or you did something unexpected :/";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ManageUsersService service;

    @RequestMapping(value = "/")
    private String authenticate() {

        return "login";
    }

    @RequestMapping(value = "/login", method=RequestMethod.POST)
    private String register(@RequestBody MultiValueMap<String,String> formData, HttpServletRequest request,
                            HttpServletResponse response, HttpSession session) {
        response.setStatus(HttpServletResponse.SC_OK);
        session.setAttribute("test", "test");
        boolean isLogOnAction = formData.get("loginBtn") != null;
        boolean isRegisterAction = formData.get("registerBtn") != null;
        if (!(isLogOnAction || isRegisterAction)){
            return SUBTLE_ERROR_MESSAGE;
        }

        if (isRegisterAction) {
            if ((formData.get("email") == null) || (formData.get("password") == null)) {
                return SUBTLE_ERROR_MESSAGE;
            }
            String username = formData.get("email").get(0);
            String password = formData.get("password").get(0);
            if (username == null || password == null) {
                return SUBTLE_ERROR_MESSAGE;
            }
            service.registerUser(username, password);
            log.info("registered user: " + username +" with password: " + password);
            return "tweetWall";
        }
        return "tweetWall";
    }
}
