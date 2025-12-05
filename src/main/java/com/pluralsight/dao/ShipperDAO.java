package com.pluralsight.dao;

import com.pluralsight.models.Shipper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShipperDAO {

    // CLASS ATTRIBUTES
    DataSource dataSource;

    // CONSTRUCTOR
    public ShipperDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // this method displays all shippers in the database sorted by shipper id
    public ArrayList<Shipper> displayAllShippers() {

        // create an empty list
        ArrayList<Shipper> shippers = new ArrayList<>();

        // try to
        try (
                // get a connection
                Connection connection = this.dataSource.getConnection();

                //
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT
                            ShipperID,
                            CompanyName,
                            Phone
                        FROM
                            Shippers
                        ORDER BY
                            ShipperID;
                        """
                );

                // get the results
                ResultSet results = preparedStatement.executeQuery()
        ) {

            addResults(results, shippers);

        } catch (SQLException e) {
            System.out.println("Could not get all the shippers");
            System.exit(1);
        }

        // return the list
        return shippers;
    }

    // this method adds a shipper to the database
    public void addShipper(String companyName, String phone) {

        // declare and initialize the SQL query string
        String sql = "INSERT INTO Shippers (CompanyName, Phone) VALUES (?, ?)";

        // try to run a query
        try (
                // get a connection
                Connection connection = this.dataSource.getConnection();

                // declare and initialize the prepared statement with the query string
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, companyName);
            preparedStatement.setString(2, phone);

            // Insert row
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            System.out.println("Could not add the shipper" + e);
            System.exit(1);
        }

    }

    // this method updates a shipper's phone number
    public void updateShipperPhone(int shipperID, String phone) {

        // declare and initialize the SQL query string
        String sql = "UPDATE Shippers SET Phone = ? WHERE ShipperID = ?";

        // try to run a query
        try (
                // get a connection
                Connection connection = this.dataSource.getConnection();

                // declare and initialize the prepared statement with the query string
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            preparedStatement.setString(1, phone);
            preparedStatement.setInt(2, shipperID);

            // update row
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            System.out.println("Could not update the shipper" + e);
            System.exit(1);
        }

    }

    // this method removes a shipper from the database
    public void deleteShipper(int shipperID) {

        // declare and initialize the SQL query string
        String sql = "DELETE FROM Shippers WHERE ShipperID = ?";

        // try to run a query
        try (
                // get a connection
                Connection connection = this.dataSource.getConnection();

                // declare and initialize the prepared statement with the query string
                PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ) {

            preparedStatement.setInt(1, shipperID);

            // delete row
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Could not remove the shipper" + e);
            System.exit(1);
        }

    }

    // this method loop over results to create shipper objects and add them to the list
    private static void addResults(ResultSet results, ArrayList<Shipper> shippers) throws SQLException {

        while (results.next()) {
            // create a new Shipper from the results returned from DB
            Shipper newShipper = new Shipper(
                    results.getInt("ShipperID"),
                    results.getString("CompanyName"),
                    results.getString("Phone")
            );

            // add the actor to the list
            shippers.add(newShipper);
        }
    }

}
