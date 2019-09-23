package healthcare.housing.controllers;

import healthcare.housing.models.Role;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String admin (Model model, RedirectAttributes attributes,
                         @CookieValue(name="ASID", required=false) String activeSessionId) {
        Iterable<Session> currentSessionList = sessionDao.findAll();
        String requestedUrl = "/admin";
        attributes.addFlashAttribute("requestedUrl", requestedUrl);
        //check if valid session
        if (Security.isValidSessionId(activeSessionId, currentSessionList)){
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            activeSession.refreshSession();
            sessionDao.save(activeSession);
            if (activeSession.getUser().getRole().getIntValue() > 1){
                attributes.addFlashAttribute("redirectMessage", Security.sessionNoPrivilege());
                return "redirect:/";
            }
            model.addAttribute("title", "Administration");
            model.addAttribute("activeSession", activeSession);
            return "admin";
        }
        //if session expires
        else if(Security.isSessionExpired(activeSessionId,currentSessionList)){
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            sessionDao.delete(activeSession);
            attributes.addFlashAttribute("loginMessage", Security.sessionTimeoutMessage());
            return "redirect:/login";
        }
        //if no active session
        else {
            attributes.addFlashAttribute("loginMessage", Security.sessionNoSessionMessage());
            return "redirect:/login";
        }
    }
    @RequestMapping(value = "user-maintenance", method = RequestMethod.GET)
    public String userMaintenance (Model model, RedirectAttributes attributes,
                                   @CookieValue(name="ASID", required=false) String activeSessionId) {
        List<User> viewableUsers = new ArrayList<>();
        Iterable<Session> currentSessionList = sessionDao.findAll();
        String requestedUrl = "/admin/user-maintenance";
        attributes.addFlashAttribute("requestedUrl", requestedUrl);
        //check if valid session
        if (Security.isValidSessionId(activeSessionId, currentSessionList)){
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            activeSession.refreshSession();
            sessionDao.save(activeSession);
            if (activeSession.getUser().getRole().getIntValue() > 1){
                attributes.addFlashAttribute("redirectMessage", Security.sessionNoPrivilege());
                return "redirect:/";
            }
            for (User user : userDao.findAll()) {
                if (user.getRole().getIntValue() > activeSession.getUser().getRole().getIntValue()) {
                    viewableUsers.add(user);
                }
            }
            model.addAttribute("users", viewableUsers);
            model.addAttribute("title", "User Maintenance");
            model.addAttribute("activeSession", activeSession);
            return "admin/user-maintenance";
        }
        //if session expires
        else if(Security.isSessionExpired(activeSessionId,currentSessionList)){
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            sessionDao.delete(activeSession);
            attributes.addFlashAttribute("loginMessage", Security.sessionTimeoutMessage());
            return "redirect:/login";
        }
        //if no active session
        else {
            attributes.addFlashAttribute("loginMessage", Security.sessionNoSessionMessage());
            return "redirect:/login";
        }
    }
    @RequestMapping(value = "modify-user/{userId}", method = RequestMethod.GET)
    public String modifyUserById (@PathVariable int userId, Model model, RedirectAttributes attributes,
                                  @CookieValue(name="ASID", required=false) String activeSessionId) {
        //set variables needed for security
        Iterable<Session> currentSessionList = sessionDao.findAll();
        String requestedUrl = "/admin/modify-user/" + userId;
        attributes.addFlashAttribute("requestedUrl", requestedUrl);
        //check if valid session
        if (Security.isValidSessionId(activeSessionId, currentSessionList)){
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            activeSession.refreshSession();
            sessionDao.save(activeSession);
            if (activeSession.getUser().getRole().getIntValue() > 1){
                attributes.addFlashAttribute("redirectMessage", Security.sessionNoPrivilege());
                return "redirect:/";
            }
            model.addAttribute("roles", Role.values());
            model.addAttribute("title", "Modify" + userId);
            User modifiedUser = userDao.findById(userId).get();
            model.addAttribute("activeSession", activeSession);
            model.addAttribute(modifiedUser);
            return "admin/modify-user";
        }
        //if session expires
        else if(Security.isSessionExpired(activeSessionId,currentSessionList)){
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            sessionDao.delete(activeSession);
            attributes.addFlashAttribute("loginMessage", Security.sessionTimeoutMessage());
            return "redirect:/login";
        }
        //if no active session
        else {
            attributes.addFlashAttribute("loginMessage", Security.sessionNoSessionMessage());
            return "redirect:/login";
        }
    }
}
