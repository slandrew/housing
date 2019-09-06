package healthcare.housing.controllers;

import healthcare.housing.models.User;
import healthcare.housing.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String login (Model model){
        model.addAttribute("title", "Login");
        return "login";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String processLogin (Model model, @RequestParam("email") String email, @RequestParam("password") String password){
        for (User user : userDao.findAll()){
            if (user.getEmail().equals(email)){
                User loggngUser = user;
                //verify password
                if (Security.hashPass((password + user.getPassSalt())).equals(user.getPassHash())){
                    model.addAttribute("loginMessage", "Login successful!");
                    return "login";
                }
            }
        }
        model.addAttribute("loginMessage", "Incorrect email or password.");
        return "login";
    }
}
