package hac.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

//@JsonIgnoreProperties(ignoreUnknown = true)
public record Joke(String category, String type, String setup, String delivery, String joke, int id, boolean safe, String lang) { }

//public class Joke{
//    private String category;
//    private String type;
//    private String setup;
//    private String delivery;
//    private String joke;
////    private Flags flags;
//    private int id;
//    private boolean safe;
//    private String lang;
//
//    public Joke() {
//    }
//
//    public Joke(String category, String type, String setup, String delivery, String joke, int id, boolean safe, String lang) {
//        this.category = category;
//        this.type = type;
//        this.setup = setup;
//        this.delivery = delivery;
//        this.joke = joke;
////        this.flags = flags;
//        this.id = id;
//        this.safe = safe;
//        this.lang = lang;
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
//
////    public Flags getFlags() {
////        return flags;
////    }
////
////    public void setFlags(Flags flags) {
////        this.flags = flags;
////    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public boolean isSafe() {
//        return safe;
//    }
//
//    public void setSafe(boolean safe) {
//        this.safe = safe;
//    }
//
//    public String getLang() {
//        return lang;
//    }
//
//    public void setLang(String lang) {
//        this.lang = lang;
//    }
//}