package healthcare.housing.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("signup")
public class SignupController {
    @RequestMapping("")
    public String signup (Model model) {
        model.addAttribute("title", "Signup");
        return "signup";
    }
}
