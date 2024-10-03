package org.carleton;

public class Game {
    private static final int NUM_PLAYERS = 4;

    private Player[] players;
    private AdventureDeck adventureDeck;
    private EventDeck eventDeck;

    public Game() {

    }

    public void init() {
        this.initDecks();
        this.initPlayers();
    }

    public void initDecks() {
        this.adventureDeck = new AdventureDeck();
        this.eventDeck = new EventDeck();
    }

    public void initPlayers() {
        this.players = new Player[NUM_PLAYERS];

        for (int i = 0; i < NUM_PLAYERS; i++) {
            this.players[i] = new Player();
            this.players[i].setTurn(false);
        }

        players[0].setTurn(true);
    }

    public void dealCards() {
        for (int cardNum = 0; cardNum < 12; cardNum++) {
            for (int playerNum = 0; playerNum < players.length; playerNum++) {
                Card c = this.adventureDeck.drawCard();
                players[playerNum].addCardToHand(c);
            }
        }
    }

    public void endPlayersTurn() {

    }

    // Getters
    public Player[] getPlayers() { return this.players; }
    public AdventureDeck getAdventureDeck() { return adventureDeck; }
    public EventDeck getEventDeck() { return eventDeck; }
}
