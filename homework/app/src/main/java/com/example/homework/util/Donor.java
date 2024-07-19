package com.example.homework.util;

//donation class

public class Donor {
    private int id;
    private String name;
    private double donation;

    public Donor( String name, double donation) {
        this.name = name;
        this.donation = donation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getDonation() {
        return donation;
    }
}
