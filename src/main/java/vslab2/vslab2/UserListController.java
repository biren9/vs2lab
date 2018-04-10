package vslab2.vslab2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserListController {
    @RequestMapping(value = "/userlist")
    public String greetingSubmit() {
        return  "userlist";
    }
}