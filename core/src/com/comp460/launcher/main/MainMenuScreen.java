package com.comp460.launcher.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector3;
import com.comp460.common.GameScreen;
import com.comp460.launcher.Button;
import com.comp460.launcher.TextButton;
import com.comp460.launcher.practice.battle.BattlePracticeScreen;
import com.comp460.launcher.splash.SplashScreen;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class MainMenuScreen extends GameScreen {

    private enum MainMenuState {BUILD_MENU, DONE, POST, TRANSITION};
    
    public static class Constants {
        public static final Vector3 TITLE_SCALE = new Vector3(50f, 50f, 0f);
        public static final Vector3 TITLE_POS = new Vector3(400f / 2f - 280f * (TITLE_SCALE.x / 100f) / 2f, 240f - 100f * (TITLE_SCALE.y / 100f) - 10f, 0f);

        public static final Color WORD_COLOR = Color.WHITE;
        public static final Color WORD_OUTLINE_COLOR = Color.BLACK;
    }

    private MainMenuState curMenuState;

    private Button[] buttons;
    private Button curSelectedButton;

    private Vector3 cursorPos = new Vector3(0, 0, 0);


    private NinePatch cursor = new NinePatch(MainMenuAssets.TEXTURE_CURSOR, 2, 2, 2, 2);

    public MainMenuScreen(Game game, GameScreen prevScreen) {
        super(game, prevScreen);
        curMenuState = MainMenuState.BUILD_MENU;

        TextButton newGameButton = new TextButton(Constants.TITLE_POS.x, Constants.TITLE_POS.y - MainMenuAssets.TEXTURE_BUTTON_NORMAL.getRegionHeight(), "---", MainMenuAssets.FONT_MENU_ITEM, MainMenuAssets.TEXTURE_BUTTON_NORMAL, MainMenuAssets.TEXTURE_BUTTON_NORMAL, ()->{});
        TextButton loadGameButton = new TextButton(Constants.TITLE_POS.x, Constants.TITLE_POS.y - 2 * MainMenuAssets.TEXTURE_BUTTON_NORMAL.getRegionHeight(), "---", MainMenuAssets.FONT_MENU_ITEM, MainMenuAssets.TEXTURE_BUTTON_NORMAL, MainMenuAssets.TEXTURE_BUTTON_NORMAL, () -> {
        });
        TextButton battlePracticeButton = new TextButton(Constants.TITLE_POS.x, Constants.TITLE_POS.y - 3 * MainMenuAssets.TEXTURE_BUTTON_NORMAL.getRegionHeight(), "Battle\nPractice", MainMenuAssets.FONT_MENU_ITEM, MainMenuAssets.TEXTURE_BUTTON_NORMAL, MainMenuAssets.TEXTURE_BUTTON_NORMAL, () -> {
            game.setScreen(new BattlePracticeScreen(game, this));
        });

        buttons = new Button[]{newGameButton, loadGameButton, battlePracticeButton};
        for (int i = 0; i < buttons.length; i++) {
            if (i > 0)
                buttons[i].up = buttons[i - 1];
            if (i < buttons.length - 1)
                buttons[i].down = buttons[i + 1];
        }

        curSelectedButton = buttons[0];
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.setColor(Color.WHITE);
        batch.begin();
        batch.draw(MainMenuAssets.TEXTURE_BG, 0f, 0f);
        batch.end();

        batch.setColor(Constants.WORD_OUTLINE_COLOR);
        batch.begin();
        batch.draw(MainMenuAssets.TEXTURE_TITLE_BG, Constants.TITLE_POS.x, Constants.TITLE_POS.y, MainMenuAssets.TEXTURE_TITLE_BG.getRegionWidth() * Constants.TITLE_SCALE.x / 100f, MainMenuAssets.TEXTURE_TITLE.getRegionHeight() * Constants.TITLE_SCALE.y / 100f);
        batch.end();

        batch.setColor(Constants.WORD_COLOR);
        batch.begin();
        batch.draw(MainMenuAssets.TEXTURE_TITLE, Constants.TITLE_POS.x, Constants.TITLE_POS.y, MainMenuAssets.TEXTURE_TITLE.getRegionWidth() * Constants.TITLE_SCALE.x / 100f, MainMenuAssets.TEXTURE_TITLE.getRegionHeight() * Constants.TITLE_SCALE.y / 100f);
        batch.end();

        batch.setColor(Color.WHITE);
        batch.begin();
        for (Button b : buttons) {
            b.render(batch);
        }
        cursor.draw(batch, cursorPos.x-2, cursorPos.y-2, curSelectedButton.width + 4, curSelectedButton.height + 4);
        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) curSelectedButton = curSelectedButton.left;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) curSelectedButton = curSelectedButton.right;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) curSelectedButton = curSelectedButton.up;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) curSelectedButton = curSelectedButton.down;
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            curSelectedButton.click();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            game.setScreen(new SplashScreen(game));
        }

        cursorPos.slerp(curSelectedButton.pos, .3f);

        switch (curMenuState) {
            case BUILD_MENU:
                break;
            case DONE:
                break;
            case POST:
                break;
            case TRANSITION:
                break;
        }
    }
}
