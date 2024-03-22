package com.example.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class QueueController {
    @FXML
    private Label customer1_1;
    @FXML
    private Label customer1_2;
    @FXML
    private Label customer2_1;
    @FXML
    private Label customer2_2;
    @FXML
    private Label customer2_3;
    @FXML
    private Label customer3_1;
    @FXML
    private Label customer3_2;
    @FXML
    private Label customer3_3;
    @FXML
    private Label customer3_4;
    @FXML
    private Label customer3_5;
    @FXML
    private Label waitingCustomer1;
    @FXML
    private Label waitingCustomer2;
    @FXML
    private Label waitingCustomer3;
    @FXML
    private Label waitingCustomer4;
    @FXML
    private Label waitingCustomer5;
    @FXML
    private TextField searchName;
    @FXML
    private TextArea customerInfo;

    ArrayList<Customer> customerList = new ArrayList<>();

    // Method to set program data and update UI
    public void setProgramData(FoodQueue[] queue, CircularQueue waitingQueue) {
        Label[] customersPosition = {
                customer1_1, customer1_2,
                customer2_1, customer2_2, customer2_3,
                customer3_1, customer3_2, customer3_3, customer3_4, customer3_5};

        Customer[] queue1 = queue[0].getCustomers();
        Customer[] queue2 = queue[1].getCustomers();
        Customer[] queue3 = queue[2].getCustomers();

        for (int i = 0; i < queue.length; i++) {
            int temp;

            if (i == 0) {
                temp = 0;
            } else if (i == 1) {
                temp = 2;
            } else {
                temp = 5;
            }

            for (int j = 0; j < queue[i].getCustomers().length; j++) {

                if (queue[i].getCustomers()[j] == null) {
                    continue;
                }
                String customerFullName = queue[i].getCustomers()[j].getFirstName() +" "+queue[i].getCustomers()[j].getSecondName();
                customersPosition[j + temp].setText(customerFullName);

            }
        }

        setWaitingQueue(waitingQueue);

        //Adding all customer objects to 'customerList' for searching purpose
        for (int i=0; i<queue1.length; i++) {
            customerList.add(queue1[i]);
        }
        for (int i=0; i<queue2.length; i++) {
            customerList.add(queue2[i]);
        }
        for (int i=0; i<queue3.length; i++) {
            customerList.add(queue3[i]);
        }
    }

    // Method to update waiting queue labels and add customers to customerList
    public void setWaitingQueue(CircularQueue waitingQueue){
        Label[] waitingList = {waitingCustomer1, waitingCustomer2, waitingCustomer3, waitingCustomer4, waitingCustomer5};
        int front = waitingQueue.getFront();
        int rear = waitingQueue.getRear();
        int i = 0;

        if (waitingQueue.isEmpty()) {
            for (Label lbl : waitingList) {
                lbl.setText("...........");
            }
            return;
        }
        while (front != rear) {
            // Update waiting labels with customer names
            waitingList[i].setText(waitingQueue.getWaitingQueue()[front].getFirstName());
            customerList.add(waitingQueue.getWaitingQueue()[front]);
            front = (front + 1) % waitingQueue.getSize();
            i++;
        }

        // Update the last waiting label with the customer name
        waitingList[i].setText(waitingQueue.getWaitingQueue()[front].getFirstName());
        customerList.add(waitingQueue.getWaitingQueue()[front]);
    }

    public void searchCustomer() {
        try {
            boolean nameFound = false;
            String searched = searchName.getText().trim().toLowerCase();

            for (Customer customer : customerList) {
                String firstName = customer.getFirstName().toLowerCase();
                String secondName = customer.getSecondName().toLowerCase();

                if (searched.equals(firstName) || searched.equals(secondName)) {
                    nameFound = true;
                    customerInfo.setText("Customer details: \n\nFirst Name: "+customer.getFirstName()+"\nSecond Name: "+customer.getSecondName()+"\nBurger Count: "+customer.getBurgerAmount());
                    break;
                }
            }
            if (!nameFound) {
                customerInfo.setText("There is no customer called "+searched);
            }
        }
        catch (Exception e) {
            System.out.println();
        }
    }

    public void resetSearch(){
        searchName.clear();
        customerInfo.clear();
    }
}