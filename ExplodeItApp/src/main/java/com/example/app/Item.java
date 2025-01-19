package com.example.app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Item {
    private int itemID;
    private int timeOfEffect;
    private int charactersSpeed;

    private int charactersPower;

    private int explosionsSpeed;

    private int charactersHp;

    private double x;
    private double y;

    public Item(int itemID, double x, double y) {
        this.itemID = itemID;
        this.x = x;
        this.y = y;

        // Fetch item data from the database
        int[] itemData = getItemData(itemID);

        if (itemData != null) {
            this.timeOfEffect = itemData[0];
            this.charactersSpeed = itemData[1];
            this.charactersPower = itemData[2];
            this.explosionsSpeed = itemData[3];
            this.charactersHp = itemData[4];
        } else {
            throw new IllegalArgumentException("Invalid itemID: " + itemID);
        }
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
        String sql = "SELECT timeOfEffect, CharacterSpeed, ExplodePower, ExplosionSpeed, hp " +
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
                itemData[1] = resultSet.getInt("CharacterSpeed");
                itemData[2] = resultSet.getInt("ExplodePower");
                itemData[3] = resultSet.getInt("ExplosionSpeed");
                itemData[4] = resultSet.getInt("hp");

                return itemData;  // Return the array containing the retrieved data
            } else {
                System.out.println("Item not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching item data: " + e.getMessage());
        }

        return null; // Return null if the item is not found
    }

    //getters
    public int getItemID() {
        return itemID;
    }

    public int getTimeOfEffect() {
        return timeOfEffect;
    }

    public int getCharactersSpeed() {
        return charactersSpeed;
    }

    public int getCharactersPower() {
        return charactersPower;
    }

    public int getExplosionsSpeed() {
        return explosionsSpeed;
    }

    public int getCharactersHp() {
        return charactersHp;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
