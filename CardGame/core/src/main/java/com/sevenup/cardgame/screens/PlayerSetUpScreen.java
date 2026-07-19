package com.sevenup.cardgame.screens;

import com.badlogic.gdx.Screen;
import com.sevenup.cardgame.Main;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.*;

public class PlayerSetUpScreen implements Screen{
    private final Main game; 
    private Stage stage;
    private Skin skin;

    private int playerCount;
    private Table table;
    private Label title;

    private float boxWidth;
    private float boxHeight;

    public PlayerSetUpScreen(Main game,int playerCount) {
        this.game = game;
        this.playerCount = playerCount;
    }

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

        title = new Label("Select Player Count", titleStyle);

        boxWidth = stage.getWidth() * 0.4f;
        boxHeight = stage.getWidth() * 0.15f;

        table.add(title).colspan(3).padBottom(60);
        table.row();
        for(int i = 0;i < playerCount; i ++){
            table.add(createPlayerBox(i + 1)).pad(10);
            if(i % 2 == 1){
                table.row();
            }
        }
        
    }

    private Table createPlayerBox(int playerNo){
        Table newTable = new Table();

        Drawable purpleBackGround = skin.getDrawable("white");
        newTable.setBackground(((TextureRegionDrawable) purpleBackGround).tint(new Color(0.6f,0.4f,0.8f,1f)));
        newTable.add(new Label("User " + playerNo, skin))
            .pad(boxWidth * 0.04f)
            .width(boxWidth * 0.3f)
            .height(boxHeight * 0.5f);
            
        newTable.add(new TextButton("Login", skin))
            .pad(boxWidth * 0.04f)
            .width(boxWidth * 0.2f)
            .height(boxHeight * 0.5f);
            
        newTable.add(new TextButton("Guest", skin))
            .pad(boxWidth * 0.04f)
            .width(boxWidth * 0.2f)
            .height(boxHeight * 0.5f);
        return newTable;
    }

    public void render(float delta) {
        ScreenUtils.clear(0.93f, 0.90f, 0.98f, 1);
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        table.clearChildren();
        title.setFontScale(width/640.0f);
        table.add(title).colspan(3).padBottom(60);
        table.row();

        boxWidth = width * 0.4f;
        boxHeight = height * 0.15f;

        for(int i = 0;i < playerCount; i ++){
            table.add(createPlayerBox(i + 1)).width(width * 0.4f).height(height * 0.15f).pad(width * 0.02f);
            if(i % 2 == 1){
                table.row();
            }
        }
    }

    public void hide() {
        
    }

    public void pause() {

    }

    public void resume(){

    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
