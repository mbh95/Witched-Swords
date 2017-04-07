package com.comp460.screens.launcher.practice.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.comp460.MainGame;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.common.GameScreen;
import com.comp460.common.GameUnit;
import com.comp460.common.ui.Button;
import com.comp460.common.ui.TexturedButton;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class BattlePracticeScreen extends GameScreen {

    private CharacterButton selectedPlayerButton;
    private CharacterButton selectedAiButton;

    private Animation<TextureRegion> playerUnitIdle;
    private Animation<TextureRegion> aiUnitIdle;

    private float frameTime = 0f;

    private BattlePracticeAssets assets;
    private Button[] buttons;
    private Button selectedButton;

    private InfoUnit infoUnit = null;
    private float infoWidth = 200;
    private float infoHeight = 100 - 2;

    private BattleScreen dummyScreen = new BattleScreen(this.game, this, GameUnit.loadFromJSON("json/units/protagonists/andre.json"), GameUnit.loadFromJSON("json/units/protagonists/andre.json"), true, true, 30f);

    private Vector3 cursorPos = new Vector3(0, 0, 0);

    private int bottomBorder = 0;

    public BattlePracticeScreen(MainGame game, GameScreen prevScreen) {
        super(game, prevScreen);

        CharacterButton andreButton = makePlayerCharacterButton("json/units/protagonists/andre.json", 0, 50 + bottomBorder);
        CharacterButton clarissaButton = makePlayerCharacterButton("json/units/protagonists/clarissa.json", 50, 50 + bottomBorder);
        CharacterButton yvonneButton = makePlayerCharacterButton("json/units/protagonists/yvonne.json", 0, 0 + bottomBorder);
        CharacterButton zaneButton = makePlayerCharacterButton("json/units/protagonists/zane.json", 50, 0 + bottomBorder);


//        addPlayerButton("common/units/bulba.json", 200, 0, 100, 100);

        CharacterButton bulbaButton = makeAiCharacterButton("json/units/enemies/bulba.json", 400 - 50, 50 + bottomBorder);
        CharacterButton ghastButton = makeAiCharacterButton("json/units/enemies/ghast.json", 400 - 100, 50 + bottomBorder);
        CharacterButton trixyButton = makeAiCharacterButton("json/units/enemies/trixy.json", 400 - 50, 0 + bottomBorder);
        CharacterButton shellButton = makeAiCharacterButton("json/units/enemies/shellgon.json", 400 - 100, 0 + bottomBorder);

        Button[][] buttonMap = new Button[][]{{andreButton, clarissaButton, ghastButton, bulbaButton}, {yvonneButton, zaneButton, shellButton, trixyButton}};

        for (int r = 0; r < buttonMap.length; r++) {
            for (int c = 0; c < buttonMap[0].length; c++) {
                if (r < buttonMap.length - 1) {
                    Button down = buttonMap[r + 1][c];
                    buttonMap[r][c].down = down;
                }
                if (r > 0) {
                    Button up = buttonMap[r - 1][c];
                    buttonMap[r][c].up = up;
                }

                if (c > 0) {
                    Button left = buttonMap[r][c - 1];
                    buttonMap[r][c].left = left;
                }
                if (c < buttonMap[0].length - 1) {
                    Button right = buttonMap[r][c + 1];
                    buttonMap[r][c].right = right;
                }
            }
        }
        buttons = new Button[]{andreButton, clarissaButton, yvonneButton, zaneButton, ghastButton, trixyButton, bulbaButton, shellButton};
        selectedButton = andreButton;
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();
        batch.draw(assets.TEXTURE_BG, 0, 0);
        for (Button button : buttons) {
            button.render(batch);
        }
        batch.draw(assets.TEXTURE_PLAYER_AREA, 30, 240 - 10 - assets.TEXTURE_PLAYER_AREA.getRegionHeight());
        batch.draw(assets.TEXTURE_AI_AREA, 400 - 30 - assets.TEXTURE_AI_AREA.getRegionWidth(), 240 - 10 - assets.TEXTURE_AI_AREA.getRegionHeight());

        float scale = 3.0f;
        if (playerUnitIdle != null) {
            TextureRegion currentFrame = playerUnitIdle.getKeyFrame(frameTime, true);
            batch.draw(currentFrame, 30, 240 - 10 - assets.TEXTURE_PLAYER_AREA.getRegionHeight(), currentFrame.getRegionWidth() * scale, currentFrame.getRegionHeight() * scale);
        }
        if (aiUnitIdle != null) {
            TextureRegion currentFrame = aiUnitIdle.getKeyFrame(frameTime, true);
            batch.draw(currentFrame, 400 - 30 - assets.TEXTURE_AI_AREA.getRegionWidth(), 240 - 10 - assets.TEXTURE_AI_AREA.getRegionHeight(), currentFrame.getRegionWidth() * scale, currentFrame.getRegionHeight() * scale);
        }

        BattlePracticeAssets.NINEPATCH_CURSOR.draw(batch, cursorPos.x, cursorPos.y, selectedButton.width, selectedButton.height);

        drawInfo();

        if (selectedPlayerButton != null && selectedAiButton != null) {
//            batch.draw(BattlePracticeAssets.TEXTURE_READY_BANNER, 0, 150);

            BattlePracticeAssets.FONT_READY.draw(batch, "Ready", 30 + 120 + 5, 150 + 32);

            batch.draw(game.controller.startSprite(), 30 + 120 + 5, 150);
            BattlePracticeAssets.FONT_INFO.draw(batch, " to start", 30 + 120 + 5 + game.controller.startSprite().getRegionWidth(), 150 + 8);
        }

//        batch.draw(game.controller.button1Sprite(), 30 + 120 + 5, 128);
//        batch.draw(game.controller.button2Sprite(), 30 + 120 + 5, 114);
//        batch.draw(game.controller.directionalSprite(), 30 + 120, 100);
//        BattlePracticeAssets.FONT_INFO.draw(batch, " confirm", 30 + 120 + 5 + game.controller.button1Sprite().getRegionWidth(), 128 + 8);
//        BattlePracticeAssets.FONT_INFO.draw(batch, " back", 30 + 120 + 5 + game.controller.button2Sprite().getRegionWidth(), 114 + 8);
//        BattlePracticeAssets.FONT_INFO.draw(batch, " select", 30 + 120 + game.controller.directionalSprite().getRegionWidth(), 100 + 8);


        batch.draw(game.controller.button2Sprite(), 0 + 1, 240 - game.controller.button2Sprite().getRegionHeight() - 1);
//        batch.draw(BattlePracticeAssets.TEXTURE_BACK_BUTTON, 0 + 1 + game.controller.endSprite().getRegionWidth() + 1, 240 - game.controller.endSprite().getRegionHeight() - 1);
        BattlePracticeAssets.FONT_INFO.draw(batch, " back", 0 + 1 + game.controller.button2Sprite().getRegionWidth() + 1, 240 - 2);

        batch.end();

//        selectedButton.setNormal();
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) selectedButton = selectedButton.left;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) selectedButton = selectedButton.right;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) selectedButton = selectedButton.up;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) selectedButton = selectedButton.down;
//        selectedButton.setHovered();

        if (game.controller.button1JustPressed()) {
            selectedButton.click();

        }
        if (game.controller.button2JustPressed() || game.controller.endJustPressed()) {
            this.previousScreen();
        }

        if (selectedButton instanceof CharacterButton) {
            if (selectedPlayerButton == selectedButton) {

            }
            CharacterButton sel = ((CharacterButton) selectedButton);
            infoUnit = new InfoUnit(sel.unit);
        }

        if (selectedPlayerButton != null && selectedAiButton != null && game.controller.startJustPressed()) {
            game.setScreen(new BattleScreen(game, this, GameUnit.loadFromJSON(selectedPlayerButton.json), GameUnit.loadFromJSON(selectedAiButton.json), true, true, 30f));
        }

        frameTime += delta;
        cursorPos.lerp(selectedButton.pos, 0.3f);
    }

    private CharacterButton makeGenericCharacterButton(String json, float x, float y) {
        CharacterButton button = new CharacterButton(json, x, y, () -> {
        });
        return button;
    }

    public CharacterButton makePlayerCharacterButton(String json, float x, float y) {
        CharacterButton button = makeGenericCharacterButton(json, x, y);
        button.action = () -> {
            if (selectedPlayerButton == button) {
                playerUnitIdle = null;
                selectedPlayerButton = null;
                button.normalTexture = BattlePracticeAssets.TEXTURE_SQUARE;
            } else {
                if (selectedPlayerButton != null) {
                    selectedPlayerButton.normalTexture = BattlePracticeAssets.TEXTURE_SQUARE;
                }
                selectedPlayerButton = button;
                selectedPlayerButton.normalTexture = BattlePracticeAssets.TEXTURE_SQUARE_BLUE;

                playerUnitIdle = BattleAnimationManager.getBattleUnitAnimation(button.unit.id, "idle");
            }
        };
        return button;
    }

    public CharacterButton makeAiCharacterButton(String json, float x, float y) {
        CharacterButton button = makeGenericCharacterButton(json, x, y);
        button.action = () -> {
            if (selectedAiButton == button) {
                aiUnitIdle = null;
                selectedAiButton = null;
                button.normalTexture = BattlePracticeAssets.TEXTURE_SQUARE;
            } else {
                if (selectedAiButton != null) {
                    selectedAiButton.normalTexture = BattlePracticeAssets.TEXTURE_SQUARE;
                }
                selectedAiButton = button;
                selectedAiButton.normalTexture = BattlePracticeAssets.TEXTURE_SQUARE_RED;

                aiUnitIdle = BattleAnimationManager.getBattleUnitAnimation(button.unit.id, "idle");
            }
        };
        return button;
    }

    private void drawInfo() {
        if (infoUnit == null) {
            return;
        }

        float padding = 4;

//        float w = infoUnit.infoLayout.width + 2 * padding;
//        float h = infoUnit.infoLayout.height + 2 * padding;

//        float x = 400f / 2 - w / 2;
//        float y = 240f / 2 - h / 2;

        float w = infoWidth;
        float h = infoHeight;
        float x = 100;
        float y = 0;


//        batch.draw(assets.TEXTURE_INFO_BG, 0, 0);
        assets.NP_INFO_BG.draw(batch, (int) x, (int) y, (int) w, (int) h);

        assets.FONT_INFO.draw(batch, infoUnit.infoLayout, x + padding, y + h - padding);
    }

    private class CharacterButton extends TexturedButton {

        public BattleUnit unit;
        public TextureRegion unitIcon;
        private GlyphLayout layout;
        String json;

        public CharacterButton(String json, float x, float y, Runnable action) {
            super(x, y, assets.TEXTURE_SQUARE, action);
            this.unit = GameUnit.loadFromJSON(json).buildBattleUnit(dummyScreen, 0, 0);
            this.unitIcon = BattleAnimationManager.getBattleUnitAnimation(unit.id, "attack").getKeyFrame(0f);
            this.layout = new GlyphLayout(assets.FONT_BATTLE_PORTRAIT, unit.name);
            this.json = json;
        }

        @Override
        public void render(SpriteBatch batch) {
            super.render(batch);
            batch.draw(unitIcon, pos.x + 4, pos.y + 4);
            assets.FONT_BATTLE_PORTRAIT.draw(batch, layout, pos.x + this.normalTexture.getRegionWidth() / 2f - layout.width / 2f, pos.y + layout.height + 4);
        }
    }

    private class InfoUnit {
        public BattleUnit unit;
        public GlyphLayout infoLayout;

        public InfoUnit(BattleUnit unit) {
            this.unit = unit;


            StringBuilder text = new StringBuilder();
//            text.append("Name: \t" + unit.name);
            text.append(unit.name);

            text.append("\n");
            text.append(unit.description);
//            text.append("Description: \t" + unit.description);
//            text.append("\n");
//            text.append("Max HP: \t" + unit.maxHP);
            text.append("\n");
            text.append("\n");

//            text.append("Abilities:\n");
            text.append(unit.ability1.name + ":");
            text.append("\n");
            text.append(unit.ability1.description);
            text.append("\n");
            text.append("\n");
            text.append(unit.ability2.name + ":");
            text.append("\n");
            text.append(unit.ability2.description);

            float w = infoWidth;
            float padding = 4;

            infoLayout = new GlyphLayout(assets.FONT_INFO, text.toString(), Color.WHITE, w - 2 * padding, Align.left, true);
        }

    }

    @Override
    public void show() {
        super.show();
        game.playMusic("music/old-city-theme.ogg");
    }
}
