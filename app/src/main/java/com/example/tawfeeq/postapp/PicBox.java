package com.example.tawfeeq.postapp;

/**
 * Created by Admin on 1/21/2018.
 */

public class PicBox {

    private String title, desc, image, userName;

    public PicBox(){

    }

    public PicBox(String title, String desc, String image, String userName){
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.userName= userName;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
