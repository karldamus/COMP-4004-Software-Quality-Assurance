package org.carleton;

import java.util.ArrayList;
import java.util.Arrays;

public class Card implements Comparable<Card> {

    private char type;
    private int value;

    public Card(char type, int value) {
        this.type = type;
        this.value = value;
    }

    public char getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Card c) {
        // types are equal
        if (this.getType() == c.getType()) {
            // types are foes, can have different values
            if (this.getType() == 'F') {
                // compare values
                if (this.getValue() > c.getValue())
                    return 1;
                else if (this.getValue() < c.getValue())
                    return -1;
                else
                    return 0;
            }
            // types are weapons, can't have different values
            else {
                return 0;
            }
        }
        // types are not equal
        else {
            ArrayList<Character> orderOfAdventureTypes = new ArrayList<>(Arrays.asList('F', 'D', 'S', 'H', 'B', 'L', 'E'));

            int pos1 = orderOfAdventureTypes.indexOf(this.getType());
            int pos2 = orderOfAdventureTypes.indexOf(c.getType());

            if (pos1 > pos2)
                return 1;
            else if (pos1 < pos2)
                return -1;
            else
                return 0;
        }
    }
}
