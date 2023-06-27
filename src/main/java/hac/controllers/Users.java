package hac.controllers;

import hac.beans.UserSession;
import hac.repo.UserInfo;
import hac.services.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Users {
    @Autowired
    @Qualifier("sessionUser")
    private UserSession currUserSession;

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/users/login")
    public String login() {
        return "login";
    }

    @PostMapping("/users/login")
    public synchronized String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        Long existingUserId;
        try{
            existingUserId = userInfoService.findUser(email, password);
            currUserSession.setLoggedIn(true);
            currUserSession.setUserId(existingUserId);
            return "redirect:/";
        }catch (Exception error){
            //tali: instead of catch here, add exception handler for controller
            //tali: create Exception type for this, the rest of error returns different?
            redirectAttributes.addFlashAttribute("error", error.getMessage());
            return "redirect:/users/login";
        }
    }

    @GetMapping("/users/register")
    public String register(Model model) {
        model.addAttribute(new UserInfo());
        return "register";
    }

    //TODO: delete model
    @PostMapping("/users/register")
    public synchronized String registerUser(@Valid UserInfo userInfo, BindingResult result, Model model) {
        //retrieve register info from form and check if errors
        if (result.hasErrors()) {
            return "register";
        }
        try{
            userInfoService.registerUser(userInfo);
            return "redirect:/users/login";
        }catch (Exception error){
            //tali: currently catches existing user error -> add more error types
            result.rejectValue("email", null, error.getMessage());
            return "register";
        }
    }
}
