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
            if(hasNumberInHand(newNumberCard.getValue())) {
                this.isBusted = true; // player is busted
                this.roundScore = 0; // current round is zero
            } else { // if the player does not have this value
                this.roundScore += newNumberCard.getValue(); // add this value to the round score
            }
        }
    }

    /**
     * Checks if the player has a card with the passed value.
     * @return true if a matching duplicate exists in hand, false otherwise.
     */
    private boolean hasNumberInHand(int value) {
        // checking all the cards in the player's hand
        for(int i = 0; i < activeHand.size(); i++) {
            Card current = activeHand.get(i);

            // if the card is a number card
            if(current instanceof NumberCard) {
                NumberCard numberCard = (NumberCard) current;
                // if the card has that value
                if(numberCard. getValue() == value) {
                    return true; // there are duplicates
                }
            }
        }
        return false; // no duplicates
    }

    // Checks if the player's hand satisfies the winning condition
    // (successfully flip seven unique number cards into the hand)
    public boolean checkWinningHandCondition() {
        int numberCount = 0; 
        for(int i = 0; i < activeHand.size(); i++) {
            if(activeHand.get(i) instanceof NumberCard) {
                numberCount++;
            }
        }
        return numberCount == 7;
    }

    // Resets the round state after every round
    public void resetRoundState() {
        this.activeHand.clear(); // clears player's hand
        this.roundScore = 0; // resets the score
        this.isBusted = false; // player is not busted yet
        this.isFrozen = false; // player is not frozen yet
    }



    // Getters and setters
    public String getName() {
        return name;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public int getRoundScore() {
        return roundScore;
    }

    public void setRoundScore(int roundScore) {
        this.roundScore = roundScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    // adding points to the total score
    public void addPointsToTotalScore(int points) {
        this.totalScore += points;
    }

    public boolean isBusted() {
        return isBusted;
    }

    public void setBusted(boolean isBusted) {
        this.isBusted = isBusted;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean isFrozen) {
        this.isFrozen = isFrozen;
    }

    public ArrayList<Card> getActiveHand() {
        return activeHand;
    }

    // displays player's total score and current round score with player's name
    @Override
    public String toString() {
        return "Player: " + name + " | Total Score: " + totalScore + " | Current Round Points: " + roundScore;
    }
}

