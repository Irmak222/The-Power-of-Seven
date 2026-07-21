package com.sevenup.cardgame;

// Represents the numeric cards in the game from 0 to 12.
public class NumberCard extends Card {
    private final int value;

    public NumberCard(String id, String cardName,String cardImageName, int value) {
        super(id, cardName, cardImageName);
        this.value = value;
    }

    public int getValue() { return value; }
}
