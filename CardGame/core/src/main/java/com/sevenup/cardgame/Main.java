package com.sevenup.cardgame;

import com.badlogic.gdx.Game;
import com.sevenup.cardgame.screens.LoginScreen;

public class Main extends Game {
    
    @Override
    public void create() {
        setScreen(new LoginScreen(this));
    }
   
}
