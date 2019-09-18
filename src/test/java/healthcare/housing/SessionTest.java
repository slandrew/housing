package healthcare.housing;

public class SessionTest {
    Iterable<Session> currentSessionList = sessionDao.findAll();
    String requestedUrl = "PATH OF PAGE";
        attributes.addFlashAttribute("requestedUrl", requestedUrl);
    //check if valid session
        if (Security.isValidSessionId(activeSessionId, currentSessionList)){
        Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
        activeSession.refreshSession();
        sessionDao.save(activeSession);
        if (activeSession.getUser().getRole() > 1){
            attributes.addFlashAttribute("redirectMessage", Security.sessionNoPrivilege());
            return "redirect:/";
        }
        model.addAttribute("title", "INSERT TITLE HERE");
        model.addAttribute("activeSession", activeSession);
        return "NAME OF VIEW";
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
