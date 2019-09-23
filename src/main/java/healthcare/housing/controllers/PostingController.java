package healthcare.housing.controllers;


import healthcare.housing.models.Image;
import healthcare.housing.models.Posting;
import healthcare.housing.models.Session;
import healthcare.housing.models.data.PostingDao;
import healthcare.housing.models.data.SessionDao;
import healthcare.housing.models.data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

@Controller
@RequestMapping("posting")
public class PostingController {


    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostingDao postingDao;

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
    @RequestMapping(value = "new-posting", method = RequestMethod.POST)
    public String newPostingFormProcess (@ModelAttribute @Valid Posting newPosting, @CookieValue(name="ASID", required=false) String activeSessionId,
                                         RedirectAttributes attributes, Model model, @RequestParam("postingUserId") int postingUserId,
                                         @RequestParam("uploadPic")MultipartFile uploadPic) {
        Iterable<Session> currentSessionList = sessionDao.findAll();
        String requestedUrl = "/posting/new-posting";
        attributes.addFlashAttribute("requestedUrl", requestedUrl);
        //check if valid session
        if (Security.isValidSessionId(activeSessionId, currentSessionList)){
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            activeSession.refreshSession();
            sessionDao.save(activeSession);
            if (activeSession.getUser().getRole() > 2 || activeSession.getUser().getId() != postingUserId){
                attributes.addFlashAttribute("redirectMessage", Security.sessionNoPrivilege());
                return "redirect:/";
            }
            //TODO Refractor into maybe a class or interface
            if (!uploadPic.isEmpty()){
                String folder = "/images/" + activeSession.getUser().getId() + "/";
                Image uploadedImage = null;
                try {
                    uploadedImage = new Image(uploadPic, folder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                newPosting.addPictureURL(uploadedImage.getNewFileName());
            }
            newPosting.setUser(activeSession.getUser());
            postingDao.save(newPosting);
            model.addAttribute("title", "New Posting");
            model.addAttribute("activeSession", activeSession);
            return "redirect:/posting/" + newPosting.getId();
        }
        //if session expires
        else if(Security.isSessionExpired(activeSessionId,currentSessionList)){
            //TODO pass through entered form data after successful login
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
    @RequestMapping("{postingId}")
    public String viewPosting (@PathVariable int postingId, @CookieValue(name="ASID", required=false) String activeSessionId,
                               RedirectAttributes attributes, Model model) {
        Iterable<Session> currentSessionList = sessionDao.findAll();
        String requestedUrl = "/posting/" + postingId;
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
            Posting viewedPosting = postingDao.findById(postingId).get();
            List<String> imageURLs = viewedPosting.getPictureURLs();
            model.addAttribute("imageURLs", imageURLs);
            model.addAttribute("viewedPosting", viewedPosting);
            model.addAttribute("title", viewedPosting.getTitle());
            model.addAttribute("activeSession", activeSession);
            return "posting/view-posting";
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
    @RequestMapping(value= "modify-posting/{postingId}", method = RequestMethod.GET)
    public String modifyPostingFormProcess (@PathVariable int postingId,
                               @CookieValue(name="ASID", required=false) String activeSessionId,
                               RedirectAttributes attributes, Model model) {
        Iterable<Session> currentSessionList = sessionDao.findAll();
        String requestedUrl = "/posting/new-posting";
        attributes.addFlashAttribute("requestedUrl", requestedUrl);
        //check if valid session
        if (Security.isValidSessionId(activeSessionId, currentSessionList)){
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            activeSession.refreshSession();
            sessionDao.save(activeSession);
            if (activeSession.getUser().getRole() > 2 || activeSession.getUser().getId() != postingDao.findById(postingId).get().getUser().getId()){
                attributes.addFlashAttribute("redirectMessage", Security.sessionNoPrivilege());
                return "redirect:/";
            }
            Posting modifiedPosting = postingDao.findById(postingId).get();
            List<String> imageURLs = modifiedPosting.getPictureURLs();
            model.addAttribute("imageURLs", imageURLs);
            model.addAttribute("title", "Modify Posting" + modifiedPosting.getId());
            model.addAttribute("activeSession", activeSession);
            model.addAttribute("modifiedPosting", modifiedPosting);
            return "posting/modify-posting";
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
    @RequestMapping(value = "/add-image/{modifiedPostingId}", method = RequestMethod.POST)
    public String addImageProcess (@PathVariable int modifiedPostingId, @CookieValue(name="ASID", required=false) String activeSessionId,
                                         RedirectAttributes attributes, Model model, @RequestParam("postingUserId") int postingUserId,
                                         @RequestParam("uploadPic")MultipartFile uploadPic) {
        Iterable<Session> currentSessionList = sessionDao.findAll();
        String requestedUrl = "/posting/add-image/" + modifiedPostingId;
        attributes.addFlashAttribute("requestedUrl", requestedUrl);
        //check if valid session
        if (Security.isValidSessionId(activeSessionId, currentSessionList)){
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            activeSession.refreshSession();
            sessionDao.save(activeSession);
            if (activeSession.getUser().getRole() > 2 || activeSession.getUser().getId() != postingUserId){
                attributes.addFlashAttribute("redirectMessage", Security.sessionNoPrivilege());
                return "redirect:/";
            }
            Posting modifiedPosting = postingDao.findById(modifiedPostingId).get();
            if (!uploadPic.isEmpty()){
                String folder = "/images/" + activeSession.getUser().getId() + "/";
                Image uploadedImage = null;
                try {
                    uploadedImage = new Image(uploadPic, folder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                modifiedPosting.addPictureURL(uploadedImage.getNewFileName());
            }
            postingDao.save(modifiedPosting);
            model.addAttribute("title", "New Posting");
            model.addAttribute("activeSession", activeSession);
            return "redirect:/posting/modify-posting/" + modifiedPosting.getId();
        }
        //if session expires
        else if(Security.isSessionExpired(activeSessionId,currentSessionList)){
            //TODO pass through entered form data after successful login
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
    @RequestMapping(value = "/remove-image/{modifiedPostingId}", method = RequestMethod.POST)
    public String removeImageProcess (@PathVariable int modifiedPostingId, @CookieValue(name="ASID", required=false) String activeSessionId,
                                   RedirectAttributes attributes, Model model, @RequestParam("postingUserId") int postingUserId,
                                      @RequestParam("imageURL") String removedImageURL) {
        Iterable<Session> currentSessionList = sessionDao.findAll();
        String requestedUrl = "/posting/add-image/" + modifiedPostingId;
        attributes.addFlashAttribute("requestedUrl", requestedUrl);
        //check if valid session
        if (Security.isValidSessionId(activeSessionId, currentSessionList)){
            Session activeSession = Security.getActiveSession(activeSessionId, currentSessionList);
            activeSession.refreshSession();
            sessionDao.save(activeSession);
            if (activeSession.getUser().getRole() > 2 || activeSession.getUser().getId() != postingUserId){
                attributes.addFlashAttribute("redirectMessage", Security.sessionNoPrivilege());
                return "redirect:/";
            }
            Posting modifiedPosting = postingDao.findById(modifiedPostingId).get();
            modifiedPosting.removePictureURL(removedImageURL);
            postingDao.save(modifiedPosting);
            model.addAttribute("title", "New Posting");
            model.addAttribute("activeSession", activeSession);
            return "redirect:/posting/modify-posting/" + modifiedPosting.getId();
        }
        //if session expires
        else if(Security.isSessionExpired(activeSessionId,currentSessionList)){
            //TODO pass through entered form data after successful login
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
