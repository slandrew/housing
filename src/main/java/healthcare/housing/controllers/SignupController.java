package healthcare.housing.controllers;

import healthcare.housing.models.Session;
import healthcare.housing.models.User;
import healthcare.housing.models.data.SessionDao;
import healthcare.housing.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("signup")
public class SignupController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SessionDao sessionDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String signup (Model model, @CookieValue(name="ASID", required=false) String activeSessionId) {
        //check to see if session cookie exists at appropriate length
        if (activeSessionId.length() == 256){
            //find session in list of sessions
            for (Session session : sessionDao.findAll()){
                //if found
                if (session.getSessionId().equals(activeSessionId)){
                    //assign to variable
                    Session activeSession = session;
                    //if session has expired
                    if (activeSession.getSessionEnd() < System.currentTimeMillis()){
                        //delete session and direct to login
                        sessionDao.delete(activeSession);
                        return "redirect:/login";
                    }
                    //reset expiration
                    activeSession.setSessionEnd();
                    //save to DB
                    sessionDao.save(activeSession);
                    //pass to view
                    model.addAttribute("activeSession", activeSession);
                }
                //cleanup (might be a bad place)
                if (session.getSessionEnd() < System.currentTimeMillis()){
                    if (session.getSessionEnd() < System.currentTimeMillis()) {
                        //delete session and direct to login
                        sessionDao.delete(session);
                    }
                }
            }
        }
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
