package com.comp460.launcher.splash;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;
import com.comp460.GameScreen;
import com.comp460.FontManager;
import com.comp460.Settings;
import com.comp460.launcher.main.MainMenuScreen;

/**
 * Created by matthewhammond on 2/7/17.
 */
public class SplashScreen extends GameScreen {

    private enum SplashState {WORDS_FLY_IN, SWITCHED_FLY_IN, S_FALL, DONE, POST, TRANSITION};

    private SplashAssets assets = SplashAssets.getInstance();

    public static class Constants {
        public static final Vector3 TITLE_GOAL_POS = new Vector3(60f, 70f, 0f);

        public static final Vector3 TITLE_MENU_POS = new Vector3(MainMenuScreen.Constants.TITLE_POS.x, MainMenuScreen.Constants.TITLE_POS.y, 0f);
        public static final Vector3 TITLE_MENU_SCALE = new Vector3(MainMenuScreen.Constants.TITLE_SCALE.x, MainMenuScreen.Constants.TITLE_SCALE.y, 0f);

        public static final Vector3 GRAVITY = new Vector3(0f, -0.9f, 0f);;
        public static final float DAMPING = 0.8f;

        public static final Color WORD_COLOR = Color.WHITE;
        public static final Color WORD_OUTLINE_COLOR = Color.BLACK;
    }

    private SplashState curSplashState;

    private Vector3 topPos;
    private Vector3 botPos;

    private Color swordColor;

    private BitmapFont pressStartFont;
    private GlyphLayout pressStartLayout;

    private Vector3 titlePos;
    private Vector3 sOffset, sVel;

    private float timer = 0f;
    private float vel = 0;
    private float acc = 1f;

    private Vector3 titleScalePercent = new Vector3(100f, 100f, 0f);

    public SplashScreen(Game game) {
        super(game, null);

        topPos = new Vector3(-assets.TITLE.getRegionWidth(), Constants.TITLE_GOAL_POS.y, 0f);
        botPos = new Vector3(Settings.INTERNAL_WIDTH, Constants.TITLE_GOAL_POS.y, 0f);

        titlePos = new Vector3(Constants.TITLE_GOAL_POS);

        sOffset = new Vector3(-10f, assets.TITLE_S.getRegionHeight() / 2f, 0f);
        sVel = new Vector3(0f, 0f, 0f);

        swordColor = new Color(0f, 0f, 0f, 1f);

        curSplashState = SplashState.WORDS_FLY_IN;

        pressStartFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 8, Color.BLACK);
        pressStartLayout = new GlyphLayout(pressStartFont, "<Press Any Key to Continue>");
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.setColor(Color.WHITE);
        batch.begin();
        batch.draw(assets.BG, 0f, 0f);
        batch.end();

        batch.setColor(swordColor);
        batch.begin();
        batch.draw(assets.SWORD, botPos.x - 20 + assets.SWORD.getRegionWidth(), botPos.y - assets.SWORD.getRegionHeight(), -assets.SWORD.getRegionWidth(), assets.SWORD.getRegionHeight());
        batch.draw(assets.SWORD, topPos.x - 50, topPos.y + assets.TITLE_WITCHED.getRegionHeight());
        batch.end();

        switch(curSplashState) {
            case WORDS_FLY_IN:
            case SWITCHED_FLY_IN:
            case S_FALL:
                batch.setColor(Constants.WORD_OUTLINE_COLOR);
                batch.begin();
                batch.draw(assets.TITLE_S_BG, topPos.x + sOffset.x, topPos.y + sOffset.y);
                batch.draw(assets.TITLE_WITCHED_BG, topPos.x, topPos.y);
                batch.draw(assets.TITLE_WORDS_BG, botPos.x, botPos.y);
                batch.end();

                batch.setColor(Constants.WORD_COLOR);
                batch.begin();
                batch.draw(assets.TITLE_S, topPos.x + sOffset.x, topPos.y + sOffset.y);
                batch.draw(assets.TITLE_WITCHED, topPos.x, topPos.y);
                batch.draw(assets.TITLE_WORDS, botPos.x, botPos.y);
                batch.end();
                break;
            case DONE:
            case POST:
            case TRANSITION:
                batch.setColor(Constants.WORD_OUTLINE_COLOR);
                batch.begin();
                batch.draw(assets.TITLE_BG, titlePos.x, titlePos.y, assets.TITLE_BG.getRegionWidth() * titleScalePercent.x / 100f, assets.TITLE_BG.getRegionHeight() * titleScalePercent.y / 100f);
                batch.end();

                batch.setColor(Constants.WORD_COLOR);
                batch.begin();
                batch.draw(assets.TITLE, titlePos.x, titlePos.y, assets.TITLE.getRegionWidth() * titleScalePercent.x / 100f, assets.TITLE.getRegionHeight() * titleScalePercent.y / 100f);
                batch.end();
                break;
        }


        switch (curSplashState) {
            case WORDS_FLY_IN:

                timer += 0.1f;
                if (timer > 1f)
                    timer = 1f;
                botPos.slerp(Constants.TITLE_GOAL_POS, .15f);
                topPos.slerp(Constants.TITLE_GOAL_POS, .15f);
                swordColor.lerp(Color.WHITE, timer);

                if (botPos.epsilonEquals(Constants.TITLE_GOAL_POS, 2f)) {
                    curSplashState = SplashState.SWITCHED_FLY_IN;
                }
                break;
            case SWITCHED_FLY_IN:

                topPos.slerp(Constants.TITLE_GOAL_POS, .15f);
                botPos.slerp(Constants.TITLE_GOAL_POS, .15f);

                if (topPos.epsilonEquals(Constants.TITLE_GOAL_POS, 0.01f) && botPos.epsilonEquals(Constants.TITLE_GOAL_POS, 0.01f)) {
                    curSplashState = SplashState.S_FALL;
                }
                break;
            case S_FALL:

                sVel.add(Constants.GRAVITY);
                sOffset.add(sVel);

                if (sOffset.y < 0) {
                    sOffset.y = 0;
                    sVel.y = -sVel.y * Constants.DAMPING;
                    sVel.x = 0.3f;
                }
                if (sOffset.x > 0) {
                    sOffset.x = 0f;
                    sVel.x = 0f;
                }
                if (sOffset.epsilonEquals(0.0f, 0.0f, 0.0f, 0.01f)) {
                    curSplashState = SplashState.DONE;
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
                    curSplashState = SplashState.POST;
                    timer = 0;
                }
                break;
            case POST:
                vel += acc;
                topPos.x += vel;
                botPos.x -= vel;

                titlePos.slerp(Constants.TITLE_MENU_POS, .1f);
                titleScalePercent.slerp(Constants.TITLE_MENU_SCALE, .1f);

                if (titlePos.epsilonEquals(Constants.TITLE_MENU_POS, 0.01f)) {
                    curSplashState = SplashState.TRANSITION;
                }

                if (timer > 1f) {
                    timer = 1f;
                }
                timer += 0.01f;
                break;
            case TRANSITION:
                game.setScreen(new MainMenuScreen(game, this));
                break;
        }
    }
}
