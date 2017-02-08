package com.comp460.launcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.comp460.Settings;

/**
 * Created by matthewhammond on 2/7/17.
 */
public class SplashScreen extends ScreenAdapter {


    enum AnimState {WORDS_FLY_IN, SWITCHED_FLY_IN, S_FALL, DONE};

    private final float TITLE_BLY = 70;
    private Game game;
    private Batch batch;
    private OrthographicCamera camera;

    private Color wordColor;
    private Color wordOutlineColor;
    private Color swordColor;

    private static TextureRegion TEXTURE_S = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/S.png")));
    private TextureRegion TEXTURE_S_BG = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/S-outline.png")));

    private static TextureRegion TEXTURE_WITCHED = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/Witched.png")));
    private static TextureRegion TEXTURE_WITCHED_BG = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/Witched-outline.png")));

    private static TextureRegion TEXTURE_WORDS = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/words.png")));
    private static TextureRegion TEXTURE_WORDS_BG = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/words-outline.png")));

    private static TextureRegion TEXTURE_SWORD_R = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/sword1.png")));
    private static TextureRegion TEXTURE_SWORD_L = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/sword2.png")));

    private static TextureRegion BG = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/splash-bg.png")));

    private static BitmapFont pressStartFont = new BitmapFont(Gdx.files.local("common/fonts/KenPixel.fnt"));


    private Vector3 topPos, topGoal, topVel, topAcc;
    private Vector3 botPos, botGoal, botVel, botAcc;
    private Vector3 sOffset, sVel, g;

    private AnimState curAnimState;
    private float timer = 0f;

    public SplashScreen(Game game) {

        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT);
        camera.update();

        topPos = new Vector3(-280f, TITLE_BLY, 0f);
        topGoal = new Vector3(60f, TITLE_BLY, 0f);

        botPos = new Vector3(400f, TITLE_BLY, 0f);
        botGoal = new Vector3(60f, TITLE_BLY, 0f);

        sOffset = new Vector3(-10f, TEXTURE_S.getRegionHeight()/2f, 0f);
        sVel = new Vector3(0f, 0f, 0f);
        g = new Vector3(0f, -0.9f, 0f);

        wordColor = Color.WHITE;
        wordOutlineColor = Color.BLACK;
        swordColor = new Color(0f, 0f, 0f, 1f);

        curAnimState = AnimState.WORDS_FLY_IN;

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.local("common/fonts/kenpixel.ttf"));
        gen.generateFont(new FreeTypeFontGenerator.FreeTypeFontParameter());
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.setProjectionMatrix(camera.combined);

        batch.setColor(Color.WHITE);
        batch.begin();
        batch.draw(BG, 0f, 0f);
        batch.end();

        batch.setColor(swordColor);
        batch.begin();
        batch.draw(TEXTURE_SWORD_L, botPos.x - 20, botPos.y - TEXTURE_SWORD_L.getRegionHeight());
        batch.draw(TEXTURE_SWORD_R, topPos.x - 50, topPos.y + TEXTURE_WITCHED.getRegionHeight());
        batch.end();

        batch.setColor(wordOutlineColor);
        batch.begin();
        batch.draw(TEXTURE_S_BG, topPos.x + sOffset.x, topPos.y + sOffset.y);
        batch.draw(TEXTURE_WITCHED_BG, topPos.x, topPos.y);
        batch.draw(TEXTURE_WORDS_BG, botPos.x, botPos.y);
        batch.end();

        batch.setColor(wordColor);
        batch.begin();
        batch.draw(TEXTURE_S, topPos.x + sOffset.x, topPos.y + sOffset.y);
        batch.draw(TEXTURE_WITCHED, topPos.x, topPos.y);
        batch.draw(TEXTURE_WORDS, botPos.x, botPos.y);
        batch.end();

        timer += 0.1f;
        if (timer > 1f)
            timer = 1f;
        switch (curAnimState) {
            case WORDS_FLY_IN:

                botPos.slerp(botGoal, .15f);
                topPos.slerp(topGoal, .15f);
                swordColor.lerp(Color.WHITE, timer);

                if (botPos.epsilonEquals(botGoal,2f)) {
                    curAnimState = AnimState.SWITCHED_FLY_IN;
                }
                break;
            case SWITCHED_FLY_IN:

                topPos.slerp(topGoal, .15f);
                botPos.slerp(botGoal, .15f);

                if (topPos.epsilonEquals(topGoal, 0.01f) && botPos.epsilonEquals(botGoal, 0.01f)) {
                    curAnimState = AnimState.S_FALL;
                }
                break;
            case S_FALL:

                sVel.add(g);
                sOffset.add(sVel);

                if (sOffset.y < 0) {
                    float damp = 0.8f;
                    sOffset.y = 0;
                    sVel.y = -sVel.y*damp;
                    sVel.x = 0.3f;
                }
                if (sOffset.x > 0) {
                    sOffset.x = 0f;
                    sVel.x = 0f;
                }
                if (sOffset.epsilonEquals(0.0f,0.0f,0.0f, 0.01f)) {
                    curAnimState = AnimState.DONE;
                }
                break;
            case DONE:
                batch.setColor(Color.BLACK);
                batch.begin();
                pressStartFont.draw(batch, "Press Any Key to Begin", botPos.x, botPos.y);
                batch.end();
        }
    }
}
