package com.sevenup.cardgame.screens;

import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sevenup.cardgame.Main;

public class ClosingScreen implements Screen{
    private final Main game; 
    private Stage stage;
    private Skin skin;
    private TextButton backBtn;
    private TextButton quitBtn;
    private String username;

    private Table table;
    public ClosingScreen(Main game, String username) {
        this.game = game;
        this.username = username;
    }

    public void show() {
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        backBtn = new TextButton("No", skin);
        quitBtn = new TextButton("Yes", skin);

        table.add(quitBtn)
            .width(stage.getWidth() * 0.2f)
            .height(stage.getHeight() * 0.2f)
            .pad(40)
            .row();

        table.add(backBtn)
            .width(stage.getWidth() * 0.2f)
            .height(stage.getHeight() * 0.2f)
            .pad(40);

        quitBtn.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent e,Actor a){
                game.setScreen(null);
            }
        });

        backBtn.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent e,Actor a){
                game.setScreen(new HomeScreen(game,username));
            }
        });

        
    }

    public void render(float delta) {
        ScreenUtils.clear(0.93f, 0.90f, 0.98f, 1);
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        table.clearChildren();

        table.add(quitBtn)
            .width(stage.getWidth() * 0.2f)
            .height(stage.getHeight() * 0.2f)
            .pad(40)
            .row();

        table.add(backBtn)
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
