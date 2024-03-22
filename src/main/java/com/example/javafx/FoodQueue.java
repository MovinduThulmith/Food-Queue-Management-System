package com.example.javafx;

public class FoodQueue {
    private int capacity;
    private Customer[] customers;

    public FoodQueue(int capacity) {
        this.capacity = capacity;
        this.customers = new Customer[capacity];
    }

    public void addNewCustomers(Customer customer){
        for(int i = 0; i < customers.length; i++){
            if (customers[i] == null){
                customers[i] = customer;
                System.out.println();
                System.out.println("Customer " + customer.getFirstName() + " added to the queue");
                break;
            }
        }
    }
    public int removeCustomer(int spot){
        int burgerAmount = customers[spot].getBurgerAmount();
        customers[spot] = null;
        // Shifting the remaining customers to the front
        for (int i =0; i< customers.length; i++){
            if (i == customers.length-1){
                customers[i] = null;
            }else{
                customers[i] = customers[i+1];
            }
        }
        return burgerAmount;
    }
    // Getter method to retrieve the array of customers
    public Customer[] getCustomers() {
        return customers;
    }
}

