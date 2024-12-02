package main.java.org.carleton;

import java.io.PrintWriter;
import java.util.*;

public class Game {
    private AdventureDeck adventureDeck;
    private EventDeck eventDeck;
    private DiscardPile discardedEventCards;
    private DiscardPile discardedAdventureDeck;
    private ArrayList<Player> players;

    private int currentPlayersTurn;

    private TextDisplay textDisplay;
    Scanner input;
    PrintWriter output;
    private boolean useTextDisplay = true;

    private Quest activeQuest;

    public Game() {

    }

    public void playerTurn() {
        drawEventCard();

        requestClearScreen(currentPlayersTurn, getNextPlayerNumber(currentPlayersTurn));
        currentPlayersTurn = getNextPlayerNumber(currentPlayersTurn);
    }

    public void setTextDisplaySystemIO() {
        setTextDisplay(new Scanner(System.in), new PrintWriter(System.out));
    }

    public void setTextDisplay(Scanner input, PrintWriter output) {
        this.textDisplay = new TextDisplay();
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

        this.discardedAdventureDeck = new DiscardPile();
        this.discardedEventCards = new DiscardPile();

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
//                player.
        }
    }


    public void drawEventCard() {
        Card nextEventCard = eventDeck.drawCard();

        if (nextEventCard.getType() == 'Q') {
            // Quest Card
            int sponsorPlayerNumber = questCard(nextEventCard.getValue());

            if (sponsorPlayerNumber != -1) {
                Quest quest = initializeQuest(sponsorPlayerNumber, nextEventCard.getValue());

                while (quest.getActiveStage() <= quest.getNumberOfStages()) {
                    requestPlayersToContinue(quest);
                    playQuestStage(quest);
                }

                endQuest(quest);
                checkForGameWinners();

                drawSponsorCards(quest);
            } else {
                endQuest(null);
            }
        } else {
            // Event Card
            switch(nextEventCard.getValue()) {
                case 0: plagueCard(); break;
                case 1: queensFavor(); break;
                case 2: prosperity(); break;
            }
        }

        discardedEventCards.addCard(nextEventCard);
    }

    public void drawSponsorCards(Quest quest) {
        requestClearScreenSingle(quest.getSponsor());

        int numberOfCardsToDraw = 0;

        for (Quest.Stage stage : quest.getAllStages()) {
            for (Card card : stage.getCardsInStage()) {
                numberOfCardsToDraw += 1;
            }
        }

        numberOfCardsToDraw += quest.getNumberOfStages();

        for (int i = 1; i <= numberOfCardsToDraw; i++) {
            Card card = this.adventureDeck.drawCard();
            displayGameMessage("Drew card: " + card.toString());
            this.players.get(quest.getSponsor() - 1).addCardToHand(card);
        }

        while (needToTrimHand(quest.getSponsor())) {
            trimHand(quest.getSponsor());
        }

        displayPlayerHand(quest.getSponsor());
    }

    public void checkForGameWinners() {
        boolean winners = false;

        for (Player player : players) {
            if (player.getNumberOfShields() >= 7) {
                winners = true;
                break;
            }
        }

        if (winners) {
            displayGameMessage("==========");
            displayGameMessage("GAME OVER!");
            displayGameMessage("==========");
            for (Player player : players) {
                if (player.getNumberOfShields() >= 7) {
                    displayGameMessage("Winner: Player " + player.getPlayerNumber());
                }
            }
        }
    }

    public void endQuest(Quest quest) {
        if (quest == null) {
            displayGameMessage("No sponsor found for this quest.");
        } else {
            if (quest.hasWinners()) {
                for (int i : quest.getWinners()) {
                    players.get(i-1).addShields(quest.getNumberOfStages());
                    displayGameMessage("Quest winner: Player " + i + ". Shields awarded: " + quest.getNumberOfStages());
                }

                for (int i = 1; i <= 4; i++) {
                    displayPlayerShields(i);
                }
            } else {
                displayGameMessage("No winners for this quest.");
            }
        }
    }


    public void requestPlayersToContinue(Quest quest) {
        int stageNumber = quest.getActiveStage();

        ArrayList<Integer> activePlayers = quest.getActivePlayers();
        ArrayList<Integer> playersToRemove = new ArrayList<>();

        for (int i : activePlayers) {
            clearScreen();
            requestClearScreenSingle(i);
            displayPlayerInformation(i);
            String response = requestGameInput("Player " + i + ". Would you like to participate/remain in this quest? Stage " + quest.getActiveStage() + "/" + quest.getNumberOfStages());

            if (response.strip().equalsIgnoreCase("n")) {
                playersToRemove.add(i);
            }
        }

        clearScreen();

        quest.removeActivePlayers(playersToRemove);
    }

    public void playQuestStage(Quest quest) {
        int stageNumber = quest.getActiveStage();

        ArrayList<Integer> activePlayers = quest.getActivePlayers();

        for (int i : activePlayers) {
            requestClearScreenSingle(i);

            Card cardToDraw = adventureDeck.drawCard();
            players.get(i - 1).addCardToHand(cardToDraw);

            displayGameMessage("Drew card: " + cardToDraw);
            displayPlayerHand(i);

            while (needToTrimHand(i)) {
                trimHand(i);
            }

            buildAttack(quest, i);

            requestClearScreenSingleEnd(i);
        }

        ArrayList<Integer> playersToRemove = new ArrayList<>();

        HashMap<Integer, Boolean> completedQuest = quest.completeStage();

        for (Map.Entry<Integer, Boolean> pair : completedQuest.entrySet()) {
            if (!pair.getValue())
                playersToRemove.add(pair.getKey());

            String result = "Player " + pair.getKey() + (pair.getValue() ? " won this stage." : " lost this stage.");
            displayGameMessage(result);
        }

        quest.removeActivePlayers(playersToRemove);
    }

    public void buildAttack(Quest quest, int playerNumber) {
        ArrayList<Card> cardsInAttack = new ArrayList<>();

        while (true) {
            displayStageInfo(quest);
            displayCards("Cards included in attack:", cardsInAttack);
            displayPlayerHand(playerNumber);
            String response = requestGameInput("Enter a card to add to your attack. Enter quit to submit your attack");

            if (response.equalsIgnoreCase("quit"))
                break;

            try {
                if (response.charAt(0) == 'F') {
                    displayGameError("Cannot submit a foe card as an attack.");
                } else {
                    cardsInAttack.add(players.get(playerNumber - 1).getHand().getCard(response.strip(), true));
                }
            } catch (Exception e) {
                displayGameError("Invalid input.");
            }
        }

        if (cardsInAttack.isEmpty()) {
            displayGameMessage("You did not submit an attack. You automatically lose this quest.");
        } else {
            quest.submitAttackForCurrentStage(playerNumber, cardsInAttack);
        }

        for (Card card : cardsInAttack) {
            discardedAdventureDeck.addCard(card);
        }
    }

    public void displayStageInfo(Quest quest) {
        int activeStage = quest.getActiveStage();
        int numberOfStages = quest.getNumberOfStages();

        displayGameMessage("Submit an attack for this quest stage " + activeStage + "/" + numberOfStages + ".");
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

    public Quest initializeQuest(int sponsorPlayerNumber, int numStages) {
        int stageNumber = 1;

        Quest quest = new Quest(sponsorPlayerNumber, numStages);

        while (stageNumber <= numStages) {
            displayPlayerHand(sponsorPlayerNumber);
            displayGameMessage("Cards in stage " + stageNumber + ": " + quest.getStage(stageNumber).getCardsInStageAsString());
            String response = requestGameInput("Select cards to add to stage " + stageNumber + "/" + numStages + " or enter 'quit' to save this stage");

            if (response.equalsIgnoreCase("quit")) {
                QuestStageCodes validStage = quest.isValidStage(stageNumber);

                switch (validStage) {
                    case EMPTY_STAGE:
                        displayGameError("Stage cannot be empty."); break;
                    case NO_FOES_IN_STAGE:
                        displayGameError("Stage must contain at least 1 foe card."); break;
                    case INSUFFICIENT_VALUE_FOR_STAGE:
                        displayGameError("Total stage value must be greater than previous stages."); break;
                    case OKAY_STAGE:
                        displayGameMessage("Stage submitted with cards: " + quest.getStage(stageNumber).getCardsInStageAsString());
                        stageNumber += 1;
                }
            } else {
                try {

                    Hand hand = this.getPlayers().get(sponsorPlayerNumber - 1).getHand();

                    if (hand.contains(response))
                        quest.addCardToStage(stageNumber, this.getPlayers().get(sponsorPlayerNumber - 1).getHand().getCard(response, true));
                    else
                        displayGameError("Card '" + response + "' not in your hand.");

                } catch (Exception e) {
                    displayGameError("Invalid input.");
                }
            }
        }

        requestClearScreenSingleEnd(sponsorPlayerNumber);

        return quest;
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

    public int getNextPlayerNumber(int currentPlayerNumber) {
        int nextPlayer = (currentPlayerNumber + 1 % 4);

        if (nextPlayer == 5)
            nextPlayer = 1;

        return nextPlayer;
    }

    // =======
    // DISPLAY
    //

    public void requestClearScreenSingle(int player) {
        requestGameInput("Player " + player + " please press enter to start your task");
        clearScreen();
    }

    public void requestClearScreenSingleEnd(int player) {
        requestGameInput("Player " + player + " please press enter to clear your screen");
        clearScreen();
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
//        player.getHand().sort();
        player.getHand().newSort();
        String hand = player.getHand().getHandAsString();
        displayGameMessage("Player " + playerNumber + " hand: " + hand);
    }

    public void displayCards(String prepend, ArrayList<Card> cards) {
        StringBuilder result = new StringBuilder();

        result.append(prepend).append(" ");

        for (Card card : cards) {
            result.append(card.toString()).append(" ");
        }

        displayGameMessage(result.toString());
    }


    // ==============
    public AdventureDeck getAdventureDeck() { return this.adventureDeck; }
    public EventDeck getEventDeck() { return this.eventDeck; }
    public ArrayList<Player> getPlayers() { return this.players; }
    public int getCurrentPlayersTurn() { return currentPlayersTurn; }

    public void setAdventureDeck(AdventureDeck adventureDeck) {
        this.adventureDeck = adventureDeck;
    }

    public void setCurrentPlayersTurn(int i) {
        this.currentPlayersTurn = i;
    }

    public DiscardPile getDiscardedEventCards() {
        return discardedEventCards;
    }

    public DiscardPile getDiscardedAdventureDeck() {
        return discardedAdventureDeck;
    }
}
