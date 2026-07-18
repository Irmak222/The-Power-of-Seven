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

public class HomeScreen  implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    public HomeScreen(Main game) {
        this.game = game;
    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);

    skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

    Table table = new Table();
    table.setFillParent(true);

    
    table.top().padTop(80);

    stage.addActor(table);

    LabelStyle titleStyle = new LabelStyle();
    titleStyle.font = skin.getFont("window");
    titleStyle.fontColor = Color.BLACK;

    Label title = new Label("Welcome, User!", titleStyle);

    TextButton newGame = new TextButton("New Game", skin);
    TextButton rules = new TextButton("Rules", skin);
    TextButton profile = new TextButton("Profile", skin);
    TextButton exit = new TextButton("Exit", skin);

    table.add(title).colspan(2).padBottom(60);
    table.row();

    table.add(newGame)
            .width(220)
            .height(70)
            .pad(20);

    table.add(rules)
            .width(220)
            .height(70)
            .pad(20);

    table.row();

    table.add(profile)
            .width(220)
            .height(70)
            .pad(20);

    table.add(exit)
            .width(220)
            .height(70)
            .pad(20);

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.93f, 0.90f, 0.98f, 1);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();
    }

}
