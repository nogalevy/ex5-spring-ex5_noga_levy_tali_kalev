package hac.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;


@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(Model model) {
//        model.addAttribute("error", "Some error occured");
        return "error";
    }
}

