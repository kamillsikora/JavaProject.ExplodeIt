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

        try (Connection connection = DbConfig.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, itemID);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String look = resultSet.getString("look");

                return look;
            } else {
                System.out.println("Item not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching item look: " + e.getMessage());
        }
        return null;
    }
    public static int[] getItemData(int itemID) {
        String sql = "SELECT timeOfEffect, CharacterSpeed, ExplodePower, ExplosionSpeed, hp " +
                "FROM Item WHERE ItemID = ?";

        int[] itemData = new int[5];

        try (Connection connection = DbConfig.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, itemID);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                itemData[0] = resultSet.getInt("timeOfEffect");
                itemData[1] = resultSet.getInt("CharacterSpeed");
                itemData[2] = resultSet.getInt("ExplodePower");
                itemData[3] = resultSet.getInt("ExplosionSpeed");
                itemData[4] = resultSet.getInt("hp");

                return itemData;
            } else {
                System.out.println("Item not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching item data: " + e.getMessage());
        }

        return null;
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
