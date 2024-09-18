package org.carleton;

public class Player {
    private boolean isPlayersTurn;

    public boolean isPlayersTurn() {
        return this.isPlayersTurn;
    }

    public void setTurn(boolean isPlayersTurn) {
        this.isPlayersTurn = isPlayersTurn;
    }
}
