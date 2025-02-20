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
        output.println(message);
        output.flush();
    }

    public int requestIntegerInput(String requestMessage, Scanner input, PrintWriter output) {
        output.printf(requestMessage); output.flush();

        String inputStr;

        try {
            inputStr = input.nextLine();
        } catch (Exception e) {
            return -1;
        }

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
        this.displayPlayersHand(sponsor, output);
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

    public boolean promptPlayerToRemainInQuest(Player player, Scanner input, PrintWriter output) {
        this.forceClearScreen(output);

        String message = "Player " + player.getPlayerNumber() + ": Would you like to remain in the quest? (y/n)";
        output.println(message);

        output.flush();

        String inputResponse = input.nextLine().toLowerCase().strip();

        if (inputResponse.equals("y"))
            return true;
        else if (inputResponse.equals("n"))
            return false;
        else {
            this.singleMessage("Invalid input, please enter 'y' or 'n'", output);
            return promptPlayerToRemainInQuest(player, input, output);
        }
    }

    public void showEligiblePlayers(String preMessage, ArrayList<Integer> eligiblePlayers, PrintWriter output) {
        output.print(preMessage);

        for (int i = 0; i < eligiblePlayers.size(); i++) {
            if (i + 1 == eligiblePlayers.size())
                output.println("P" + eligiblePlayers.get(i) + ".");
            else
                output.print("P" + eligiblePlayers.get(i) + ", ");
        }

        output.flush();
    }

    public boolean endTurn(int playersTurn, Scanner input, PrintWriter output) {
        output.println("End of player " + playersTurn + " turn.");
        output.flush();
        this.promptClearScreen(playersTurn - 1, input, output);

        String inputResponse = input.nextLine();

        return inputResponse.isEmpty();
    }

    public void showWinners(ArrayList<Integer> winners, PrintWriter output) {
        output.print("Game over! Winners: ");

        for (int i = 0; i < winners.size(); i++) {
            if (i + 1 == winners.size()) {
                output.println("Player " + winners.get(i) + "!");
            } else {
                output.print("Player " + winners.get(i) + ", ");
            }
        }

        output.flush();
    }

    public ArrayList<Card> promptPlayerToAttack(Player activeAttacker, Scanner input, PrintWriter output) {
        String inputResponse = "";
        ArrayList<Card> cardsInAttack = new ArrayList<>();

        while (true) {
            this.displayPlayersHand(activeAttacker, output);
            output.println("Enter position of card to include in attack. Or enter 'Quit' to submit your attack."); output.flush();
            output.print("Current cards in attack: ");
            for (Card card : cardsInAttack) {
                output.print(card.getType() + "" + card.getValue() + " ");
            }
            output.println(); output.flush();

            inputResponse = input.nextLine().strip();

            if (inputResponse.equalsIgnoreCase("quit")) {
                break;
            } else {
                try {
                    int inputNum = Integer.parseInt(inputResponse);
                    int inputIndex = inputNum - 1;

                    if (inputNum < 1 || inputNum > activeAttacker.getHand().size()) {
                        this.forceClearScreen(output);
                        output.println("Position of card does not exist.");
                    } else {
                        if (activeAttacker.getHand().get(inputIndex).getType() == 'F') {
                            this.forceClearScreen(output);
                            output.println("Selected card must be a weapon card, not a Foe card.");
                        }
                        else {
                            cardsInAttack.add(activeAttacker.getHand().remove(inputIndex));
                            this.forceClearScreen(output);
                        }
                    }
                } catch (NumberFormatException e) {
                    output.println("Invalid input."); output.flush();
                }
            }


        }

        return cardsInAttack;
    }

    public void successfulAttack(Player attacker, ArrayList<Card> cardsInAttack, PrintWriter output) {
        this.forceClearScreen(output);

        output.print("Player " + attacker.getPlayerNumber() + " attacks with ");

        for (int i = 0; i < cardsInAttack.size(); i++) {
            if (i+1 == cardsInAttack.size()) {
                output.print(cardsInAttack.get(i).getType() + "" + cardsInAttack.get(i).getValue() + ". ");
            } else {
                output.print(cardsInAttack.get(i).getType() + "" + cardsInAttack.get(i).getValue() + ", ");
            }
        }

        output.println("The attack is successful!");
        output.flush();
    }

    public void failedAttack(Player attacker, ArrayList<Card> cardsInAttack, PrintWriter output) {
        output.print("Player " + attacker.getPlayerNumber() + " attacks with ");

        if (cardsInAttack.isEmpty()) {
            output.print("nothing.");
        } else {
            for (int i = 0; i < cardsInAttack.size(); i++) {
                if (i+1 == cardsInAttack.size()) {
                    output.print(cardsInAttack.get(i).getType() + "" + cardsInAttack.get(i).getValue() + ". ");
                } else {
                    output.print(cardsInAttack.get(i).getType() + "" + cardsInAttack.get(i).getValue() + ", ");
                }
            }
        }

        output.println("The attack fails.");
        output.flush();
    }

    public void promptClearScreen(int currentPlayer, int nextPlayer, Scanner input, PrintWriter output) {
        try {
            do {
                output.println("Player " + currentPlayer + ": Press the Enter key to clear your screen.");
                output.flush();
            } while (!input.nextLine().equals(""));
        } catch (Exception e) {
            return;
        }

        for (int i = 0; i < 20; i++) {
            output.println("\n");
        }
        output.flush();

        try {
            do {
                output.println("Player " + nextPlayer + ": Press the Enter key to execute your task / start your turn.");
                output.flush();
            } while (!input.nextLine().equals(""));
        } catch (Exception e) {
            return;
        }

    }

    public void promptClearScreen(int playersTurnIndex, Scanner input, PrintWriter output) {
        int playersTurn = playersTurnIndex + 1;

        try {
            do {
                output.println("Player " + playersTurn + ": Press the Enter key to clear your screen.");
                output.flush();
            } while (!input.nextLine().equals(""));
        } catch (Exception e) {
            return;
        }

        for (int i = 0; i < 20; i++) {
            output.println("\n");
        }
        output.flush();

        int nextPlayer = (playersTurnIndex + 1 % 4) + 1;

        if (nextPlayer == 5)
            nextPlayer = 1;

        output.println("Player " + nextPlayer + ": Press the Enter key to execute your task / start your turn.");
        output.flush();
    }

    public void forceClearScreen(PrintWriter output) {
        for (int i = 0; i < 20; i++) {
            output.println("\n");
        }
        output.flush();
    }

    public void displayCurrentCardsInStage(Quest.Stage stage, PrintWriter output) {
        output.print("Current cards in stage: ");
        for (Card c : stage.getCards()) {
            output.print(c.getType() + "" + c.getValue() + " ");
        }
        output.println();
        output.flush();
    }

    public void displayPlayerDetails(Player player, PrintWriter output) {
        this.displayPlayersHand(player, output);
        output.println("Number of shields: " + player.getNumberOfShields());


        output.println();
        output.flush();
    }

    public void promptCompleteQuest(int sponsor, Scanner input, PrintWriter output) {
        try {
            do {
                output.println("Player " + sponsor + ": Press the Enter key to clear the screen.");
                output.flush();
            } while (!input.nextLine().equals(""));
        } catch (Exception e) {
            return;
        }

        for (int i = 0; i < 20; i++) {
            output.println("\n");
        }
        output.flush();

        output.println("Player " + sponsor + ": Press the Enter key to execute your task / start your turn.");
        output.flush();
    }
}
