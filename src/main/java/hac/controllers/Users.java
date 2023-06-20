package hac.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Users {
    @GetMapping("/users/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/users/login")
    public String loginUser(Model model){
        //retrieve login info from form
        //check if user exists in UserRepository
        //if exists, set userSession to logged in
        //else, return error message
        //redirect to index
        return "index";
    }

    @GetMapping("/users/register")
    public String register(Model model) {
        return "register";
    }

    @PostMapping String registerUser(Model model){
        //retrieve register info from form
        //check if user exists in UserRepository
        //if exists, return error message
        //else, add user to UserRepository
        //set userSession to logged in
        //redirect to index
        return "index";
    }

}
