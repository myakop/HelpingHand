package com.example.homework.util;



//object for track volunteers with get and set
public class trackvolunteer {

    private String nameOfOrg;
    private String name;
    private String date;
    private String time;
    private String taskCompleted;

    public trackvolunteer(String nameOfOrg, String name, String date, String time, String taskCompleted) {
        this.nameOfOrg = nameOfOrg;
        this.name = name;
        this.date = date;
        this.time = time;
        this.taskCompleted = taskCompleted;
    }

    public String getNameOfOrg() {
        return nameOfOrg;
    }

    public void setNameOfOrg(String nameOfOrg) {
        this.nameOfOrg = nameOfOrg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTaskCompleted() {
        return taskCompleted;
    }

    public void setTaskCompleted(String taskCompleted) {
        this.taskCompleted = taskCompleted;
    }
}

