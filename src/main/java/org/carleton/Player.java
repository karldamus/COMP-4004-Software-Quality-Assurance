package org.carleton;

import java.util.ArrayList;

public class Player {
    private boolean isPlayersTurn;

    public ArrayList<Card> getHand() {
        return new ArrayList<Card>();
    }

    public boolean isPlayersTurn() {
        return this.isPlayersTurn;
    }

    public void setTurn(boolean isPlayersTurn) {
        this.isPlayersTurn = isPlayersTurn;
    }
}
