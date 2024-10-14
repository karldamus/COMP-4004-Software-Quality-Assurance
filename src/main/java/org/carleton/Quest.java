package org.carleton;

import java.io.PrintWriter;
import java.util.*;

public class Quest {
    private class Stage {
        private ArrayList<Card> cards;
        private int stageNumber;
        private int totalValue;

        public Stage(int stageNumber) {
            this.stageNumber = stageNumber;
            this.cards = new ArrayList<>();
            this.totalValue = 0;
        }

        public Stage(int stageNumber, ArrayList<Card> cards) {
            this.stageNumber = stageNumber;
            this.cards = cards;
        }

        public void addCardToStage(Card card) {
            this.cards.add(card);
            this.totalValue += card.getValue();
        }

        public ArrayList<Card> getCards() { return this.cards; }
    }

    private ArrayList<Stage> stages;
    private int numberOfStages;
    private Player[] players;
    private int indexOfSponsor;
    private Display display;
    private Scanner input;
    private PrintWriter output;

    public Quest(int numberOfStages, Player[] players, int indexOfSponsor, Display display, Scanner input, PrintWriter output) {
        this.numberOfStages = numberOfStages;
        this.players = players;
        this.indexOfSponsor = indexOfSponsor;
        this.display = display;
        this.input = input;
        this.output = output;

        this.stages = new ArrayList<>();

        this.setUpStages();
    }

    public void setUpStages() {
        // loop through each stage
        for (int i = 1; i <= numberOfStages; i++) {
            // create new stage
            stages.add(new Stage(i));
            Stage stage = stages.get(i - 1);

            // request input
            String inputResponse;

            do {
                inputResponse = display.requestSingleCardForStage(i, numberOfStages, players[indexOfSponsor], input, output);

                if (inputResponse.contains("devQuit-comp4004"))
                    break;
                if (inputResponse.contains("Quit")) {
                    if (!this.isViableStage(stage))
                        inputResponse = "Continuing";
                    else
                        display.stageAcceptedMessage(i, stages.get(i - 1).getCards(), output);
                } else {
                    attemptToAddCardToStage(inputResponse, i);
                }
            } while (!inputResponse.contains("Quit"));
        }
    }

    public void attemptToAddCardToStage(String input, int stageNumber) {
        try {
            int inputNum = Integer.parseInt(input.strip());

            if (inputNum > players[indexOfSponsor].getHand().size() || inputNum < 1) {
                String message = "Selected number exceeds range of hand.";
                display.singleMessage(message, output);
            } else {
                Card card = players[indexOfSponsor].getHand().remove(inputNum - 1);

                stages.get(stageNumber - 1).addCardToStage(card);
            }
        } catch (NumberFormatException e) {
            String message = "Input is invalid.\n";
            display.singleMessage(message, output);
        }
    }

    public boolean isValidIndexOfCardForStage(String input) {
        try {
            int inputNum = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public boolean isViableStage(Stage comparisonStage) {
        int totalValue = 0;
        int numberOfFoeCards = 0;

        for (Card card : comparisonStage.getCards()) {
            totalValue += card.getValue();

            if (card.getType() == 'F')
                numberOfFoeCards += 1;
        }

        if (totalValue == 0) {
            display.singleMessage("A stage cannot be empty.\n", output);
            return false;
        }

        if (numberOfFoeCards == 0) {
            display.singleMessage("A stage cannot contain 0 foe cards\n", output);
            return false;
        }

        boolean valueIsGreaterThanAllStages = true;

        for (int i = comparisonStage.stageNumber - 2; i >= 0; i--) {
            if (totalValue <= stages.get(i).totalValue)
                valueIsGreaterThanAllStages = false;
        }

        if (!valueIsGreaterThanAllStages) {
            display.singleMessage("Insufficient value for this stage.\n", output);
            return false;
        }

        return true;
    }


}
