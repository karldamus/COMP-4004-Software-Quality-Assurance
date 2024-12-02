package org.carleton;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class Main {
    private static Scanner input = new Scanner(System.in);
    private static PrintWriter output = new PrintWriter(System.out);

    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.setTextDisplay(input, output);

        while (!game.gameOver()) {
            game.playerTurn();
        }
    }


    private static ArrayList<ArrayList<Card>> getTestStartingHands(boolean questCardsRemoved) {
        ArrayList<Card> p1Hand = new ArrayList<>(Arrays.asList(
                new Card('F', 5),
                new Card('F', 5),
                new Card('F', 15),
                new Card('F', 15),
                new Card('D', 5),
                new Card('S', 10),
                new Card('S', 10),
                new Card('H', 10),
                new Card('H', 10),
                new Card('B', 15),
                new Card('B', 15),
                new Card('L', 20)
        ));

        ArrayList<Card> p2Hand;

        if (questCardsRemoved) {
            p2Hand = new ArrayList<>(Arrays.asList(
                    new Card('F', 5),
                    new Card('H', 10),
                    new Card('E', 30)
            ));
        } else {
            p2Hand = new ArrayList<>(Arrays.asList(
                    new Card('F', 5),
                    new Card('F', 5),
                    new Card('F', 15),
                    new Card('F', 15),
                    new Card('F', 40),
                    new Card('D', 5),
                    new Card('S', 10),
                    new Card('H', 10),
                    new Card('H', 10),
                    new Card('B', 15),
                    new Card('B', 15),
                    new Card('E', 30)
            ));
        }

        ArrayList<Card> p3Hand = new ArrayList<>(Arrays.asList(
                new Card('F', 5),
                new Card('F', 5),
                new Card('F', 5),
                new Card('F', 15),
                new Card('D', 5),
                new Card('S', 10),
                new Card('S', 10),
                new Card('S', 10),
                new Card('H', 10),
                new Card('H', 10),
                new Card('B', 15),
                new Card('L', 20)
        ));

        ArrayList<Card> p4Hand = new ArrayList<>(Arrays.asList(
                new Card('F', 5),
                new Card('F', 15),
                new Card('F', 15),
                new Card('F', 40),
                new Card('D', 5),
                new Card('D', 5),
                new Card('S', 10),
                new Card('H', 10),
                new Card('H', 10),
                new Card('B', 15),
                new Card('L', 20),
                new Card('E', 30)
        ));

        return new ArrayList<>(Arrays.asList(
                p1Hand, p2Hand, p3Hand, p4Hand
        ));
    }
}
