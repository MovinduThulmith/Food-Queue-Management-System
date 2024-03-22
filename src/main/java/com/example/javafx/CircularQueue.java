package com.example.javafx;

public class CircularQueue {
    private Customer[] waitingQueue;
    private int front, rear, size;

    public CircularQueue(int size){
        this.waitingQueue = new Customer[size];
        this.front  = this.rear = -1;
        this.size = size;
    }
    public boolean isFull() {
        return (rear + 1) % size == front;
    }
    public boolean isEmpty() {
        return front == -1;
    }
    public void enqueue(Customer customerDetails){

        if (isFull()){
            // Throw an exception if the queue is full
            throw new RuntimeException("Waiting queue is full.");
        }
        else if (isEmpty()){
            front++;
        }
        rear = (rear+1) % size; // Move the rear circularly to the next position
        waitingQueue[rear] = customerDetails; // Add the customer to the rear position
        System.out.println("Customer added to the waiting list");
    }
    public Customer dequeue() {
        if (isEmpty()) {
            // Throw an exception if the queue is empty
            throw new RuntimeException("Waiting queue is empty");
        }
        Customer customer = waitingQueue[front]; // Retrieve the customer at the front
        if (front == rear) {
            front = rear = -1;
        } else {
            front = (front + 1) % size;
        }
        return customer;
    }

    public int getFront() {
        return this.front;
    }

    public int getRear() {
        return this.rear;
    }

    public int getSize() {
        return this.size;
    }

    public Customer[] getWaitingQueue() {
        return this.waitingQueue;
    }
}


//Reference list
//https://www.programiz.com/dsa/circular-queue

