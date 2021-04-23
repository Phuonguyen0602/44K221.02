package com.ecdue.lim.events;

import com.ecdue.lim.base.BaseEvent;

public class ShowConfirmDeleteEvent extends BaseEvent {
    private String category;
    private int position;
    public ShowConfirmDeleteEvent(String message) {
        super(message);
    }

    public ShowConfirmDeleteEvent(String category, int position) {
        super(category);
        this.category = category;
        this.position = position;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
