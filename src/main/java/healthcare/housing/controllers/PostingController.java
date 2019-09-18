package healthcare.housing.controllers;


import healthcare.housing.models.Posting;
import healthcare.housing.models.Session;
import healthcare.housing.models.data.SessionDao;
import healthcare.housing.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("posting")
public class PostingController {

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "new-posting", method = RequestMethod.GET)
    public String newPosting (@CookieValue(name="ASID", required=false) String activeSessionId, RedirectAttributes attributes,
                              Model model) {
        Iterable<Session> currentSessionList = sessionDao.findAll();
        String requestedUrl = "/posting/new-posting";
        attributes.addFlashAttribute("requestedUrl", requestedUrl);
        //check if valid session
        if (Security.isValidSessionId(activeSessionId, currentSessionList)){
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            activeSession.refreshSession();
            sessionDao.save(activeSession);
            if (activeSession.getUser().getRole() > 2){
                attributes.addFlashAttribute("redirectMessage", Security.sessionNoPrivilege());
                return "redirect:/";
            }
            model.addAttribute(new Posting());
            model.addAttribute("title", "New Posting");
            model.addAttribute("activeSession", activeSession);
            return "posting/new-posting";
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
