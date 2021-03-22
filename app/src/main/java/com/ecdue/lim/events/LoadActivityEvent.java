package com.ecdue.lim.events;

public class LoadActivityEvent {
    private Class aClass;
    public LoadActivityEvent(Class cl){
        aClass = cl;
    }

    public Class getaClass() {
        return aClass;
    }
}
