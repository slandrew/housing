package healthcare.housing.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping("")
    public String index (Model model) {
        model.addAttribute("title", "Healthcare Housing");
        return "index";
    }
}
