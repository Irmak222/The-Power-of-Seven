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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sevenup.cardgame.*;

public class LoginScreen implements Screen {
    private final Main game;

    private SpriteBatch batch;
    private Texture background;
    private OrthographicCamera camera;

    private Stage stage;
    private BitmapFont font;
    private BitmapFont linkFont;
    
    private Texture inputTex;
    private Texture buttonTex;
    private Texture cursorTex;
    
    public LoginScreen(Main game){
        this.game = game;
    }

    @Override
    public void show() {
        // Initialize rendering elements
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("LoginScreen.png"));
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

        // Link font
        FreeTypeFontParameter parameterLink = new FreeTypeFontParameter();
        parameterLink.size = 32;
        parameterLink.color = Color.BLUE; // blue link
        parameterLink.minFilter = Texture.TextureFilter.Linear;
        parameterLink.magFilter = Texture.TextureFilter.Linear;
        linkFont = generator.generateFont(parameterLink);

        generator.dispose();

        // Creating input fields using Pixmap
        int fieldWidth = 300;
        int fieldHeight = 54;
        Pixmap pixmapField = new Pixmap(fieldWidth, fieldHeight, Pixmap.Format.RGBA8888);
        pixmapField.setColor(new Color(254f / 255f, 244f / 255f, 226f / 255f, 1f));
        pixmapField.fillRectangle(0, 0, fieldWidth, fieldHeight);
        pixmapField.setColor(new Color(254f / 255f, 197f / 255f, 107f / 255f, 1f));
        pixmapField.drawRectangle(0, 0, fieldWidth, fieldHeight);
        pixmapField.drawRectangle(1, 1, fieldWidth - 2, fieldHeight - 2);
        inputTex = new Texture(pixmapField);
        pixmapField.dispose();

        // Creating buttons using Pixmap
        int buttonWidth = 280;
        int buttonHeight = 76;
        Pixmap pixmapButton = new Pixmap(buttonWidth, buttonHeight, Pixmap.Format.RGBA8888);
        pixmapButton.setColor(new Color(254f / 255f, 244f / 255f, 226f / 255f, 1f));
        pixmapButton.fillRectangle(0, 0, buttonWidth, buttonHeight);
        pixmapButton.setColor(new Color(254f / 255f, 197f / 255f, 107f / 255f, 1f));
        pixmapButton.drawRectangle(0, 0, buttonWidth, buttonHeight);
        pixmapButton.drawRectangle(1, 1, buttonWidth - 2, buttonHeight - 2);
        buttonTex = new Texture(pixmapButton);
        pixmapButton.dispose();

        // Creating text field for cursor
        Pixmap pixmapCursor = new Pixmap(2, 28, Pixmap.Format.RGBA8888);
        pixmapCursor.setColor(Color.BLACK);
        pixmapCursor.fill();
        cursorTex = new Texture(pixmapCursor);
        pixmapCursor.dispose();

        // Initializing label, text field and button styles
        LabelStyle labelStyle = new LabelStyle(font, Color.BLACK);
        LabelStyle errorLabelStyle = new LabelStyle(font, Color.RED);
        LabelStyle linkStyle = new LabelStyle(linkFont, Color.BLUE);

        TextFieldStyle fieldStyle = new TextFieldStyle();
        fieldStyle.background = new TextureRegionDrawable(inputTex);
        fieldStyle.font = font;
        fieldStyle.fontColor = Color.BLACK;
        fieldStyle.cursor = new TextureRegionDrawable(cursorTex);
        fieldStyle.background.setLeftWidth(15);; // Padding inside the text fields so text does not stick to the left edge

        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(buttonTex);
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;

        // Labels 
        Label usernameLabel = new Label("Username:", labelStyle);
        Label passwordLabel = new Label("Password:", labelStyle);
        Label accountLabel = new Label("Don't have an account? ", labelStyle);
        final Label errorLabel = new Label("", errorLabelStyle);

        // Input fields 
        final TextField usernameField = new TextField("", fieldStyle);
        final TextField passwordField = new TextField("", fieldStyle);
        passwordField.setPasswordMode(true); // Enables password mode hiding characters
        passwordField.setPasswordCharacter('*'); // Turns typed characters into asterisks

        // Login button
        TextButton loginButton = new TextButton("Login", buttonStyle);

        // Register link
        Label registerLink = new Label("Register", linkStyle);

        // When login button is clicked and user succesfully logged in, the home screen is opened
       loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText();

                // Check for empty fields
                if (username.isEmpty() || password.isEmpty()) {
                    errorLabel.setText("Error: Please fill all fields!");
                    return;
                }

                // Check if username and password match perfectly
                try {
                    ProfileManager profileManager = new ProfileManager();

                    if(profileManager.login(username,password)){
                        game.setScreen(new HomeScreen(game, username));
                    }else{
                        errorLabel.setText("Wrong username or password!");
                    }

                } catch (Exception e) {
                    errorLabel.setText("Database error!");
                    e.printStackTrace();
                }
            }
        });

        // When register button is clicked, the screen is changed to Register screen
        registerLink.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new RegisterScreen(game));
            }
        }); 

        // Creating table and adding UI components to the table
        Table table = new Table();
        table.setFillParent(true);
        table.center(); // center the table

        // Adjust this value to shift the whole aligned block up or down
        table.padTop(100);

        // Error message row
        table.add(errorLabel).colspan(2).center().padBottom(15);
        table.row();

        // Username row
        table.add(usernameLabel).width(260).padBottom(20).padLeft(50);
        table.add(usernameField).width(300).height(54).padBottom(20).padLeft(50);
        table.row();

        // Password row
        table.add(passwordLabel).width(260).padBottom(40).padLeft(50);
        table.add(passwordField).width(300).height(54).padBottom(40).padLeft(50);
        table.row();

        // Login button row
        table.add(loginButton).colspan(2).width(320).height(76).center().padBottom(30);
        table.row();

        // "Don't have an account? Register" link row
        Table linkTable = new Table();
        linkTable.add(accountLabel);
        linkTable.add(registerLink);
        table.add(linkTable).colspan(2).height(50).center();

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
        batch.dispose();
        background.dispose();
        stage.dispose();
        font.dispose();
        linkFont.dispose();
        inputTex.dispose();
        buttonTex.dispose();
        cursorTex.dispose();
    }
}
