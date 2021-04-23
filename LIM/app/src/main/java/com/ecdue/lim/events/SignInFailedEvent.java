package com.ecdue.lim.events;

import com.ecdue.lim.base.BaseEvent;

public class SignInFailedEvent extends BaseEvent {
    public SignInFailedEvent(String message) {
        super(message);
    }
}
