package com.example.app;

public class CharacterLook {
    private String behind;
    private String front;
    private String leftSide;
    private String rightSide;

    public CharacterLook(String behind, String front, String leftSide, String rightSide) {
        this.behind = behind;
        this.front = front;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    public String getBehind() {
        return behind;
    }

    public String getFront() {
        return front;
    }

    public String getLeftSide() {
        return leftSide;
    }

    public String getRightSide() {
        return rightSide;
    }
}
