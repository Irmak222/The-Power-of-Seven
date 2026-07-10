// The abstract base class for all 94 cards in The Power of Seven.
public abstract class Card {
    private final String id; // unique identifier for the card 
    private final String cardName; // name of the card

    // Constructor to initialize a card with fixed identifiers. 
    public Card(String id, String cardName) {
        this.id = id;
        this.cardName = cardName;
    }

    // getter for card ID
    public String getId() {
        return id;
    }

    // getter for card name
    public String getCardName() {
        return cardName;
    }
}
