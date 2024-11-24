package main.java.org.carleton;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private AdventureDeck adventureDeck;
    private EventDeck eventDeck;
    private ArrayList<Player> players;

    private int currentPlayersTurn;

    private Display textDisplay;
    Scanner input;
    PrintWriter output;
    private boolean useTextDisplay = true;

    private Quest activeQuest;

    public Game() {

    }

    public void setTextDisplaySystemIO() {
        setTextDisplay(new Scanner(System.in), new PrintWriter(System.out));
    }

    public void setTextDisplay(Scanner input, PrintWriter output) {
        this.textDisplay = new Display();
        this.setInput(input);
        this.setOutput(output);
    }

    public void setInput(Scanner input) {
        this.input = input;
    }

    public void setOutput(PrintWriter output) {
        this.output = output;
    }

    public void initialize() {
        this.initializePlayers();
        this.initializeDecks();
        this.dealCards();
    }

    public void initializeDecks() {
        this.adventureDeck = new AdventureDeck();
        this.eventDeck = new EventDeck();

        this.adventureDeck.shuffle();
        this.eventDeck.shuffle();
    }

    public void initializePlayers() {
        this.players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            this.players.add(new Player(i + 1));
        }

        this.currentPlayersTurn = 1;
    }

    public void dealCards() {
        for (int i = 0; i < 12; i++) {
            for (Player player : players)
                player.addCardToHand(this.adventureDeck.drawCard());
        }
    }

    public void startOfPlayersTurn() {

    }

    public void drawEventCard() {
        Card nextEventCard = eventDeck.drawCard();

        if (nextEventCard.getType() == 'Q') {
            // Quest Card
            int sponsorPlayerNumber = questCard(nextEventCard.getValue());
            if (sponsorPlayerNumber != -1) {
                initializeQuest(sponsorPlayerNumber, nextEventCard.getValue());
            }
        } else {
            // Event Card
            switch(nextEventCard.getValue()) {
                case 0: plagueCard(); break;
                case 1: queensFavor(); break;
                case 2: prosperity(); break;
            }
        }
    }

    public int questCard(int numStages) {
        displayGameMessage("Quest with " + numStages + " stages drawn!");

        int currPlayer = this.getCurrentPlayersTurn();
        int sponsorPlayerNumber = -1;

        for (int i = 0; i < 4; i++) {
            displayPlayerHand(currPlayer);
            String willSponsor = requestGameInput("Player " + currPlayer + " would you like to sponsor a quest with " + numStages + " stages (y/n)");

            if (willSponsor.toLowerCase().strip().equals("y")) {
                sponsorPlayerNumber = currPlayer;

                break;
            } else {
                int nextPlayer = getNextPlayerNumber(currPlayer);
                requestClearScreen(currPlayer, nextPlayer);
                currPlayer = nextPlayer;
            }
        }

        return sponsorPlayerNumber;
    }

    public void initializeQuest(int sponsorPlayerNumber, int numStages) {

    }

    public void plagueCard() {
        displayGameMessage("Plague card drawn. Lose 2 shields.");
        this.players.get(currentPlayersTurn - 1).removeShields(2);
        displayPlayerInformation(currentPlayersTurn);
    }

    public void queensFavor() {
        Card c1 = this.adventureDeck.drawCard();
        Card c2 = this.adventureDeck.drawCard();
        displayGameMessage("Queen's Favor card drawn. Drawing 2 adventure cards: " + c1.toString() + ", " + c2.toString());
        this.players.get(currentPlayersTurn - 1).getHand().addCard(c1);
        this.players.get(currentPlayersTurn - 1).getHand().addCard(c2);
        displayPlayerHand(currentPlayersTurn);

        while (needToTrimHand(currentPlayersTurn)) {
            trimHand(currentPlayersTurn);
        }
    }

    public boolean needToTrimHand(int playerNumber) {
        return this.players.get(playerNumber - 1).getHandSize() > 12;
    }

    public void trimHand(int playerNumber) {
        Hand hand = this.players.get(playerNumber - 1).getHand();

        String cardToRemoveAsString = requestGameInput("Enter a card to remove (ex. F5)");

        if (hand.contains(cardToRemoveAsString)) {
            hand.remove(cardToRemoveAsString);
            displayGameMessage("Removed card " + cardToRemoveAsString);
            displayPlayerHand(playerNumber);
        }
        else {
            displayGameError("You don't have card '" + cardToRemoveAsString + "' in your hand.");
        }
    }

    public void prosperity() {
        displayGameMessage("Prosperity card drawn. Each player in order 1-4 will draw 2 adventure cards.");

        if (currentPlayersTurn != 1)
            requestClearScreen(currentPlayersTurn, 1);

        for (int currentProsperityPlayer = 1; currentProsperityPlayer <= 4; currentProsperityPlayer++) {

            Card c1 = this.adventureDeck.drawCard();
            Card c2 = this.adventureDeck.drawCard();
            displayGameMessage("Player " + currentProsperityPlayer + " you drew " + c1.toString() + ", " + c2.toString());
            this.players.get(currentProsperityPlayer - 1).getHand().addCard(c1);
            this.players.get(currentProsperityPlayer - 1).getHand().addCard(c2);

            displayPlayerHand(currentProsperityPlayer);

            while (needToTrimHand(currentProsperityPlayer)) {
                trimHand(currentProsperityPlayer);
            }

            requestClearScreen(currentProsperityPlayer, getNextPlayerNumber(currentProsperityPlayer));
        }
    }

    public void requestClearScreen(int currentPlayer, int nextPlayer) {
        requestGameInput("Player " + currentPlayer + " please press enter to clear your screen");
        clearScreen();
        requestGameInput("Player " + nextPlayer + " please press enter to start your task");
        clearScreen();
    }

    public void clearScreen() {
        if (useTextDisplay) {
            textDisplay.clearScreen(output);
        }
    }

    public int getNextPlayerNumber(int currentPlayerNumber) {
        int nextPlayer = (currentPlayerNumber + 1 % 4);

        if (nextPlayer == 5)
            nextPlayer = 1;

        return nextPlayer;
    }

    public String requestGameInput(String message) {
        if (useTextDisplay) {
            String input = textDisplay.getInput(message, this.input, output);
            return input.strip();
        }

        return "";
    }

    public void displayGameMessage(String message) {
        if (useTextDisplay) {
            textDisplay.singleMessage(message, output);
        }
    }

    public void displayGameError(String error) {
        if (useTextDisplay) {
            textDisplay.errorMessage(error, output);
        }
    }

    public void displayPlayerInformation(int playerNumber) {
        displayGameMessage("Player " + playerNumber);
        displayPlayerShields(playerNumber);
        displayPlayerHand(playerNumber);
    }

    public void displayPlayerShields(int playerNumber) {
        Player player = players.get(playerNumber - 1);
        displayGameMessage("Player " + playerNumber + " has " + player.getNumberOfShields() + " shields.");
    }

    public void displayPlayerHand(int playerNumber) {
        Player player = players.get(playerNumber - 1);
        player.getHand().sort();
        String hand = player.getHand().getHandAsString();
        displayGameMessage("Player " + playerNumber + " hand: " + hand);
    }

    public void getInput() {
        if (useTextDisplay) {

        }
    }


    // ==============
    public AdventureDeck getAdventureDeck() { return this.adventureDeck; }
    public EventDeck getEventDeck() { return this.eventDeck; }
    public ArrayList<Player> getPlayers() { return this.players; }
    public int getCurrentPlayersTurn() { return currentPlayersTurn; }

    public void setAdventureDeck(AdventureDeck adventureDeck) {
        this.adventureDeck = adventureDeck;
    }
}
