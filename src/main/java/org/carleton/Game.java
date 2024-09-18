package org.carleton;

public class Game {
    private static final int NUM_PLAYERS = 4;

    private Player[] players;

    public Game() {

    }

    public void init() {
        this.players = new Player[NUM_PLAYERS];

        for (int i = 0; i < NUM_PLAYERS; i++) {
            this.players[i] = new Player();
            this.players[i].setTurn(false);
        }

        players[0].setTurn(true);
    }

    public Player[] getPlayers() {
        return this.players;
    }
}
