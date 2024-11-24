package main.java.org.carleton;

import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    private static Scanner input = new Scanner(System.in);
    private static PrintWriter output = new PrintWriter(System.out);

    public static void main(String[] args) {
        Game game = new Game();
        game.initialize();
        game.setTextDisplay(input, output);



        game.questCard(3);
    }
}
