package hac.beans;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * save the search filter form data
 */
@Component
public class SearchFilter implements Serializable {
    private String[] selectedCategories = new String[]{"Any"};
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
