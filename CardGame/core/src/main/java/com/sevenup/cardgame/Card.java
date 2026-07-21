package com.sevenup.cardgame;

import com.badlogic.gdx.graphics.Texture;

// The abstract base class for all 94 cards in The Power of Seven.
public abstract class Card {
    private final String id; // unique identifier for the card 
    private final String cardName; // name of the card
     private final Texture cardImage; // card images
    
    // Constructor to initialize a card with fixed identifiers. 
    public Card(String id, String cardName, String cardImageName) {
        this.id = id;
        this.cardName = cardName;
        this.cardImage = new Texture(cardImageName);        
    }

    // getter for card ID
    public String getId() {
        return id;
    }

    // getter for card name
    public String getCardName() {
        return cardName;
    }

    //getter for card image
    public Texture getImage() {
        return cardImage;
    }

    @Override
    public String toString() {
        return cardName;
    }
}