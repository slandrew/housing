package healthcare.housing.controllers;

import healthcare.housing.models.Session;
import healthcare.housing.models.data.SessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private SessionDao sessionDao;

    @RequestMapping("")
    public String index (Model model, @CookieValue(name="ASID", required=false) String activeSessionId) {
        Iterable<Session> currentSessionList = sessionDao.findAll();
        //check if valid session
        if (Security.isValidSessionId(activeSessionId, currentSessionList)) {
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            activeSession.refreshSession();
            sessionDao.save(activeSession);
            model.addAttribute("title", "Healthcare Housing");
            model.addAttribute("activeSession", activeSession);
        }
        //if session expires
        else if (Security.isSessionExpired(activeSessionId, currentSessionList)) {
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            sessionDao.delete(activeSession);
            model.addAttribute("redirectMessage", Security.sessionTimeoutMessage());
        }
        return "index";
    }
}
