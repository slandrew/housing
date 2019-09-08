package healthcare.housing.controllers;

import healthcare.housing.models.Session;
import healthcare.housing.models.User;
import healthcare.housing.models.data.SessionDao;
import healthcare.housing.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SessionDao sessionDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String login (Model model){
        model.addAttribute("title", "Login");
        return "login";
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public String processLogin (Model model, @RequestParam("email") String email, @RequestParam("password") String password,
                                HttpServletResponse response){
        for (User user : userDao.findAll()){
            if (user.getEmail().equals(email)){
                User loggingUser = user;
                //verify password
                if (Security.hashPass((password + user.getPassSalt())).equals(user.getPassHash())){
                    Session activeSession = new Session();
                    activeSession.setUser(user);
                    //todocheck for duplicate session Ids
                    sessionDao.save(activeSession);
                    response.addCookie(new Cookie("activeSession", activeSession.getSessionId()));
                    model.addAttribute("activeSession", activeSession);
                    model.addAttribute("loginMessage", "Login successful!");
                    return "login";
                }
            }
        }
        model.addAttribute("loginMessage", "Incorrect email or password.");
        return "login";
    }
}
