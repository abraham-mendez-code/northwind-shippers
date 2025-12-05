package com.pluralsight;

import com.pluralsight.dao.ShipperDAO;
import com.pluralsight.models.Shipper;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    // CLASS ATTRIBUTES
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args)  {

        // get credentials from the env
        String username = System.getenv("username");
        String password = System.getenv("password");

        if (username == null || password == null) {
            System.out.println("Error: username or password not set in the environment.");
            System.exit(1);
        }

        // start app
        startMenu(username, password);

    }

    // this method display the start menu options for the app
    private static void startMenu(String username, String password) {

        // try to connect to the northwind database using the username and password we provided
        try (BasicDataSource ds = new BasicDataSource()) {

            // set the DS config
            ds.setUrl("jdbc:mysql://localhost:3306/northwind");
            ds.setUsername(username);
            ds.setPassword(password);

            ShipperDAO shipperDAO = new ShipperDAO(ds);

            // initialize menu message
            String message = """
                What do you want to do?
                    1) Display All Shippers
                    2) Add a Shipper
                    3) Update Shipper Contact
                    4) Delete a Shipper
                    0) Exit
                Select an option:\s""";

            while (true) {

                int input = getAInteger(message);

                switch (input) {
                    case 0:
                        System.out.println("Goodbye!");
                        System.exit(0);
                    case 1:
                        processDisplayAllShippers(shipperDAO);
                        break;
                    case 2:
                        processAddShipper(shipperDAO);
                        break;
                    case 3:
                        processUpdateShipperPhone(shipperDAO);
                        break;
                    case 4:
                        processDeleteShipper(shipperDAO);
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            }

        } catch (SQLException e) {
            System.out.println("Failed to connect to DB");
            System.exit(1);
        }
    }

    // this method displays all shippers in the database sorted by shipper id
    private static void processDisplayAllShippers(ShipperDAO shipperDAO) {

        ArrayList<Shipper> shippers = shipperDAO.displayAllShippers();

        if (shippers.isEmpty()) {
            System.out.println("No results");
        } else {
            shippers.forEach(System.out::println);
        }
    }

    // this method adds a shipper to the database
    private static void processAddShipper(ShipperDAO shipperDAO) {

        System.out.print("What is the shipper company name? ");
        String companyName = scanner.nextLine();

        System.out.print("What is the shipper phone number? ");
        String phone = scanner.nextLine();

        shipperDAO.addShipper(companyName, phone);
        processDisplayAllShippers(shipperDAO);

    }

    // this method updates a shipper's phone number
    private static void processUpdateShipperPhone(ShipperDAO shipperDAO) {

        int shipperID = getAInteger("What is the shipper ID? ");

        System.out.print("What is the shipper's new phone number? ");
        String phone = scanner.nextLine();

        shipperDAO.updateShipperPhone(shipperID, phone);
        processDisplayAllShippers(shipperDAO);

    }

    // this method removes a shipper from the database
    private static void processDeleteShipper(ShipperDAO shipperDAO) {

        int shipperID = getAInteger("What is the shipper ID? ");

        // verify user decision
        System.out.println("Are you sure you want to delete this shipper? " + shipperID);
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("yes") || input.contains("y")) {
            // delete row
            shipperDAO.deleteShipper(shipperID);
            System.out.println("Shipper removed successfully");
            processDisplayAllShippers(shipperDAO);
        } else {
            System.out.println("Operation canceled, no changes have been made.");
        }
    }

    // this method gets an integer
    public static int getAInteger(String message) {
        int input;
        while (true) {
            try {
                System.out.print(message);
                input = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Only numbers allowed");
            }
        }
        return input;
    }

}
