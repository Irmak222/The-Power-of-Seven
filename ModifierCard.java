// Represents score modifier cards
public class ModifierCard extends Card {
    private final int bonusPoints; // bonus points to add (0 if it is a multiplier card) or points to be taken away
    private final boolean isMultiplier; // flag to determine if this card multiplies the score instead of adding bonus points or dividing points.
    private final boolean isDivider; // flag to determine if this card divides the score instead of adding bonus points or multiplies points.
    
    // Constructor for ModifierCard
    public ModifierCard(String id, String cardName, int bonusPoints, boolean isMultiplier) {
        super(id, cardName);
        this.bonusPoints = bonusPoints;
        this.isMultiplier = isMultiplier;
    }

    // getter for bonus points
    public int getBonusPoints() {
        return bonusPoints;
    }

    // getter to check if it is a multiplier card
    public boolean isMultiplier() {
        return isMultiplier;
    }

    // getter to check if it is a divider card
    public boolean isDivider(){
        return isDivider;
    }
    
}
