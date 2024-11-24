package main.java.org.carleton;

import java.util.ArrayList;

public class Quest {
    private int sponsor;
    private ArrayList<Integer> winners;
    private ArrayList<Stage> stages;

    private class Stage {
        private ArrayList<Card> cardsInStage;
        private int stageValue;

        public Stage() {

        }
    }

    public Quest() {

    }

    public boolean isValidStage(Stage stage) {
        if (stage.stageValue <= 0)
            return false;

        if (stage.stageValue <= stages.getLast().stageValue)
            return false;

        // TODO
    }
}
