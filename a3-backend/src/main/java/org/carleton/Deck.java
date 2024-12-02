package org.carleton;

import java.util.ArrayList;
import java.util.Random;

public class Deck {
    private ArrayList<Card> deck;

    public Deck() {

    }

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

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }

    public void appendDeck(ArrayList<Card> deck) {
        this.deck.addAll(deck);
    }

    public void addCard(Card card) {
        this.deck.add(card);
    }

    public void setTopCard(Card card) {
        this.deck.set(0, card);
    }

    public Card drawCard() {
        return this.deck.removeFirst();
    }

    public ArrayList<Card> getDeck() { return this.deck; }
    public int getSize() { return this.deck.size(); }
}
