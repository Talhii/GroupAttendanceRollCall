package com.example.groupattendancerollcall;

public class EventTypeListViewModel {
    String name, active;

    public EventTypeListViewModel(String name, String active) {
        this.name = name;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public String getActive() {
        return active;
    }
}
