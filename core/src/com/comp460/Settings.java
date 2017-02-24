package com.comp460;

import com.badlogic.gdx.Gdx;

/**
 * Created by matthewhammond on 1/12/17.
 */
public class Settings {

    public static int WINDOW_WIDTH = 1200;
    public static int WINDOW_HEIGHT = 720;

    public static final int INTERNAL_WIDTH = 400;
    public static final int INTERNAL_HEIGHT = 240;

    public static void load() {
        Gdx.graphics.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);
    }
}
