package org.carleton;

import java.util.ArrayList;

public class Deck {
    ArrayList<Card> deck;

    public ArrayList<Card> getAllCards() {
        return this.deck;
    }

    public int getSize() {
        return this.deck.size();
    }

    public void insertCard(Card c) {
        deck.add(c);
    }
}
