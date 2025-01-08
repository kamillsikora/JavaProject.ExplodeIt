package com.example.app;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Character {
    private int id;
    private String name;
    private int characterSpeed;
    private int explodePower;
    private int explodeSpeed;
    private CharacterLook look;
    private int hp;

    public Character(int id, String name, int characterSpeed, int explodePower, int explodeSpeed, CharacterLook look, int hp) {
        this.id = id;
        this.name = name;
        this.characterSpeed = characterSpeed;
        this.explodePower = explodePower;
        this.explodeSpeed = explodeSpeed;
        this.look = look;
        this.hp = hp;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCharacterSpeed() {
        return characterSpeed;
    }

    public int getExplodePower() {
        return explodePower;
    }

    public int getExplodeSpeed() {
        return explodeSpeed;
    }

    public CharacterLook getLook() {
        return look;
    }

    public int getHp() {
        return hp;
    }

    public static List<Character> fetchCharacters() {
        List<Character> characters = new ArrayList<>();
        try (Connection connection = DbConfig.connect()) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                String query = "SELECT c.*, cl.behind, cl.front, cl.leftSide, cl.rightSide " +
                        "FROM characters c " +
                        "JOIN characterlook cl ON c.CharacterLookID = cl.CharacterLookID";
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    int id = resultSet.getInt("CharacterID");
                    String name = resultSet.getString("name");
                    int characterSpeed = resultSet.getInt("characterSpeed");
                    int explodePower = resultSet.getInt("explodePower");
                    int explodeSpeed = resultSet.getInt("explosionSpeed");
                    int hp = resultSet.getInt("hp");

                    CharacterLook look = new CharacterLook(
                            resultSet.getString("behind"),
                            resultSet.getString("front"),
                            resultSet.getString("leftSide"),
                            resultSet.getString("rightSide")
                    );

                    characters.add(new Character(id, name, characterSpeed, explodePower, explodeSpeed, look, hp));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return characters;
    }
}
