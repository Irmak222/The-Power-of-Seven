// Represents the numeric cards in the game from 0 to 12.
public class NumberCard extends Card {
    private final int value;  // the numeric value of the card

    // Constructor for NumberCard
    public NumberCard(String id, String cardName, int value) {
        super(id, cardName);
        this.value = value;
    }

    // getter for the value of the card
    public int getValue() {
        return value;
    }
}
