package org.carleton;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    ArrayList<Card> deck;

    public void shuffle() {
        Random rand = new Random();

        for (int i = 0; i < this.getSize(); i++) {
            int pos1 = rand.nextInt(this.getSize() - 1);
            int pos2 = rand.nextInt(this.getSize() - 1);

            Card tempCard = deck.get(pos1);
            deck.set(pos1, deck.get(pos2));
            deck.set(pos2, tempCard);
        }
    }

    public void setDeck(ArrayList<Card> cards) {
        this.deck = cards;
    }

    public Card drawCard() {
        return deck.removeFirst();
    }

    public ArrayList<Card> getAllCards() {
        return this.deck;
    }

    public int getSize() {
        return this.deck.size();
    }

    public void insertCard(Card c) {
        deck.add(c);
    }

    public void setTopCard(char e, int i) {
        deck.set(0, new Card(e, i));
    }
}
