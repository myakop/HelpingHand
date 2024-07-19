package com.example.homework.util;

public interface OnUserValidationListener {
    void onUserValidation(boolean isValid);
    void onValidationFailure(Exception e);
}
