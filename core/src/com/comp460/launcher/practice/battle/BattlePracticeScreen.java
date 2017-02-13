package com.comp460.launcher.practice.battle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.comp460.AssetMgr;
import com.comp460.GameScreen;
import com.comp460.Settings;
import com.comp460.battle.ActionFactory;
import com.comp460.battle.BattleMove;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.BattleUnit;
import com.comp460.common.GameUnit;
import com.comp460.launcher.Button;
import com.comp460.launcher.TextButton;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class BattlePracticeScreen extends GameScreen {

    private String playerUnitJSON = "";
    private String aiUnitJSON = "";

    private Animation<TextureRegion> playerUnitIdle;
    private Animation<TextureRegion> aiUnitIdle;
    private float frameTime = 0f;

    private BattlePracticeAssets assets;
    private Button[] buttons;
    private Button selectedButton;

    private InfoUnit infoUnit = null;
    private boolean infoMode = false;

    public BattlePracticeScreen(Game game, GameScreen prevScreen) {
        super(game, prevScreen);
        this.assets = BattlePracticeAssets.getInstance();

        Button backButton = new Button(0f, 240f - assets.TEXTURE_BACK_BUTTON.getRegionHeight(), assets.TEXTURE_BACK_BUTTON, assets.TEXTURE_BACK_BUTTON_HOVERED, () -> {
            previousScreen();
        });

        TextButton fightButton = new TextButton(160f, 0f, "Fight!    ", assets.FONT_FIGHT, assets.TEXTURE_FIGHT_BUTTON, assets.TEXTURE_FIGHT_BUTTON_HOVER, () -> {
            if (playerUnitJSON != null && aiUnitJSON != null) {
                game.setScreen(new BattleScreen(game, Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT, GameUnit.loadFromJSON(playerUnitJSON), GameUnit.loadFromJSON(aiUnitJSON), this));
            }
        });

        CharacterButton rubyButton = makePlayerCharacterButton("common/units/ruby.json", 0, 0);
        CharacterButton shieldmanButton = makePlayerCharacterButton("common/units/shieldman.json", 50, 0);
        CharacterButton clarissaButton = makePlayerCharacterButton("common/units/clarissa.json", 0, 50);

//        addPlayerButton("common/units/bulba.json", 200, 0, 100, 100);

        CharacterButton bulbaButton = makeAiCharacterButton("common/units/bulba.json", 400 - 50, 0);
        CharacterButton ghastButton = makeAiCharacterButton("common/units/ghast.json", 400 - 100, 0);
        CharacterButton trixyButton = makeAiCharacterButton("common/units/trixy.json", 400 - 50, 50);

        rubyButton.up = clarissaButton;
        rubyButton.right = shieldmanButton;

        shieldmanButton.left = rubyButton;
        shieldmanButton.right = fightButton;
        shieldmanButton.up = clarissaButton;

        clarissaButton.down = rubyButton;
        clarissaButton.up = backButton;
        clarissaButton.right = fightButton;

        fightButton.right = ghastButton;
        fightButton.left = shieldmanButton;

        bulbaButton.left = ghastButton;
        bulbaButton.up = trixyButton;


        ghastButton.left = fightButton;
        ghastButton.right = bulbaButton;
        ghastButton.up = trixyButton;

        trixyButton.left = fightButton;
        trixyButton.down = bulbaButton;
        backButton.down = rubyButton;

        buttons = new Button[]{
                rubyButton, shieldmanButton, clarissaButton, bulbaButton, ghastButton, trixyButton, fightButton, backButton
        };

        selectedButton = rubyButton;
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
        if (infoMode) {
            drawInfo();
        }

        batch.end();


        if (infoMode) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                infoMode = false;
            }
        } else {
            selectedButton.setNormal();
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) selectedButton = selectedButton.left;
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) selectedButton = selectedButton.right;
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) selectedButton = selectedButton.up;
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) selectedButton = selectedButton.down;
            if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
                selectedButton.click();
            } else {
                selectedButton.setHovered();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                this.previousScreen();
            }
        }

        frameTime += delta;
    }

    private CharacterButton makeGenericCharacterButton(String json, float x, float y) {
        GameUnit unit = GameUnit.loadFromJSON(json);
        TextureRegion icon = new TextureRegion(AssetMgr.getAnimation(unit.getId(), AssetMgr.BattleAnimation.ATTACK).getKeyFrame(0));
        CharacterButton button = new CharacterButton(json, unit.getName(), icon, x, y, () -> {
        });
        return button;
    }

    public CharacterButton makePlayerCharacterButton(String json, float x, float y) {
        CharacterButton button = makeGenericCharacterButton(json, x, y);
        button.action = () -> {
            if (playerUnitJSON.equals(json)) {
                infoMode = true;
                infoUnit = new InfoUnit(GameUnit.loadFromJSON(playerUnitJSON));
            } else {
                playerUnitJSON = json;
                playerUnitIdle = AssetMgr.getAnimation(GameUnit.loadFromJSON(json).getId(), AssetMgr.BattleAnimation.IDLE);
            }
        };
        return button;
    }

    public CharacterButton makeAiCharacterButton(String json, float x, float y) {
        CharacterButton button = makeGenericCharacterButton(json, x, y);
        button.action = () -> {
            if (aiUnitJSON.equals(json)) {
                infoMode = true;
                infoUnit = new InfoUnit(GameUnit.loadFromJSON(aiUnitJSON));
            } else {
                aiUnitJSON = json;
                aiUnitIdle = AssetMgr.getAnimation(GameUnit.loadFromJSON(json).getId(), AssetMgr.BattleAnimation.IDLE);
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
        assets.NP_INFO_BG.draw(batch, x, y, w, h);

        assets.FONT_INFO.draw(batch, infoUnit.infoLayout, x + padding, y + h - padding);
    }

    private class CharacterButton extends Button {

        public String json;
        public TextureRegion unitIcon;
        public String name;
        private GlyphLayout layout;

        public CharacterButton(String json, String name, TextureRegion icon, float x, float y, Runnable action) {
            super(x, y, assets.TEXTURE_SQUARE, assets.TEXTURE_SQUARE_HOVERED, action);
            this.json = json;
            this.name = name;
            this.unitIcon = icon;
            this.layout = new GlyphLayout(assets.FONT_BATTLE_PORTRAIT, name);
        }

        @Override
        public void render(SpriteBatch batch) {
            super.render(batch);
            batch.draw(unitIcon, pos.x + 4, pos.y + 4);
            assets.FONT_BATTLE_PORTRAIT.draw(batch, name, pos.x + this.normalTexture.getRegionWidth() / 2f - layout.width / 2f, pos.y + layout.height + 4);
            if (this.json.equals(playerUnitJSON) || this.json.equals(aiUnitJSON)) {
                batch.draw(assets.TEXTURE_i, pos.x + this.normalTexture.getRegionWidth() - 1.5f * assets.TEXTURE_i.getRegionWidth(), pos.y + this.normalTexture.getRegionHeight() - 1.5f * assets.TEXTURE_i.getRegionHeight());
            }
        }
    }

    private class InfoUnit {
        public GameUnit base;
        public BattleMove move1, move2;
        public GlyphLayout infoLayout;

        public InfoUnit(GameUnit gameUnit) {
            this.base = gameUnit;
            this.move1 = getInfoMove(base.getAction1());
            this.move2 = getInfoMove(base.getAction2());

            StringBuilder text = new StringBuilder();
            text.append("Name: \t" + base.getName());
            text.append("\n");
            text.append("Description: \t" + base.getDescription());
            text.append("\n");
            text.append("Max HP: \t" + base.getMaxHP());
            text.append("\n");
            text.append("\n");

            text.append("Move 1: \t" + (move1 == null ? "None" : move1.name));
            text.append("\n");
            text.append((move1 == null ? "" : move1.description));
            text.append("\n");
            text.append("\n");
            text.append("Move 2: \t" + (move2 == null ? "None" : move2.name));
            text.append("\n");
            text.append(move2 == null ? "" : move2.description);

            float w = 300;
            float padding = 4;

            infoLayout = new GlyphLayout(assets.FONT_INFO, text.toString(), Color.WHITE, w - 2 * padding, Align.left, true);
        }
    }

    public static BattleMove getInfoMove(String id) {
        if (id.equalsIgnoreCase("null")) {
            return null;
        }

        String path = "common/moves/" + id + ".json";

        Json json = new Json();
        BattleMove move = json.fromJson(BattleMove.class, Gdx.files.local(path));
        return move;
    }
}
