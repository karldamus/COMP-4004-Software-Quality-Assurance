package org.carleton;

import java.util.ArrayList;

public class EventDeck extends Deck {
    private class QuestCard extends Card {
        public QuestCard(int value) {
            super('Q', value);
        }
    }
    private class EventCard extends Card {
        public EventCard(int value) {
            super('E', value);
        }
    }

    public EventDeck() {
        this.setDeck(new ArrayList<>());

        int[] typesOfQuestCards = new int[]{2, 3, 4, 5};
        int[] numberOfQuestCards = new int[]{3, 4, 3, 2};

        for (int i = 0; i < typesOfQuestCards.length; i++) {
            for (int j = 0; j < numberOfQuestCards[i]; j++) {
                Card q = new QuestCard(typesOfQuestCards[i]);
                this.addCard(q);
            }
        }

        // One Plague card, 2 Queens Favor cards, 2 Prosperity cards.
        // Denoted by the value of the card. Plague = 0, Queen Favor = 1, Prosperity = 2.
        int[] valueOfEventCards = new int[]{0, 1, 2};
        int[] numberOfEventCards = new int[]{1, 2, 2};

        for (int i = 0; i < valueOfEventCards.length; i++) {
            for (int j = 0; j < numberOfEventCards[i]; j++) {
                Card e = new EventCard(valueOfEventCards[i]);
                this.addCard(e);
            }
        }
    }
}
