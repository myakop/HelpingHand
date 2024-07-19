package com.example.homework.util;
// volunteer help object (object of one help )
public class Volunteer {
    private int id;
    private String name;
    private String date;
    private String time;
    private String des;
    private String iscomplete;

    public Volunteer( String name, String date, String time,String des) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.des = des;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate
            () {
        return date;
    }

    public String getDes() {
        return des;
    }

    public String getTime() {
        return time;
    }
}

