package healthcare.housing.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("search")

public class SearchController {

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String returnResults(Model model){
        model.addAttribute("title", "Login");
        return "search";
    }
}
