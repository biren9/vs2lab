package vslab2.vslab2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vslab2.vslab2.entity.RegisterRequestEntity;
import vslab2.vslab2.service.ManageUsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthenticationController {
    @Autowired
    ManageUsersService service;

    @RequestMapping(value = "/")
    private String authenticate() {

        return "login";
    }

    @RequestMapping(value = "/register")
    @ResponseBody
    private String register(@RequestBody RegisterRequestEntity registerRequestEntity, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
        String jsonResponse = service.registerUser(registerRequestEntity.getUsername(), registerRequestEntity.getPassword());
        return jsonResponse;
    }
}
