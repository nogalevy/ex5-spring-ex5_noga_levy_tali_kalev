package hac.controllers;

import hac.beans.UserSession;
import hac.exceptions.UserNotFound;
import hac.repo.UserInfo;
import hac.services.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/users")
public class Users {
    @Autowired
    @Qualifier("sessionUser")
    private UserSession currUserSession;

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public synchronized String loginUser(@RequestParam("email") String email, @RequestParam("password") String password) throws UserNotFound {
        Long existingUserId;
        existingUserId = userInfoService.findUser(email, password);
        currUserSession.setLoggedIn(true);
        currUserSession.setUserId(existingUserId);
        return "redirect:/";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute(new UserInfo());
        return "register";
    }

    @PostMapping("/register")
    public synchronized String registerUser(@Valid UserInfo userInfo, BindingResult result) throws Exception{
        //retrieve register info from form and check if errors
        if (result.hasErrors()) {
            return "register";
        }
        try{
            userInfoService.registerUser(userInfo);
            return "redirect:/users/login";
        } catch (IllegalArgumentException error){
            result.rejectValue("email", null, error.getMessage());
            return "register";
        } catch (Exception e){
            throw e;
        }
    }

    @ExceptionHandler({UserNotFound.class})
    public String handleUserNotFoundExceptions(UserNotFound e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/users/login";
    }

    @ExceptionHandler({Exception.class})
    public String handleExceptions(Exception e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("error","An unexpected error occured: " + e.getMessage());
        return "redirect:/error";
    }
}
