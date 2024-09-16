package org.carleton;

public class Card {

    private char type;
    private int value;

    public Card(char type, int value) {
        this.type = type;
        this.value = value;
    }

    public char getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
