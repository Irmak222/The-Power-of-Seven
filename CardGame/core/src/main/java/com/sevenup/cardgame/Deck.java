package com.sevenup.cardgame;

import java.util.ArrayList;
import java.util.Collections;

// The card deck for the game.
public class Deck {
    // remaining cards waiting to be drawn
    private final ArrayList<Card> cards;
    // discard pile for used cards
    private final ArrayList<Card> discardPile;

    // Constructor for Deck with generating all game cards.
    public Deck() {
        this.cards = new ArrayList<>();
        this.discardPile = new ArrayList<>();
        initializeDeck(); // generates all 94 game cards
        shuffle(); // shuffle the cards for starting the game
    }

    // Shuffles the deck of cards
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // Initializes deck for the game by generating 94 game cards
    private void initializeDeck() {
        cards.clear();
        discardPile.clear();

        int idCounter = 1; // counter for card ID's

        // generating number cards 
        cards.add(new NumberCard("card_" + idCounter++, "Number 0","0.png", 0)); // there is one Number 0 card in the deck
        cards.add(new NumberCard("card_" + idCounter++, "Number 1","1.png", 1)); // there is one number 1 card in th deck

        // from Number 2 to Number 12, adding cards equivalent to the card's numerical value
        for(int value = 2; value <= 12; value++) {
            for(int count = 0; count < value; count++) {
                cards.add(new NumberCard("card_" + idCounter++, "Number" + value, value + ".png", value));
            }
        }

        // generating action cards
        for(int i = 0; i < 3; i++) { // thrre cards for each action card
            cards.add(new ActionCard("card_" + idCounter++, "Freeze","Freeze.png", ActionCard.FREEZE));
            cards.add(new ActionCard("card_" + idCounter++, "Flip Three","FlipThree.png", ActionCard.FLIP_THREE));
            cards.add(new ActionCard("card_" + idCounter++, "Second Chance","SecondChance.png", ActionCard.SECOND_CHANCE));
        }

        // generating modifier cards
        cards.add(new ModifierCard("card_" + idCounter++, "Modifier +2", "PlusTwo.png",2, false));
        cards.add(new ModifierCard("card_" + idCounter++, "Modifier +4", "PlusFour.png",4, false));
        cards.add(new ModifierCard("card_" + idCounter++, "Modifier +6", "PlusSix.png",6, false));
        cards.add(new ModifierCard("card_" + idCounter++, "Modifier +8", "PlusEight.png",8, false));
        cards.add(new ModifierCard("card_" + idCounter++, "Modifier +10", "PlusTen.png",10, false));
        cards.add(new ModifierCard("card_" + idCounter++, "Modifier x2", "TimesTwo.png",0, true));
        

    }

    /**  
    * Drawing card by removing the top card from the deck
    * @return the drawn Card
    */
    public Card draw() {
        // if the drawing deck is empty
        if(cards.isEmpty()) {
            // if the discard pile is empty
            if(discardPile.isEmpty()) {
                initializeDeck(); // generate the card games
            } else { // if the discard pile is not empty
                cards.addAll(discardPile); // add all the cards that are discarded to the deck
                discardPile.clear(); // clear the discard pile
            }
            // shuffle the deck
            shuffle(); 
        }
        // remove the top card from the deck and return that drawn card
        return cards.remove(cards.size() - 1);
    }

    //Discarding card by adding the card to the discardPile
    public void discard(Card card) {
        discardPile.add(card);
    }

    /** Helper method for tracking current state
     * @return the active remaining card count
     */
    public int getRemainingCardCount() {
        return cards.size();
    }
}
