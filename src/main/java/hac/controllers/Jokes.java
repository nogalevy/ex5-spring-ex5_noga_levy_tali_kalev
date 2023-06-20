package hac.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hac.beans.JokesList;
import hac.beans.SearchFilter;
import hac.records.Joke;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

//NOGA: CURRENTLY UNUSED
@Controller
public class Jokes {
//    @Autowired
//    @Qualifier("searchFilterSession")
//    private SearchFilter currSearchFilter;

    //TODO : move to Default controller
//    @GetMapping("/getJokes")
//    public ResponseEntity<String> getJokes() {
//        List<Joke> jokes = JokesList.getJokesFromApi(currSearchFilter); //NOGA: i dont knowwwwwwwwww
//        if (jokes == null) {
//            String errorResponse = "{\"error\": \"Something happened...no joke at the moment\"}";
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        } else {
//            Joke joke = jokes.get(0);
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                String jokeResponse = objectMapper.writeValueAsString(joke);
//                return ResponseEntity.ok(jokeResponse);
//            } catch (Exception e) {
//                e.printStackTrace();
//                String errorResponse = "{\"error\": \"Failed to process joke data\"}";
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//            }
//        }
//    }

//    @PostMapping("/search")
//    public String search(@ModelAttribute SearchFilter searchFilter, Model model) {
//        System.out.println("=+=+=+=+=+=+=+=+=+=+=+=+=+=+=+="+searchFilter.getSelectedOption());
////        return "index";
////        List<String> selectedCategories = searchFilter.getSelectedCategories();
//        currSearchFilter.setSelectedCategories(searchFilter.getSelectedCategories());
//        currSearchFilter.setSelectedOption(searchFilter.getSelectedOption());
//
//        model.addAttribute("searchFilter", searchFilter);
//
//        return "redirect:/";
//    }
}
