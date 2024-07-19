package com.example.homework.util;
// object user with get and set
public class User {
    private String userId;
    private String name;
    private String password;
    private String phoneNumber;
    private String gender;
    private String birthday;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String userId, String name, String password, String phoneNumber, String gender, String birthday) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthday = birthday;
    }

    public User(String name, String password, String phoneNumber, String gender, String birthday) {
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthday = birthday;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}

