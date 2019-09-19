package healthcare.housing.controllers;

import healthcare.housing.models.Posting;
import healthcare.housing.models.Session;
import healthcare.housing.models.User;
import healthcare.housing.models.data.PostingDao;
import healthcare.housing.models.data.SessionDao;
import healthcare.housing.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostingDao postingDao;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public String signup (@PathVariable int userId, RedirectAttributes attributes,
                          Model model, @CookieValue(name="ASID", required=false) String activeSessionId) {
        Iterable<Session> currentSessionList = sessionDao.findAll();
        String requestedUrl = "/profile/" + userId;
        attributes.addFlashAttribute("requestedUrl", requestedUrl);
        //check if valid session
        if (Security.isValidSessionId(activeSessionId, currentSessionList)) {
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            activeSession.refreshSession();
            sessionDao.save(activeSession);
            if (activeSession.getUser().getRole() > 2) {
                attributes.addFlashAttribute("redirectMessage", Security.sessionNoPrivilege());
                return "redirect:/";
            }
            model.addAttribute("activeSession", activeSession);
            Optional<User> viewedUser = userDao.findById(userId);
            model.addAttribute("viewedUser", viewedUser);
            List<Posting> viewedUserPostings = new ArrayList<>();
            for (Posting posting : postingDao.findAll()){
                if (posting.getUser().getId() == userId){
                    viewedUserPostings.add(posting);
                }
            }
            model.addAttribute("viewedUserPostings", viewedUserPostings);
            model.addAttribute("title", viewedUser.get().getFirstName() +
                    " " + viewedUser.get().getLastName() + "'s Profile");
            if (viewedUser.get().getId() == activeSession.getUser().getId()){
                model.addAttribute("ownProfile", true);
            }
            return "profile";
        }
        //if session expires
        else if (Security.isSessionExpired(activeSessionId, currentSessionList)) {
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
