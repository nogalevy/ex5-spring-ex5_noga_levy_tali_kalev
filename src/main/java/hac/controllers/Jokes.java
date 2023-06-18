package hac.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import hac.beans.JokesList;
import hac.records.Joke;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class Jokes {

    @GetMapping("/getJokes")
    public ResponseEntity<String> getJokes() {
        List<Joke> jokes = JokesList.getJokesFromApi();
        if (jokes == null) {
            String errorResponse = "{\"error\": \"Something happened...no joke at the moment\"}";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } else {
            Joke joke = jokes.get(0);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jokeResponse = objectMapper.writeValueAsString(joke);
                return ResponseEntity.ok(jokeResponse);
            } catch (Exception e) {
                e.printStackTrace();
                String errorResponse = "{\"error\": \"Failed to process joke data\"}";
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
            }
        }
    }
}
