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
import java.util.Set;

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
        Set<String> sub = service.getSubs(username);
        sub.remove(username);
        Set<String> followers = service.getFollowers(username);
        followers.remove(username);

        model.addAttribute("userPage", username);
        model.addAttribute("follows", sub);
        model.addAttribute("followers", followers);

        return  "profile";
    }
}
