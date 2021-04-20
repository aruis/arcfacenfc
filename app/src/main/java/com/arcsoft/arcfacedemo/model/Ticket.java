package com.arcsoft.arcfacedemo.model;

public class Ticket {
    String id;
    String source;
    String target;
    int year;
    int month;
    int day;

    byte[] face;

    boolean use;

    public Ticket(String id, String source, String target, int year, int month, int day, byte[] face) {
        this.id = id;
        this.source = source;
        this.target = target;
        this.year = year;
        this.month = month;
        this.day = day;
        this.face = face;
    }
}
