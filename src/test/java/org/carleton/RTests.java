package org.carleton;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RTests {

    @Test
    @DisplayName("RESP-1-test-1")
    @Description("This tests the creation of an adventure and event deck. Verifies the size of the decks are as expected.")
    void RESP1Test1() {
        Game game = new Game();
        game.initDecks();
        AdventureDeck adventureDeck = game.getAdventureDeck();
        EventDeck eventDeck = game.getEventDeck();

        assertAll(
                () -> assertEquals(100, adventureDeck.getSize()),
                () -> assertEquals(12 + 5, eventDeck.getSize())
        );
    }

    @Test
    @DisplayName("RESP-1-test-2")
    @Description("This tests the validity of a new adventure deck.")
    void RESP1Test2() {
        Game game = new Game();
        game.initDecks();
        AdventureDeck adventureDeck = game.getAdventureDeck();
        ArrayList<Card> deck = adventureDeck.getAllCards();

        int sumOfFoeCardValues = 0, sumOfWeaponCardValues = 0;
        // x*y where there are y# of foe cards with value x
        int expectedSumOfFoeCardValues = 5*8+10*7+15*8+20*7+25*7+30*4+35*4+40*2+50*2+70*1;
        // x*y where there are y# of weapon cards with value x
        int expectedSumOfWeaponCardValues = 5*6+10*12+10*16+15*8+20*6+30*2;

        for (Card c : deck) {
            if (c.getType() == 'F')
                sumOfFoeCardValues += c.getValue();
            else
                sumOfWeaponCardValues += c.getValue();
        }

        int finalSumOfFoeCardValues = sumOfFoeCardValues;
        int finalSumOfWeaponCardValues = sumOfWeaponCardValues;

        assertAll(
                () -> assertEquals(expectedSumOfFoeCardValues, finalSumOfFoeCardValues),
                () -> assertEquals(expectedSumOfWeaponCardValues, finalSumOfWeaponCardValues)
        );
    }

    @Test
    @DisplayName("RESP-1-test-3")
    @Description("This tests the validity of a new event deck.")
    void RESP1Test3() {
        Game game = new Game();
        game.initDecks();
        EventDeck eventDeck = game.getEventDeck();
        ArrayList<Card> deck = eventDeck.getAllCards();

        int Q2Cards = 0, Q3Cards = 0, Q4Cards = 0, Q5Cards = 0;
        int plagueCards = 0, queensFavorCards = 0, prosperityCards = 0;

        for (Card c : deck) {
            if (c.getType() == 'Q') {
                switch (c.getValue()) {
                    case 2: Q2Cards += 1; break;
                    case 3: Q3Cards += 1; break;
                    case 4: Q4Cards += 1; break;
                    case 5: Q5Cards += 1; break;
                }
            }
            else if (c.getType() == 'E') {
                switch (c.getValue()) {
                    case 0: plagueCards += 1; break;
                    case 1: queensFavorCards += 1; break;
                    case 2: prosperityCards += 1; break;
                }
            }
        }

        int finalQ2Cards = Q2Cards;
        int finalQ3Cards = Q3Cards;
        int finalQ4Cards = Q4Cards;
        int finalQ5Cards = Q5Cards;
        int finalPlagueCards = plagueCards;
        int finalQueensFavorCards = queensFavorCards;
        int finalProsperityCards = prosperityCards;

        assertAll(
                () -> assertEquals(3, finalQ2Cards),
                () -> assertEquals(4, finalQ3Cards),
                () -> assertEquals(3, finalQ4Cards),
                () -> assertEquals(2, finalQ5Cards),
                () -> assertEquals(1, finalPlagueCards),
                () -> assertEquals(2, finalQueensFavorCards),
                () -> assertEquals(2, finalProsperityCards)
        );
    }

    // RESP-2 Tests
    // Ensure each player has 12 cards
    // Ensure deck has 48 fewer cards
    @Test
    @DisplayName("RESP-2-test-1")
    @Description("Test distributing 12 cards to each player")
    void RESP2Test1() {
        Game game = new Game();
        game.initDecks();
        game.initPlayers();
        game.dealCards();

        assertAll(
                () -> assertEquals(12, game.getPlayers()[0].getHand().size()),
                () -> assertEquals(12, game.getPlayers()[1].getHand().size()),
                () -> assertEquals(12, game.getPlayers()[2].getHand().size()),
                () -> assertEquals(12, game.getPlayers()[3].getHand().size()),
                () -> assertEquals(100 - (12 * 4), game.getAdventureDeck().getSize())
        );
    }

    @Test
    @DisplayName("RESP-5-test-1")
    @Description("Test the shuffling of a deck using statistical analysis")
    void RESP5Test1() {
        Game game = new Game();
        Game game2 = new Game();
        game.initDecks();
        game2.initDecks();

        Deck deck1 = game.getAdventureDeck();
        Deck deck2 = game2.getAdventureDeck();

        deck2.shuffle();

        ArrayList<Card> deck1_1 = deck1.getAllCards();
        ArrayList<Card> deck2_2 = deck2.getAllCards();

        int numChanged = 0;

        // for each card in deck1 and 2, check if they are different cards
        for (int i = 0; i < deck2_2.size(); i++) {
            if (deck2_2.get(i).getType() != deck1_1.get(i).getType()
            || deck2_2.get(i).getValue() != deck1_1.get(i).getValue()) {
                numChanged += 1;
            }
        }

        int minCardsChanged = deck2.getSize() / 5;

        assertTrue(numChanged > minCardsChanged);
    }

    @Test
    @DisplayName("RESP-6-test-1")
    @Description("This tests the creation of 4 new player objects and validates that player #1 starts")
    void RESP6Test1() {
        Game game = new Game();
        game.initPlayers();

        Player[] players = game.getPlayers();

        assertAll(
                () -> assertEquals(4, players.length),
                () -> assertTrue(players[0].isPlayersTurn())
        );
    }

    @Test
    @DisplayName("RESP-12-test-1")
    @Description("Game selects next player as the current player to play")
    void RESP12Test1() {
        Game game = new Game();
        game.initPlayers();

        Player[] players = game.getPlayers();

        Boolean[] playersTurns = new Boolean[]{false, false, false, false};

        for (int i = 0; i < players.length; i++) {
            playersTurns[i] = players[i].isPlayersTurn();
            game.endPlayersTurn();
        }

        boolean isAllTrue = Arrays.asList(playersTurns).stream().allMatch(val -> val == true);

        assertTrue(isAllTrue);
    }

    @Test
    @DisplayName("RESP-12-test-2")
    @Description("Game selects next players turn when it is currently the fourth players turn.")
    void RESP12Test2() {
        Game game = new Game();
        game.initPlayers();

        Player[] players = game.getPlayers();

        game.endPlayersTurn(); // end first players turn
        game.endPlayersTurn(); // end second players turn
        game.endPlayersTurn(); // end third players turn
        game.endPlayersTurn(); // end fourth players turn

        assertAll(
                () -> assertTrue(players[0].isPlayersTurn()),
                () -> assertFalse(players[1].isPlayersTurn()),
                () -> assertFalse(players[2].isPlayersTurn()),
                () -> assertFalse(players[3].isPlayersTurn())
        );
    }

}