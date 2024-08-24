package com.example.memoryGame.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

        for (int i = 0; i < numPairs; i++) {
            String value = generateCardValue(i);
            cardList.add(new Card(value, this));
            cardList.add(new Card(value, this));
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