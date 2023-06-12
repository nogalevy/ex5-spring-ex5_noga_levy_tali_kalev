package hac.repo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Favourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @NotEmpty(message = "FIRST_NAME_MANDATORY")
//    private String userId;

    @NotEmpty(message = "LAST_NAME_MANDATORY")
    private String jokeId;

    public Favourite(/*String userId, */String jokeId) {
//        this.userId = userId;
        this.jokeId = jokeId;
    }

    public Favourite() {

    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    //getters and setters
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }

    public String getJokeId() {
        return jokeId;
    }

    public void setJokeId(String jokeId) {
        this.jokeId = jokeId;
    }
}
