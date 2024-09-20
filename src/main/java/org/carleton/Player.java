package org.carleton;

import java.util.ArrayList;

public class Player {
    ArrayList<Card> hand;
    private boolean isPlayersTurn;

    public Player() {
        this.hand = new ArrayList<Card>();
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
}
