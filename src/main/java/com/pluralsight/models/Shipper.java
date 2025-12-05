package com.pluralsight.models;

public class Shipper {

    // CLASS ATTRIBUTES
    int shipperID;
    String companyName;
    String phone;

    // CONSTRUCTOR
    public Shipper (int shipperID, String companyName, String phone){

        this.shipperID = shipperID;
        this.companyName = companyName;
        this.phone = phone;
    }

    // toString method to get Shipper information
    @Override
    public String toString() {
        return "Shipper{" +
                "shipperID=" + shipperID +
                ", companyName='" + companyName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
