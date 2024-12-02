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
        this.hand.addLast(card);
    }

    public boolean isOrderedAs(String listOfCards) {
        this.newSort();

        String regexLetters = "[a-zA-Z]";
        String[] cardsAsArray = listOfCards.split(",");

        for (int i = 0; i < cardsAsArray.length; i++) {
            try {
                String card = cardsAsArray[i];
                String[] cardDetails = card.split(regexLetters);
                char cardType = card.charAt(0);
                int cardValue = Integer.parseInt(cardDetails[1]);

                if (hand.get(i).getType() != cardType || hand.get(i).getValue() != cardValue)
                    return false;
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e);
                return false;
            } catch (Error e) {
                System.out.println(e);
                return false;
            }
        }

        return true;
    }

    public Card getCard(char type, int value, boolean remove) {
        for (int i = 0; i < hand.size(); i++) {
            Card activeCard = hand.get(i);

            if (activeCard.getType() == type && activeCard.getValue() == value)
                return remove ? hand.remove(i) : hand.get(i);
        }

        return null;
    }

    public void newSort() {
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

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public int size() {
        return hand.size();
    }

    public String getHandAsString() {
        String handAsString = "";

//        StringBuilder handAsString = new StringBuilder();

        for (Card card : this.hand) {
            handAsString += card.getType() + "" + card.getValue() + " ";
        }

        return handAsString;
    }

    public Card getCard(String cardAsString, boolean remove) {
        String regexLetters = "[a-zA-Z]";

        try {
            String[] cardDetails = cardAsString.split(regexLetters);

            char cardType = cardAsString.charAt(0);
            int cardValue = Integer.parseInt(cardDetails[1]);

            for (int i = 0; i < hand.size(); i++) {
                if (hand.get(i).getType() == cardType && hand.get(i).getValue() == cardValue) {
                    return remove ? hand.remove(i) : hand.get(i);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
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
        } catch (Exception e) {
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
