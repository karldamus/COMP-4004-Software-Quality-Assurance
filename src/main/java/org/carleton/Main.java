package org.carleton;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static Scanner input = new Scanner(System.in);
    private static PrintWriter output = new PrintWriter(System.out);

    public static void main(String[] args) {
        Game game = new Game();
        game.initPlayers();
        game.initDecks();
        game.getEventDeck().shuffle();
        game.getAdventureDeck().shuffle();
        game.dealCards();


        while (game.getWinners().isEmpty())
            game.playersTurn(input, output);
    }

}