package org.carleton;

import java.io.PrintWriter;
import java.util.ArrayList;

public class Player {
    ArrayList<Card> hand;
    private boolean isPlayersTurn;
    private int numberOfShields;
    private int playerNumber;

    public Player() {
        this.hand = new ArrayList<Card>();
    }

    public void sortHand() {
       for (int i = 1; i < hand.size(); i++) {
           Card curr = hand.get(i);
           int j = i - 1;

           while (j >= 0 && hand.get(j).compareTo(curr) > 0) {
               hand.set(j + 1, hand.get(j));
               j--;
           }

           hand.set(j + 1, curr);
       }
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

    public void setPlayerNumber(int i) { this.playerNumber = i; }
    public int getPlayerNumber() { return this.playerNumber; }
}
