package main.java.org.carleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static main.java.org.carleton.QuestStageCodes.*;

public class Quest {
    private int sponsor;
//    private ArrayList<Integer> winners;
    private ArrayList<Stage> stages;
    private int activeStage;
    private ArrayList<Integer> activePlayers;

    public void setActiveStage(int i) {
        this.activeStage = i;
    }

    public void removeActivePlayers(ArrayList<Integer> playersToRemove) {
        for (int playerToRemove : playersToRemove) {
            for (int i = 0; i < activePlayers.size(); i++) {
                if (activePlayers.get(i) == playerToRemove) {
                    activePlayers.remove(i);
                    break;
                }
            }
        }
    }

    public boolean hasWinners() {
        return activeStage >= stages.size() && !activePlayers.isEmpty();
    }

    // only used after hasWinners call in Game.endQuest
    public ArrayList<Integer> getWinners() {
        return this.activePlayers;
    }

    public ArrayList<Integer> getActivePlayers() {
        return this.activePlayers;
    }

    public int getActiveStage() {
        return this.activeStage;
    }

    public HashMap<Integer, Boolean> completeStage() {
        this.activeStage += 1;

        return this.stages.get(activeStage - 2).getAttacks();
    }

    public void addCardToStage(int stageNumber, Card card) {
        if (card == null)
            return;
        this.stages.get(stageNumber - 1).addCardToStage(card);
    }

    public int getNumberOfStages() {
        return stages.size();
    }

    public void submitAttackForCurrentStage(int playerNumber, ArrayList<Card> cardsInAttack) {
        this.stages.get(activeStage - 1).addAttack(playerNumber, cardsInAttack);
    }

    public class Stage {
        private ArrayList<Card> cardsInStage;
        private int stageValue;
        private int stageNumber;

        private HashMap<Integer, Boolean> attacks;

        public Stage(int stageNumber) {
            this.cardsInStage = new ArrayList<>();
            this.stageValue = 0;
            this.stageNumber = stageNumber;
            this.attacks = new HashMap<>();
        }

        public HashMap<Integer, Boolean> getAttacks() {
            return this.attacks;
        }

        public void addAttack(int playerNumber, ArrayList<Card> cardsInAttack) {
            int totalAttackValue = 0;

            for (Card card : cardsInAttack) {
                totalAttackValue += card.getValue();
            }

            this.attacks.put(playerNumber, totalAttackValue >= this.stageValue);
        }


        public int getStageValue() {
            return this.stageValue;
        }

        public ArrayList<Card> getCardsInStage() {
            return this.cardsInStage;
        }

        public void addCardToStage(Card card) {
            this.cardsInStage.add(card);
            this.stageValue += card.getValue();
        }

        public String getCardsInStageAsString() {
            StringBuilder cardsAsString = new StringBuilder();

            for (Card card : cardsInStage) {
                cardsAsString.append(card.toString()).append(" ");
            }

            return cardsAsString.toString();
        }
    }

    public Quest(int sponsor, int numStages) {
        this.sponsor = sponsor;
        this.stages = new ArrayList<>();
//        this.winners = new ArrayList<>();
        this.activePlayers = new ArrayList<>();
        this.activeStage = 1;

        for (int i = 0; i < numStages; i++) {
            this.stages.add(new Stage(i + 1));
        }

        for (int i = 1; i <= 4; i++) {
            if (i != sponsor)
                activePlayers.add(i);
        }
    }

    public int getSponsor() {
        return this.sponsor;
    }

    public ArrayList<Stage> getAllStages() {
        return this.stages;
    }

    public Stage getStage(int stageNumber) {
        return this.stages.get(stageNumber - 1);
    }



    public QuestStageCodes isValidStage(int stageNumber) {
        Stage stage = this.getStage(stageNumber);

        if (stage.stageValue <= 0)
            return EMPTY_STAGE;

        if (numberOfFoesInStage(stage) <= 0)
            return NO_FOES_IN_STAGE;

        if (stage.stageNumber != 1)
            if (stage.stageValue <= stages.get(stage.stageNumber - 2).stageValue)
                return INSUFFICIENT_VALUE_FOR_STAGE;


        return OKAY_STAGE;
    }

    public int numberOfFoesInStage(Stage stage) {
        int numFoes = 0;

        for (Card card : stage.cardsInStage) {
            if (card.getType() == 'F')
                numFoes += 1;
        }

        return numFoes;
    }
}
