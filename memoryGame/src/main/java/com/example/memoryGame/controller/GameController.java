package com.example.memoryGame.controller;

import com.example.memoryGame.model.Game;
import com.example.memoryGame.model.GameDifficulty;
import com.example.memoryGame.model.User;
import com.example.memoryGame.service.GameService;
import com.example.memoryGame.service.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameServiceImpl gameService;

    @PostMapping("/start")
    public ResponseEntity<Game> startGame(@RequestParam GameDifficulty difficulty, @RequestParam String username) {
        User user = gameService.findOrCreateUserByUsername(username);
        Game game = gameService.startNewGame(user, difficulty);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable Integer gameId) {
        return gameService.getGameById(gameId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{gameId}/end")
    public ResponseEntity<Game> endGame(@PathVariable Integer gameId) {
        Game game = gameService.endGame(gameId);
        return ResponseEntity.ok(game);
    }
}
