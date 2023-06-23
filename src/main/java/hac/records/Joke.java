package hac.records;

public record Joke(String category, String type, String setup, String delivery, String joke, long id, boolean isFavourite, Joke jokeObj) {

    public Joke(Joke joke, boolean isFavourite) {
        this(joke.category(), joke.type(), joke.setup(), joke.delivery(), joke.joke(), joke.id(), isFavourite, joke);
    }
}

//
//public class Joke{
//    private String category;
//    private String type;
//    private String setup;
//    private String delivery;
//    private String joke;
//    private long id;
//    private boolean isFavourite = false;
//    public Joke(String category, String type, String setup, String delivery, String joke, long id, boolean b) {
//    }
//
//    public Joke(Joke joke, boolean isFavourite){
//        this(joke.getCategory(), joke.getType(), joke.getSetup(), joke.getDelivery(), joke.getJoke(), joke.getId(), isFavourite);
////        this.category = joke.getCategory();
////        this.type = joke.getType();
////        this.setup = joke.getSetup();
////        this.delivery = joke.getDelivery();
////        this.joke = joke.getJoke();
////        this.id = joke.getId();
////        this.isFavourite = isFavourite;
//    }
//    public Joke(String category, String type, String setup, String delivery, String joke, long id) {
//        this.category = category;
//        this.type = type;
//        this.setup = setup;
//        this.delivery = delivery;
//        this.joke = joke;
//        this.id = id;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getSetup() {
//        return setup;
//    }
//
//    public void setSetup(String setup) {
//        this.setup = setup;
//    }
//
//    public String getDelivery() {
//        return delivery;
//    }
//
//    public void setDelivery(String delivery) {
//        this.delivery = delivery;
//    }
//
//    public String getJoke() {
//        return joke;
//    }
//
//    public void setJoke(String joke) {
//        this.joke = joke;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//    public boolean getIsFavourite() {
//        return isFavourite;
//    }
//    public void setIsFavourite(boolean isFavourite) {
//        this.isFavourite = isFavourite;
//    }
//
//}