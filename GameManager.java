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

    // Starts a new round and resets all players' states
    public void startNewRound() {
        currentRound++;
        for( Player player : players){
            for( Card card : player.getActiveHand()){
                deck.discard(card);
            }
            player.resetRoundState();
        }
    }

    // Executing Flip action (drawing card)
    public void flip() {
        Card card = deck.draw();
        players.get(currentPlayerIndex).addCardToHand(card);
    }

    // Executing Stay action
    public void stay() {
        players.get(currentPlayerIndex).freeze();
    }

    // Passes turn to the next active player
    public void passTurn() {
        if(currentPlayerIndex == players.size() - 1){
            currentPlayerIndex = 0;
        }
        else{
            currentPlayerIndex++;
        }
    }

    // Checks if any player reachs the target score
    public boolean isGameOver() {
        for( Player player : players){
            if(player.getTotalScore() > TARGET_SCORE){
                return true;
            }
        }
        return false;
    }

}

