package vslab2.vslab2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import vslab2.vslab2.service.ManageUsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {

    @Autowired
    private final ManageUsersService service;

    public ProfileController(ManageUsersService service) {
        this.service = service;
    }

    @RequestMapping(value = "/profile/{username}", method = RequestMethod.GET)
    public String showProfile(
            HttpSession session,
            @PathVariable String username,
            Model model,
            HttpServletRequest req)
    {
        model.addAttribute("userPage", username);
        model.addAttribute("follows", service.getSubs(username));
        model.addAttribute("followers", service.getFollowers(username));

        return  "profile";
    }
}
