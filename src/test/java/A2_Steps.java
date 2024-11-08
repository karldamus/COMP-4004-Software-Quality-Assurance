import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.carleton.Card;
import org.carleton.Game;
import org.carleton.Player;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class A2_Steps {
    private Game game;
    String input;

    @Given("a new game of quests")
    public void aNewGameOfQuests() {
        game = new Game();
        game.init();
        input = """
                """;
    }

    @Given("a new game of quests is rigged for a1 scenario")
    public void a1Scenario() {
        this.aNewGameOfQuests();
        this.playerHasHand(1, "F5,F5,F15,F15,D5,S10,S10,H10,H10,B15,B15,L20");
        this.playerHasHand(2, "F5,F5,F15,F15,F40,D5,S10,H10,H10,H10,B15,B15,E30");
        this.playerHasHand(3, "F5,F5,F5,F15,D5,S10,S10,S10,H10,H10,B15,L20");
        this.playerHasHand(4, "F5,F15,F15,F40,D5,D5,S10,H10,H10,B15,L20,E30");
        this.theAdventureDeckHasTheCards("F30,S10,B15,F10,L20,L20,B15,S10,F30,L20");
        this.theAdventureDeckHasAnAdditionalCardsOnTheEnd(15, "F", 5);
    }

    @Given("a new game of quests is rigged for 2 winner game")
    public void twoWinnerGameScenario() {
        this.aNewGameOfQuests();
        this.playerHasHand(1, "F15,F15,D5,F20,H10,F25,D5,S10,D5,D5,F5,F5");
        this.playerHasHand(2, "B15,S10,S10,L20,H10,L20,H10,S10,D5,H10,D5,E30");
        this.playerHasHand(3, "S10,F5,F10,F15,F5,F5,F5,F5,F5,F5,F5,F5");
        this.playerHasHand(4, "B15,B15,H10,B15,S10,D5,D5,E30,S10,H10,H10,H10");
        this.theAdventureDeckHasTheCards("F5,F5,F5,D5,S10,D5,S10,D5,D5");
        this.theAdventureDeckHasAnAdditionalCardsOnTheEnd(12, "F", 5);
        this.theAdventureDeckHasAnAdditionalCardsOnTheEnd(20, "D", 5);
    }

    @Given("a new game of quests is rigged for one winner game")
    public void a_new_game_of_quests_is_rigged_for_one_winner_game() {
        this.aNewGameOfQuests();
        this.playerHasHand(1, "F5,F10,F15,F20,F10,F15,F20,D5,D5,D5,D5,D5");
        this.playerHasHand(2, "D5,S10,B15,L20,S10,B15,L20,F5,F5,F5,F5,F5");
        this.playerHasHand(3, "D5,S10,B15,L20,S10,B15,L20,F5,F5,F5,F5,F5");
        this.playerHasHand(4, "D5,S10,B15,L20,D5,D5,D5,D5,F5,F5,F5,F5");

        game.getAdventureDeck().setDeck(new ArrayList<>());
        for (int i = 0; i < 50; i++) {
            game.getAdventureDeck().insertCard(new Card('F', 5));
        }
    }

    @Given("a new game of quests is rigged for zero winner quest")
    public void a_new_game_of_quests_is_rigged_for_zero_winner_quest() {
        this.aNewGameOfQuests();

        // empty player hands
        for (Player player : game.getPlayers())
            player.setHand(new ArrayList<>());

        // fill player hands with rigged cards
        for (int i = 0; i < 12; i++) {
            game.getPlayers()[0].getHand().add(new Card('F', 70));
            for (int j = 1; j < 4; j++) {
                game.getPlayers()[j].getHand().add(new Card('D', 5));
            }
        }

        // rig adventure deck
        ArrayList<Card> riggedAdventureDeck = new ArrayList<>();
        for (int i = 0; i < 50; i++) { riggedAdventureDeck.add(new Card('D', 5)); }
        game.getAdventureDeck().setDeck(riggedAdventureDeck);

        // rig event deck
        game.getEventDeck().setDeck(new ArrayList<>(Arrays.asList(
                new Card('Q', 2),
                new Card('Q', 2)
        )));
    }

    @When("player {int} draws a quest of {int} stages")
    public void playerDrawsAQuestOfNStages(int playerNumber, int numStages) {
        game.getEventDeck().setTopCard('Q', numStages);
    }

    @Then("player {int} draws a {string} event card")
    public void player_draws_a_event_card(Integer int1, String string) {
        switch (string) {
            case "plague":
                game.getEventDeck().setTopCard('E', 0);
                break;
            case "queens favor":
                game.getEventDeck().setTopCard('E', 1);
                break;
            case "prosperity":
                game.getEventDeck().setTopCard('E', 2);
                break;
        }
    }

    @And("player {int} has hand {string}")
    public void playerHasHand(int playerNumber, String handAsString) {
        game.getPlayers()[playerNumber - 1].setHand(getListOfCardsFromString(handAsString));
    }

    @And("the adventure deck has the cards {string}")
    public void theAdventureDeckHasTheCards(String cardsAsString) {
        game.getAdventureDeck().setDeck(getListOfCardsFromString(cardsAsString));
    }

    @And("the adventure deck has an additional {int} {string} {int} cards on the end")
    public void theAdventureDeckHasAnAdditionalCardsOnTheEnd(int numCards, String cardTypeAsString, int cardValue) {
        char cardType = cardTypeAsString.charAt(0);

        for (int i = 0; i < numCards; i++)
            game.getAdventureDeck().insertCard(new Card(cardType, cardValue));
    }



    @And("player {int} declines to sponsor the quest")
    public void playerDeclinesToSponsorTheQuest(int arg0) {
        input += "n\n";
    }

    @And("player {int} accepts to sponsor the quest")
    public void playerAcceptsToSponsorTheQuest(int playerNumber) {
        input += "y\n";
    }

    @And("player {int} builds the {int} stage quest with the following card positions {string}")
    public void playerBuildsTheStageQuestWithTheFollowingCardPositions(int playerNumber, int numStages, String cardsInput) {
        String[] inputsBeforeQuitStatements = cardsInput.split(" ");

        for (String s : inputsBeforeQuitStatements) {
            String[] singleInputs = s.split(",");
            for (String s1 : singleInputs) {
                input += s1 + "\n";
            }

            input += "Quit\n";
        }
    }

    @And("player {int} decides to participate")
    public void playerDecidesToParticipate(int playerNumber) {
        input += "y\n";
    }

    @And("player {int} declines to participate")
    public void playerDeclinesToParticipate(int playerNumber) {
        input += "n\n";
    }

    @And("player {int} discards card {int}")
    public void playerDiscardsCard(int playerNumber, int cardNumber) {
        input += cardNumber + "\n";
    }

    @And("player {int} builds attack with cards {string}")
    public void playerBuildsAttackWithCards(int playerNumber, String cardNumbers) {
        for (String s : cardNumbers.split(","))
            input += s + "\n";

        input += "Quit\n";
    }

    @And("player {int} continues")
    public void playerContinues(int arg0) {
        input += "\n";
    }

    @And("player {int} discards cards {string}")
    public void playerDiscardsCards(int arg0, String cards) {
        for (String s : cards.split(","))
            input += s + "\n";
    }

    @And("the scenario has been executed")
    public void theScenarioHasBeenExecuted() {
        game.playersTurn(new Scanner(input), new PrintWriter(new StringWriter()));
    }

    @And("player {int}'s turn is over")
    public void playerSTurnIsOver(int playerNumber) {
        game.playersTurn(new Scanner(input), new PrintWriter(new StringWriter()));

        input = """
                """;
    }

    @And("player {int} should have {int} shields")
    public void playerShouldHaveShields(int player, int expectedNumShields) {
        int index = player - 1;

        assertEquals(expectedNumShields, game.getPlayers()[index].getNumberOfShields());
    }

    @And("all players should have {int} shields")
    public void allPlayersShouldHaveNShields(int expectedNumberOfShields) {
        assertAll(
                () -> assertEquals(expectedNumberOfShields, game.getPlayers()[0].getNumberOfShields()),
                () -> assertEquals(expectedNumberOfShields, game.getPlayers()[1].getNumberOfShields()),
                () -> assertEquals(expectedNumberOfShields, game.getPlayers()[2].getNumberOfShields()),
                () -> assertEquals(expectedNumberOfShields, game.getPlayers()[3].getNumberOfShields())
        );
    }


    @And("player {int} should have cards {string}")
    public void playerNShouldHaveCards(int player, String cards) {
        String regexLetters = "[a-zA-Z]";

        int index = player - 1;
        String[] cardsArray = cards.split(",");

        ArrayList<Character> cardTypes = new ArrayList<Character>();
        ArrayList<Integer> cardValues = new ArrayList<Integer>();

        try {
            for (String card : cardsArray) {
                String[] cardDetails = card.split(regexLetters);
                cardTypes.add(card.charAt(0));
                cardValues.add(Integer.valueOf(cardDetails[1]));
            }
        } catch (Error e) {
            System.out.println(e);
        }

        boolean allCardsEqual = true;

        for (int i = 0; i < cardTypes.size(); i++) {
            char actualType = game.getPlayers()[index].getHand().get(i).getType();
            int actualValue = game.getPlayers()[index].getHand().get(i).getValue();
            if (actualType != cardTypes.get(i) || actualValue != cardValues.get(i))
                allCardsEqual = false;
        }


        assertTrue(allCardsEqual);
    }

    @And("player {int} should have {int} cards in their hand")
    public void playerShouldHaveCardsInTheirHand(int player, int expectedNumCards) {
        int index = player - 1;

        assertEquals(expectedNumCards, game.getPlayers()[index].getHand().size());
    }

    @And("player {int} is unable to sponsor the quest")
    public void playerIsUnableToSponsorTheQuest(int playerNumber) {
    }

    @And("player {int} should be a winner")
    public void playerShouldBeAWinner(int playerNumber) {
        ArrayList<Integer> winners = game.getWinners();

        assertTrue(winners.contains(playerNumber));
    }


    // ===========
    public ArrayList<Card> getListOfCardsFromString(String cardsAsString) {
        String regexLetters = "[a-zA-Z]";

        String[] cardsArray = cardsAsString.split(",");

        ArrayList<Character> cardTypes = new ArrayList<Character>();
        ArrayList<Integer> cardValues = new ArrayList<Integer>();

        try {
            for (String card : cardsArray) {
                String[] cardDetails = card.split(regexLetters);
                cardTypes.add(card.charAt(0));
                cardValues.add(Integer.valueOf(cardDetails[1]));
            }
        } catch (Error e) {
            System.out.println(e);
        }

        ArrayList<Card> cards = new ArrayList<>();

        for (int i = 0; i < cardTypes.size(); i++) {
            cards.add(new Card(cardTypes.get(i), cardValues.get(i)));
        }

        return cards;
    }
}
