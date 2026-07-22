package com.sevenup.cardgame.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sevenup.cardgame.Main;
import com.sevenup.cardgame.game.GameManager;
import com.sevenup.cardgame.game.NumberCard;
import com.sevenup.cardgame.game.Player;
import com.sevenup.cardgame.game.ActionCard;
import com.sevenup.cardgame.game.Card;
import com.sevenup.cardgame.game.CardProcessor;

public class GameScreen implements Screen {
    private final Main game;
    private final int playerCount; // 3, 4 or 5 player

    private GameManager gameManager;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Stage stage;
    private BitmapFont font;
    private BitmapFont messageFont; 

    private Texture deckPicture; // deck picture
    private Texture emptyCardSlot; // card slots 
    private Texture bustedBackground; 
    private Texture frozenBackground;
    private Texture stayedBackground;

    private Texture blueButtonTex;   // Freeze button blue
    private Texture greenButtonTex;  // Flip Three button green
    private Texture purpleButtonTex; // Help , flip , stay button purple
    private Texture roundEndTex; // Background round end message
    
    // tables for organization
    private Table controlPanel;
    private Table[] playerAreas;
    private Table[] cardsGrids;
    private Table[] bustedPanels;
    private Table[] frozenPanels;
    private Table[] stayedPanels;
    private Table roundEndPanel;

    // Labels for status and scores
    private Label turnLabel; // shows which player's turn
    private Label[] playerScoreLabels;
    private Label roundEndLabel;

    // next round button on the round end panel
    private TextButton nextRoundButton;

    private TextButton mainMenuButton;

    // how many card flips remain (for flip three action)
    private int flipThreeRemainings = 0;

    // who flips three cards when flip three card is drawn
    private Player flipThreeTargetPlayer = null;

    // Stores the currentle drawn Action card that waits player action
    private ActionCard activeActioncard = null;
    
    public GameScreen(Main game, int playerCount, ArrayList<String> registeredPlayerNames) {
        this.game = game;
        this.playerCount = playerCount;
        this.gameManager = new GameManager();
        
        // Add registered players or create guest players
        for(int i = 0; i < playerCount; i++) {
            String name;
            boolean isGuest; 

            if(registeredPlayerNames != null && i < registeredPlayerNames.size()) {
                name = registeredPlayerNames.get(i);
                isGuest = false;
            } else {
                name = "Guest Player " + (i + 1);
                isGuest = true;
            }

            this.gameManager.addPlayer(new Player(name, isGuest));
        }
    }

    @Override
    public void show() {
        // Initialize rendering elements
        batch = new SpriteBatch();
        camera = new OrthographicCamera();

        // Create the Stage with ScreenViewport to scale UI elements to screen pixels
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Using font file for button text's fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ARIAL.TTF"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.BLACK;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        font = generator.generateFont(parameter);

        // Busted message font
        FreeTypeFontParameter bustedParameter = new FreeTypeFontParameter();
        bustedParameter.size = 48;
        bustedParameter.color = Color.WHITE;
        bustedParameter.minFilter = Texture.TextureFilter.Linear;
        bustedParameter.magFilter = Texture.TextureFilter.Linear;
        messageFont = generator.generateFont(bustedParameter);

        generator.dispose();

        // Card slots
        emptyCardSlot = createBorderedTexture(75, 100, new Color(0.5f, 0.2f, 0.6f, 1f));

        // Busted message background
        bustedBackground = createTransparentTexture(1, 1, new Color(139f / 255f, 26f / 255f, 26f / 255f,0.65f));
        // Frozen message background
        frozenBackground = createTransparentTexture(1, 1, new Color(0f / 255f, 159f / 255f, 227f / 255f,0.65f));
        // Stayed message background
        stayedBackground = createTransparentTexture(1, 1, new Color(238f / 255f, 186f / 255f, 81f / 255f,0.65f));

        // Buttons on this screen
        blueButtonTex = createButtonTexture(100, 35, new Color(0.7f, 0.8f, 1f, 1f), Color.BLACK);   
        greenButtonTex = createButtonTexture(140, 35, new Color(0.6f, 0.9f, 0.7f, 1f), Color.BLACK); 
        purpleButtonTex = createButtonTexture(85, 35, new Color(0.85f, 0.8f, 0.95f, 1f), Color.BLACK); 

        // Round end message backgraound
        roundEndTex = createTransparentTexture(1, 1, new Color(0.2f, 0.1f, 0.35f, 0.92f));

        initializeControlPanel();
        initializePlayerAreas();
        buildDynamicLayout();
        initializeRoundEndPanel();
        updateTurnDisplay();
        updateScoresDisplay();
        updatePlayerCardsAndBustedStates();
    }

