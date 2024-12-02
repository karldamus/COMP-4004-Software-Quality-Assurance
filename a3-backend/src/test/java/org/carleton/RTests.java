package test.java.org.carleton;

import main.java.org.carleton.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
    @DisplayName("RESP-3-test-1")
    void RESP3Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        game.getPlayers().get(0).addShields(7);
        game.checkForGameWinners();

        assertTrue(output.toString().contains("Winner: Player 1"));
    }

    @Test
    @DisplayName("RESP-6-test-1")
    void RESP6Test1() {
        Game game = new Game();
        game.initialize();

        assertAll(
                () -> assertEquals(4, game.getPlayers().size()),
                () -> assertEquals(1, game.getCurrentPlayersTurn())
        );
    }

    @Test
    @DisplayName("RESP-7-test-1")
    void RESP7Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = enterOverride();
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        for (int i = 0; i < 4; i++) {
            game.getPlayers().get(i).setHand(new Hand());
        }

        for (int i = 0; i < 3; i++) {
            game.getEventDeck().setTopCard(new Card('E', i));
            game.drawEventCard();
        }

        assertAll(
                () -> assertTrue(output.toString().contains("Plague")),
                () -> assertTrue(output.toString().contains("Queen's Favor")),
                () -> assertTrue(output.toString().contains("Prosperity"))
        );
    }

    @Test
    @DisplayName("RESP-8-test-1")
    void RESP8Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        game.setTextDisplay(new Scanner(""), new PrintWriter(output));

        game.getPlayers().get(0).addShields(0);

        game.plagueCard();

        assertTrue(output.toString().contains("Player 1 has 0 shields."));
    }

    @Test
    @DisplayName("RESP-8-test-2")
    void RESP8Test2() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        game.setTextDisplay(new Scanner(""), new PrintWriter(output));

        game.getPlayers().get(0).addShields(1);

        game.plagueCard();

        assertTrue(output.toString().contains("Player 1 has 0 shields."));
    }

    @Test
    @DisplayName("RESP-8-test-3")
    void RESP8Test3() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        game.setTextDisplay(new Scanner(""), new PrintWriter(output));

        game.getPlayers().get(0).addShields(6);

        game.plagueCard();

        assertTrue(output.toString().contains("Player 1 has 4 shields."));
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


    @Test
    @DisplayName("RESP-12-test-1")
    void RESP12Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = enterOverride();
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        game.getEventDeck().setTopCard(new Card('E', 0));

        game.playerTurn();

        assertEquals(2, game.getCurrentPlayersTurn());
    }

    @Test
    @DisplayName("RESP-12-test-2")
    void RESP12Test2() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = enterOverride();
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        game.setCurrentPlayersTurn(4);

        game.getEventDeck().setTopCard(new Card('E', 0));
        game.playerTurn();

        assertEquals(1, game.getCurrentPlayersTurn());
    }

    @Test
    @DisplayName("RESP-13-test-1")
    void RESP13Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = enterOverride();
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        game.getEventDeck().setTopCard(new Card('E', 0));
        game.playerTurn();

        assertAll(
                () -> assertEquals('E', game.getDiscardedEventCards().getDeck().getFirst().getType()),
                () -> assertEquals(0, game.getDiscardedEventCards().getDeck().getFirst().getValue())
        );
    }

    @Test
    @DisplayName("RESP-14-test-1")
    void RESP14Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = enterOverride();
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        game.getPlayers().getFirst().addCardToHand(new Card('F', 5));

        game.displayPlayerHand(1);

        assertTrue(game.needToTrimHand(1));
    }

    @Test
    @DisplayName("RESP-15-test-1")
    void RESP15Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "F5\n";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        game.getPlayers().getFirst().addCardToHand(new Card('F', 5));

        game.trimHand(1);

        assertAll(
                () -> assertEquals(12, game.getPlayers().getFirst().getHandSize()),
                () -> assertTrue(output.toString().contains("Removed card F5"))
        );
    }

    @Test
    @DisplayName("RESP-15-test-2")
    void RESP15Test2() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "1\nF5\n";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        game.getPlayers().getFirst().addCardToHand(new Card('F', 5));

        game.trimHand(1);

        assertAll(
                () -> assertTrue(output.toString().contains("ERROR: You don't have card '1' in your hand."))
        );
    }

    @Test
    @DisplayName("RESP-16-test-1")
    void RESP16Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "n\n\n\nn\n\n\nn\n\n\nn\n\n\n";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        game.getEventDeck().setTopCard(new Card('Q', 1));

        game.drawEventCard();

        assertTrue(output.toString().contains("No sponsor found for this quest"));
    }

    @Test
    @DisplayName("RESP-17-test-1")
    void RESP17Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "F5\nquit\nF15\nquit\n\n";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        game.getPlayers().getFirst().setHand(new Hand(getTestStartingHands(false).getFirst()));
        game.initializeQuest(1, 2);

        assertAll(
                () -> assertTrue(output.toString().contains("Stage submitted with cards: F5")),
                () -> assertTrue(output.toString().contains("Stage submitted with cards: F15"))
        );
    }

    @Test
    @DisplayName("RESP-19-test-1")
    void RESP19Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "\nn\n\ny\n\nn\n";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        game.getPlayers().getFirst().setHand(new Hand(getTestStartingHands(false).getFirst()));

        Quest quest = new Quest(1, 1);

        game.requestPlayersToContinue(quest);

        assertAll(
                () -> assertEquals(3, quest.getActivePlayers().get(0)),
                () -> assertEquals(1, quest.getActivePlayers().size())
        );
    }

    @Test
    @DisplayName("RESP-20-test-1")
    void RESP20Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "\n\n";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        game.requestClearScreen(1, 2);

        assertAll(
                () -> assertTrue(output.toString().contains("Player 1 please press enter to clear your screen")),
                () -> assertTrue(output.toString().contains("Player 2 please press enter to start your task"))
        );
    }

    @Test
    @DisplayName("RESP-21-test-1")
    void RESP21Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "\nE30\nquit\n\n";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        Quest quest = new Quest(1, 1);

        quest.getStage(1).addCardToStage(new Card('F', 5));
        quest.getActivePlayers().remove(1);
        quest.getActivePlayers().remove(1);

        game.getPlayers().get(1).getHand().setHand(new ArrayList<Card>(
                Arrays.asList(
                        new Card('F', 5),
                        new Card('E', 30)
                )
        ));

        game.playQuestStage(quest);
        assertTrue(output.toString().contains("Player 2 won this stage."));
    }

    @Test
    @DisplayName("RESP-22-test-1")
    void RESP22Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "\n\nquit\n\n";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        Quest quest = new Quest(1, 1);

        quest.getStage(1).addCardToStage(new Card('F', 5));
        quest.getActivePlayers().remove(1);
        quest.getActivePlayers().remove(1);

        game.getPlayers().get(1).getHand().setHand(new ArrayList<>());

        game.playQuestStage(quest);

        assertTrue(output.toString().contains("You did not submit an attack. You automatically lose this quest."));
    }

    @Test
    @DisplayName("RESP-23-test-1")
    public void RESP23Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "L20\nB15\nquit\n";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        Quest quest = new Quest(1, 1);

        quest.getStage(1).addCardToStage(new Card('F', 5));
        quest.getActivePlayers().remove(1);
        quest.getActivePlayers().remove(1);

        game.getPlayers().get(1).setHand(new Hand(getTestStartingHands(false).getFirst()));
        game.buildAttack(quest, 2);

        assertAll(
                () -> assertEquals('L', game.getDiscardedAdventureDeck().getDeck().getFirst().getType()),
                () -> assertEquals(20, game.getDiscardedAdventureDeck().getDeck().getFirst().getValue()),
                () -> assertEquals('B', game.getDiscardedAdventureDeck().getDeck().get(1).getType()),
                () -> assertEquals(15, game.getDiscardedAdventureDeck().getDeck().get(1).getValue())
        );
    }

    @Test
    @DisplayName("RESP-24-test-1")
    public void RESP24Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "F5\n";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        game.getPlayers().getFirst().setHand(new Hand(getTestStartingHands(false).getFirst()));

        game.getPlayers().getFirst().addCardToHand(game.getAdventureDeck().drawCard());

        int sizeOfHandBefore = game.getPlayers().getFirst().getHandSize();

        game.trimHand(1);

        assertAll(
                () -> assertEquals(13, sizeOfHandBefore),
                () -> assertEquals(12, game.getPlayers().getFirst().getHandSize())
        );
    }

    @Test
    @DisplayName("RESP-25-test-1")
    public void RESP25Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "\nD5\nquit\n\n\nD5\nquit\n\n\nE30\nquit\n\n";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        Quest quest = new Quest(1, 2);

        quest.getStage(1).addCardToStage(new Card('F', 20));

        for (Player player : game.getPlayers()) {
            player.setHand(new Hand(new ArrayList<>(Arrays.asList(
                 new Card('D', 5),
                 new Card('E', 30)
            ))));
        }

        game.playQuestStage(quest);

        assertAll(
                () -> assertTrue(output.toString().contains("Player 2 lost this stage.")),
                () -> assertTrue(output.toString().contains("Player 3 lost this stage.")),
                () -> assertTrue(output.toString().contains("Player 4 won this stage.")),
                () -> assertEquals(1, quest.getActivePlayers().size())
        );
    }

    @Test
    @DisplayName("RESP-26-test-1")
    void RESP26Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "F5\nquit\n";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        game.getPlayers().get(1).addCardToHand(new Card('F', 5));

        Quest quest = new Quest(1, 1);

        game.buildAttack(quest, 2);

        assertTrue(output.toString().contains("ERROR: Cannot submit a foe card as an attack."));
    }

    @Test
    @DisplayName("RESP-27-test-1")
    void RESP27Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        Quest quest = new Quest(1, 1);
        quest.setActiveStage(2);

        game.endQuest(quest);

        assertAll(
                () -> assertEquals(1, game.getPlayers().get(1).getNumberOfShields()),
                () -> assertEquals(1, game.getPlayers().get(2).getNumberOfShields()),
                () -> assertEquals(1, game.getPlayers().get(3).getNumberOfShields()),
                () -> assertTrue(output.toString().contains("Player 2 has 1 shields.")),
                () -> assertTrue(output.toString().contains("Player 3 has 1 shields.")),
                () -> assertTrue(output.toString().contains("Player 4 has 1 shields."))

        );
    }

    @Test
    @DisplayName("RESP-28-test-1")
    void RESP28Test1() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        String input = "";
        game.setTextDisplay(new Scanner(input), new PrintWriter(output));

        Quest quest = new Quest(1, 1);
        quest.setActiveStage(2);

        quest.removeActivePlayers(new ArrayList<>(Arrays.asList(
                2, 3, 4
        )));

        game.endQuest(quest);

        assertTrue(output.toString().contains("No winners for this quest."));
    }

    @Test
    @DisplayName("A-TEST JP-Scenario")
    void ATest() {
        Game game = new Game();
        game.initialize();
        StringWriter output = new StringWriter();
        game.setTextDisplay(new Scanner(""), new PrintWriter(output));

        for (int i = 0; i < 4; i++)
            game.getPlayers().get(i).setHand(new Hand(getTestStartingHands(false).get(i)));

        setInput(game, "n,,,y");
        int sponsorNumber = game.questCard(4);

        setInput(game, "F5,H10,quit,F15,S10,quit,F15,D5,B15,quit,F40,B15,quit,,");
        Quest quest = game.initializeQuest(sponsorNumber, 4);

        // stage 1
        setInput(game, ",y,,y,,y,");
        game.requestPlayersToContinue(quest);

        rigTopOfDeck(game, new Card('B', 15));
        rigTopOfDeck(game, new Card('S', 10));
        rigTopOfDeck(game, new Card('F', 30));

        setInput(game, ",F5,D5,S10,quit,,,F5,S10,D5,quit,,,F5,D5,H10,quit,,");
        game.playQuestStage(quest);

        // stage 2
        setInput(game, ",y,,y,,y,");
        game.requestPlayersToContinue(quest);

        rigTopOfDeck(game, new Card('L', 20));
        rigTopOfDeck(game, new Card('L', 20));
        rigTopOfDeck(game, new Card('F', 10));

        setInput(game, ",H10,S10,quit,,,B15,S10,quit,,,H10,B15,quit,,");
        game.playQuestStage(quest);

        // stage 3
        setInput(game, ",y,,y,");
        game.requestPlayersToContinue(quest);

        rigTopOfDeck(game, new Card('S',10 ));
        rigTopOfDeck(game, new Card('B', 15));

        setInput(game, ",L20,H10,S10,quit,,,B15,S10,L20,quit,,");
        game.playQuestStage(quest);

        // stage 4
        setInput(game, ",y,,y,");
        game.requestPlayersToContinue(quest);

        rigTopOfDeck(game, new Card('L', 20));
        rigTopOfDeck(game, new Card('F', 30));

        setInput(game, ",B15,H10,L20,quit,,,D5,S10,L20,E30,quit,,");
        game.playQuestStage(quest);

        rigTopOfDeck(game, new Card('F', 5));
        rigTopOfDeck(game, new Card('F', 5));
        rigTopOfDeck(game, new Card('F', 5));
        rigTopOfDeck(game, new Card('F', 5));

        setInput(game, ",F5,F5,F5,F5,");
        game.endQuest(quest);
        game.drawSponsorCards(quest);

        // =======
        Player p1 = game.getPlayers().get(0);
        Player p2 = game.getPlayers().get(1);
        Player p3 = game.getPlayers().get(2);
        Player p4 = game.getPlayers().get(3);

        assertAll(
                () -> assertEquals(0, p1.getNumberOfShields()),
                () -> assertTrue(p1.getHand().isOrderedAs("F5,F10,F15,F15,F30,H10,B15,B15,L20")),
                () -> assertEquals(0, p3.getNumberOfShields()),
                () -> assertTrue(p3.getHand().isOrderedAs("F5,F5,F15,F30,S10")),
                () -> assertEquals(4, p4.getNumberOfShields()),
                () -> assertTrue(p4.getHand().isOrderedAs("F15,F15,F40,L20")),
                () -> assertEquals(12, p2.getHandSize())
        );
    }

    private void setInput(Game game, String input) {
        input = input.replace(',', '\n');
        game.setInput(new Scanner(input));
    }

    private void rigTopOfDeck(Game game, Card card) {
        game.getAdventureDeck().getDeck().addFirst(card);
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


    private String enterOverride() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            result.append("\n");
        }

        return result.toString();
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
