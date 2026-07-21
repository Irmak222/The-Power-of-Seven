package com.sevenup.cardgame.screens;

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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sevenup.cardgame.Main;
import com.sevenup.cardgame.Player;
import com.sevenup.cardgame.ProfileManager;

public class PlayerLoginScreen implements Screen {

    private final Main game;
    private final PlayerSetUpScreen setupScreen;
    private final int playerIndex;

    private SpriteBatch batch;
    private Texture background;
    private OrthographicCamera camera;

    private Stage stage;
    private BitmapFont font;

    private Texture inputTex;
    private Texture buttonTex;
    private Texture cursorTex;

    public PlayerLoginScreen(Main game,
                             PlayerSetUpScreen setupScreen,
                             int playerIndex) {

        this.game = game;
        this.setupScreen = setupScreen;
        this.playerIndex = playerIndex;
    }

    @Override
    public void show() {

        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("LoginScreen.png"));
        camera = new OrthographicCamera();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("ARIAL.TTF"));

        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = Color.BLACK;
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;

        font = generator.generateFont(parameter);
        generator.dispose();

        Pixmap field = new Pixmap(300, 54, Pixmap.Format.RGBA8888);
        field.setColor(new Color(254f / 255f, 244f / 255f, 226f / 255f, 1f));
        field.fill();
        field.setColor(new Color(254f / 255f, 197f / 255f, 107f / 255f, 1f));
        field.drawRectangle(0, 0, 300, 54);
        inputTex = new Texture(field);
        field.dispose();

        Pixmap button = new Pixmap(280, 76, Pixmap.Format.RGBA8888);
        button.setColor(new Color(254f / 255f, 244f / 255f, 226f / 255f, 1f));
        button.fill();
        button.setColor(new Color(254f / 255f, 197f / 255f, 107f / 255f, 1f));
        button.drawRectangle(0, 0, 280, 76);
        buttonTex = new Texture(button);
        button.dispose();

        Pixmap cursor = new Pixmap(2, 28, Pixmap.Format.RGBA8888);
        cursor.setColor(Color.BLACK);
        cursor.fill();
        cursorTex = new Texture(cursor);
        cursor.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        Label.LabelStyle errorStyle = new Label.LabelStyle(font, Color.RED);

        TextField.TextFieldStyle fieldStyle = new TextField.TextFieldStyle();
        fieldStyle.background = new TextureRegionDrawable(inputTex);
        fieldStyle.font = font;
        fieldStyle.fontColor = Color.BLACK;
        fieldStyle.cursor = new TextureRegionDrawable(cursorTex);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(buttonTex);
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;

        Label usernameLabel = new Label("Username:", labelStyle);
        Label passwordLabel = new Label("Password:", labelStyle);
        Label errorLabel = new Label("", errorStyle);

        TextField usernameField = new TextField("", fieldStyle);
        TextField passwordField = new TextField("", fieldStyle);

        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        TextButton loginButton = new TextButton("Login", buttonStyle);
        TextButton backButton = new TextButton("Back", buttonStyle);

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                String username = usernameField.getText().trim();
                String password = passwordField.getText();

                if (username.isEmpty() || password.isEmpty()) {
                    errorLabel.setText("Please fill all fields!");
                    return;
                }

                try {

                    ProfileManager manager = new ProfileManager();

                    if (manager.login(username, password)) {

                        if (setupScreen.isUsernameAlreadyUsed(username)) {
                            errorLabel.setText("This account is already in the game!");
                            return;
                        }

                        Player player = new Player(username, false);

                        setupScreen.setPlayer(playerIndex, player);

                        game.setScreen(setupScreen);

                    } else {

                        errorLabel.setText("Wrong username or password!");

                    }

                } catch (Exception e) {

                    errorLabel.setText("Database error!");
                    e.printStackTrace();

                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(setupScreen);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.padTop(100);

        table.add(errorLabel).colspan(2).padBottom(15);
        table.row();

        table.add(usernameLabel).width(220).padBottom(20);
        table.add(usernameField).width(300).height(54).padBottom(20);
        table.row();

        table.add(passwordLabel).width(220).padBottom(40);
        table.add(passwordField).width(300).height(54).padBottom(40);
        table.row();

        table.add(loginButton).width(220).height(76).padRight(20);
        table.add(backButton).width(220).height(76);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        camera.setToOrtho(false, sw, sh);
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0, sw, sh);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        stage.dispose();
        font.dispose();
        inputTex.dispose();
        buttonTex.dispose();
        cursorTex.dispose();
    }
}