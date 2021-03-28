package com.ecdue.lim.events;

import com.ecdue.lim.base.BaseEvent;

public class SignInFailed extends BaseEvent {
    public SignInFailed(String message) {
        super(message);
    }
}
