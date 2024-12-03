package org.carleton;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class Controller {
//    private Game game;
    private final Game gameService;

    public Controller(Game gameService) {
//        this.gameService = new Game();
//        this.gameService.initialize();
        this.gameService = gameService;
    }

    @PostMapping("/input")
    public void receiveInput(@RequestBody String input) {
        gameService.setNextInput(input);
    }

    @GetMapping("/output")
    public List<String> getOutput() {
        return gameService.fetchAndClearOutputs();
    }

    @GetMapping("newGame")
    public void newGame() {
//        gameService.run();
        gameService.requestNewGame();
    }
//
//    @GetMapping("newGame")
//    public void newGame() {
//        game = new Game();
//        game.initialize();
//        game.useSpringDisplay();
//
//        game.playerTurn();
//
////        return "Player " + game.getCurrentPlayersTurn() + " it is your turn.";
//    }
//
//    @GetMapping("setInput")
//    public void setInput(@RequestParam("input") String input) {
//        game.setUIInput(input);
//    }
//
//    @GetMapping("getOutput")
//    public String getOutput() {
//        return game.getUiOutput();
//    }
//
//    @GetMapping("playerHand")
//    public String playerHand(@RequestParam("playerNumber") int playerNumber) {
//        return game.getPlayers().get(playerNumber - 1).getHand().getHandAsString();
//    }
//
//    @GetMapping("sendMessage")
//    public String sendMessage(@RequestParam("message") String message) {
//        if (message.equals("test")) {
//            return "confirmed test";
//        } else {
//            return "test incomplete";
//        }
//    }
//
////    @GetMapping("rigHand")
//
//    @GetMapping("input")
//    public void input(@RequestParam("input") String input) {
//
//    }



//    @GetMapping("test")
//    public String test() {
//        return "test";
//    }
}
