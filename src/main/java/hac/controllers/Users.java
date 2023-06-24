package hac.controllers;

import hac.beans.UserSession;
import hac.repo.UserInfo;
import hac.repo.UserInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/users/login") //tali: should change to obj? with validation then check binding result? -> currently if empty returns Invalid email or password
    public synchronized String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, RedirectAttributes redirectAttributes) {
        //check if user exists in UserRepository
        UserInfo existingUser = userInfoRepository.findUserByEmail(email);
        if(existingUser != null  && passwordEncoder.matches(password, existingUser.getPassword())){
            //if exists, set userSession to logged in
            currUserSession.setLoggedIn(true);
            currUserSession.setUserId(existingUser.getId());
            return "redirect:/";
        }else {
            //else return error message
            //redirecting avoids resubmission upon refresh //tali: maybe add this in other places too?
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/users/login";
//            model.addAttribute("error", "Invalid email or password");
//            return "login";
        }
    }

    @GetMapping("/users/register")
    public String register(Model model) {
        model.addAttribute(new UserInfo());
        return "register";
    }

    @PostMapping("/users/register")
    public synchronized String registerUser(@Valid UserInfo userInfo, BindingResult result, Model model) {
        //retrieve register info from form and check if errors
        if (result.hasErrors()) {
            return "register";
        }
        //check if user exists in UserRepository
        UserInfo existingUser = userInfoRepository.findUserByEmail(userInfo.getEmail());
        if (existingUser != null && existingUser.getEmail().equals(userInfo.getEmail())) {
            //if exists, return error message
            result.rejectValue("email", null, "There is already an account registered with that email");
            return "register";
        }
        //if not, encrypt password and add user to repository
        String encryptedPassword = passwordEncoder.encode(userInfo.getPassword());
        UserInfo newUser = new UserInfo(userInfo.getFirstName(), userInfo.getLastName(), userInfo.getEmail(), encryptedPassword);
        userInfoRepository.save(newUser);
        return "redirect:/users/login";
    }
}
