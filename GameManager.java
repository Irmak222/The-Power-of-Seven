import java.util.ArrayList;

public class GameManager {
    // A card deck for the game
    private final Deck deck;
    // Players
    private final ArrayList<Player> players;
    // Tracks which player's turn
    private int currentPlayerIndex;
    // The target score that players should reach to win the game
    private static final int TARGET_SCORE = 200;

    // Constructor
    public GameManager() {
        this.deck = new Deck(); // initialize a new deck
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
    }

    // Starts a new round and resets all players' states
    public void startNewRound() {
        for( Player player : players){
            if(!player.isBusted()) {
            player.setTotalScore(player.getTotalScore() - player.getRoundScore() + CardProcessor.calculateHandScore(player.getActiveHand()));
            }
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
        int count = 0;

        // if the next player is busted or frozen, keep passing the turn
        while((players.get(currentPlayerIndex).isBusted() || players.get(currentPlayerIndex).isFrozen()) && count < players.size()) {
            if (currentPlayerIndex == players.size() - 1) {
                currentPlayerIndex = 0;
            } else {
                currentPlayerIndex++;
            }
            
            count++;
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

    // Adds a player
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    // Getters
    public Deck getDeck() {
        return deck;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public static int getTargetScore() {
        return TARGET_SCORE;
    }

}

