package com.example.homework.util;

////poor person object

public class PoorPeople {
    private int id;
    private String name;
    private String address;
    private String phone;

    public PoorPeople(String name, String address, String phone) {

        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}

