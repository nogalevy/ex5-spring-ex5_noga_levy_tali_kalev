package hac.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

/**
 * a purchase is a record of a user buying a product. You should not need to edit this file
 * but if you feel like you need to, please get in touch with the teacher.
 */
@Entity
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "FIRST_NAME_MANDATORY")
    private String firstName;

    @NotEmpty(message = "LAST_NAME_MANDATORY")
    private String lastName;

    @NotEmpty(message = "EMAIL_MANDATORY")
    @Email(message = "EMAIL_VALID")
    private String email;

    @NotEmpty(message = "PASSWORD_MANDATORY")
    private String password;

    public User(String email, String password, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User() {

    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
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


