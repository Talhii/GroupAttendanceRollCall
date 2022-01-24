package com.example.groupattendancerollcall;

public class EventMainViewModel {


    String eventFromDate,eventFromTime,eventToDate,eventToTime,eventTypeName,eventDescription;

    public EventMainViewModel(String eventFromDate, String eventFromTime, String eventToDate, String eventToTime, String eventTypeName, String eventDescription) {
        this.eventFromDate = eventFromDate;
        this.eventFromTime = eventFromTime;
        this.eventToDate = eventToDate;
        this.eventToTime = eventToTime;
        this.eventTypeName = eventTypeName;
        this.eventDescription = eventDescription;
    }

    public String getEventFromDate() {
        return eventFromDate;
    }

    public void setEventFromDate(String eventFromDate) {
        this.eventFromDate = eventFromDate;
    }

    public String getEventFromTime() {
        return eventFromTime;
    }

    public void setEventFromTime(String eventFromTime) {
        this.eventFromTime = eventFromTime;
    }

    public String getEventToDate() {
        return eventToDate;
    }

    public void setEventToDate(String eventToDate) {
        this.eventToDate = eventToDate;
    }

    public String getEventToTime() {
        return eventToTime;
    }

    public void setEventToTime(String eventToTime) {
        this.eventToTime = eventToTime;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
