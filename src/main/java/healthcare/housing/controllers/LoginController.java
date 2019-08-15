package healthcare.housing.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("login")
public class LoginController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String login (Model model){
        model.addAttribute("title", "Login");
        return "login";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String processLogin (Model model){
        model.addAttribute("title", "Login");
        return "login";
    }
}