    // creating buttons textures
    private Texture createButtonTexture(int width, int height, Color bgColor, Color borderColor) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(bgColor);
        pixmap.fill();
        pixmap.setColor(borderColor);
        pixmap.drawRectangle(0, 0, width, height);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    // Creating rectangles for card slots
    private Texture createBorderedTexture(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.drawRectangle(0, 0, width, height);
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    // Creating transparent background messages
    private Texture createTransparentTexture(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    // Initializing control panel that has deck image, turn status, Flip, Stay and Help buttons
    private void initializeControlPanel() {
        controlPanel = new Table();

        LabelStyle labelStyle = new LabelStyle(font, Color.BLACK);

        TextButtonStyle purpleStyle = new TextButtonStyle();
        purpleStyle.font = font;
        purpleStyle.fontColor = Color.BLACK;
        purpleStyle.up = new TextureRegionDrawable(purpleButtonTex);

        deckPicture = new Texture(Gdx.files.internal("BackOfTheCard.png"));
        Image deckImage = new Image(deckPicture);

        turnLabel = new Label("", labelStyle);

        // Buttons
        TextButton flipButton = new TextButton("Flip", purpleStyle);
        TextButton stayButton = new TextButton("Stay", purpleStyle);

        // When flip button is clicked, player flips a card form the deck
        flipButton.addListener(new ClickListener() {
           @Override
            public void clicked(InputEvent event, float x, float y) {
                if (activeActioncard != null) { 
                    return;
                } 

                Player currentPlayer = gameManager.getPlayers().get(gameManager.getCurrentPlayerIndex());

                // if the player is not frozen and not busted
                if(!currentPlayer.isFrozen() && !currentPlayer.isBusted() && !currentPlayer.isStayed()) {
                    // draw a card
                    Card drawnCard = gameManager.getDeck().draw();
                    // add to the player's hand
                    currentPlayer.addCardToHand(drawnCard, gameManager.getDeck());
                    
                    // if the drawn card is an action card
                    if (drawnCard instanceof ActionCard) {
                        ActionCard actionCard = (ActionCard) drawnCard;
                        // player uses the action card according to its type
                        if (actionCard.getActionType().equals(ActionCard.FREEZE) || actionCard.getActionType().equals(ActionCard.FLIP_THREE)) {
                            activeActioncard = actionCard;
                        } else {
                            // otherwise next player flips
                            gameManager.passTurn();
                        }
                    } else {
                        
                            gameManager.passTurn();
                        
                    }

                    // Updates turns, scores, cards, and status
                    updateTurnDisplay();
                    updateScoresDisplay();
                    updatePlayerCardsAndBustedStates();
                    checkRoundStatus();
                }
            }
        });

        // When stay button is clicked, player stays, does not flip a card, and player turn is changed.
        stayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(activeActioncard != null) {
                    return;
                }

                gameManager.stay(); 
                gameManager.passTurn(); 
                updateTurnDisplay();
                updatePlayerCardsAndBustedStates();
                checkRoundStatus();
            }
        });

        // Card deck and help button
        Table deckTable = new Table();
        deckTable.add(deckImage).width(80).height(110).row();

        // Turn label, flip and stay button
        Table actionTable = new Table();
        actionTable.add(turnLabel).padBottom(5).row();
        actionTable.add(flipButton).padBottom(5).width(80).height(35).row();
        actionTable.add(stayButton).width(80).height(35);
        
