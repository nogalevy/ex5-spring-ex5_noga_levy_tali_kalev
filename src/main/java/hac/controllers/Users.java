package hac.controllers;

import hac.DTO.RegistrationForm;
import hac.beans.UserSession;
import hac.repo.UserInfo;
import hac.repo.UserInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class Users {

    @Autowired
    @Qualifier("sessionUser")
    private UserSession currUserSession;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @GetMapping("/users/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/users/login")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model){
        //retrieve login info from form
        //check if user exists in UserRepository
        UserInfo existingUser = userInfoRepository.findUserByEmail(email);
        if(existingUser != null && existingUser.getPassword().equals(password)){
            //if exists, set userSession to logged in
            currUserSession.setLoggedIn(true);
            currUserSession.setUserId(existingUser.getId());
            return "redirect:/";
        }else {
            //else return error message
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/users/register")
    public String register(Model model) {
        model.addAttribute(new UserInfo());
        return "register";
    }

    @PostMapping("/users/register")
    public String registerUser(@Valid UserInfo userInfo, BindingResult result, Model model) {
        //retrieve register info from form
        if (result.hasErrors()) {
            return "register";
        }
        //check if user exists in UserRepository
        UserInfo existingUser = userInfoRepository.findUserByEmail(userInfo.getEmail());
        if (existingUser != null && existingUser.getEmail().equals(userInfo.getEmail())) {
//            existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()
            //if exists, return error message
            result.rejectValue("email", null, "There is already an account registered with that email");
            return "register";
        }

//        else, add user to UserRepository
        UserInfo newUser = new UserInfo(userInfo.getFirstName(), userInfo.getLastName(), userInfo.getEmail(), userInfo.getPassword());
        userInfoRepository.save(newUser);
        //set userSession to logged in
//        currUserSession.setLoggedIn(true);
        //redirect to index
//        return "redirect:/";
        return "redirect:/users/login";
    }

    @PostMapping("/users/logout")
    public String logoutUser(HttpServletRequest request, Model model){
        //set userSession to logged out
        request.getSession().invalidate();
//        currUserSession.setLoggedIn(false);
//        currUserSession.setUserId(-1);

        System.out.println("logging out user");
        //todo: add message on successful logout
        //redirect to login page
        return "redirect:/users/login";
    }

}
