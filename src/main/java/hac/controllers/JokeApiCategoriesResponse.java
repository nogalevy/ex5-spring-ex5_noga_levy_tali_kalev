package hac.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record JokeApiCategoriesResponse(boolean error, List<String> categories) {
}
