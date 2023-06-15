package hac.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/** this is a test controller, delete/replace it when you start working on your project */
@Controller
public class Default {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("greeting", "Hello World");
        return "index";
    }

    @GetMapping("/favourite")
    public String favourite(Model model) {
        return "favourite";
    }

    @GetMapping("/user")
    public String userProfile(Model model) {
        return "userProfile";
    }

    @GetMapping("/getJokes")
    private @ResponseBody Boolean getJokes()
    {
        final String uri = "https://v2.jokeapi.dev/joke/Any?amount=4?format=json";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
//        NOGA:
        System.out.println(result);
        return true;
    }
}
