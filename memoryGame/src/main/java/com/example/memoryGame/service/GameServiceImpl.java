package com.example.memoryGame.service;

import com.example.memoryGame.dao.GameRepository;
import com.example.memoryGame.dao.GameResultRepository;
import com.example.memoryGame.dao.UserRepository;
import com.example.memoryGame.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameResultRepository gameResultRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Game startNewGame(User user, GameDifficulty difficulty) {
        User persistentUser = findOrCreateUserByUsername(user.getUsername());

        Game game = new Game();
        game.setUser(persistentUser);
        game.setDifficulty(difficulty);
        game.setStatus(GameStatus.IN_PROGRESS);

        switch (difficulty) {
            case EASY:
                game.setGridSize(4);
                break;
            case MEDIUM:
                game.setGridSize(6);
                break;
            case HARD:
                game.setGridSize(8);
                break;
        }

        game.initializeCards();
        gameRepository.save(game);
        return game;
    }

    public User findOrCreateUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(username);
                    return userRepository.save(newUser);
                });
    }

    @Override
    public Optional<Game> getGameById(Integer gameId) {
        return gameRepository.findById(gameId);
    }

    @Override
    @Transactional
    public Game endGame(Integer gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));

        // Ensure the game is not already ended
        if (game.getStatus() == GameStatus.COMPLETED) {
            throw new IllegalStateException("Game is already completed");
        }

        game.endGame();
        gameRepository.save(game);

        GameResult gameResult = new GameResult();
        gameResult.setGame(game);
        gameResult.setStartTime(game.getStartTime());
        gameResult.setEndTime(game.getEndTime());
        gameResult.calculateAndSetTimeTaken();
        gameResultRepository.save(gameResult);

        return game;
    }
}
