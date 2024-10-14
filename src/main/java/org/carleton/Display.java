package org.carleton;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Display {
    public Display() { }

    public void displayPlayersHand(Player player, PrintWriter output) {
        player.sortHand();

        output.print("Player " + player.getPlayerNumber() + " Hand: ");

        for (Card c : player.getHand()) {
            output.print(c.getType());
            output.print(c.getValue());
            output.print(" ");
        }

        output.println();
        output.flush();
    }

    public void displayCurrentEventCard(Card c, PrintWriter output) {
        output.print("Event Card: ");

        if (c.getType() == 'Q') {
            output.print("QUEST - Stages: " + c.getValue());
        } else {
            switch (c.getValue()) {
                case 0: output.print("Plague"); break;
                case 1: output.print("Queen's Favor"); break;
                case 2: output.print("Prosperity"); break;
            }
        }

        output.println();
        output.flush();
    }

    public void singleMessage(String message, PrintWriter output) {
        output.printf(message);
        output.flush();
    }

    public int requestIntegerInput(String requestMessage, Scanner input, PrintWriter output) {
        output.printf(requestMessage); output.flush();

        String inputStr = input.nextLine();
        int inputNum = -1;

        try {
            inputNum = Integer.parseInt(inputStr);
        } catch (NumberFormatException e) {
            output.println("Input is not formatted as a number."); output.flush();
        }

        return inputNum;
    }

    public boolean requestToSponsorQuest(int numberOfQuestStages, Player player, Scanner input, PrintWriter output) {
        String message = "Player " + player.getPlayerNumber() + ": Would you like to sponsor this quest with " + numberOfQuestStages + " stages? (y/n)";
        output.println(message); output.flush();

        String inputResponse = (input.nextLine()).toLowerCase().strip();

        if (inputResponse.equals("y")) {
            return true;
        } else if (inputResponse.equals("n")) {
            return false;
        } else {
            output.println("Invalid input."); output.flush();
            return requestToSponsorQuest(numberOfQuestStages, player, input, output);
        }
    }

    public String requestSingleCardForStage(int currentStageNumber, int numberOfStages, Player sponsor, Scanner input, PrintWriter output) {
        String message = "Player " + sponsor.getPlayerNumber() + ": Enter number of a card to add to stage (" + currentStageNumber + "/" + numberOfStages + ") or enter 'Quit' to save current stage.";
        output.println(message); output.flush();

        return input.nextLine().strip();
    }

    public void stageAcceptedMessage(int stageNumber, ArrayList<Card> cards, PrintWriter output) {
        output.print("Stage " + stageNumber + " accepted. Cards included in stage: ");

        for (Card card : cards) {
            output.print(card.getType() + "" + card.getValue() + " ");
        }

        output.println(); output.flush();
    }

    public void startOfStage(Quest quest, PrintWriter output) {
        ArrayList<Integer> eligiblePlayers = quest.getEligiblePlayersForCurrentStage();

        output.println("QUEST Stage " + quest.getCurrentStage());
        output.print("Eligible players: ");

        for (int i = 0; i < eligiblePlayers.size(); i++) {
            Integer eligiblePlayerNumber = eligiblePlayers.get(i);

            if (i + 1 == eligiblePlayers.size())
                output.println("P" + eligiblePlayerNumber + ".");
            else
                output.print("P" + eligiblePlayerNumber + ", ");
        }

        output.flush();
    }
}
