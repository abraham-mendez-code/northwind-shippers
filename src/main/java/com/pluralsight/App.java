package com.pluralsight;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.*;
import java.util.Scanner;

public class App {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String username = System.getenv("username");
        String password = System.getenv("password");

        if (username == null || password== null){
            System.out.println("Error: username or password not set in the environment.");
            System.exit(1);
        }

        startMenu(username, password);

    }

    // this method display the start menu options for the app
    private static void startMenu(String username, String password) {

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/northwind");
        ds.setUsername(username);
        ds.setPassword(password);

        // try to connect to the northwind database using the username and password we provided
        try (Connection connection = ds.getConnection()){

            while (true){
                int input = getAInteger("""
                        What do you want to do?
                            1) Display All Shippers
                            2) Add a Shipper
                            3) Update Shipper Contact
                            4) Delete a Shipper
                            0) Exit
                        Select an option:\s""");

                switch (input) {
                    case 0:
                        System.out.println("Goodbye!");
                        System.exit(0);
                    case 1:
                        displayAllShippers(connection);
                        break;
                    case 2:
                        //addShipper(connection);
                        break;
                    case 3:
                        //updateShipperPhone(connection);
                        break;
                    case 4:
                        //deleteShipper(connection);
                    default:
                        System.out.println("Invalid choice");
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not connect to DB");
            System.exit(1);
        }
    }

    // this method displays all shippers in the database sorted by shipper id
    private static void displayAllShippers(Connection connection) {

        try (

                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT
                            *
                        FROM
                            Shippers
                        ORDER BY
                            ShipperID;
                        """
                );

                ResultSet results = preparedStatement.executeQuery();

        ) {
            printResults(results);
        } catch (SQLException e) {
            System.out.println("Could not get all the shippers");
            System.exit(1);
        }

    }

    // this method will be used in the displayMethods to actually print the results to the screen
    private static void printResults(ResultSet results) throws SQLException {
        // get the metadata so we have access to the field names
        ResultSetMetaData metaData = results.getMetaData();
        // get the number of rows returned
        int columnCount = metaData.getColumnCount();

        // prints an empty line to make the results prettier
        System.out.println();

        while (results.next()) {

            for (int i = 1; i <= columnCount; i++) {
                // gets the current column name
                String columnName = metaData.getColumnName(i);

                // get the current column value
                String value = results.getString(i);

                // print out the column name and column value
                System.out.println(columnName + ": " + value + " ");
            }

            // prints an empty line to make the results prettier
            System.out.println();

        }

    }

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
