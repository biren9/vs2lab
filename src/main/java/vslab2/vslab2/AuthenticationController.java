package vslab2.vslab2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthenticationController {
    @RequestMapping(value = "/")
    private String authenticate(@ModelAttribute Credentials credentials, Model model) {
        model.addAttribute("credentials", credentials != null ? credentials : new Credentials());
        return "login";
    }
}
