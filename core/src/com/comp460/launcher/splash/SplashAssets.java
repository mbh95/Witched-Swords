package com.comp460.launcher.splash;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class SplashAssets {

    private static SplashAssets singleton;

    public static SplashAssets getInstance() {
        if (singleton == null) {
            singleton = new SplashAssets();
        }
        return singleton;
    }

    private SplashAssets() {
        load();
        singleton = this;
    }

    public TextureRegion TITLE_S;
    public TextureRegion TITLE_S_BG;
    public TextureRegion TITLE_WITCHED;
    public TextureRegion TITLE_WITCHED_BG;
    public TextureRegion TITLE_WORDS;
    public TextureRegion TITLE_WORDS_BG;

    public TextureRegion TITLE;
    public TextureRegion TITLE_BG;
    
    public TextureRegion SWORD;

    public TextureRegion BG;

    private void load() {
        TITLE_S = new TextureRegion(new Texture(Gdx.files.internal("launcher/splash/sprites/S.png")));
        TITLE_S_BG = new TextureRegion(new Texture(Gdx.files.internal("launcher/splash/sprites/S-outline.png")));
        TITLE_WITCHED = new TextureRegion(new Texture(Gdx.files.internal("launcher/splash/sprites/Witched.png")));
        TITLE_WITCHED_BG = new TextureRegion(new Texture(Gdx.files.internal("launcher/splash/sprites/Witched-outline.png")));
        TITLE_WORDS = new TextureRegion(new Texture(Gdx.files.internal("launcher/splash/sprites/words.png")));
        TITLE_WORDS_BG = new TextureRegion(new Texture(Gdx.files.internal("launcher/splash/sprites/words-outline.png")));

        SWORD = new TextureRegion(new Texture(Gdx.files.internal("launcher/splash/sprites/sword1.png")));

        TITLE = new TextureRegion(new Texture(Gdx.files.internal("launcher/common/sprites/title.png")));
        TITLE_BG = new TextureRegion(new Texture(Gdx.files.internal("launcher/common/sprites/title-outline.png")));

        BG = new TextureRegion(new Texture(Gdx.files.internal("launcher/common/sprites/bg.png")));

    }
}
