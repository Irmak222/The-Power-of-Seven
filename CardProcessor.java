import java.util.ArrayList;

public class CardProcessor {
    
    private CardProcessor() {

    }

    // Checks if the player has same card with drawn card
    public static boolean hasDuplicate(ArrayList<Card> hand, NumberCard cardDrawn) {
        int count = 0;
        for(int i = 0; i < hand.size(); i++){
            if(hand.get(i) instanceof NumberCard){
                NumberCard newNumberCard = (NumberCard) hand.get(i);
                if(newNumberCard.getValue() == cardDrawn.getValue()){
                    count++; // increase if the values are equal
                }
            }
        }
        // if there is more than one of this value, there is duplicate of the card that player drew before.
        return count > 1;
    }

    // Checks if the player has Second Chance card
    public static boolean hasSecondChance(Player player) {
        for(Card card : player.getActiveHand()){
            if(card.getCardName().equals("Second Chance")){
                return true;
            }
        }
        return false;
    }

    // Player using Second Chance card to revive
    public static void useSecondChance(Player player, Card duplicate) {
        player.getActiveHand().remove(duplicate);
        player.setIsBusted(false);

    }

    // Player playing Freeze card against opponent player
    public static void playFreeze(Player targetPlayer) {
        targetPlayer.freeze();
    }

    // Player flipping three cards consecutively
    public static void flipThree(Player player, Deck deck) {
        player.addCardToHand(deck.draw());
        player.addCardToHand(deck.draw());
        player.addCardToHand(deck.draw());
    }

    // Ends round with score calculation and clears the table
    public static int calculateHandScore(ArrayList<Card> hand) {
        int roundTotalScore = 0;
        boolean hasMultiplier = false;
        
        for(int i = 0; i < hand.size(); i++){
            if(hand.get(i) instanceof NumberCard){
                NumberCard newNumberCard = (NumberCard) hand.get(i);
                roundTotalScore += newNumberCard.getValue();
            }
            else if(hand.get(i) instanceof ModifierCard){
                ModifierCard newModifierCard = (ModifierCard) hand.get(i);
                if(newModifierCard.isMultiplier()){
                    hasMultiplier = true;
                } else{
                    roundTotalScore += newModifierCard.getBonusPoints();
                }
            }
        }
        if(hasMultiplier){
            roundTotalScore *= 2;
        }
        return roundTotalScore;
    }

    // Clears player's hand
    public static void clearHand(Player player) {
        player.getActiveHand().clear();
    }
}
