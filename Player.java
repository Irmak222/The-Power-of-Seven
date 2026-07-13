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
    public void addCardToHand(Card card) {
        activeHand.add(card); // add card to the hand

        // check if the drawn card is number card
        if(card instanceof NumberCard) {
            NumberCard newNumberCard = (NumberCard) card;

            // if the player has the card with the drawn card's value 
            if(CardProcessor.hasDuplicate(this.activeHand,newNumberCard.getValue())) {
                this.isBusted = true; // player is busted
                this.roundScore = 0; // current round is zero
            } else { // if the player does not have this value
                this.roundScore += newNumberCard.getValue(); // add this value to the round score
            }
        }
    }

    // Resets the round state after every round
    public void resetRoundState() {
        this.activeHand.clear(); // clears player's hand
        this.roundScore = 0; // resets the score
        this.isBusted = false; // player is not busted yet
        this.isFrozen = false; // player is not frozen yet
    }

    // Player being frozen
    public void freeze() {
        this.isFrozen = true;
    }

    // adding points to the total score
    public void addPointsToTotalScore(int points) {
        this.totalScore += points;
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

    public void setIsBusted(boolean setBusted) {
        isBusted = setBusted;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public ArrayList<Card> getActiveHand() {
        return activeHand;
    }

}


