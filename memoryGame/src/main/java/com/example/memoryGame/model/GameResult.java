package com.example.memoryGame.model;

import java.time.Duration;
import java.time.Instant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "start_time", updatable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "time_taken")
    private Long timeTaken;

    public void calculateAndSetTimeTaken() {
        if (startTime != null && endTime != null) {
            this.timeTaken = Duration.between(startTime, endTime).getSeconds();
        }
    }
}
