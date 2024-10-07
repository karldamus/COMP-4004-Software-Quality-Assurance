package org.carleton;

public class Game {
    private static final int NUM_PLAYERS = 4;

    private Player[] players;
    private AdventureDeck adventureDeck;
    private EventDeck eventDeck;
    private DiscardPile discardedEventCards;
    private int currentPlayersTurn;

    public Game() {

    }

    public void init() {
        this.initDecks();
        this.initPlayers();
    }

    public void initDecks() {
        this.adventureDeck = new AdventureDeck();
        this.eventDeck = new EventDeck();
        this.discardedEventCards = new DiscardPile();
    }

    public void initPlayers() {
        this.players = new Player[NUM_PLAYERS];

        for (int i = 0; i < NUM_PLAYERS; i++) {
            this.players[i] = new Player();
            this.players[i].setTurn(false);
        }

        this.currentPlayersTurn = 0;
        players[currentPlayersTurn].setTurn(true);
    }

    public void dealCards() {
        for (int cardNum = 0; cardNum < 12; cardNum++) {
            for (int playerNum = 0; playerNum < players.length; playerNum++) {
                Card c = this.adventureDeck.drawCard();
                players[playerNum].addCardToHand(c);
            }
        }
    }

    public Card drawEventCard() {
        Card card = this.eventDeck.drawCard();

        switch(card.getType()) {
            case 'E':
                if (card.getValue() == 0)
                    this.players[currentPlayersTurn].plague();
                break;
        }

        this.discardedEventCards.insertCard(card);
        return card;
    }

    public Card drawAdventureCard() {
        return null;
    }

    public int computeNumberOfCardsToDiscard(Player player) {
        return -1;
    }

    public void endPlayersTurn() {
        this.players[currentPlayersTurn].setTurn(false);

        this.currentPlayersTurn = (this.currentPlayersTurn + 1) % NUM_PLAYERS;
        this.players[currentPlayersTurn].setTurn(true);
    }

    // Getters
    public Player[] getPlayers() { return this.players; }
    public AdventureDeck getAdventureDeck() { return adventureDeck; }
    public EventDeck getEventDeck() { return eventDeck; }
    public Player getCurrentPlayer() { return this.players[this.currentPlayersTurn]; }
    public DiscardPile getDiscardedEventCards() { return discardedEventCards; }
}
