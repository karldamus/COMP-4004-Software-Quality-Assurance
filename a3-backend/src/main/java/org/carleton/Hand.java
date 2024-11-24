package main.java.org.carleton;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> hand;

    public Hand() {
        this.hand = new ArrayList<>();
    }

    public Hand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public Card getCard(char type, int value, boolean remove) {
        for (int i = 0; i < hand.size(); i++) {
            Card activeCard = hand.get(i);

            if (activeCard.getType() == type && activeCard.getValue() == value)
                return remove ? hand.remove(i) : hand.get(i);
        }

        return null;
    }

    public void sort() {
        for (int i = 1; i < hand.size(); i++) {
            Card curr = hand.get(i);
            int j = i - 1;

            while (j >= 0 && hand.get(j).compareTo(curr) > 0) {
                hand.set(j + 1, hand.get(i));
                j--;
            }

            hand.set(j + 1, curr);
        }
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public int size() {
        return hand.size();
    }

    public String getHandAsString() {
        StringBuilder handAsString = new StringBuilder();

        for (Card card : this.hand) {
            handAsString.append(card.getType()).append(card.getValue()).append(" ");
        }

        return handAsString.toString();
    }

    public boolean contains(String cardToRemoveAsString) {
        String regexLetters = "[a-zA-Z]";

        try {
            String[] cardDetails = cardToRemoveAsString.split(regexLetters);

            char cardType = cardToRemoveAsString.charAt(0);
            int cardValue = Integer.parseInt(cardDetails[1]);

            for (Card card : hand) {
                if (cardType == card.getType() && cardValue == card.getValue())
                    return true;
            }
        } catch (Error e) {
            return false;
        }

        return false;
    }

    public void remove(String cardToRemoveAsString) {
        String regexLetters = "[a-zA-Z]";
        String[] cardDetails = cardToRemoveAsString.split(regexLetters);

        char cardType = cardToRemoveAsString.charAt(0);
        int cardValue = Integer.parseInt(cardDetails[1]);

        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getType() == cardType && hand.get(i).getValue() == cardValue) {
                hand.remove(i);
                return;
            }
        }
    }
}
