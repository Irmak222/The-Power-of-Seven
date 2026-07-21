package com.sevenup.cardgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sevenup.cardgame.Main;
import com.sevenup.cardgame.Player;



public class PlayerSetUpScreen implements Screen {

    private final Main game;

    private Stage stage;
    private Skin skin;

    private int playerCount;
    private Table table;
    private Label title;

    private float boxWidth;
    private float boxHeight;

    private Player[] players;
    private String ownerUsername;

    public PlayerSetUpScreen(Main game, int playerCount, String ownerUsername) {
        this.game = game;
        this.playerCount = playerCount;
        this.players = new Player[playerCount];
        this.ownerUsername = ownerUsername;
        players = new Player[playerCount];
        players[0] = new Player(ownerUsername, false);
    }

    @Override
    public void show() {

        table = new Table();
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        table.setFillParent(true);
        stage.addActor(table);

        LabelStyle titleStyle = new LabelStyle();
        titleStyle.font = skin.getFont("window");
        titleStyle.fontColor = Color.BLACK;

        title = new Label("Select Players", titleStyle);

        boxWidth = stage.getWidth() * 0.4f;
        boxHeight = stage.getHeight() * 0.15f;

        table.add(title).colspan(2).padBottom(60);
        table.row();

        for (int i = 0; i < playerCount; i++) {

            table.add(createPlayerBox(i + 1)).pad(10);

            if (i % 2 == 1)
                table.row();
        }
    }

    private Table createPlayerBox(int playerNo) {

    final int index = playerNo - 1;

    Table newTable = new Table();

    Drawable purpleBackground = skin.getDrawable("white");
    newTable.setBackground(
            ((TextureRegionDrawable) purpleBackground)
                    .tint(new Color(0.6f, 0.4f, 0.8f, 1f)));

    
    if (index == 0) {

        newTable.add(new Label("User 1", skin)).colspan(2);
        newTable.row();

        newTable.add(new Label(ownerUsername, skin))
                .colspan(2)
                .pad(15);

        return newTable;
    }
    
    if (players[index] != null) {

        newTable.add(new Label("User " + playerNo, skin)).colspan(2);
        newTable.row();

        newTable.add(new Label(players[index].getName(), skin))
                .colspan(2)
                .pad(15);

        return newTable;
    }

    Label userLabel = new Label("User " + playerNo, skin);

    newTable.add(userLabel)
            .pad(boxWidth * 0.04f)
            .width(boxWidth * 0.30f)
            .height(boxHeight * 0.50f);

    TextButton loginButton = new TextButton("Login", skin);
    TextButton guestButton = new TextButton("Guest", skin);

    loginButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {

            game.setScreen(
                    new PlayerLoginScreen(
                            game,
                            PlayerSetUpScreen.this,
                            index
                    )
            );
        }
    });

    guestButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {

            setPlayer(index, new Player("Guest", true));

        }
    });

    newTable.add(loginButton)
            .pad(boxWidth * 0.04f)
            .width(boxWidth * 0.20f)
            .height(boxHeight * 0.50f);

    newTable.add(guestButton)
            .pad(boxWidth * 0.04f)
            .width(boxWidth * 0.20f)
            .height(boxHeight * 0.50f);

    return newTable;
}
    public void setPlayer(int index, Player player) {

        players[index] = player;

        table.clearChildren();

        table.add(title).colspan(2).padBottom(60);
        table.row();

        for (int i = 0; i < playerCount; i++) {

            table.add(createPlayerBox(i + 1)).pad(10);

            if (i % 2 == 1)
                table.row();
        }
    }

    public boolean isUsernameAlreadyUsed(String username) {

        for (Player player : players) {

            if (player != null &&
                !player.isGuest() &&
                player.getName().equals(username)) {

                return true;
            }
        }

        return false;
    }

    public Player[] getPlayers() {
        return players;
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.93f, 0.90f, 0.98f, 1);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);

        table.clearChildren();

        title.setFontScale(width / 640f);

        table.add(title).colspan(2).padBottom(60);
        table.row();

        boxWidth = width * 0.4f;
        boxHeight = height * 0.15f;

        for (int i = 0; i < playerCount; i++) {

            table.add(createPlayerBox(i + 1))
                    .width(width * 0.4f)
                    .height(height * 0.15f)
                    .pad(width * 0.02f);

            if (i % 2 == 1)
                table.row();
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}