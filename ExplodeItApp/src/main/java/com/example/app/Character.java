package com.example.app;

public class Character {
    private String name;
    private int characterSpeed;

    private int explodePower;

    private int explodeSpeed;

    private CharacterLook look;

    private int hp;

    Character(String name, int characterSpeed, int explodePower, int explodeSpeed, CharacterLook look, int hp){
        this.name = name;
        this.characterSpeed = characterSpeed;
        this.explodePower = explodePower;
        this.explodeSpeed = explodeSpeed;
        this.look = look;
        this.hp = hp;
    }
}

