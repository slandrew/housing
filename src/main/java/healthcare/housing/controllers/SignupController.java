package healthcare.housing.controllers;

import healthcare.housing.models.User;
import healthcare.housing.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.Query;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("signup")
public class SignupController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String signup (Model model) {
        model.addAttribute("title", "Signup");
        model.addAttribute(new User());
        return "signup";
    }
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String signupForm (@ModelAttribute @Valid User newUser, Errors errors, Model model,
                              @RequestParam("password") String password1,
                              @RequestParam("password2") String password2) {
        //validation
        if (errors.hasErrors()) {
            model.addAttribute("title", "Signup");
            return "/signup";
        }
        for (User user : userDao.findAll()){
            //username taken
            if (user.getUsername().equals(newUser.getUsername())){
                model.addAttribute("usernameError", "Username already taken.");
                return "/signup";
            }
            //email taken
            if (user.getEmail().equals(newUser.getEmail())){
                if (user.getEmailVerified() == 1){
                    model.addAttribute("emailError", "Email already registered.");
                    return "/signup";}
                model.addAttribute("emailError", "Email already registered. Please check email for verification.");
                return "/signup";
            }
        }

        //if password mismatch
        if (password1.equals(password2) == false){
            model.addAttribute("passError", "Password mismatch!");
            return "/signup";
        }

        String passSalt = Security.saltPass();
        newUser.setPassSalt(passSalt);
        newUser.setPassHash(password1 + passSalt);
        userDao.save(newUser);
        return "redirect:";
    }
}
