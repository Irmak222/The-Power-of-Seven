package com.sevenup.cardgame.screens;

import com.badlogic.gdx.Screen;
import com.sevenup.cardgame.Main;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.*;

public class PlayerCountScreen implements Screen{
    private final Main game; 
    private Stage stage;
    private Skin skin;
    private TextButton threeBtn;
    private TextButton fourBtn;
    private TextButton fiveBtn;
    private Table table;
    private Label title;

    public PlayerCountScreen(Main game) {
        this.game = game;
    }

    public void show() {
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        LabelStyle titleStyle = new LabelStyle();
        titleStyle.font = skin.getFont("window");
        titleStyle.fontColor = Color.BLACK;

        title = new Label("Select Player Count", titleStyle);

        threeBtn = new TextButton("3", skin);
        fourBtn = new TextButton("4", skin);
        fiveBtn = new TextButton("5", skin);

        table.add(title).colspan(3).padBottom(60);
        table.row();

        table.add(threeBtn)
            .width(stage.getWidth() * 0.2f)
            .height(stage.getHeight() * 0.2f)
            .pad(40);

        table.add(fourBtn)
            .width(stage.getWidth() * 0.2f)
            .height(stage.getHeight() * 0.2f)
            .pad(40);


        table.add(fiveBtn)
            .width(stage.getWidth() * 0.2f)
            .height(stage.getHeight() * 0.2f)
            .pad(40);

        
        }

    public void render(float delta) {
        ScreenUtils.clear(0.93f, 0.90f, 0.98f, 1);
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        table.clearChildren();
        table.add(title).colspan(3).padBottom(60);
        table.row();

        table.add(threeBtn)
            .width(stage.getWidth() * 0.2f)
            .height(stage.getHeight() * 0.2f)
            .pad(40);

        table.add(fourBtn)
            .width(stage.getWidth() * 0.2f)
            .height(stage.getHeight() * 0.2f)
            .pad(40);


        table.add(fiveBtn)
            .width(stage.getWidth() * 0.2f)
            .height(stage.getHeight() * 0.2f)
            .pad(40);

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