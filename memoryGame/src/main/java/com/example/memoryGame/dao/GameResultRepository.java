package com.example.memoryGame.dao;

import com.example.memoryGame.model.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultRepository extends JpaRepository<GameResult, Integer> {

}
