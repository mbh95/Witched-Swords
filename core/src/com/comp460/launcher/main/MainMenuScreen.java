package com.comp460.launcher.main;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;
import com.comp460.MainGame;
import com.comp460.assets.FontManager;
import com.comp460.common.GameScreen;
import com.comp460.launcher.Button;
import com.comp460.launcher.NinePatchTextButton;
import com.comp460.launcher.mapselect.MapSelectScreen;
import com.comp460.launcher.practice.battle.BattlePracticeScreen;
import com.comp460.launcher.splash.SplashScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class MainMenuScreen extends GameScreen {

    private enum MainMenuState {BUILD_MENU, DONE, POST, TRANSITION}

    public static class Constants {
        public static final Vector3 TITLE_SCALE = new Vector3(50f, 50f, 0f);
        public static final Vector3 TITLE_POS = new Vector3(400f / 2f - 280f * (TITLE_SCALE.x / 100f) / 2f, 240f - 100f * (TITLE_SCALE.y / 100f) - 10f, 0f);

        public static final Color WORD_COLOR = Color.WHITE;
        public static final Color WORD_OUTLINE_COLOR = Color.BLACK;
    }

    private class TemplateRow {
        public String text;
        public Runnable action;

        public TemplateRow(String text, Runnable action) {
            this.text = text;
            this.action = action;
        }
    }

    public TemplateRow[] buttonTemplates = new TemplateRow[] {
            new TemplateRow("New Game", ()->{
                game.setScreen(new MapSelectScreen(game, this));
            }),
            new TemplateRow("Load Game", ()->{}),
            new TemplateRow("Practice", () -> {
                game.setScreen(new BattlePracticeScreen(game, this));
            })
    };

    private MainMenuState curMenuState;

    private List<Button> buttons = new ArrayList<>(buttonTemplates.length);
    private Button curSelectedButton;

    private Vector3 cursorPos = new Vector3(0, 0, 0);

    private int buttonWidth = 120;
    private int buttonHeight = 32;

    private int buttonX;
    private int topButtonY;

    private int inputHintX = 2;
    private int inputHintY = 2;
    private int inputHintLineHeight = 16;

    private BitmapFont hintFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.WHITE);

    public MainMenuScreen(MainGame game, GameScreen prevScreen) {
        super(game, prevScreen);
        curMenuState = MainMenuState.BUILD_MENU;

        buttonX = (int) (Constants.TITLE_POS.x + (Constants.TITLE_SCALE.y / 100f) * MainMenuAssets.TEXTURE_TITLE.getRegionWidth() / 2f - buttonWidth / 2f);
        topButtonY = (int) (Constants.TITLE_POS.y - buttonHeight - buttonHeight/2);

        for (int i = 0; i < buttonTemplates.length; i++) {
            TemplateRow template = buttonTemplates[i];
            NinePatchTextButton newButton = new NinePatchTextButton(buttonX, topButtonY - i * buttonHeight, buttonWidth, buttonHeight, new GlyphLayout(MainMenuAssets.FONT_MENU_ITEM, template.text), MainMenuAssets.FONT_MENU_ITEM, MainMenuAssets.NINEPATCH_BUTTON, template.action);
            buttons.add(newButton);
        }


        for (int i = 0; i < buttons.size(); i++) {
            if (i > 0)
                buttons.get(i).up = buttons.get(i - 1);
            if (i < buttons.size() - 1)
                buttons.get(i).down = buttons.get(i + 1);
        }

        curSelectedButton = buttons.get(0);
        cursorPos = new Vector3(curSelectedButton.pos);
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
        MainMenuAssets.NINEPATCH_CURSOR.draw(batch, cursorPos.x - 2, cursorPos.y - 2, curSelectedButton.width + 4, curSelectedButton.height + 4);


        batch.draw(game.controller.button1Sprite(), inputHintX, inputHintY + 2 * inputHintLineHeight);
        hintFont.draw(batch, "Confirm", inputHintX + game.controller.button1Sprite().getRegionWidth() + 2, inputHintY + 2 * inputHintLineHeight + 8);

        batch.draw(game.controller.button2Sprite(), inputHintX, inputHintY + inputHintLineHeight);
        hintFont.draw(batch, "Back", inputHintX + game.controller.button1Sprite().getRegionWidth() + 2, inputHintY + 1 * inputHintLineHeight + 8);

        batch.draw(game.controller.directionalSprite(), inputHintX, inputHintY);
        hintFont.draw(batch, "Select", inputHintX + game.controller.directionalSprite().getRegionWidth() + 2, inputHintY + 8);

        batch.end();

        if (game.controller.leftJustPressed()) curSelectedButton = curSelectedButton.left;
        if (game.controller.rightJustPressed()) curSelectedButton = curSelectedButton.right;
        if (game.controller.upJustPressed()) curSelectedButton = curSelectedButton.up;
        if (game.controller.downJustPressed()) curSelectedButton = curSelectedButton.down;
        if (game.controller.button1JustPressed()) {
            curSelectedButton.click();
        }
        if (game.controller.button2JustPressed()) {
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
