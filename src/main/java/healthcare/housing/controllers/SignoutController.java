package healthcare.housing.controllers;

import healthcare.housing.models.Session;
import healthcare.housing.models.data.SessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("signout")
public class SignoutController {

    @Autowired
    private SessionDao sessionDao;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String signup (Model model, @CookieValue(name="ASID", required=false) String activeSessionId){
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
                    //delete session
                    sessionDao.delete(activeSession);
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
        model.addAttribute("title", "Signout");
        return "signout";
    }
}
