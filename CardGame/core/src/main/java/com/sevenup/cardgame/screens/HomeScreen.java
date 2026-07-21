
package com.sevenup.cardgame.screens;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.Screen;
import com.sevenup.cardgame.Main;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.graphics.*;

public class HomeScreen  implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private String username;
    public HomeScreen(Main game, String username) {
        this.game = game;
        this.username = username;
    }
    public HomeScreen(Main game) {
        this(game, "User");
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

    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ARIAL.TTF"));

    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    parameter.size = 48;
    parameter.color = Color.BLACK;

    BitmapFont titleFont = generator.generateFont(parameter);
    generator.dispose();

    LabelStyle titleStyle = new LabelStyle(titleFont, Color.BLACK);
    Label title = new Label("Welcome, " + username + "!", titleStyle);

    TextButton newGame = new TextButton("New Game", skin);
    newGame.addListener(new ClickListener() {
    @Override
    public void clicked(InputEvent event, float x, float y) {
        game.setScreen(new PlayerCountScreen(game, username));
    }
});
    TextButton rules = new TextButton("Rules", skin);
    rules.addListener(new ClickListener() {
    @Override
    public void clicked(InputEvent event, float x, float y) {
        game.setScreen(new RulesScreen(game,username));
    }
});
    TextButton profile = new TextButton("Profile", skin);

    TextButton exit = new TextButton("Exit", skin);
    exit.addListener(new ClickListener() {
    @Override
    public void clicked(InputEvent event, float x, float y) {
        game.setScreen(new ClosingScreen(game,username));
    }
});

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
