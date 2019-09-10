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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String admin (Model model, @CookieValue(name="ASID", required=false) String activeSessionId) {
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
                    //redirect if not enough privileges
                    if (activeSession.getUser().getRole() > 1){
                        return "redirect:";
                    }
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
        //if no session
        else {
            return "redirect:/login";
        }
        model.addAttribute("title", "Administration");
        return "/admin";
    }
    @RequestMapping(value = "user-maintenance", method = RequestMethod.GET)
    public String userMaintenance (Model model, @CookieValue(name="ASID", required=false) String activeSessionId) {
        List viewableUsers = new ArrayList();
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
                    //redirect if not enough privileges
                    if (activeSession.getUser().getRole() > 1){
                        return "redirect:";
                    }
                    for (User user : userDao.findAll()){
                        if (user.getRole() > activeSession.getUser().getRole()){
                            viewableUsers.add(user);
                        }
                    }
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
        //if no session
        else {
            return "redirect:/login";
        }
        model.addAttribute("users", viewableUsers);
        model.addAttribute("title", "Administration");
        return "/admin/user-maintenance";
    }
    @RequestMapping(value = "modify-user/{userId}", method = RequestMethod.GET)
    public String signup (@PathVariable int userId, Model model, @CookieValue(name="ASID", required=false) String activeSessionId) {
        if (Security.isValidSessionId(activeSessionId, sessionDao.findAll())){
            Session activeSession = Security.getActiveSession(activeSessionId, sessionDao.findAll());
            User modifiedUser = userDao.findById(userId).get();
            model.addAttribute("activeSession", activeSession);
            model.addAttribute(modifiedUser);
            return "admin/modify-user";
        }
        //if no session
        else {
            return "redirect:/login";
        }
    }
}
