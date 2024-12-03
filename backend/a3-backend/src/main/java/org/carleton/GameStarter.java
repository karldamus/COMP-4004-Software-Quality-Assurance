package org.carleton;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class GameStarter implements CommandLineRunner {
    private final GameRunner gameRunner;

    public GameStarter(GameRunner gameRunner) {
        this.gameRunner = gameRunner;
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(gameRunner).start();
    }
}
