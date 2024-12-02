package main.java.org.carleton;

import java.util.ArrayList;

public class AdventureDeck extends Deck {
    private class FoeCard extends Card {
        public FoeCard(int value) {
            super('F', value);
        }
    }

    private class WeaponCard extends Card {
        public WeaponCard(char type, int value) {
            super(type, value);
        }
    }

    public AdventureDeck() {
        this.setDeck(new ArrayList<>());

        int[] valueOfFoes = new int[]{5, 10, 15, 20, 25, 30, 35, 40, 50, 70};
        int[] numberOfFoes = new int[]{8, 7, 8, 7, 7, 4, 4, 2, 2, 1};

        for (int i = 0; i < valueOfFoes.length; i++) {
            for (int j = 0; j < numberOfFoes[i]; j++) {
                Card f = new FoeCard(valueOfFoes[i]);
                this.addCard(f);
            }
        }

        char[] typesOfWeapons = new char[]{'D', 'S', 'H', 'B', 'L', 'E'};
        int[] valuesOfWeapons = new int[]{5, 10, 10, 15, 20, 30};
        int[] numberOfWeapons = new int[]{6, 16, 12, 8, 6, 2};

        for (int i = 0; i < typesOfWeapons.length; i++) {
            for (int j = 0; j < numberOfWeapons[i]; j++) {
                Card w = new WeaponCard(typesOfWeapons[i], valuesOfWeapons[i]);
                this.addCard(w);
            }
        }
    }
}
