package hac.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import static hac.utils.Constants.*;

@Entity
public class UserInfo implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2 ,max = 30, message = STRING_LENGTH_2_TO_30)
    @NotEmpty(message = FIRST_NAME_MANDATORY)
    private String firstName;

    @Size(min = 2 ,max = 30, message = STRING_LENGTH_2_TO_30)
    @NotEmpty(message = LAST_NAME_MANDATORY)
    private String lastName;

    @NotEmpty(message = EMAIL_MANDATORY)
    @Email(message = EMAIL_NOT_VALID)
    private String email;

    @Size(min = 2 ,max = 30, message = STRING_LENGTH_2_TO_30)
    @NotEmpty(message = PASSWORD_MANDATORY)
    private String password;

    public UserInfo(String firstName, String lastName, String email, String password) {
        this.email = email.trim();
        this.password = password.trim();
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
    }

    public UserInfo() {

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


