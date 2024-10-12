package org.carleton;

import java.io.PrintWriter;
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

}
