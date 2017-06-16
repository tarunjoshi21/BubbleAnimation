package com.example.tarun.bubbleanimation.model;

/**
 * Created by tarun on 14/6/17.
 */

public class BubbleModel {
    private int id;
    private String title;
    private boolean isBurst;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isBurst() {
        return isBurst;
    }

    public void setBurst(boolean burst) {
        isBurst = burst;
    }
}