        controlPanel.add(deckTable).pad(15);
        controlPanel.add(actionTable).pad(15);
    }

    // Initializing card grids, scores, and action buttons for players
    private void initializePlayerAreas() {
        playerAreas = new Table[playerCount];
        playerScoreLabels = new Label[playerCount];
        cardsGrids = new Table[playerCount];
        bustedPanels = new Table[playerCount];
        frozenPanels = new Table[playerCount];
        stayedPanels = new Table[playerCount];

        LabelStyle labelStyle = new LabelStyle(font, Color.BLACK);
        LabelStyle messageLabelStyle = new LabelStyle(messageFont, Color.WHITE);

        // Freeze button style 
        TextButtonStyle blueStyle = new TextButtonStyle();
        blueStyle.font = font;
        blueStyle.fontColor = Color.BLACK;
        blueStyle.up = new TextureRegionDrawable(blueButtonTex);

        // Flip Three button style
        TextButtonStyle greenStyle = new TextButtonStyle();
        greenStyle.font = font;
        greenStyle.fontColor = Color.BLACK;
        greenStyle.up = new TextureRegionDrawable(greenButtonTex);

        for(int i = 0; i < playerCount; i++) {
            final int playerIndex = i;
            Table area = new Table();

            Player currentPlayer = gameManager.getPlayers().get(i);
            String name = currentPlayer.getName();
            int score = currentPlayer.getTotalScore();

            Label nameLabel = new Label(name + "\nScore: " + score, labelStyle);
            playerScoreLabels[i] = nameLabel;

            TextButton freezeButton = new TextButton("Freeze", blueStyle);
            TextButton flipThreeButton = new TextButton("Flip Three", greenStyle);

            // Freeze the target player
            freezeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (activeActioncard != null && ActionCard.FREEZE.equals(activeActioncard.getActionType())) {
                        int currentIndex = gameManager.getCurrentPlayerIndex();
                        Player currentPLayer = gameManager.getPlayers().get(currentIndex);
                        Player targetPlayer = gameManager.getPlayers().get(playerIndex);
            
                        // player who draws a freeze card can freeze any player that is not frozen or busted (including self if there is not other option)
                        if (!targetPlayer.isFrozen() && !targetPlayer.isBusted() && !targetPlayer.isStayed()) {
                            CardProcessor.playFreeze(targetPlayer);
                            
                            // remove the freeze card from the player's hand
                            currentPLayer.getActiveHand().remove(activeActioncard);
                            gameManager.getDeck().discard(activeActioncard);
                            activeActioncard = null; 

                            gameManager.passTurn();
                
                            // Update turns, scores, cards, and status
                            updateTurnDisplay();
                            updateScoresDisplay();
                            updatePlayerCardsAndBustedStates();
                            checkRoundStatus();
                        }
                    }
                }
            });

            // Flip three cards 
            flipThreeButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    int currentIndex = gameManager.getCurrentPlayerIndex();
                    Player currentPlayer = gameManager.getPlayers().get(currentIndex);
                    Player targetPlayer = gameManager.getPlayers().get(playerIndex);
        
                    if (activeActioncard != null && ActionCard.FLIP_THREE.equals(activeActioncard.getActionType())) {
                        if(!targetPlayer.isBusted() && !targetPlayer.isFrozen() && !targetPlayer.isStayed()) {
                        // remove flip three card from the player's hand
                        currentPlayer.getActiveHand().remove(activeActioncard);
                        gameManager.getDeck().discard(activeActioncard);
                        activeActioncard = null;

                        // set up flip three process
                        flipThreeRemainings = 3;
                        flipThreeTargetPlayer = targetPlayer;

                        // update turns, scores, cards and status
                        updateTurnDisplay();
                        updateScoresDisplay();
                        updatePlayerCardsAndBustedStates();
                        }
                        return;
                    }

                    if (flipThreeRemainings > 0 && flipThreeTargetPlayer != null && playerIndex == gameManager.getPlayers().indexOf(flipThreeTargetPlayer)) {
                        if (flipThreeTargetPlayer.canFlipThree()) {
                            Card drawnCard = gameManager.getDeck().draw();
                            if(drawnCard.equals(ActionCard.FREEZE)) {
                                targetPlayer.isFrozen();
                            }
                            flipThreeTargetPlayer.addCardToHand(drawnCard, gameManager.getDeck());
                            flipThreeRemainings--;

                            // Stop process if player is busted, flips 7 unique numbers, or runs out of flips
                            if (flipThreeTargetPlayer.isBusted() || gameManager.hasSevenUniqueNumbers(flipThreeTargetPlayer) || flipThreeRemainings == 0) {
                                flipThreeRemainings = 0;
                                flipThreeTargetPlayer = null;
                                gameManager.passTurn();
                            }
                        } else {
                            flipThreeRemainings = 0;
                            flipThreeTargetPlayer = null;
                            gameManager.passTurn();
                        }

                        updateTurnDisplay();
                        updateScoresDisplay();
                        updatePlayerCardsAndBustedStates();
                        checkRoundStatus();
                    }

                    
                }
            });

            // Table that contains player's name, freeze and flip three button
            Table infoRow = new Table();
            infoRow.add(nameLabel).padRight(15);
            infoRow.add(freezeButton).width(90).height(32).padRight(10);
            infoRow.add(flipThreeButton).width(130).height(32);

            Table cardsGrid = new Table();
            cardsGrids[i] = cardsGrid;

            // Busted message panel
            Table bustedPanel = new Table();
            bustedPanel.setBackground(new Image(bustedBackground).getDrawable());
            Label bustedText = new Label("Busted", messageLabelStyle);
            bustedPanel.add(bustedText).center();
            bustedPanel.setVisible(false);
            bustedPanels[i] = bustedPanel;

            // Frozen message panel
            Table frozenPanel = new Table();
            frozenPanel.setBackground(new Image(frozenBackground).getDrawable());
            Label frozenText = new Label("Frozen", messageLabelStyle);
            frozenPanel.add(frozenText).center();
            frozenPanel.setVisible(false);
            frozenPanels[i] = frozenPanel;

            // Stayed message panel
            Table stayedPanel = new Table();
            stayedPanel.setBackground(new Image(stayedBackground).getDrawable());
            Label stayedText = new Label("Stayed", messageLabelStyle);
            stayedPanel.add(stayedText).center();
            stayedPanel.setVisible(false);
            stayedPanels[i] = stayedPanel;

            // Stack card slots and messages over each other
            Stack cardStack = new Stack();
            cardStack.add(cardsGrid);
            cardStack.add(bustedPanel);
            cardStack.add(frozenPanel);
            cardStack.add(stayedPanel);

            area.add(cardStack).padBottom(10).row();
            area.add(infoRow);

            playerAreas[i] = area;
        }
    }

    // Create a round end message panel for each round end
    public void initializeRoundEndPanel() {
        roundEndPanel = new Table();
        roundEndPanel.setBackground(new TextureRegionDrawable(roundEndTex));

        LabelStyle labelStyle = new LabelStyle(messageFont, Color.WHITE);
        roundEndLabel = new Label("Round Ended!", labelStyle);

        TextButtonStyle purpleStyle = new TextButtonStyle();
        purpleStyle.font = font;
        purpleStyle.fontColor = Color.BLACK;
        purpleStyle.up = new TextureRegionDrawable(purpleButtonTex);

        nextRoundButton = new TextButton("Next Round", purpleStyle); 
        

        // when next round button is clicked, current hands are cleared and new rouns is started
        nextRoundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameManager.startNewRound();
                roundEndPanel.setVisible(false);
                updateTurnDisplay();
                updateScoresDisplay();
                updatePlayerCardsAndBustedStates();
            }
        });

        // Adding labels and buttons to the round end panel
        roundEndPanel.add(roundEndLabel).padBottom(30).colspan(2).row();
        roundEndPanel.add(nextRoundButton).width(160).height(45).padRight(20);
        roundEndPanel.setVisible(false);
        roundEndPanel.toFront();

        stage.addActor(roundEndPanel);
        roundEndPanel.setSize(480, 220);
        // place the round end panel to the center of the screen
        roundEndPanel.setPosition((Gdx.graphics.getWidth() - roundEndPanel.getWidth()) / 2f, (Gdx.graphics.getHeight() - roundEndPanel.getHeight()) / 2f);
    }
    
    // Updating card slots for all players 
    public void updatePlayerCardsAndBustedStates() {
        for (int i = 0; i < playerCount; i++) {
            Player player = gameManager.getPlayers().get(i); 
            Table grid = cardsGrids[i];
            grid.clearChildren(); // clear 
            
            ArrayList<Card> hand = player.getActiveHand(); 
            ArrayList<Card> numberCards = new ArrayList<>();
            ArrayList<Card> actionAndModifiers = new ArrayList<>();

            for(Card card : hand) {
                if(card instanceof NumberCard) { 
                    numberCards.add(card);
                } else {
                    actionAndModifiers.add(card);
                }
            }

            // First row of card slots for number cards
            for (int col = 0; col < 7; col++) {
                if (col < numberCards.size()) {
                    Card currentCard = numberCards.get(col);
                    // Card image added
                    Image cardImage = new Image(currentCard.getImage());
                    grid.add(cardImage).width(60).height(88).pad(3);
                } else {
                    Image cardSlot = new Image(emptyCardSlot);
                    grid.add(cardSlot).width(60).height(88).pad(3);
                }
            }
            grid.row(); 

            // Second row of card slots for modifier and action cards
            for (int col = 0; col < 7; col++) {
                if (col < actionAndModifiers.size()) {
                    Card currentCard = actionAndModifiers.get(col);
                    // Card image added
                    Image cardImage = new Image(currentCard.getImage());
                    grid.add(cardImage).width(60).height(88).pad(3);
                } else {
                    Image cardSlot = new Image(emptyCardSlot);
                    grid.add(cardSlot).width(60).height(88).pad(3);
                }
            }

            // if the player is busted, shpw the busted message
            if(player.isBusted()) {
                bustedPanels[i].setVisible(true);
            } else {
                bustedPanels[i].setVisible(false);
            }

            // if the player is frozen, show the frozen message
            if(player.isFrozen()) {
                frozenPanels[i].setVisible(true);
            } else {
                frozenPanels[i].setVisible(false);
            }

            if(player.isStayed()) {
                stayedPanels[i].setVisible(true);
            } else {
                stayedPanels[i].setVisible(false);
            }
        }
    }

    // Building player areas depending on total player count
    private void buildDynamicLayout() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);

        mainTable.padLeft(30).padRight(30).padTop(10).padBottom(10);

        if (playerCount == 4) {
            mainTable.add(playerAreas[0]).expand().center().padLeft(40);
            mainTable.add(playerAreas[1]).expand().center().padRight(40);
            mainTable.row();
            mainTable.add(controlPanel).colspan(2).height(130).center();
            mainTable.row();
            mainTable.add(playerAreas[2]).expand().center().padLeft(40);
            mainTable.add(playerAreas[3]).expand().center().padRight(40);
        } else if (playerCount == 3) {
            mainTable.add(playerAreas[0]).expand().center();
            mainTable.add(controlPanel).expand().center();
            mainTable.row();
            mainTable.add(playerAreas[1]).expand().center();
            mainTable.add(playerAreas[2]).expand().center();
        } else if (playerCount == 5) {
            mainTable.add(playerAreas[0]).expand().pad(5);
            mainTable.add(playerAreas[1]).expand().pad(5);
            mainTable.add(playerAreas[2]).expand().pad(5);
            mainTable.row();
            mainTable.add(playerAreas[3]).expand().pad(5);
            mainTable.add(playerAreas[4]).expand().pad(5);
            mainTable.add(controlPanel).expand().pad(5);
        }
        stage.addActor(mainTable);
    }

    // Updating turn status (shows current player's turn or special action senetnces according to drawn card)
    public void updateTurnDisplay() {
        if(turnLabel != null) {
            int currentIndex = gameManager.getCurrentPlayerIndex();
            Player currentPlayer = gameManager.getPlayers().get(currentIndex);
            
            String status = "'s Turn";

            if (flipThreeRemainings > 0 && flipThreeTargetPlayer != null) {
                status = " (FLIP THREE for " + flipThreeTargetPlayer.getName() + ": " + flipThreeRemainings + " left)";
            } else if(currentPlayer.isSecondChanceUsed()) {
                status = " Saved by Second Chance!";
            } else if (activeActioncard != null) {
                if (ActionCard.FREEZE.equals(activeActioncard.getActionType())) {
                    status = " (Choose player to FREEZE!)";
                } else if (ActionCard.FLIP_THREE.equals(activeActioncard.getActionType())) {
                    status = " (Choose player to FLIP THREE!)";
                }
            }

            turnLabel.setText(currentPlayer.getName() + status);
        }
    }

    // Updating player score text
    public void updateScoresDisplay() {
        for (int i = 0; i < playerCount; i++) {
            if (playerScoreLabels[i] != null) {
                Player player = gameManager.getPlayers().get(i);
                String status = "";

                playerScoreLabels[i].setText(player.getName() + status + "\nScore: " + player.getTotalScore());
                
                
            }
        }
    }

    // Checks if the current round is completed and shows the round end messages
    public void checkRoundStatus() {
        if (gameManager.isRoundOver()) {
            
            String endMessage = "ROUND OVER!";
            boolean foundWinner = false;

            // Check if any active player flipped 7 unique number cards
            for (int i = 0; i < gameManager.getPlayers().size() && !foundWinner; i++) {
                Player p = gameManager.getPlayers().get(i);
                
                if (!p.isBusted()) {
                    if (gameManager.hasSevenUniqueNumbers(p)) {
                        int roundScore = CardProcessor.calculateHandScore(p.getActiveHand());
                        int totalScoreAfterRound = p.getTotalScore() + roundScore + 15; // adds 15 bonus points to total score
                        
                        endMessage = p.getName() + " FLIPPED 7 CARDS!\n(+15 Bonus Points!)\nRound Score: " + roundScore + " | Total Score: " + totalScoreAfterRound;
                        foundWinner = true; 
                    }
                }
            }

            // If no one flipped 7 unique number cards, find the highest hand score
            if (!foundWinner) {
                Player roundWinner = null;
                int maxRoundScore = -1;

                for (Player player : gameManager.getPlayers()) {
                    if (!player.isBusted()) {
                        int score = CardProcessor.calculateHandScore(player.getActiveHand());
                        if (score > maxRoundScore) {
                            maxRoundScore = score;
                            roundWinner = player;
                        }
                    }
                }

                if (roundWinner != null) {
                    int totalScoreAfterRound = roundWinner.getTotalScore() + maxRoundScore;
                    endMessage = roundWinner.getName() + " Won The Round!\nRound Score: " + maxRoundScore + " | Total Score: " + totalScoreAfterRound;
                } else {
                    endMessage = "ROUND OVER!\nEveryone Busted!";
                }
            }


            // check if the game is over
            if (gameManager.isGameOver()) {
                Player winner = gameManager.getPlayers().get(0);
                for (Player player : gameManager.getPlayers()) {
                    if (player.getTotalScore() > winner.getTotalScore()) {
                        winner = player;
                    }
                }
                roundEndLabel.setText("GAME OVER!\nWinner: " + winner.getName() + "\nFinal Score: " + winner.getTotalScore());
                mainMenuButton.setVisible(true);
                roundEndPanel.add(mainMenuButton);
                nextRoundButton.setVisible(false);
            } else {
                roundEndLabel.setText(endMessage);
                nextRoundButton.setVisible(true);
            }

            float roundEndPanelWidth = 900f;  
            float roundEndPanelHeight = 450f; 
            
            roundEndPanel.setSize(roundEndPanelWidth, roundEndPanelHeight);
            // Place the round end message panel at the center
            roundEndPanel.setPosition((Gdx.graphics.getWidth() - roundEndPanelWidth) / 2f,(Gdx.graphics.getHeight() - roundEndPanelHeight) / 2f);

            roundEndPanel.setVisible(true);
            roundEndPanel.toFront();


            updateScoresDisplay();
            updatePlayerCardsAndBustedStates();

        }
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(238f / 255f, 231f / 255f, 255f / 255f, 1f); // background color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Updating metrics to solve multidisplay pixel conflicts
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        // Update and draw the UI buttons
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Update stage sizes when the window size changes
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        font.dispose();
        messageFont.dispose();
        deckPicture.dispose();
        emptyCardSlot.dispose();
        bustedBackground.dispose();
        frozenBackground.dispose();
        stayedBackground.dispose();
        blueButtonTex.dispose();
        greenButtonTex.dispose();
        purpleButtonTex.dispose();
    }
}