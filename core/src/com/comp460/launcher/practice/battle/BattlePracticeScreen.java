package com.comp460.launcher.practice.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.comp460.MainGame;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.units.BattleUnit;
import com.comp460.common.GameScreen;
import com.comp460.common.GameUnit;
import com.comp460.launcher.Button;
import com.comp460.launcher.TexturedButton;

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

    private boolean infoMode = false;
    private InfoUnit infoUnit = null;

    private BattleScreen dummyScreen = new BattleScreen(this.game, this, GameUnit.loadFromJSON("json/units/protagonists/andre.json"), GameUnit.loadFromJSON("json/units/protagonists/andre.json"), true);

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


        TexturedButton fightButton = new TexturedButton(150, 50, BattlePracticeAssets.TEXTURE_FIGHT_BUTTON, () -> {
            if (selectedAiButton != null && selectedPlayerButton != null) {
                game.setScreen(new BattleScreen(game, this, GameUnit.loadFromJSON(selectedPlayerButton.json), GameUnit.loadFromJSON(selectedAiButton.json), true));
            }
        });

        TexturedButton optionsButton = new TexturedButton(200, 50, BattlePracticeAssets.TEXTURE_FIGHT_BUTTON, () -> {
            if (selectedAiButton != null && selectedPlayerButton != null) {
                game.setScreen(new BattleScreen(game, this, GameUnit.loadFromJSON(selectedPlayerButton.json), GameUnit.loadFromJSON(selectedAiButton.json), true));
            }
        });

        TexturedButton backButton = new TexturedButton(150, 0, BattlePracticeAssets.TEXTURE_FIGHT_BUTTON, () -> {
            if (selectedAiButton != null && selectedPlayerButton != null) {
                game.setScreen(new BattleScreen(game, this, GameUnit.loadFromJSON(selectedPlayerButton.json), GameUnit.loadFromJSON(selectedAiButton.json), true));
            }
        });

        TexturedButton helpButton = new TexturedButton(200, 0, BattlePracticeAssets.TEXTURE_FIGHT_BUTTON, () -> {
            if (selectedAiButton != null && selectedPlayerButton != null) {
                game.setScreen(new BattleScreen(game, this, GameUnit.loadFromJSON(selectedPlayerButton.json), GameUnit.loadFromJSON(selectedAiButton.json), true));
            }
        });

        Button[][] buttonMap = new Button[][]{{andreButton, clarissaButton, fightButton, optionsButton, ghastButton, bulbaButton}, {yvonneButton, zaneButton, backButton, helpButton, shellButton, trixyButton}};

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
        buttons = new Button[]{andreButton, clarissaButton, yvonneButton, zaneButton, ghastButton, trixyButton, bulbaButton, shellButton, fightButton, optionsButton, helpButton, backButton};
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

        if (infoMode) {
            drawInfo();
        }

        batch.end();

//        selectedButton.setNormal();
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) selectedButton = selectedButton.left;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) selectedButton = selectedButton.right;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) selectedButton = selectedButton.up;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) selectedButton = selectedButton.down;
//        selectedButton.setHovered();

        if (infoMode) {
            if (selectedButton instanceof CharacterButton) {
                CharacterButton sel = ((CharacterButton) selectedButton);
                if (sel.unit != infoUnit.unit)
                    infoUnit = new InfoUnit(sel.unit);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.X) || Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                infoMode = false;
            }
        } else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                selectedButton.click();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                this.previousScreen();
            }
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
                infoMode = true;
                infoUnit = new InfoUnit(button.unit);
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
                infoMode = true;
                infoUnit = new InfoUnit(button.unit);
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
        if (!infoMode || infoUnit == null) {
            return;
        }

        float padding = 4;

        float w = infoUnit.infoLayout.width + 2 * padding;
        float h = infoUnit.infoLayout.height + 2 * padding;

        float x = 400f / 2 - w / 2;
        float y = 240f / 2 - h / 2;


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
            if (this == selectedPlayerButton || this == selectedAiButton) {
                batch.draw(assets.TEXTURE_i, pos.x + this.normalTexture.getRegionWidth() - 1.5f * assets.TEXTURE_i.getRegionWidth(), pos.y + this.normalTexture.getRegionHeight() - 1.5f * assets.TEXTURE_i.getRegionHeight());
            }
        }
    }

    private class InfoUnit {
        public BattleUnit unit;
        public GlyphLayout infoLayout;

        public InfoUnit(BattleUnit unit) {
            this.unit = unit;


            StringBuilder text = new StringBuilder();
            text.append("Name: \t" + unit.name);
            text.append("\n");
            text.append("Description: \t" + unit.description);
            text.append("\n");
            text.append("Max HP: \t" + unit.maxHP);
            text.append("\n");
            text.append("\n");

            text.append("Abilities:\n");
            text.append(unit.ability1.name + ":");
            text.append("\n");
            text.append(unit.ability1.description);
            text.append("\n");
            text.append("\n");
            text.append(unit.ability2.name + ":");
            text.append("\n");
            text.append(unit.ability2.description);

            float w = 300;
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
