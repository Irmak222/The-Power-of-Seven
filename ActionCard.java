// Represents action cards that can change game state
public class ActionCard extends Card {
    // Freeze card: Forces a target player to stop flipping and lock current points.
    private static final String FREEZE = "FREEZE";
    // Flip Three card: Forces a target to draw 3 consecutive cards.
    private static final String FLIP_THREE = "FLIP_THREE";
    // Second Chance card: Discarded to prevent a bust on duplicate cards.
    private static final String SECOND_CHANCE = "SECOND_CHANCE";
    // holds action type for a specific card
    private final String actionType;

    // Constructor for ActionCard
    public ActionCard(String id, String cardName, String actionType) {
        super(id, cardName);
        this.actionType = actionType;  
    }

    // getter for the specific action type of the card
    public String getActionType() {
        return actionType;
    }
}
