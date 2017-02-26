package com.comp460.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.comp460.MainGame;
import com.comp460.Settings;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = Settings.WINDOW_TITLE;
        config.width = Settings.WINDOW_WIDTH;
        config.height = Settings.WINDOW_HEIGHT;
        new LwjglApplication(new MainGame(), config);
    }
}
