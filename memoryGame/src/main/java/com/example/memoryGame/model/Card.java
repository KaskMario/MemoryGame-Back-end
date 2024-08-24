package com.example.memoryGame.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @JsonIgnore
    private Game game;
    private Integer id;
    private String value;

    public Card(String value, Game game) {
        this.value = value;
        this.game = game;
    }
}
