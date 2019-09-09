package healthcare.housing.controllers;

import healthcare.housing.models.Session;
import healthcare.housing.models.User;
import healthcare.housing.models.data.SessionDao;
import healthcare.housing.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Controller
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public String signup (@PathVariable int userId, Model model, @CookieValue(name="ASID", required=false) String activeSessionId) {
        if (activeSessionId.length() == 256){
            for (Session session : sessionDao.findAll()){
                if (session.getSessionId().equals(activeSessionId)){
                    Session activeSession = session;
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
        Optional<User> user = userDao.findById(userId);
        model.addAttribute("user", user);
        model.addAttribute("title", user.get().getFirstName() + "'s Profile");
        return "/profile";
    }
}
