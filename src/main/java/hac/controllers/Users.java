package hac.controllers;

import hac.beans.UserSession;
import hac.exceptions.UserNotFound;
import hac.repo.UserInfo;
import hac.services.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Users controller
 */
@Controller
@RequestMapping(value = "/users")
public class Users {
    @Autowired
    @Qualifier("sessionUser")
    private UserSession currUserSession;

    @Autowired
    private UserInfoService userInfoService;

    /**
     * renders login page
     * @return login page
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * logs in user and redirects to index page
     * @param email string
     * @param password string
     * @return index page
     * @throws UserNotFound exception if not found in table
     */
    @PostMapping("/login")
    public synchronized String loginUser(@RequestParam("email") String email, @RequestParam("password") String password) throws UserNotFound {
        Long existingUserId;
        existingUserId = userInfoService.findUser(email, password);
        currUserSession.setLoggedIn(true);
        currUserSession.setUserId(existingUserId);
        return "redirect:/";
    }

    /**
     * renders user register page
     * @param model model
     * @return register page
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute(new UserInfo());
        return "register";
    }

    /**
     * retrieves register info from form and checks if errors exist, if not registers user and redirects to login page
     * @param userInfo user info object
     * @param result binding result
     * @return login page
     * @throws Exception exception if issue with adding to table other than user already existing
     */
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

    /**
     * redirects to login page with error message
     * @param e exception
     * @param redirectAttributes redirect attributes
     * @return login page
     */
    @ExceptionHandler({UserNotFound.class})
    public String handleUserNotFoundExceptions(UserNotFound e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/users/login";
    }

    /**
     * handles all other exceptions, redirects to error page with error message
     * @param e exception
     * @param redirectAttributes redirect attributes
     * @return error page
     */
    @ExceptionHandler({Exception.class})
    public String handleExceptions(Exception e, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("error","An unexpected error occured: " + e.getMessage());
        return "redirect:/error";
    }
}
