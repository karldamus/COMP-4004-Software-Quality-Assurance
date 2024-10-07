package org.carleton;

import java.util.ArrayList;

public class Player {
    ArrayList<Card> hand;
    private boolean isPlayersTurn;
    private int numberOfShields;

    public Player() {
        this.hand = new ArrayList<Card>();
    }

    public void plague() {
        if (numberOfShields >= 2)
            this.numberOfShields -= 2;
        else
            numberOfShields = 0;
    }

    public ArrayList<Card> getHand() {
        return this.hand;
    }

    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

    public boolean isPlayersTurn() {
        return this.isPlayersTurn;
    }

    public void setTurn(boolean isPlayersTurn) {
        this.isPlayersTurn = isPlayersTurn;
    }

    public int getNumberOfShields() {
        return this.numberOfShields;
    }

    public void awardShields(int i) {
        this.numberOfShields += i;
    }
}
