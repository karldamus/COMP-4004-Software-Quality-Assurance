package org.carleton;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;

public class Game {
    private static final int NUM_PLAYERS = 4;

    private Player[] players;
    private AdventureDeck adventureDeck;
    private EventDeck eventDeck;
    private DiscardPile discardedEventCards;
    private DiscardPile discardedAdventureCards;
    private int currentPlayersTurn;
    private Card currentEventCard;
    private Display display;
    private Quest activeQuest;
    private boolean newGame;

    ArrayList<Integer> winners = new ArrayList<>();


    public Game() {
        this.display = new Display();
        this.newGame = true;
    }

    public void init() {
        this.initDecks();
        this.initPlayers();
    }

    public void playersTurn(Scanner input, PrintWriter output) {
        if (newGame) {
            display.singleMessage("***************************", output);
            display.singleMessage("WELCOME TO A GAME OF QUESTS", output);
            display.singleMessage("***************************", output);
            display.singleMessage("", output);
            this.newGame = false;
        }

        display.singleMessage("Player " + getCurrentPlayer().getPlayerNumber() + "'s turn.", output);
        display.displayPlayerDetails(getCurrentPlayer(), output);
        this.drawEventCard(input, output);

        this.endPlayersTurn(input, output);
    }

    public void initDecks() {
        this.adventureDeck = new AdventureDeck();
        this.eventDeck = new EventDeck();
        this.discardedEventCards = new DiscardPile();
        this.discardedAdventureCards = new DiscardPile();
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
                if (this.currentEventCard.getValue() == 0) {
                    this.players[currentPlayersTurn].plague();
                    break;
                }
                if (this.currentEventCard.getValue() == 1) {
                    this.drawAdventureCard(this.getCurrentPlayer(), output);
                    this.drawAdventureCard(this.getCurrentPlayer(), output);
                    display.displayPlayersHand(this.getCurrentPlayer(), output);
                    this.trimHand(getCurrentPlayer(), input, output);
                    break;
                }

                if (this.currentEventCard.getValue() == 2) {
                    boolean firstThrough = true;
                    for (Player player : players) {
                        if (firstThrough) {
                            display.promptClearScreen(currentPlayersTurn + 1, player.getPlayerNumber(), input, output);
                            firstThrough = false;
                        }
                        else {
                            display.promptClearScreen(player.getPlayerNumber() - 1, player.getPlayerNumber(), input, output);
                        }
                        this.drawAdventureCard(player, output);
                        this.drawAdventureCard(player, output);
                        display.displayPlayersHand(player, output);
                        this.trimHand(player, input, output);

                        if (player.getPlayerNumber() == 4)
                            display.forceClearScreen(output);
                    }
                    break;
                }

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
                        display.forceClearScreen(output);

                        String message = "Player " + (indexOfPlayerToSponsorQuest + 1) + " you are sponsoring this quest with " + this.currentEventCard.getValue() + " stages.";
                        display.singleMessage(message, output);

                        // Create new Quest
                        this.activeQuest = new Quest(this.currentEventCard.getValue(), players, indexOfPlayerToSponsorQuest, this.display, input, output);

                        questLoop(input, output);

                        break;
                    }
                }

                if (indexOfPlayerToSponsorQuest == -1) {
                    String message = "All players declined or were unable to sponsor this quest.\n";
                    display.singleMessage(message, output);
                }

                break;
        }

        this.discardedEventCards.insertCard(this.currentEventCard);
        return this.currentEventCard;
    }

    public void questLoop(Scanner input, PrintWriter output) {
        for (int i = 1; i <= activeQuest.getNumberOfStages(); i++) {

            activeQuest.questContinuationPrompt();

            if (activeQuest.getEligiblePlayersForCurrentStage().isEmpty()) {
                endQuest();
                break;
            }

            ArrayList<Integer> failedPlayers = new ArrayList<>();

            for (Integer playerNumber : activeQuest.getEligiblePlayersForCurrentStage()) {
                Player activeAttacker = players[playerNumber - 1];

                this.drawAdventureCard(activeAttacker, output);
                this.trimHand(activeAttacker, input, output);

                ArrayList<Card> cardsInAttack = display.promptPlayerToAttack(activeAttacker, input, output);

                int totalAttackValue = 0;
                for (Card card : cardsInAttack)
                    totalAttackValue += card.getValue();

                if (totalAttackValue >= activeQuest.getTotalValueOfCurrentStage()) {
                    display.successfulAttack(activeAttacker, cardsInAttack, output);
                } else {
                    display.failedAttack(activeAttacker, cardsInAttack, output);
//                    activeQuest.removePlayerFromQuest(activeAttacker.getPlayerNumber());
                    failedPlayers.add(activeAttacker.getPlayerNumber());

                    if (activeQuest.getEligiblePlayersForCurrentStage().isEmpty())
                        break;
                }

                // discard cards used in attack
                for (int j = 0; j < cardsInAttack.size(); j++) {
                    discardedAdventureCards.insertCard(cardsInAttack.remove(j));
                }
            }

            for (Integer failedPlayer : failedPlayers) {
                activeQuest.removePlayerFromQuest(failedPlayer);
            }

            activeQuest.endCurrentStage();
        }

        if (activeQuest.getEligiblePlayersForCurrentStage().isEmpty()) {
            String message = "No winners for this quest.";
            display.singleMessage(message, output);
        } else {
            for (int i = 0; i < activeQuest.getEligiblePlayersForCurrentStage().size(); i++) {
                int indexOfPlayer = activeQuest.getEligiblePlayersForCurrentStage().get(i) - 1;

                players[indexOfPlayer].awardShields(activeQuest.getNumberOfStages());

                display.singleMessage("", output);
                display.singleMessage("*******************************", output);
                display.singleMessage("       QUEST COMPLETE!         ", output);
                display.singleMessage("*******************************", output);
                display.singleMessage("", output);
                display.singleMessage("Results of quest:", output);
                String message = "  Awarded " + activeQuest.getNumberOfStages() + " shields to player " + activeQuest.getEligiblePlayersForCurrentStage().get(i) + "!";
                display.singleMessage(message, output);
                display.singleMessage("", output);
            }
        }

        display.promptCompleteQuest(activeQuest.getIndexOfSponsor() + 1, input, output);

        // sponsor activity
        for (Quest.Stage stage : activeQuest.getStages()) {
            for (Card card : stage.getCards()) {
                discardedAdventureCards.insertCard(card);
                this.drawAdventureCard(players[activeQuest.getIndexOfSponsor()], output);
            }
        }

        for (int i = 1; i <= activeQuest.getNumberOfStages(); i++)
            this.drawAdventureCard(players[activeQuest.getIndexOfSponsor()], output);

        this.trimHand(players[activeQuest.getIndexOfSponsor()], input, output);
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

    public Card drawAdventureCard(Player player, PrintWriter output) {
        Card card = this.adventureDeck.drawCard();

        player.addCardToHand(card);

        String message = "Player " + player.getPlayerNumber() + ": You drew an adventure card: " + card.getType() + "" + card.getValue();
        display.singleMessage(message, new PrintWriter(output));
        display.displayPlayersHand(player, new PrintWriter(output));

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

        checkForWinners(output);
        if (!winners.isEmpty())
            return;

        this.players[currentPlayersTurn].setTurn(false);

        this.currentPlayersTurn = (this.currentPlayersTurn + 1) % NUM_PLAYERS;
        this.players[currentPlayersTurn].setTurn(true);
    }

    public void checkForWinners(PrintWriter output) {
        winners = new ArrayList<>();

        for (Player player : players) {
            if (player.getNumberOfShields() >= 7)
                winners.add(player.getPlayerNumber());
        }

        if (!winners.isEmpty()) {
            display.showWinners(winners, output);
        }
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

        if (computeNumberOfCardsToDiscard(player) > 0)
            trimHand(player, input, output);
    }

    // Getters
    public Player[] getPlayers() { return this.players; }
    public AdventureDeck getAdventureDeck() { return adventureDeck; }
    public EventDeck getEventDeck() { return eventDeck; }
    public Player getCurrentPlayer() { return this.players[this.currentPlayersTurn]; }
    public DiscardPile getDiscardedEventCards() { return discardedEventCards; }
    public Card getCurrentEventCard() { return this.currentEventCard; }
    public DiscardPile getDiscardedAdventureCards() {
        return this.discardedAdventureCards;
    }
    public ArrayList<Integer> getWinners() { return this.winners; }
}
