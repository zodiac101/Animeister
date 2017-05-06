package com.example.rohil.animeister;

/**
 * Created by Rohil on 12/10/2016.
 */

public class ListItem {


    String title;
    String picture;
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ListItem(String title, String picture, String id) {
        this.title = title;
        this.picture = picture;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getPicture() {

        return picture;
    }

    public void setPicture(String picture) {

        this.picture = picture;
    }

    public void setTitle(String title) {

        this.title = title;
    }
}
