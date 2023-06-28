package hac.beans;

import org.springframework.stereotype.Component;

import java.io.Serializable;

import static hac.utils.Constants.INITIAL_SEARCH_FILTER;

/**
 * save the search filter form data
 */
@Component
public class SearchFilter implements Serializable {
    private String[] selectedCategories = new String[]{INITIAL_SEARCH_FILTER};
    private int selectedOption;

    // Getters and setters for the members
    public String[] getSelectedCategories() {
        return selectedCategories;
    }

    public void setSelectedCategories(String[] selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }
}
