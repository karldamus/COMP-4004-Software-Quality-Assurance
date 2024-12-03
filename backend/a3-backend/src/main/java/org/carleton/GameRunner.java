package org.carleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameRunner implements Runnable {
    private final Game gameService;
//    private final Game game = new Game();

    @Autowired
    public GameRunner(Game gameService) {
        this.gameService = gameService;
    }

    @Override
    public void run() {
        gameService.initialize();
        while (!gameService.gameOver()) {
            gameService.playerTurn();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (gameService.newGameRequested()) {
                gameService.initialize();
            }
//        while (!gameService.gameOver()) {
//            try {
////                String input = gameService.getNextInput();
//
//                gameService.playerTurn();
//
////                gameService.appendOutput(response);
//
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                break;
//            }
        }
    }

    private String processInput(String input) {
        return "Processed: " + input;
    }
}
