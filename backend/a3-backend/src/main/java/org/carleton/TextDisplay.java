package org.carleton;

import java.io.PrintWriter;
import java.util.Scanner;

public class TextDisplay {
    public TextDisplay() {

    }

    public void singleMessage(String message, PrintWriter output) {
        output.println(message);
        output.flush();
    }

    public String getInput(String message, Scanner input, PrintWriter output) {
        output.printf(message + ": ");
        output.flush();

        String inputStr;

        try {
            inputStr = input.nextLine();
        } catch (Exception e) {
            return null;
        }

        output.println();
        output.flush();

        return inputStr;
    }

    public void errorMessage(String error, PrintWriter output) {
        output.println("ERROR: " + error);
        output.flush();
    }

    public void clearScreen(PrintWriter output) {
        for (int i = 0; i < 20; i++) {
            output.println();
        }

        output.flush();
    }
}
