package org.carleton;

public class Player {
    private int playerNumber;
    private Hand hand;
    private int numberOfShields;

    public Player(int playerNumber) {
        this.playerNumber = playerNumber;
        this.hand = new Hand();
        this.numberOfShields = 0;
    }

    public void removeShields(int numberOfShields) {
        this.numberOfShields -= numberOfShields;

        if (this.numberOfShields < 0)
            this.numberOfShields = 0;
    }

    public void addShields(int numberOfShields) {
        this.numberOfShields += numberOfShields;
    }

    public int getNumberOfShields() {
        return this.numberOfShields;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public Hand getHand() {
        return this.hand;
    }

    public void addCardToHand(Card card) {
        this.hand.addCard(card);
    }

    public int getHandSize() {
        return this.hand.size();
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
}
