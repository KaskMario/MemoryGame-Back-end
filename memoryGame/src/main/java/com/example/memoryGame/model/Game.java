package com.example.memoryGame.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private GameDifficulty difficulty;
    private Integer gridSize;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time", updatable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Transient
    private List<Card> cards;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public void initializeCards() {
        int numPairs = (gridSize * gridSize) / 2;
        List<Card> cardList = new ArrayList<>();
        Set<String> generatedExpressions = new HashSet<>();
        Set<Integer> generatedResults = new HashSet<>();

        if (difficulty == GameDifficulty.EASY) {
            Random random = new Random();
            while (cardList.size() < numPairs * 2) {
                int leftOperand = random.nextInt(10);
                int rightOperand = random.nextInt(10);
                String expression = leftOperand + "+" + rightOperand;
                int result = leftOperand + rightOperand;

                if (!generatedExpressions.contains(expression) && !generatedResults.contains(result)) {
                    generatedExpressions.add(expression);
                    generatedResults.add(result);

                    cardList.add(new Card(expression, this));
                    cardList.add(new Card(String.valueOf(result), this));
                }
            }
        } else if (difficulty == GameDifficulty.MEDIUM) {
            char startChar = 'a';
            for (int i = 0; i < numPairs; i++) {
                if ((startChar + i) <= 'z') {
                    String value = String.valueOf((char) (startChar + i));
                    cardList.add(new Card(value, this));
                    cardList.add(new Card(value, this));
                } else {
                    throw new RuntimeException("Not enough unique letters to generate the grid.");
                }
            }
        } else {
            for (int i = 0; i < numPairs; i++) {
                String value = generateCardValue(i);
                cardList.add(new Card(value, this));
                cardList.add(new Card(value, this));
            }
        }

        Collections.shuffle(cardList);
        this.cards = cardList;
    }



    private String generateCardValue(int index) {
        int value = index % 100;
        return String.valueOf(value);
    }


    public void endGame() {
        this.endTime = Instant.now();
        this.status = GameStatus.COMPLETED;
    }
}


