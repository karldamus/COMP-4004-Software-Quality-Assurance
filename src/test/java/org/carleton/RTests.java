package org.carleton;

import jdk.jfr.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

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
    @DisplayName("RESP-3-test-1")
    @Description("At the end of a turn, game determines if one or more players have 7 shields. Outputs players that win.")
    void RESP3Test1() {
        Game game = new Game();
        game.initPlayers();

        String input = "\n";
        StringWriter output = new StringWriter();

        game.getPlayers()[0].awardShields(7);

        game.endPlayersTurn(new Scanner(input), new PrintWriter(output));

        assertTrue(output.toString().contains("Game over! Winners: Player 1!"));
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
    @DisplayName("RESP-7-test-1")
    @Description("Game displays next event card")
    void RESP7Test1() {
        Game game = new Game();
        Display display = new Display();

        ArrayList<StringWriter> outputs = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            outputs.add(new StringWriter());

        game.initPlayers();
        game.initDecks();

        String input = "";
        StringWriter output = new StringWriter();

        for (int i = 0; i < 3; i++) {
            game.getEventDeck().setTopCard('E', i);
            game.drawEventCard(new Scanner(input), new PrintWriter(outputs.get(i)));
        }

        assertAll(
                () -> assertTrue(outputs.get(0).toString().contains("Event Card: Plague\n")),
                () -> assertTrue(outputs.get(1).toString().contains("Event Card: Queen's Favor\n")),
                () -> assertTrue(outputs.get(2).toString().contains("Event Card: Prosperity\n"))
        );
    }

    @Test
    @DisplayName("RESP-8-test-1")
    @Description("Player draws Plague card, have 0 shields already")
    void RESP8Test1() {
        Game game = new Game();
        game.initPlayers();
        game.initDecks();

        Player player = game.getCurrentPlayer();
        int startingNumberOfShields = player.getNumberOfShields();

        String input = "";
        StringWriter output = new StringWriter();

        game.getEventDeck().setTopCard('E', 0);
        game.drawEventCard(new Scanner(input), new PrintWriter((output)));

        int finalNumberOfShields = player.getNumberOfShields();

        assertAll(
                () -> assertEquals(0, startingNumberOfShields),
                () -> assertEquals(0, finalNumberOfShields)
        );
    }

    @Test
    @DisplayName("RESP-8-test-2")
    @Description("Player draws Plague card, have 1 shields already")
    void RESP8Test2() {
        Game game = new Game();
        game.initPlayers();
        game.initDecks();

        Player player = game.getCurrentPlayer();
        player.awardShields(1);

        int startingNumberOfShields = player.getNumberOfShields();

        String input = "";
        StringWriter output = new StringWriter();

        game.getEventDeck().setTopCard('E', 0);
        game.drawEventCard(new Scanner(input), new PrintWriter((output)));

        int finalNumberOfShields = game.getCurrentPlayer().getNumberOfShields();

        assertAll(
                () -> assertEquals(1, startingNumberOfShields),
                () -> assertEquals(0, finalNumberOfShields)
        );
    }

    @Test
    @DisplayName("RESP-8-test-3")
    @Description("Player draws Plague card, has 6 shields already")
    void RESP8Test3() {
        Game game = new Game();
        game.initPlayers();
        game.initDecks();

        Player player = game.getCurrentPlayer();
        player.awardShields(6);

        int startingNumberOfShields = player.getNumberOfShields();

        String input = "";
        StringWriter output = new StringWriter();

        game.getEventDeck().setTopCard('E', 0);
        game.drawEventCard(new Scanner(input), new PrintWriter((output)));

        int finalNumberOfShields = game.getCurrentPlayer().getNumberOfShields();

        assertAll(
                () -> assertEquals(6, startingNumberOfShields),
                () -> assertEquals(4, finalNumberOfShields)
        );
    }

    @Test
    @DisplayName("RESP-9-test-1")
    @Description("Player draws Queen's favor card")
    void RESP9Test1() {
        Game game = new Game();
        game.initPlayers();
        game.initDecks();
        game.getEventDeck().setTopCard('E', 1);

        String input = "";
        StringWriter output = new StringWriter();

        int sizeOfHandBefore = game.getPlayers()[0].getHand().size();

        game.drawEventCard(new Scanner(input), new PrintWriter(output));

        int sizeOfHandAfter = game.getPlayers()[0].getHand().size();

        assertAll(
                () -> assertEquals(0, sizeOfHandBefore),
                () -> assertEquals(2, sizeOfHandAfter)
        );
    }

    @Test
    @DisplayName("RESP-9-test-2")
    @Description("Player draws Queen's favor card and trims hand")
    void RESP9Test2() {
        Game game = new Game();
        game.initPlayers();
        game.initDecks();
        game.dealCards();
        game.getEventDeck().setTopCard('E', 1);

        String input = "1\n1\n";
        StringWriter output = new StringWriter();

        int sizeOfHandBefore = game.getPlayers()[0].getHand().size();

        game.drawEventCard(new Scanner(input), new PrintWriter(output));

        int sizeOfHandAfter = game.getPlayers()[0].getHand().size();

        assertAll(
                () -> assertEquals(12, sizeOfHandBefore),
                () -> assertEquals(12, sizeOfHandAfter)
        );
    }

    @Test
    @DisplayName("RESP-10-test-1")
    @Description("Player draws Prosperity card")
    void RESP10Test1() {
        Game game = new Game();
        game.initPlayers();
        game.initDecks();

        game.getEventDeck().setTopCard('E', 2);

        String input = "";
        StringWriter output = new StringWriter();

        ArrayList<Integer> sizesOfHandsBefore = new ArrayList<>();
        for (Player player : game.getPlayers())
            sizesOfHandsBefore.add(player.getHand().size());

        game.drawEventCard(new Scanner(input), new PrintWriter(output));

        ArrayList<Integer> sizesOfHandsAfter = new ArrayList<>();
        for (Player player : game.getPlayers())
            sizesOfHandsAfter.add(player.getHand().size());

        boolean allEqualZero = true;
        for (Integer i : sizesOfHandsBefore) {
            if (i != 0)
                allEqualZero = false;
        }
        boolean allEqualTwo = true;
        for (Integer i : sizesOfHandsAfter) {
            if (i != 2)
                allEqualTwo = false;
        }

        boolean finalAllEqualZero = allEqualZero;
        boolean finalAllEqualTwo = allEqualTwo;

        assertAll(
                () -> assertTrue(finalAllEqualTwo),
                () -> assertTrue(finalAllEqualZero)
        );
    }


    @Test
    @DisplayName("RESP-11-test-1")
    @Description("Game displays hand of current player")
    void RESP11Test1() {
        Game game = new Game();
        Display display = new Display();

        game.initPlayers();
        game.initDecks();
        game.getAdventureDeck().shuffle();
        game.dealCards();

        Card[] riggedHand = new Card[]{
                new Card('F', 5),
                new Card('F', 10),
                new Card('F', 25),
                new Card('F', 30),
                new Card('F', 70),
                new Card('D', 5),
                new Card('D', 5),
                new Card('S', 10),
                new Card('H', 10),
                new Card('B', 15),
                new Card('B', 15),
                new Card('L', 20)
        };

        // replace current players hand with the rigged hand in randomized order
        // randomized by adding the cards to a new deck to use the shuffle method
        Deck deck = new Deck();
        deck.deck = new ArrayList<>();
        for (Card c : riggedHand)
            deck.insertCard(c);
        deck.shuffle();

        int size = deck.getSize();
        for (int i = 0; i < size; i++)
            game.getCurrentPlayer().getHand().set(i, deck.drawCard());

        StringWriter output = new StringWriter();
        display.displayPlayersHand(game.getCurrentPlayer(), new PrintWriter(output));

        assertTrue(output.toString().contains("Player 1 Hand: F5 F10 F25 F30 F70 D5 D5 S10 H10 B15 B15 L20"));
    }



    @Test
    @DisplayName("RESP-12-test-1")
    @Description("Game selects next player as the current player to play")
    void RESP12Test1() {
        Game game = new Game();
        game.initPlayers();

        String input = "\n";
        StringWriter output = new StringWriter();

        Player[] players = game.getPlayers();

        Boolean[] playersTurns = new Boolean[]{false, false, false, false};

        for (int i = 0; i < players.length; i++) {
            playersTurns[i] = players[i].isPlayersTurn();
            game.endPlayersTurn(new Scanner(input), new PrintWriter(output));
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

        String input = "\n";
        StringWriter output = new StringWriter();

        game.endPlayersTurn(new Scanner(input), new PrintWriter(output)); // end first players turn
        game.endPlayersTurn(new Scanner(input), new PrintWriter(output)); // end second players turn
        game.endPlayersTurn(new Scanner(input), new PrintWriter(output)); // end third players turn
        game.endPlayersTurn(new Scanner(input), new PrintWriter(output)); // end fourth players turn

        assertAll(
                () -> assertTrue(players[0].isPlayersTurn()),
                () -> assertFalse(players[1].isPlayersTurn()),
                () -> assertFalse(players[2].isPlayersTurn()),
                () -> assertFalse(players[3].isPlayersTurn())
        );
    }

    @Test
    @DisplayName("RESP-13-test-1")
    @Description("Drawn Event cards are added to Event Deck Discard Pile")
    void RESP13Test1() {
        Game game = new Game();
        game.initDecks();
        game.initPlayers();
        game.getEventDeck().shuffle();

        DiscardPile testEventDeck = new DiscardPile();

        String input = "";
        StringWriter output = new StringWriter();

        int eventDeckSize = game.getEventDeck().getSize();
        for (int i = 0; i < eventDeckSize; i++) {
            // rig eventDeck to be a plague card to avoid other inputs
            // as we are just testing the discard pile responsibility.
            game.getEventDeck().setTopCard('E', 0);
            testEventDeck.insertCard(game.drawEventCard(new Scanner(input), new PrintWriter(output)));
        }

        DiscardPile discardedEventCards = game.getDiscardedEventCards();

        boolean allCardsSame = true;

        int discardEventSize = discardedEventCards.getSize();
        for (int i = 0; i < discardEventSize; i++) {
            Card c1 = testEventDeck.drawCard();
            Card c2 = discardedEventCards.drawCard();

            if (c1.getType() != c2.getType() || c1.getValue() != c2.getValue())
                allCardsSame = false;
        }

        assertTrue(allCardsSame);
    }

    @Test
    @DisplayName("RESP-14-test-1")
    @Description("Game computes n, number of cards to discard by player")
    void RESP14Test1() {
        Game game = new Game();
        game.initPlayers();
        game.initDecks();
        game.dealCards();

        int n1 = game.computeNumberOfCardsToDiscard(game.getCurrentPlayer());

        game.drawAdventureCard();

        int n2 = game.computeNumberOfCardsToDiscard(game.getCurrentPlayer());

        game.drawAdventureCard();
        game.drawAdventureCard();

        int n3 = game.computeNumberOfCardsToDiscard(game.getCurrentPlayer());

        assertAll(
                () -> assertEquals(0, n1),
                () -> assertEquals(1, n2),
                () -> assertEquals(3, n3)
        );
    }

    @Test
    @DisplayName("RESP-15-test-1")
    @Description("Game prompts player for position of next card to delete and player enters a valid position.")
    void RESP15Test1() {
        Game game = new Game();

        game.initPlayers();
        game.initDecks();

        ArrayList<Card> riggedHand = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            riggedHand.add(new Card('F', 5));
        }

        game.getCurrentPlayer().setHand(riggedHand);

        game.getAdventureDeck().setTopCard('F', 10);
        game.drawAdventureCard();

        StringWriter output = new StringWriter();
        String input = "2";

        int sizeBeforeTrim = game.getCurrentPlayer().getHand().size();

        game.trimHand(game.getCurrentPlayer(), new Scanner(input), new PrintWriter(output));

        int sizeAfterTrim = game.getCurrentPlayer().getHand().size();

        assertAll(
                () -> assertEquals(13, sizeBeforeTrim),
                () -> assertEquals(12, sizeAfterTrim),
                () -> assertTrue(output.toString().contains("Card 2 Removed")),
                () -> assertTrue(output.toString().contains("Player 1 Hand: F5 F5 F5 F5 F5 F5 F5 F5 F5 F5 F5 F10"))
        );
    }

    @Test
    @DisplayName("RESP-15-test-2")
    @Description("Game prompts player for position of next card to delete and player enters invalid position.")
    void RESP15Test2() {
        Game game = new Game();

        game.initPlayers();
        game.initDecks();

        game.dealCards();
        game.drawAdventureCard();

        StringWriter output = new StringWriter();
        String input = "14\n1";

        game.trimHand(game.getCurrentPlayer(), new Scanner(input), new PrintWriter(output));

        assertTrue(output.toString().contains("Entered position is too high or low."));
    }

    @Test
    @DisplayName("RESP-15-test-3")
    @Description("Game prompts player for position of next card to delete and player enters NAN.")
    void RESP15Test3() {
        Game game = new Game();

        game.initPlayers();
        game.initDecks();

        game.dealCards();
        game.drawAdventureCard();

        StringWriter output = new StringWriter();
        String input = "this is not a number";

        game.trimHand(game.getCurrentPlayer(), new Scanner(input), new PrintWriter(output));

        assertTrue(output.toString().contains("Input is not formatted as a number."));
    }


    @Test
    @DisplayName("RESP-16-test-1")
    @Description("All players decline to sponsor a quest")
    void RESP16Test1() {
        Game game = new Game();

        String input = "n\nn\nn\nn\n\n\n";
        StringWriter output = new StringWriter();

        game.initPlayers();
        game.initDecks();

        int playersTurnAtStart = game.getCurrentPlayer().getPlayerNumber();

        game.dealCards();
        game.getEventDeck().setTopCard('Q', 3);
        game.drawEventCard(new Scanner(input), new PrintWriter((output)));

        int playersTurnAtEnd = game.getCurrentPlayer().getPlayerNumber();

        String expectedOutput = "All players declined or were unable to sponsor this quest.";

        assertAll(
                () -> assertTrue(output.toString().contains(expectedOutput)),
                () -> assertEquals(1, playersTurnAtStart),
                () -> assertEquals(2, playersTurnAtEnd)
        );
    }

    @Test
    @DisplayName("RESP-17-test-1")
    @Description("Sponsor sets up a valid quest.")
    void RESP17Test1() {
        Game game = new Game();
        game.initPlayers();
        game.initDecks();
        game.getAdventureDeck().shuffle();
        game.dealCards();

        String input = "y\nQuit\ndevQuit-comp4004\nn\nn\nn\n";
        String input2 = "y\n2\nQuit\n1\nQuit\ndevQuit-comp4004\nn\nn\nn\n";
        StringWriter output = new StringWriter();
        StringWriter output2 = new StringWriter();

        ArrayList<Card> riggedHand = new ArrayList<>(Arrays.asList(
                new Card('F', 5),
                new Card('F', 10),
                new Card('F', 15),
                new Card('F', 20)
        ));

        for (int i = 0; i < 8; i ++) {
            riggedHand.add(new Card('D', 5));
        }

        game.getCurrentPlayer().setHand(riggedHand);

        game.getEventDeck().setTopCard('Q', 1);
        game.drawEventCard(new Scanner(input), new PrintWriter(output));

        game.getEventDeck().setTopCard('Q', 2);
        game.drawEventCard(new Scanner(input2), new PrintWriter(output2));

        assertAll(
            () -> assertTrue(output.toString().contains("A stage cannot be empty.")),
            () -> assertTrue(output2.toString().contains("Stage 1 accepted. Cards included in stage: F10")),
            () -> assertTrue(output2.toString().contains("Insufficient value for this stage."))
        );

    }

    @Test
    @DisplayName("RESP-18-test-1")
    @Description("Game determines and displays set of eligible participants for stage of quest.")
    void RESP18Test1() {
        Game game = new Game();
        Display display = new Display();
        game.initPlayers();

        String input = "devQuit-comp4004\ndevQuit-comp4004\ndevQuit-comp4004\ndevQuit-comp4004\n";
        StringWriter output = new StringWriter();

        ArrayList<ArrayList<Card>> hands = getTestStartingHands(true);

        int i = 0;
        for (ArrayList<Card> hand : hands) {
            game.getPlayers()[i].setHand(hand);
        }

        Quest quest = getP24StageQuest(game.getPlayers(), display, new Scanner(input), new PrintWriter(output));

        display.startOfStage(quest, new PrintWriter(output));

        assertTrue(output.toString().contains("QUEST Stage 1\nEligible players: P1, P3, P4."));
    }

    @Test
    @DisplayName("RESP-19-test-1")
    @Description("Game prompts in turn each eligible participant as to whether they withdraw from the quest or tackle the current stage of the quest.")
    void RESP19Test1() {
        Game game = new Game();
        Display display = new Display();
        game.initPlayers();

        String bypassQuestSetup = "devQuit-comp4004\ndevQuit-comp4004\ndevQuit-comp4004\ndevQuit-comp4004\n";
        // Player 1: Remains
        // Player 3: Leaves
        // Player 4: Remains
        String gameInput = "y\nn\ny\n";
        String input = bypassQuestSetup + gameInput;

        StringWriter output = new StringWriter();

        ArrayList<ArrayList<Card>> hands = getTestStartingHands(true);

        int i = 0;
        for (ArrayList<Card> hand : hands) {
            game.getPlayers()[i].setHand(hand);
        }

        Quest quest = getP24StageQuest(game.getPlayers(), display, new Scanner(input), new PrintWriter(output));

        quest.questContinuationPrompt();

        assertTrue(output.toString().contains("Players remaining in Quest: P1, P4."));
    }

    @Test
    @DisplayName("RESP-20-test-1")
    @Description("Game indicates turn of currrent player has ended and clears the 'hotseat' display. Once that player presses the return key.")
    public void RESP20Test1() {
        Game game = new Game();

        String input = "\n";
        StringWriter output = new StringWriter();

        game.initPlayers();
        game.initDecks();
        game.dealCards();

        game.endPlayersTurn(new Scanner(input), new PrintWriter(output));

        assertTrue(output.toString().contains("End of player 1 turn. Press Enter to switch player."));
    }

    @Test
    @DisplayName("RESP-21-test-1")
    @Description("Participant enters valid position of a card for an attack and defeats the stage.")
    public void RESP21Test1() {
        Game game = new Game();
        game.initDecks();
        game.initPlayers();
        Display display = new Display();


        String input = """
                y
                1
                Quit
                y
                n
                n
                1
                12
                Quit""";
        StringWriter output = new StringWriter();

        ArrayList<ArrayList<Card>> hands = getTestStartingHands(false);

        int i = 0;
        for (ArrayList<Card> hand : hands) {
            game.getPlayers()[i].setHand(hand);
            i++;
        }

        game.getEventDeck().setTopCard('Q', 1);
        game.drawEventCard(new Scanner(input), new PrintWriter(output));

        assertTrue(output.toString().contains("Player 2 attacks with E30. The attack is successful!"));
    }

    @Test
    @DisplayName("RESP-22-test-1")
    @Description("Participant enters invalid position of a card for an attack, then enters valid position and does not defeat the stage.")
    public void RESP22Test1() {
        Game game = new Game();
        game.initDecks();
        game.initPlayers();

        String input = """
                y
                1
                5
                5
                Quit
                y
                n
                n
                1
                notANumber
                30
                6
                Quit""";

        StringWriter output = new StringWriter();

        ArrayList<ArrayList<Card>> hands = getTestStartingHands(false);

        int i = 0;
        for (ArrayList<Card> hand : hands) {
            game.getPlayers()[i].setHand(hand);
            i++;
        }

        game.getEventDeck().setTopCard('Q', 1);
        game.drawEventCard(new Scanner(input), new PrintWriter(output));

        assertAll(
                () -> assertTrue(output.toString().contains("Invalid input.")),
                () -> assertTrue(output.toString().contains("Position of card does not exist.")),
                () -> assertTrue(output.toString().contains("Player 2 attacks with D5. The attack fails."))
        );
    }

    @Test
    @DisplayName("RESP-23-test-1")
    @Description("All cards used in attack are discarded.")
    public void RESP23Test1() {
        Game game = new Game();
        game.initDecks();
        game.initPlayers();
        Display display = new Display();

        String input = """
                y
                1
                Quit
                y
                n
                n
                1
                12
                Quit""";

        StringWriter output = new StringWriter();

        ArrayList<ArrayList<Card>> hands = getTestStartingHands(false);

        int i = 0;
        for (ArrayList<Card> hand : hands) {
            game.getPlayers()[i].setHand(hand);
            i++;
        }

        game.getEventDeck().setTopCard('Q', 1);
        game.drawEventCard(new Scanner(input), new PrintWriter(output));

        Card discardedCard = game.getDiscardedAdventureCards().getAllCards().get(0);

        assertAll(
            () -> assertEquals('E', discardedCard.getType()),
            () ->assertEquals(30, discardedCard.getValue())
        );
    }

    @Test
    @DisplayName("RESP-24-test-1")
    @Description("Eligible participant draws 1 adventure card and possibly trims their hand.")
    public void RESP24Test1() {
        Game game = new Game();
        game.initDecks();
        game.initPlayers();

        String input = """
                y
                1
                Quit
                y
                n
                n
                Quit""";

        StringWriter output = new StringWriter();

        ArrayList<ArrayList<Card>> hands = getTestStartingHands(false);

        int i = 0;
        for (ArrayList<Card> hand : hands) {
            game.getPlayers()[i].setHand(hand);
            i++;
        }

        game.getPlayers()[1].getHand().remove(1);
        int sizeOfHandBefore = game.getPlayers()[1].getHand().size();

        game.getEventDeck().setTopCard('Q', 1);
        game.drawEventCard(new Scanner(input), new PrintWriter(output));

        int sizeOfHandAfter = game.getPlayers()[1].getHand().size();

        assertAll(
                () -> assertEquals(11, sizeOfHandBefore),
                () -> assertEquals(12, sizeOfHandAfter)
        );
    }

    @Test
    @DisplayName("RESP-25-test-1")
    @Description("Participants with an attack less than the value of current stage become unavailable to further participate in the quest.")
    public void RESP25Test1() {
        Game game = new Game();
        game.initDecks();
        game.initPlayers();

        String input = """
                y
                3
                Quit
                3
                3
                Quit
                y
                y
                n
                12
                7
                Quit
                12
                12
                11
                Quit
                y
                7
                7
                7
                Quit""";

        StringWriter output = new StringWriter();

        ArrayList<ArrayList<Card>> hands = getTestStartingHands(false);

        int i = 0;
        for (ArrayList<Card> hand : hands) {
            game.getPlayers()[i].setHand(hand);
            i++;
        }

        game.getEventDeck().setTopCard('Q', 2);
        game.drawEventCard(new Scanner(input), new PrintWriter(output));


        assertTrue(output.toString().contains("Players remaining in Quest: P3."));
    }


    @Test
    @DisplayName("RESP-26-test-1")
    @Description("Participant tries to use a Foe card for their attack.")
    public void RESP26Test1() {
        Game game = new Game();
        game.initDecks();
        game.initPlayers();

        String input = """
                y
                1
                Quit
                y
                n
                n
                2
                1
                6
                Quit""";

        StringWriter output = new StringWriter();

        ArrayList<ArrayList<Card>> hands = getTestStartingHands(false);

        int i = 0;
        for (ArrayList<Card> hand : hands) {
            game.getPlayers()[i].setHand(hand);
            i++;
        }

        game.getEventDeck().setTopCard('Q', 1);
        game.drawEventCard(new Scanner(input), new PrintWriter(output));


        assertTrue(output.toString().contains("Selected card must be a weapon card, not a Foe card."));
    }


    // =============================================
    private Quest getP24StageQuest(Player[] players, Display display, Scanner input, PrintWriter output) {
        Quest quest = new Quest(4, players, 1, display, input, output);

        ArrayList<Card> stage1Cards = new ArrayList<>(Arrays.asList(
            new Card('F', 5),
            new Card('H', 10)
        ));

        ArrayList<Card> stage2Cards = new ArrayList<>(Arrays.asList(
            new Card('F', 15),
            new Card('S', 10)
        ));

        ArrayList<Card> stage3Cards = new ArrayList<>(Arrays.asList(
            new Card('F', 15),
            new Card('D', 5),
            new Card('B', 15)
        ));

        ArrayList<Card> stage4Cards = new ArrayList<>(Arrays.asList(
            new Card('F', 40),
            new Card('B', 15)
        ));

        quest.addStage(1, stage1Cards);
        quest.addStage(2, stage2Cards);
        quest.addStage(3, stage3Cards);
        quest.addStage(4, stage4Cards);

        return quest;
    }

    private ArrayList<ArrayList<Card>> getTestStartingHands(boolean questCardsRemoved) {
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

        return new ArrayList<ArrayList<Card>>(Arrays.asList(
                p1Hand, p2Hand, p3Hand, p4Hand
        ));
    }
}