package com.example.javafx;

public class Customer {
    private String firstName;
    private String secondName;
    private int burgerAmount;

    public Customer(String firstName, String secondName, int burgerAmount) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.burgerAmount = burgerAmount;
    }
    public String getFirstName() {
        return firstName;
    }

    public String  getSecondName() {
        return secondName;
    }

    public int getBurgerAmount() {
        return burgerAmount;
    }

}

