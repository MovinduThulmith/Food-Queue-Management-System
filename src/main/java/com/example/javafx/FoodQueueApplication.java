package com.example.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class FoodQueueApplication extends Application {
    private static char[][] queues = {
            {'X', 'X'},
            {'X', 'X', 'X'},
            {'X', 'X', 'X', 'X', 'X'}};

    public static int[] incomeOfQueues = {0,0,0};

    private static int burger_stock = 50;

    public static CircularQueue waitingQueue = new CircularQueue(5);

    public static FoodQueue[] queue;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FoodQueueApplication.class.getResource("gui-view.fxml"));
        Parent root = fxmlLoader.load();
        QueueController controller = fxmlLoader.getController();
        controller.setProgramData(queue, waitingQueue);
        Scene scene = new Scene(root, 616, 426);
        stage.setTitle("Foodies Fave Food center");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {

        queue = new FoodQueue[3];
        queue[0] = new FoodQueue(2);
        queue[1] = new FoodQueue(3);
        queue[2] = new FoodQueue(5);

        showMenu();
    }

    // Displays the current state of all queues
    private static void viewAllQueues() {
        System.out.println();
        System.out.println("****************");
        System.out.println("*   Cashiers   *");
        System.out.println("****************");
        System.out.println("\t" + queues[0][0] + "\t" + queues[1][0] + "\t" + queues[2][0]);
        System.out.println("\t" + queues[0][1] + "\t" + queues[1][1] + "\t" + queues[2][1]);
        System.out.println("\t" + " " + "\t" + queues[1][2] + "\t" + queues[2][2]);
        System.out.println("\t" + " " + "\t" + " " + "\t" + queues[2][3]);
        System.out.println("\t" + " " + "\t" + " " + "\t" + queues[2][4]);
    }

    // Displays the number of empty slots in each queue
    private static void viewEmptyQueues() {

        for (int i = 0; i < queues.length; i++) {
            int emptySlotCount = 0;
            for (int j = 0; j < queues[i].length; j++) {
                if (queues[i][j] == 'X') {
                    emptySlotCount++;
                }
            }
            System.out.println();
            System.out.println("There are " + emptySlotCount + " empty slots in cashier " + (i + 1));
        }
    }

    // Adds a customer to the shortest queue
    private static void addCustomer() {
        boolean occupied = false;
        try {
            while (!occupied) {
                Scanner input = new Scanner(System.in);
                System.out.print("Enter the first name of the customer: ");
                String firstName = input.nextLine();
                System.out.print("Enter the second name of the customer: ");
                String secondName = input.nextLine();
                System.out.print("Enter the number of burgers required: ");
                int burgerAmount = input.nextInt();

                if (firstName.isEmpty() || secondName.isEmpty()) {
                    System.out.println();
                    System.out.println("Please enter the name correctly. Do not leave blank.");
                    continue;
                }

                if (burger_stock == 0){
                    System.out.println("No burgers in stock. Please add burgers.");
                    break;
                } else if (burgerAmount > burger_stock) {
                    System.out.println();
                    System.out.println("There are no " + burgerAmount + " burgers in stock to serve.");
                    System.out.println("There are only " + burger_stock + " burgers in stock.");
                    continue;
                }

                Customer customer = new Customer(firstName, secondName, burgerAmount);

                for (int i = 0; i < queues.length; i++) {
                    if (queues[i][0] == 'X') {
                        queues[i][0] = 'O';
                        queue[i].addNewCustomers(customer);
                        occupied = true;
                        break;
                    }
                }
                if (!occupied) {
                    for (int i = 0; i < queues.length; i++) {
                        if (queues[i][1] == 'X') {
                            queues[i][1] = 'O';
                            queue[i].addNewCustomers(customer);
                            occupied = true;
                            break;
                        }
                    }
                    if (!occupied) {
                        for (int i = 1; i < queues.length; i++) {
                            if (queues[i][2] == 'X') {
                                queues[i][2] = 'O';
                                queue[i].addNewCustomers(customer);
                                occupied = true;
                                break;
                            }
                        }
                        if (!occupied) {
                            if (queues[2][3] == 'X') {
                                queues[2][3] = 'O';
                                queue[2].addNewCustomers(customer);
                                occupied = true;
                            } else if (queues[2][4] == 'X') {
                                queues[2][4] = 'O';
                                queue[2].addNewCustomers(customer);
                                occupied = true;
                            } else {
                                System.out.println("Cannot add more customers. All queues are full");
                                try {
                                    waitingQueue.enqueue(customer);
                                } catch (RuntimeException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch(InputMismatchException e){
            System.out.println("Please enter a valid input.");
        }
    }

    private static void removeCustomer() {
        try {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter queue number (1, 2, or 3): ");
            int removingQueueNumber = input.nextInt();
            System.out.print("Enter position of customer to remove: ");
            int removingPosition = input.nextInt();

            //updating the removed customers position
            if (queues[removingQueueNumber - 1][removingPosition - 1] == 'O') {
                queues[removingQueueNumber - 1][removingPosition - 1] = 'X';
                queue[removingQueueNumber-1].removeCustomer(removingPosition-1);
                System.out.println();
                System.out.println("Customer at Queue: " + removingQueueNumber + "  Position: " + removingPosition + " removed.");

                //shifting the customers behind the removed customer's to 1 position front
                for (int i = (removingPosition - 1); i < queues[removingQueueNumber - 1].length; i++) {
                    if (i == (queues[removingQueueNumber - 1].length - 1)) {
                        if (!waitingQueue.isEmpty()){
                            queues[removingQueueNumber - 1][i] = 'O';
                            queue[removingQueueNumber-1].addNewCustomers(waitingQueue.dequeue());
                        }
                        else {
                            queues[removingQueueNumber - 1][i] = 'X';
                        }
                    } else {
                        queues[removingQueueNumber - 1][i] = queues[removingQueueNumber - 1][i + 1];
                    }
                }
            } else {
                System.out.println();
                System.out.println("There is no customer in that position");
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter an valid value.");
        }
    }

    private static void removeServedCustomer() {
        try {
            Scanner input = new Scanner(System.in);
            System.out.print("Enter queue number (1, 2, or 3): ");
            int servedQueue = input.nextInt();

            if (burger_stock == 0) {
                System.out.println();
                System.out.println("Please add burgers to the stock to serve a customer.");
            }

            //updating the served customer's queue
            else if (queues[servedQueue - 1][0] == 'O') {
                int boughtBurgers = queue[servedQueue - 1].removeCustomer(0);
                if (burger_stock - boughtBurgers < 0){
                    System.out.println("There is not enough stock to serve");
                }
                else {
                    queues[servedQueue - 1][0] = 'X';

                    burger_stock -= boughtBurgers;
                    incomeOfQueues[servedQueue - 1] += boughtBurgers * 650;
                    System.out.println();
                    System.out.println("A customer in queue " + servedQueue + " was serverd.");

                    //shifting the customers 1 position to front
                    for (int i = 0; i < queues[servedQueue - 1].length; i++) {
                        if (i == queues[servedQueue - 1].length - 1) {
                            if (!waitingQueue.isEmpty()) {
                                queues[servedQueue - 1][i] = 'O';
                                queue[servedQueue - 1].addNewCustomers(waitingQueue.dequeue());
                            } else {
                                queues[servedQueue - 1][i] = 'X';
                            }
                        } else {
                            queues[servedQueue - 1][i] = queues[servedQueue - 1][i + 1];
                        }
                    }
                }
            } else {
                System.out.println();
                System.out.println("There are no customers in that queue.");
            }

            if (burger_stock <= 10 && burger_stock > 0) {
                System.out.println();
                System.out.println("Warning: Burger stock is low - Only " + burger_stock + " remaining");
            } else if (burger_stock == 0) {
                System.out.println();
                System.out.println("There are no burgers left in the stock.");
            }
        } catch (Exception e) {
            System.out.println("Please enter a valid queue number.");
        }
    }

    private static void queueIncome(){
        for (int i = 0; i < 3; i++){
            System.out.println();
            System.out.println("Income of queue "+(i+1)+" is: "+incomeOfQueues[i]);
        }
    }

    private static void customerSort() {
        ArrayList<String> customerNames = new ArrayList<>();
        for (int i = 0; i < queue.length; i++){
            Customer[] customers = queue[i].getCustomers();
            for (int j = 0; j< customers.length; j++){
                if(customers[j] != null){
                    String fullName = customers[j].getFirstName() + " " + customers[j].getSecondName();
                    customerNames.add(fullName);
                }
            }
        }
        for (int i = 0; i < customerNames.size(); i++){
            for (int j = i+1; j < customerNames.size(); j++){
                if (customerNames.get(i).compareTo(customerNames.get(j))>0){
                    String temp = customerNames.get(i);
                    customerNames.set(i, customerNames.get(j));
                    customerNames.set(j, temp);
                }
            }
        }
        System.out.println();
        System.out.println("Customer names in alphabetical order: ");
        for (String name: customerNames){
            System.out.println(" - "+ name);
        }
    }

    // Stores program data (customer details, stock, incomes) into a file
    private static void readFromFile() {
        try {
            File file = new File("Customer_data.txt");
            Scanner read = new Scanner(file);
            String fileLine;
            //This will read the file as long as it has a next line
            //when there is no new line, the loop will end
            while (read.hasNext()) {
                fileLine = read.nextLine();
                System.out.println(fileLine);
            }
            read.close();

        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
        System.out.println();
    }

    private static void storeDataToFile(){
        try {
            FileWriter fileWriter = new FileWriter("Customer_data.txt");
            for (int i=0; i<queue.length; i++) {
                fileWriter.write("Customer details in queue " + (i+1) + ":\n");
                // getting 'customers' array for each 'FoodQueue' object
                Customer[] customers = queue[i].getCustomers();
                for (Customer customer : customers) {
                    if (customer != null) {
                        fileWriter.write(" - " + (i+1) + " " + customer.getFirstName()+" "+customer.getSecondName()+"\n");
                    }
                }
                fileWriter.write("\n");
            }
            fileWriter.write("\nRemaining burger stock: " + burger_stock);
            fileWriter.write("\n\nIncomes of each queue:\n");
            for (int i = 0; i < queue.length; i++) {
                fileWriter.write("Queue " + (i+1) + " - " + incomeOfQueues[i] + "\n");
            }
            fileWriter.close();
            System.out.println("Data stored successfully");
        }
        catch (IOException e) {
            System.out.println("Something went wrong!");
        }
    }

    private static void addBurgers() {
        try {
            Scanner input1 = new Scanner(System.in);
            System.out.print("Enter the number of burgers need to add: ");
            int addedBurgers = input1.nextInt();

            //adding burgers only if it is less than 50
            if (burger_stock < 50 && (addedBurgers + burger_stock) <= 50) {
                burger_stock += addedBurgers;
                System.out.println();
                System.out.println(addedBurgers + " burgers added to stock");
            } else {
                System.out.println();
                System.out.println("You cannot add " + addedBurgers + " burgers to stock.");
                System.out.println("Maximum burger stock is 50.");
            }

        } catch (Exception e) {
            System.out.println("Invalid input. Please enter an integer value.");
        }

    }

    //This is used to call the launch() method more than once and open GUI multiple times
    private static volatile boolean javaFxLaunched = false;
    public static void launchGUI(Class<? extends Application> applicationClass) {
        if (!javaFxLaunched) { // First time
            Platform.setImplicitExit(false);
            new Thread(() -> Application.launch(applicationClass)).start();
            javaFxLaunched = true;
        } else { // Next times
            Platform.runLater(() ->
            {
                try {
                    Application application = applicationClass.newInstance();
                    Stage primaryStage = new Stage();
                    application.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private static void showMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println();
            System.out.println("100 or VFQ: View all Queues.");
            System.out.println("101 or VEQ: View all Empty Queues.");
            System.out.println("102 or ACQ: Add customer to a Queue.");
            System.out.println("103 or RCQ: Remove a customer from a Queue.");
            System.out.println("104 or PCQ: Remove a served customer.");
            System.out.println("105 or VCS: View Customers Sorted in alphabetical order.");
            System.out.println("106 or SPD: Store Program Data into file.");
            System.out.println("107 or LPD: Load Program Data from file.");
            System.out.println("108 or STK: View Remaining burgers Stock.");
            System.out.println("109 or AFS: Add burgers to Stock.");
            System.out.println("110 or IFQ: Print income of each queue.");
            System.out.println("112 or GUI: Display the GUI");
            System.out.println("999 or EXT: Exit the Program.");

            Scanner input = new Scanner(System.in);
            System.out.print("Choose an option: ");
            String option = input.nextLine();

            switch (option) {
                case "100":
                case "VFQ":
                    viewAllQueues();
                    break;

                case "101":
                case "VEQ":
                    viewEmptyQueues();
                    break;

                case "102":
                case "ACQ":
                    addCustomer();
                    break;

                case "103":
                case "RCQ":
                    removeCustomer();
                    break;

                case "104":
                case "PCQ":
                    removeServedCustomer();
                    break;

                case "105":
                case "VCS":
                    customerSort();
                    break;

                case "106":
                case "SPD":
                    storeDataToFile();
                    break;

                case "107":
                case "LPD":
                    readFromFile();
                    break;

                case "108":
                case "STK":
                    System.out.println("Remaining number of burgers: " + burger_stock);
                    break;

                case "109":
                case "AFS":
                    addBurgers();
                    break;

                case "110":
                case "IFQ":
                    queueIncome();
                    break;

                case "112":
                case "GUI":
                    System.out.println("GUI launched.");
                    launchGUI(FoodQueueApplication.class);
                    break;

                case "999":
                case "EXT":
                    System.out.println("Exiting the programme.....");
                    exit = true;
                    break;

                default:
                    System.out.println("Please enter a valid option");
            }
        }
    }
}

//Reference List
//https://stackoverflow.com/questions/24320014/how-to-call-launch-more-than-once-in-java
