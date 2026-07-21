package com.sevenup.cardgame;

// Represents a user session in the local gameplay.
// Manages the player's identification, dynamic score calculations, current hand status
// and specific state flags (e.g. busted, frozen).

import java.util.ArrayList;

public class Player {
    // name
    private final String name;
    // flag to check whether the user is guest or not
    private final boolean isGuest;
    // temporary score accumulated in the ongoing round
    private int roundScore;
    // cumulative points accumulated in all completed rounds
    private int totalScore;
    // flag to check if the player draws a duplicate NumberCard and fails the round
    private boolean isBusted;
    // flag to check if the player gets restricted by other player's Freeze action or hits 'Stay'
    private boolean isFrozen;
    // all cards that are drawn during the current round
    private final ArrayList<Card> activeHand;
    // flag to checl if the player used second chance card
    private boolean secondChanceUsed = false;

    // Constructor for Player
    public Player(String name, boolean isGuest) {
        this.name = name;
        this.isGuest = isGuest;
        this.roundScore = 0;
        this.totalScore = 0;
        this.isBusted = false;
        this.isFrozen = false;
        this.activeHand = new ArrayList<>();
    }

    // Adding the drawn card to the player's hand
    public void addCardToHand(Card card, Deck deck) {
        // check if the drawn card is number card
        if(card instanceof NumberCard) {
            NumberCard newNumberCard = (NumberCard) card;
            activeHand.add(card); // add card to the hand

            // if the player has the card with the drawn card's value 
            if (CardProcessor.hasDuplicate(this.activeHand, newNumberCard)) {
                // if the player has a second chance card
                if (CardProcessor.hasSecondChance(this)) {
                    // remove the duplicate from the player's hand and discard
                    activeHand.remove(card);
                    deck.discard(card);

                    // Remove the second chance card frpm the player's hand and discard
                    Card secondChanceCard = null;
                    boolean found = false;

                    for (int i = 0; i < activeHand.size() && !found; i++) {
                        if (activeHand.get(i).getCardName().equals("Second Chance")) {
                            secondChanceCard = activeHand.get(i);
                            found = true;
                        }
                    }

                    if (secondChanceCard != null) {
                        activeHand.remove(secondChanceCard);
                        deck.discard(secondChanceCard);
                    }

                    // player is not busted since player used the second chance card
                    this.isBusted = false;
                    this.secondChanceUsed = true;
                } else {
                    // if player does not have second chance card, player is busted
                    this.isBusted = true;
                    this.roundScore = 0;
                }
            } else {
                // if there is no duplicate , add the point
                this.roundScore += newNumberCard.getValue();
            }
        } else {
            activeHand.add(card);
        }
    }

    // Resets the round state after every round
    public void resetRoundState() {
        this.activeHand.clear(); // clears player's hand
        this.roundScore = 0; // resets the score
        this.isBusted = false; // player is not busted yet
        this.isFrozen = false; // player is not frozen yet
    }

    // check if the player can flip three cards
    public boolean canFlipThree() {
        if(this.isBusted()) {
            return false;
        }

        int count = 0;
        for (Card card : this.getActiveHand()) {
            if (card instanceof NumberCard) {
                count++;
            }
        }
        return count < 7; 
    }

    // Player being frozen
    public void freeze() {
        this.isFrozen = true;
    }

    // Player being unfrozen
    public void unFreeze() {
        isFrozen = false;
    }

    // adding points to the total score
    public void addPointsToTotalScore(int points) {
        this.totalScore += points;
    }
    
    //Setters
    public void setIsBusted(boolean setBusted) {
        isBusted = setBusted;
    }
    
    public void setTotalScore(int value){
        totalScore = value;
    }

    // Getters
    public String getName() {
        return name;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public int getRoundScore() {
        return roundScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public boolean isBusted() {
        return isBusted;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public ArrayList<Card> getActiveHand() {
        return activeHand;
    }

    public boolean isSecondChanceUsed() {
        return secondChanceUsed;
    }

    public void setSecondChanceUsed(boolean isUsed) {
        this.secondChanceUsed = isUsed;
    }

}
