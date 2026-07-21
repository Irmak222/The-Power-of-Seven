package com.sevenup.cardgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sevenup.cardgame.Main;

public class RulesScreen implements Screen{
    private final Main game; 
    private Stage stage;
    private Skin skin;
    private TextButton backBtn;
    private Texture texture;
    private SpriteBatch batch;
    private String username;

    private Table table;
    public RulesScreen(Main game, String username) {
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

        texture = new Texture(Gdx.files.internal("RulesScreen.png"));

        backBtn = new TextButton("Back", skin);

        table.add(backBtn)
            .width(stage.getWidth() * 0.2f)
            .height(stage.getHeight() * 0.2f)
            .expand()
            .bottom()
            .right()
            .pad(40);

        backBtn.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent e,Actor a){
                game.setScreen(new HomeScreen(game,username));
            }
        });
    }

    public void render(float delta) {
        ScreenUtils.clear(0.93f, 0.90f, 0.98f, 1);
        stage.getBatch().begin();
        stage.getBatch().draw(texture, 0, 0, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.getBatch().end();;
        stage.act(delta);
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        table.clearChildren();

        table.add(backBtn)
            .width(stage.getWidth() * 0.2f)
            .height(stage.getHeight() * 0.2f)
            .expand()
            .bottom()
            .right()
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
        texture.dispose();
    }
}
