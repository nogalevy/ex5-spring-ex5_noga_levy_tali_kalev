package hac.beans;
import hac.records.Joke;
import org.springframework.stereotype.Component;

import java.io.Serializable;

//TALI: CURRENTLY UNUSED
@Component
public class JokeBean implements Serializable {
//    Joke jokeRecord;
    private Integer id;
    private String category;
    private String type;
    private String joke;
    private String setup;
    private String delivery;

    public JokeBean(){

    }

    public JokeBean(Integer id, String category, String type, String joke, String setup, String delivery){
//        jokeRecord = new Joke(category, type, setup, delivery, joke, id);
        this.id = id;
        this.category = category;
        this.type = type;
        this.joke = joke;
        this.setup = setup;
        this.delivery = delivery;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }


}
