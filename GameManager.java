import java.util.ArrayList;

public class GameManager {
    // A card deck for the game
    private final Deck deck;
    // Players
    private final ArrayList<Player> players;
    // Tracks which player's turn
    private int currentPlayerIndex;
    // Tracks the which rouns is being played currently
    private int currentRound;
    // The target score that players should reach to win the game
    private static final int TARGET_SCORE = 200;

    // Constructor
    public GameManager() {
        this.deck = new Deck(); // initialize a new deck
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.currentRound = 1;
    }

    // Starting a new game 
    public void startNewGame(ArrayList<Player> activePlayers) {

    }

    // Starting a new round by shuffling the deck and resets all players' states
    public void startNewRound() {

    }

    // Executing Flip action (drawing card)
    public void flip() {

    }

    // Executing Stay action
    public void stay() {

    }

    // Passes turn to the next active player
    private void passTurn() {

    }

    // Checks if any player reachs the target score
    public boolean isGameOver() {

    }

}

