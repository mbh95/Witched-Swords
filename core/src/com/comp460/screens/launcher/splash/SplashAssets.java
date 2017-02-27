package com.comp460.screens.launcher.splash;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.SpriteManager;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class SplashAssets {
    
    public static final TextureRegion TITLE_S = SpriteManager.SPLASH.findRegion("S");
    public static final TextureRegion TITLE_S_BG = SpriteManager.SPLASH.findRegion("S-outline");
    public static final TextureRegion TITLE_WITCHED = SpriteManager.SPLASH.findRegion("Witched");
    public static final TextureRegion TITLE_WITCHED_BG = SpriteManager.SPLASH.findRegion("Witched-outline");
    public static final TextureRegion TITLE_WORDS = SpriteManager.SPLASH.findRegion("words");
    public static final TextureRegion TITLE_WORDS_BG = SpriteManager.SPLASH.findRegion("words-outline");

    public static final TextureRegion SWORD = SpriteManager.SPLASH.findRegion("sword1");

    public static final TextureRegion TITLE = SpriteManager.COMMON.findRegion("title");
    public static final TextureRegion TITLE_BG = SpriteManager.COMMON.findRegion("title-outline");

    public static final TextureRegion BG = SpriteManager.COMMON.findRegion("bg");
}
