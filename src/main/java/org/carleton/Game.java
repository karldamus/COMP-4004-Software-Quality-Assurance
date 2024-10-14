package org.carleton;

import java.io.PrintWriter;
import java.util.Scanner;

public class Game {
    private static final int NUM_PLAYERS = 4;

    private Player[] players;
    private AdventureDeck adventureDeck;
    private EventDeck eventDeck;
    private DiscardPile discardedEventCards;
    private int currentPlayersTurn;
    private Card currentEventCard;
    private Display display;
    private Quest activeQuest;


    public Game() {
        this.display = new Display();
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
            this.players[i].setPlayerNumber(i + 1);
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

    public Card drawEventCard(Scanner input, PrintWriter output) {
        this.currentEventCard = this.eventDeck.drawCard();

        display.displayCurrentEventCard(this.currentEventCard, output);

        switch(this.currentEventCard.getType()) {
            case 'E':
                if (this.currentEventCard.getValue() == 0)
                    this.players[currentPlayersTurn].plague();
                break;

            case 'Q':
                int indexOfPlayerToSponsorQuest = -1;

                for (int i = 0; i < this.players.length; i++) {
                    int currentIndex = (this.currentPlayersTurn + i) % this.players.length;
                    if (canPlayerSponsorQuest(this.currentEventCard.getValue(), players[currentIndex])) {
                        Boolean playerWillSponsorQuest = display.requestToSponsorQuest(this.currentEventCard.getValue(), players[currentIndex], input, output);
                        if (playerWillSponsorQuest) {
                            indexOfPlayerToSponsorQuest = currentIndex;
                        }
                    } else {
                        String message = "Player " + (currentIndex + 1) + " is unable to sponsor this quest.\n";
                        display.singleMessage(message, output);
                    }

                    if (indexOfPlayerToSponsorQuest != -1) {
                        // Create new Quest
                        this.activeQuest = new Quest(this.currentEventCard.getValue(), players, indexOfPlayerToSponsorQuest, this.display, input, output);

                        questLoop();

                        break;
                    }
                }

                if (indexOfPlayerToSponsorQuest == -1) {
                    String message = "All players declined or were unable to sponsor this quest.\n";
                    display.singleMessage(message, output);
                    this.endPlayersTurn(input, output);
                }

                break;
        }

        this.discardedEventCards.insertCard(this.currentEventCard);
        return this.currentEventCard;
    }

    public void questLoop() {
        for (int i = 1; i <= activeQuest.getNumberOfStages(); i++) {

            activeQuest.questContinuationPrompt();

            if (activeQuest.getEligiblePlayersForCurrentStage().isEmpty()) {
                endQuest();
                break;
            }

            activeQuest.endCurrentStage();
        }
    }

    public void endQuest() {

    }

    public boolean canPlayerSponsorQuest(int numberOfQuestStages, Player player) {
        int numFoeCards = 0;

        for (Card card : player.getHand()) {
            if (card.getType() == 'F')
                numFoeCards += 1;
        }

        return numFoeCards >= numberOfQuestStages;
    }

    public Card drawAdventureCard() {
        Card card = this.adventureDeck.drawCard();

        this.getCurrentPlayer().addCardToHand(card);

        return card;
    }

    public int computeNumberOfCardsToDiscard(Player player) {
        if (player.getHand().size() <= 12)
            return 0;
        else
            return player.getHand().size() - 12;
    }

    public void endPlayersTurn(Scanner input, PrintWriter output) {
        boolean endTurn = display.endTurn(currentPlayersTurn + 1, input, output);
        while (!endTurn) {
            endTurn = display.endTurn(currentPlayersTurn + 1, input, output);
        }

        this.players[currentPlayersTurn].setTurn(false);

        this.currentPlayersTurn = (this.currentPlayersTurn + 1) % NUM_PLAYERS;
        this.players[currentPlayersTurn].setTurn(true);
    }

    public void trimHand(Player player, Scanner input, PrintWriter output) {
        int numberOfCardsToDiscard = computeNumberOfCardsToDiscard(player);

        if (numberOfCardsToDiscard == 0)
            return;

        // make request
        String requestMessage = "Enter the position of a card to remove (1-" + player.getHand().size() + ")\n";
        int inputNum = display.requestIntegerInput(requestMessage, input, output);

        if (inputNum == -1)
            return;

        // analyze request
        if (inputNum < 1 || inputNum > player.getHand().size()) {
            String message = "Entered position is too high or low.\n";
            display.singleMessage(message, output);
        } else {
            int actualIndex = inputNum - 1;
            player.getHand().remove(actualIndex);

            String message = "Card " + inputNum + " Removed.\n";
            display.singleMessage(message, output);

            display.displayPlayersHand(player, output);
        }
    }

    // Getters
    public Player[] getPlayers() { return this.players; }
    public AdventureDeck getAdventureDeck() { return adventureDeck; }
    public EventDeck getEventDeck() { return eventDeck; }
    public Player getCurrentPlayer() { return this.players[this.currentPlayersTurn]; }
    public DiscardPile getDiscardedEventCards() { return discardedEventCards; }
    public Card getCurrentEventCard() { return this.currentEventCard; }
}
