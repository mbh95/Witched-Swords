package com.comp460.launcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;
import com.comp460.FontManager;
import com.comp460.Settings;


/**
 * Created by matthewhammond on 2/7/17.
 */
public class SplashScreen extends ScreenAdapter {


    enum AnimState {WORDS_FLY_IN, SWITCHED_FLY_IN, S_FALL, DONE, POST, TRANSITION};

    private static final float TITLE_BLY = 70;
    private Game game;
    private Batch batch;
    private OrthographicCamera camera;

    protected static Color wordColor;
    protected static Color wordOutlineColor;
    protected static Color swordColor;

    private static TextureRegion TEXTURE_S = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/S.png")));
    private static TextureRegion TEXTURE_S_BG = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/S-outline.png")));

    private static TextureRegion TEXTURE_WITCHED = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/Witched.png")));
    private static TextureRegion TEXTURE_WITCHED_BG = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/Witched-outline.png")));

    private static TextureRegion TEXTURE_WORDS = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/words.png")));
    private static TextureRegion TEXTURE_WORDS_BG = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/words-outline.png")));

    protected static TextureRegion TEXTURE_SWORD_R = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/sword1.png")));
    protected static TextureRegion TEXTURE_SWORD_L = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/sword2.png")));

    protected static TextureRegion TEXTURE_TITLE = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/title.png")));
    protected static TextureRegion TEXTURE_TITLE_BG = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/title-outline.png")));

    protected static TextureRegion BG = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/splash-bg.png")));

    private static BitmapFont pressStartFont;
    private GlyphLayout pressStartLayout;

    protected static final Vector3 titleGoal = new Vector3(60f, TITLE_BLY, 0f);
    private static Vector3 titleScaleGoal = new Vector3(MainMenu.titleScale.x * 100, MainMenu.titleScale.y * 100, 0f);

    private Vector3 topPos, topGoal, topVel, topAcc;
    private Vector3 botPos, botGoal, botVel, botAcc;
    private Vector3 titlePos;
    private Vector3 sOffset, sVel, g;

    private AnimState curAnimState;
    private float timer = 0f;
    private float vel = 0;
    private float acc = 1f;

    private Vector3 titleScale = new Vector3(1f, 1f, 0f);
    private Vector3 titleScaleProxy = new Vector3(100f, 100f, 0f);

    public SplashScreen(Game game) {

        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT);
        camera.update();

        topPos = new Vector3(-280f, TITLE_BLY, 0f);
        topGoal = titleGoal;

        botPos = new Vector3(400f, TITLE_BLY, 0f);
        botGoal = titleGoal;

        titlePos = new Vector3(titleGoal);

        sOffset = new Vector3(-10f, TEXTURE_S.getRegionHeight() / 2f, 0f);
        sVel = new Vector3(0f, 0f, 0f);
        g = new Vector3(0f, -0.9f, 0f);

        wordColor = Color.WHITE;
        wordOutlineColor = Color.BLACK;
        swordColor = new Color(0f, 0f, 0f, 1f);

        curAnimState = AnimState.WORDS_FLY_IN;

        pressStartFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 8, Color.BLACK);
        pressStartLayout = new GlyphLayout(pressStartFont, "<Press Any Key to Continue>");
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

        if (curAnimState != AnimState.POST) {
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
        } else {
            batch.setColor(wordOutlineColor);
            batch.begin();
            batch.draw(TEXTURE_TITLE_BG, titlePos.x, titlePos.y, TEXTURE_TITLE_BG.getRegionWidth() * titleScale.x, TEXTURE_TITLE_BG.getRegionHeight() * titleScale.y);
            batch.end();

            batch.setColor(wordColor);
            batch.begin();
            batch.draw(TEXTURE_TITLE, titlePos.x, titlePos.y, TEXTURE_TITLE.getRegionWidth() * titleScale.x, TEXTURE_TITLE.getRegionHeight() * titleScale.y);
            batch.end();
        }


        switch (curAnimState) {
            case WORDS_FLY_IN:

                timer += 0.1f;
                if (timer > 1f)
                    timer = 1f;
                botPos.slerp(botGoal, .15f);
                topPos.slerp(topGoal, .15f);
                swordColor.lerp(Color.WHITE, timer);

                if (botPos.epsilonEquals(botGoal, 2f)) {
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
                    sVel.y = -sVel.y * damp;
                    sVel.x = 0.3f;
                }
                if (sOffset.x > 0) {
                    sOffset.x = 0f;
                    sVel.x = 0f;
                }
                if (sOffset.epsilonEquals(0.0f, 0.0f, 0.0f, 0.01f)) {
                    curAnimState = AnimState.DONE;
                    timer = 0;
                }
                break;
            case DONE:
                if (((int) timer) % 2 == 0) {
                    batch.setColor(Color.BLACK);
                    batch.begin();
                    pressStartFont.draw(batch, "<Press Any Key to Begin>", 400 / 2 - pressStartLayout.width / 2, botPos.y - 30);
                    batch.end();
                }
                timer += 0.03;
                if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                    curAnimState = AnimState.POST;
                    timer = 0;
                }
                break;
            case POST:
                vel += acc;
                topPos.x += vel;
                botPos.x -= vel;

                titlePos.slerp(MainMenu.titlePos, .1f);
                titleScaleProxy.slerp(titleScaleGoal, .1f);
                titleScale.x = titleScaleProxy.x / 100f;
                titleScale.y = titleScaleProxy.y / 100f;


                if (titlePos.epsilonEquals(MainMenu.titlePos, 0.01f)) {
                    curAnimState = AnimState.TRANSITION;
                }

                if (timer > 1f) {
                    timer = 1f;
                }
                timer += 0.01f;
                break;
            case TRANSITION:
                game.setScreen(new MainMenu(game));
                dispose();
                break;
        }
    }
}
