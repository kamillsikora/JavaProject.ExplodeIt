package com.example.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Item {
    private String name;
    private ItemLook look;
    private int timeOfEffect;

    private int dropProbability;

    private int charactersSpeed;

    private int charactersPower;

    private int explosionsSpeed;

    private int charactersHp;

    public Item(String name, ItemLook look, int timeOfEffect, int dropProbability, int charactersSpeed, int charactersPower, int exposionsSpeed, int charactersHp){
        this.name = name;
        this.look = look;
        this.timeOfEffect = timeOfEffect;
        this.dropProbability = dropProbability;
        this.charactersSpeed = charactersSpeed;
        this.charactersPower = charactersPower;
        this.explosionsSpeed = exposionsSpeed;
        this.charactersHp = charactersHp;
    }

    public static String getItemLook(int itemID) {
        String sql = "SELECT i.ItemLookID, il.look FROM Item i " +
                "JOIN ItemLook il ON i.ItemLookID = il.ItemLookID " +
                "WHERE i.ItemID = ?";

        // Use the DbConfig class to connect to the database
        try (Connection connection = DbConfig.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the item ID in the query
            statement.setInt(1, itemID);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Check if we got a result
            if (resultSet.next()) {
                // Retrieve the 'look' from the result
                String look = resultSet.getString("look");

                // Return the ItemLook object
                return look;
            } else {
                System.out.println("Item not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching item look: " + e.getMessage());
        }

        return null; // Return null if the item look is not found
    }
    public static int[] getItemData(int itemID) {
        String sql = "SELECT timeOfEffect, charactersSpeed, charactersPower, explosionsSpeed, charactersHp " +
                "FROM Item WHERE ItemID = ?";

        // Declare an array to hold the retrieved data
        int[] itemData = new int[5];  // Array to hold: [timeOfEffect, charactersSpeed, charactersPower, explosionsSpeed, charactersHp]

        // Use the DbConfig class to connect to the database
        try (Connection connection = DbConfig.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the item ID in the query
            statement.setInt(1, itemID);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Check if we got a result
            if (resultSet.next()) {
                // Retrieve the data and store it in the array
                itemData[0] = resultSet.getInt("timeOfEffect");
                itemData[1] = resultSet.getInt("charactersSpeed");
                itemData[2] = resultSet.getInt("charactersPower");
                itemData[3] = resultSet.getInt("explosionsSpeed");
                itemData[4] = resultSet.getInt("charactersHp");

                return itemData;  // Return the array containing the retrieved data
            } else {
                System.out.println("Item not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching item data: " + e.getMessage());
        }

        return null; // Return null if the item is not found
    }





}
