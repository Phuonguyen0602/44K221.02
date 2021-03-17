package com.ecdue.lim.events;

public class BaseEvent {
    private String message;
    public BaseEvent(String message){
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
