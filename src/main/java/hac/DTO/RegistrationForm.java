package hac.DTO;
//TALI : NO IN USE

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class RegistrationForm {
    @NotEmpty(message = "First name is mandatory")
    private String firstName;

    @NotEmpty(message = "Last name is mandatory")
    private String lastName;

    @NotEmpty(message = "Email is mandatory")
//    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Password is mandatory")
    private String password;

    public RegistrationForm() {
        // Default constructor
    }

    public RegistrationForm(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() { return password; }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName=firstName;
    }

    public void setLastName(String lastName) {
        this.lastName=lastName;
    }

    public void setPassword(String password) {
        this.password=password;
    }

    public void setEmail(String email) {
        this.email=email;
    }
}
