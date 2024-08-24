package com.example.memoryGame.service;

import com.example.memoryGame.model.Game;
import com.example.memoryGame.model.GameDifficulty;
import com.example.memoryGame.model.User;

import java.util.Optional;

public interface GameService {
    Game startNewGame(User user, GameDifficulty difficulty);
    Optional<Game> getGameById(Integer gameId);
    Game endGame(Integer gameId);
}
