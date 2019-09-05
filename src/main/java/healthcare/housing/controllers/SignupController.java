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

import javax.validation.Valid;

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

        if (errors.hasErrors()) {
            model.addAttribute("title", "Signup");
            return "/signup";
        }
        //if username exists

        //if email exists or is not yet verified

        //if password mismatch
        if (password1.equals(password2) == false){
            return "/signup";
        }

        newUser.setPassHash(password1);
        newUser.setPassSalt();
        userDao.save(newUser);
        return "redirect:";
    }
}
