package com.abdulaleem.appcon;

import android.net.Uri;

import java.util.ArrayList;

public class Progress {
    String img;
    String key,id;


    public Progress(){}
    public Progress(String img, String key, String id) {
        this.img = img;
        this.key = key;
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public String getKey() {
        return key;
    }

    public String getpId() {
        return id;
    }
}
