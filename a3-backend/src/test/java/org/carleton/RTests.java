package test.java.org.carleton;

import main.java.org.carleton.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class RTests {
    @Test
    @DisplayName("RESP-1-test-1")
    void RESP1Test1() {
        AdventureDeck deck = new AdventureDeck();
        assertEquals(100, deck.getSize());
    }

    @Test
    @DisplayName("RESP-1-test-2")
    void RESP1Test2() {
        EventDeck deck = new EventDeck();
        assertEquals(12 + 5, deck.getSize());
    }

    @Test
    @DisplayName("RESP-1-test-3")
    void RESP1Test3() {
        Game game = new Game();
        game.initializeDecks();
        assertAll(
                () -> assertEquals(100, game.getAdventureDeck().getSize()),
                () -> assertEquals(12 + 5, game.getEventDeck().getSize())
        );
    }

    @Test
    @DisplayName("RESP-1-test-4")
    void RESP1Test4() {
        AdventureDeck deck = new AdventureDeck();
        ArrayList<Card> cards = deck.getDeck();

        int sumOfFoeCardValues = 0, sumOfWeaponCardValues = 0;
        // x*y where there are y# of foe cards with value x
        int expectedSumOfFoeCardValues = 5*8+10*7+15*8+20*7+25*7+30*4+35*4+40*2+50*2+70*1;
        // x*y where there are y# of weapon cards with value x
        int expectedSumOfWeaponCardValues = 5*6+10*12+10*16+15*8+20*6+30*2;

        for (Card c : cards) {
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
    @DisplayName("RESP-1-test-5")
    void RESP1Test5() {
        EventDeck deck = new EventDeck();
        ArrayList<Card> cards = deck.getDeck();

        int Q2Cards = 0, Q3Cards = 0, Q4Cards = 0, Q5Cards = 0, plagueCards = 0, queensFavorCards = 0, prosperityCards = 0;

        for (Card c : cards) {
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

        int finalQ2Cards = Q2Cards; int finalQ3Cards = Q3Cards; int finalQ4Cards = Q4Cards;
        int finalQ5Cards = Q5Cards; int finalPlagueCards = plagueCards; int finalQueensFavorCards = queensFavorCards;
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

    @Test
    @DisplayName("RESP-2-test-1")
    void RESP2Test1() {
        Game game = new Game();
        game.initializePlayers();
        game.initializeDecks();
        game.dealCards();

        assertAll(
                () -> assertEquals(12, game.getPlayers().get(0).getHandSize()),
                () -> assertEquals(12, game.getPlayers().get(1).getHandSize()),
                () -> assertEquals(12, game.getPlayers().get(2).getHandSize()),
                () -> assertEquals(12, game.getPlayers().get(3).getHandSize()),
                () -> assertEquals(100 - (12*4), game.getAdventureDeck().getSize())
        );
    }

    @Test
    @DisplayName("RESP-8-test-1")
    void RESP8Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        game.setTextDisplay(new Scanner(""), new PrintWriter(output));

        game.getPlayers().get(0).addShields(4);

        game.plagueCard();

        assertTrue(output.toString().contains("Player 1 has 2 shields."));
    }

    @Test
    @DisplayName("RESP-9-test-1")
    void RESP9Test1() {
        Game game = new Game();
        game.initialize();

        game.getPlayers().getFirst().setHand(getF5Hands().getFirst());

        StringWriter output = new StringWriter();
        game.setTextDisplay(new Scanner("F5\nE2\nF5"), new PrintWriter(output));

        game.queensFavor();

        assertEquals(12, game.getPlayers().getFirst().getHandSize());
    }

    @Test
    @DisplayName("RESP-10-test-1")
    void RESP10Test1() {
        Game game = new Game();
        game.initialize();

        ArrayList<Hand> riggedHands = getF5Hands();
        for (Player player : game.getPlayers()) {
            player.setHand(riggedHands.removeFirst());
        }

        game.setAdventureDeck(getF5AdventureDeck());

        StringWriter output = new StringWriter();
        game.setTextDisplay(new Scanner("F5\nF5\n\n\nF5\nF5\n\n\nF5\nF5\n\n\nF5\nF5\n\n\n"), new PrintWriter(output));

        game.prosperity();

        assertAll(
                () -> assertEquals(12, game.getPlayers().get(0).getHandSize()),
                () -> assertEquals(12, game.getPlayers().get(1).getHandSize()),
                () -> assertEquals(12, game.getPlayers().get(2).getHandSize()),
                () -> assertEquals(12, game.getPlayers().get(3).getHandSize())
        );
    }

    @Test
    @DisplayName("RESP-11-test-1")
    void RESP11Test1() {
        Game game = new Game();
        game.initialize();

        game.getPlayers().getFirst().setHand(getF5Hands().getFirst());

        StringWriter output = new StringWriter();
        game.setTextDisplay(new Scanner(""), new PrintWriter(output));

        game.displayPlayerHand(game.getCurrentPlayersTurn());

        assertTrue(output.toString().contains("Player 1 hand: F5 F5 F5 F5 F5 F5 F5 F5 F5 F5 F5 F5"));
    }


    // =======
    // Helpers
    // =======


    ArrayList<Hand> getF5Hands() {
        ArrayList<Hand> hands = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Hand hand = new Hand();

            for (int j = 0; j < 12; j++) {
                hand.addCard(new Card('F', 5));
            }

            hands.add(hand);
        }

        return hands;
    }

    AdventureDeck getF5AdventureDeck() {
        AdventureDeck deck = new AdventureDeck();

        for (int i = 0; i < 50; i++) {
            deck.addCard(new Card('F', 5));
        }

        return deck;
    }
}
