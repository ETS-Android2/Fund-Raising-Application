package com.abdulaleem.appcon;

public class User {
    private String uid, name, email, profileImage;

    public User()
    {

    }
    public User(String uid, String name, String email,String profileImage) {
        this.uid = uid;
        this.profileImage = profileImage;
        this.name = name;
        this.email = email;
    }

    public void setProfileImage(String profileImage){
        this.profileImage = profileImage;
    }
    public String getname() {
        return name;
    }
    public String getProfileImage() {
        return profileImage;
    }


    public String getEmail() {
        return email;
    }



    public String getUid() {
        return uid;
    }
}
