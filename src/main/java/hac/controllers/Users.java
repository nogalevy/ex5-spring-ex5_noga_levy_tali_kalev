package hac.controllers;

import hac.DTO.RegistrationForm;
import hac.beans.SearchFilter;
import hac.beans.UserSession;
import hac.repo.UserInfo;
import hac.repo.UserRepository;
import jakarta.validation.Valid;
import org.apache.catalina.User;
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
    private UserRepository userRepository;

    @GetMapping("/users/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/users/login")
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model){
        //retrieve login info from form
        //check if user exists in UserRepository
        UserInfo existingUser = userRepository.findUserByEmail(email);
        if(existingUser != null && existingUser.getPassword().equals(password)){
            //if exists, set userSession to logged in
            currUserSession.setLoggedIn(true);
            currUserSession.setUser_id(existingUser.getId());
            return "redirect:/";
        }else {
            //else return error message
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/users/register")
    public String register(Model model) {
        model.addAttribute(new RegistrationForm());
        return "register";
    }

    @PostMapping("/users/register")
    public String registerUser(@Valid RegistrationForm registrationForm, BindingResult result, Model model) {
        //retrieve register info from form
        if (result.hasErrors()) {
            return "register";
        }
        //check if user exists in UserRepository
        UserInfo existingUser = userRepository.findUserByEmail(registrationForm.getEmail());
        if (existingUser != null && existingUser.getEmail().equals(registrationForm.getEmail())) {
//            existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()
            //if exists, return error message
            result.rejectValue("email", null, "There is already an account registered with that email");
            return "register";
        }
        System.out.println("email: " + registrationForm.getEmail());
        System.out.println("password: " + registrationForm.getPassword());
        System.out.println("firstname: " + registrationForm.getFirstName());
        System.out.println("lastname: " + registrationForm.getLastName());

        //else, add user to UserRepository
        UserInfo newUser = new UserInfo(registrationForm.getFirstName(), registrationForm.getLastName(), registrationForm.getEmail(), registrationForm.getPassword());
        userRepository.save(newUser);
        //set userSession to logged in
        currUserSession.setLoggedIn(true);
        //redirect to index
        return "redirect:/";
    }

}
