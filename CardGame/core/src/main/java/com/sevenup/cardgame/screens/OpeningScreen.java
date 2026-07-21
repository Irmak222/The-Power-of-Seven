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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sevenup.cardgame.Main;

// First screen the users see when the game is opened
public class OpeningScreen implements Screen {
    private final Main game;

    private SpriteBatch batch;
    private Texture background;
    private OrthographicCamera camera;

    private Stage stage;
    private BitmapFont font;
    private Texture buttonTex;

    public OpeningScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Initialize rendering elements
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("OpeningScreen.png")); // background picture
        camera = new OrthographicCamera();

        // Create the Stage with ScreenViewport to scale UI elements to screen pixels
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Using font file for button text's fonts
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ARIAL.TTF"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 32; // font size 
        parameter.color = Color.BLACK; 
        // Using linear filters to make font smooth
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        font = generator.generateFont(parameter);
        
        generator.dispose();

        // Creating button using Pixmap
        int btnWidth = 300;
        int btnHeight = 80;
        Pixmap pixmap = new Pixmap(btnWidth, btnHeight, Pixmap.Format.RGBA8888);

        // Background color and rectangle for buttons
        pixmap.setColor(new Color(254f / 255f, 244f / 255f, 226f / 255f, 1f));
        pixmap.fillRectangle(0, 0, btnWidth, btnHeight);

        // Border color and border for buttons
        pixmap.setColor(new Color(254f / 255f, 197f / 255f, 107f / 255f, 1f));
        pixmap.drawRectangle(0, 0, btnWidth, btnHeight);
        pixmap.drawRectangle(1, 1, btnWidth - 2, btnHeight - 2);

        // Convert the pixmap to use as a texture
        buttonTex = new Texture(pixmap);
        pixmap.dispose();

        // Initializing button style
        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(buttonTex);
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;

        // Register button
        TextButton registerButton = new TextButton("Register", buttonStyle);
        // Login button
        TextButton loginButton = new TextButton("Login", buttonStyle);

        // When register button is clicked, the screen is changed to Register screen
        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new RegisterScreen(game));
            }
        });

        //  When login button is clicked, the screen is changed to Login screen
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoginScreen(game));
            }
        });

        // Creating table to arrange buttons on the screen
        Table table = new Table();
        table.setFillParent(true); // make table cover the full screen
        table.center(); // center the table

        table.padTop(180); // Adjust this value to shift the whole button layout down or up 

        // Adding buttons to the table with width, height and gap between them
        table.add(registerButton).width(300).height(80).padBottom(30); 
        table.row(); // move to the next row (under the register button)
        table.add(loginButton).width(300).height(80);

        // Adding table on the stage
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Window width and height to adjust for desktop
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Updating metrics to solve multidisplay pixel conflicts
        camera.setToOrtho(false, screenWidth, screenHeight);
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        // Image width and height
        float imageWidth = background.getWidth();
        float imageHeight = background.getHeight();

        // Image and screen ratios
        float imageRatio = imageWidth / imageHeight;
        float screenRatio = screenWidth / screenHeight;

        float drawWidth;
        float drawHeight;

         // Screen is wider than the image 
        if (screenRatio > imageRatio) {
            drawWidth = screenWidth;
            drawHeight = drawWidth / imageRatio;
        } else {
            // Screen is narrower then the image
            drawHeight = screenHeight;
            drawWidth = drawHeight * imageRatio;
        }

        // Center position for the background picture
        float x = (screenWidth - drawWidth) / 2f;
        float y = (screenHeight - drawHeight) / 2f;

        // Drawing background
        batch.begin();
        batch.draw(background, x, y, drawWidth, drawHeight);
        batch.end();

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
        // Stop listening the clicks when this screen is closed.
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // Clean graphics and memory
        batch.dispose();
        background.dispose();
        stage.dispose();
        font.dispose();
        buttonTex.dispose();
    }
}