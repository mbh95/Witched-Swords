package com.comp460.common;

import com.badlogic.gdx.ScreenAdapter;
import com.comp460.GameDriver;

/**
 * Created by matthewhammond on 1/27/17.
 */
public class GameScreen extends ScreenAdapter {

    private GameDriver driver;

    public GameScreen(GameDriver driver) {
        this.driver = driver;
    }

    public <T extends GameScreen> void endAndSwitchTo(T nextScreen)  {
        driver.popScreen();
    }

    public <T extends GameScreen> void pushTo(T childScreen) {
        driver.pushScreen(childScreen);
    }
}
