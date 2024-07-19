package com.example.homework.util;
// Organization object
public class Organization {

    String name;
    String des;

    public Organization( String name, String des) {

        this.name = name;
        this.des = des;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}