package com.abdulaleem.appcon;

public class Funds {
    private String title;
    private String description;
    private String status;
    private String goal;
    private String category;
private String  key;
    private String photo;
    private String id;

    public Funds(){

    }
    public Funds(String category, String title, String status, String description, String photo, String goal, String key, String id){

        this.category = category;
        this.title = title;
        this.status = status;
        this.description = description;
        this.photo = photo;
        this.goal = goal;
        this.key = key;
        this.id = id;
    }

    public String getStatus() {
        return status;
    }
    public String getGoal() {
        return goal;
    }
    public String getTitle() {
        return title;
    }
    public String getKey(){return key;}
    public String getId(){return id;}
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
