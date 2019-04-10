package com.example.codercoral.interview;

import android.support.v4.app.FragmentActivity;

public class Fruit  extends FragmentActivity {
    private String name;
    private int imageID;

    public Fruit(String name, int imageID) {
        this.name = name;
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public int getImageID() {
        return imageID;
    }
}
