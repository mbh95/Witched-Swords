package com.comp460.launcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.comp460.AssetManager;
import com.comp460.FontManager;
import com.comp460.Settings;
import com.comp460.battle.BattleScreen;
import com.comp460.common.GameUnit;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by matthewhammond on 2/6/17.
 */
public class BattlePracticeMenu extends ScreenAdapter {

    private static BitmapFont battleIconFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.WHITE);
    private static BitmapFont fightFont = FontManager.getFont(FontManager.KEN_VECTOR_FUTURE, 16, Color.WHITE);
    private static BitmapFont vsFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 48, Color.RED);

    private static TextureRegion TEXTURE_SQUARE = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/battle-square.png")));
    private static TextureRegion TEXTURE_SQUARE_HOVERED = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/battle-square-hovered.png")));

    private static TextureRegion TEXTURE_BACK_BUTTON = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/back-button.png")));
    private static TextureRegion BG = SplashScreen.BG;

    private static TextureRegion TEXTURE_PLAYER_AREA = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/player-area.png")));
    private static TextureRegion TEXTURE_AI_AREA = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/ai-area.png")));

    private static TextureRegion TEXTURE_FIGHT_BUTTON = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/fight-button.png")));
    private static TextureRegion TEXTURE_FIGHT_BUTTON_HOVER = new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/fight-button-hover.png")));


    private SpriteBatch batch;
    private Game game;
    OrthographicCamera camera;

    GameUnit playerUnit;
    GameUnit aiUnit;

    Animation<TextureRegion> playerUnitIdle;
    Animation<TextureRegion> aiUnitIdle;

    Collection<MenuButton> buttons = new ArrayList<>();

    private float frameTime = 0;

    private BackButton backButton;
    private MenuButton fightButton;

    private CharacterIcon rubyButton;
    private CharacterIcon shieldmanButton;
    private CharacterIcon clarissaButton;


    private CharacterIcon bulbaButton;
    private CharacterIcon ghastButton;

    MenuButton selectedButton;

    public BattlePracticeMenu(Game parent) {
        super();
        batch = new SpriteBatch();
        camera = new OrthographicCamera(400, 240);
        camera.setToOrtho(false, 400, 240);
        game = parent;

        rubyButton = addButton("common/units/ruby.json", 0, 0);
        shieldmanButton = addButton("common/units/shieldman.json", 50, 0);
        clarissaButton = addButton("common/units/clarissa.json", 0, 50);

//        addPlayerButton("common/units/bulba.json", 200, 0, 100, 100);

        bulbaButton = addButton("common/units/bulba.json", 400 - 50, 0);
        ghastButton = addButton("common/units/ghast.json", 400 - 100, 0);

        backButton = new BackButton(0, 240 - TEXTURE_BACK_BUTTON.getRegionHeight(), TEXTURE_BACK_BUTTON);
        buttons.add(backButton);

        fightButton = new MenuButton("Fight!    ", 160, 0, fightFont, TEXTURE_FIGHT_BUTTON, TEXTURE_FIGHT_BUTTON_HOVER, TEXTURE_FIGHT_BUTTON_HOVER);
        buttons.add(fightButton);

        rubyButton.up = clarissaButton;
        rubyButton.right = shieldmanButton;

        shieldmanButton.left = rubyButton;
        shieldmanButton.right = fightButton;

        clarissaButton.down = rubyButton;
        clarissaButton.up = backButton;
        clarissaButton.right = fightButton;

        fightButton.right = ghastButton;
        fightButton.left = shieldmanButton;

        bulbaButton.left = ghastButton;

        ghastButton.left = fightButton;
        ghastButton.right = bulbaButton;
//        bulbaButton.up = backButton;

        backButton.down = rubyButton;

        rubyButton.action = ()->{
            playerUnit = rubyButton.unit;
            playerUnitIdle = AssetManager.getAnimation(playerUnit.getId(), AssetManager.BattleAnimation.IDLE);
        };

        shieldmanButton.action = ()->{
            playerUnit = shieldmanButton.unit;
            playerUnitIdle = AssetManager.getAnimation(playerUnit.getId(), AssetManager.BattleAnimation.IDLE);
        };

        clarissaButton.action = ()->{
            playerUnit = clarissaButton.unit;
            playerUnitIdle = AssetManager.getAnimation(playerUnit.getId(), AssetManager.BattleAnimation.IDLE);
        };

        bulbaButton.action = ()->{
            aiUnit = bulbaButton.unit;
            aiUnitIdle = AssetManager.getAnimation(aiUnit.getId(), AssetManager.BattleAnimation.IDLE);
        };
        ghastButton.action = ()->{
            aiUnit = ghastButton.unit;
            aiUnitIdle = AssetManager.getAnimation(aiUnit.getId(), AssetManager.BattleAnimation.IDLE);
        };


        backButton.action = ()->{
            this.game.setScreen(new MainMenu(game));
            dispose();
        };

        fightButton.action = ()->{
            if (playerUnit != null && aiUnit != null) {
                this.game.setScreen(new BattleScreen(game, 400, 240, playerUnit, aiUnit));
            }
        };

        selectedButton = rubyButton;
    }

    private CharacterIcon addButton(String jsonFile, int x, int y) {
        GameUnit unit = GameUnit.loadFromJSON(jsonFile);
        System.out.println("LOADING BUTTON FOR " + unit.getId());
        CharacterIcon icon = new CharacterIcon(unit.getName(), x, y, battleIconFont, new TextureRegion(AssetManager.getAnimation(unit.getId(), AssetManager.BattleAnimation.ATTACK).getKeyFrame(0)), unit, true);
        buttons.add(icon);
        return icon;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(BG, 0, 0);
        for (MenuButton butt : buttons) {
            butt.render(batch);
        }

        batch.draw(TEXTURE_PLAYER_AREA, 30, 240-10-TEXTURE_PLAYER_AREA.getRegionHeight());
        batch.draw(TEXTURE_AI_AREA, 400-30-TEXTURE_AI_AREA.getRegionWidth(), 240-10-TEXTURE_AI_AREA.getRegionHeight());

        float scale = 3.0f;
        if (playerUnitIdle != null) {
            TextureRegion currentFrame = playerUnitIdle.getKeyFrame(frameTime, true);
            batch.draw(currentFrame, 30, 240-10-TEXTURE_PLAYER_AREA.getRegionHeight(), currentFrame.getRegionWidth() * scale, currentFrame.getRegionHeight() * scale);
        }
        if (aiUnitIdle != null) {
            TextureRegion currentFrame = aiUnitIdle.getKeyFrame(frameTime, true);
            batch.draw(currentFrame, 400-30-TEXTURE_AI_AREA.getRegionWidth(), 240-10-TEXTURE_AI_AREA.getRegionHeight(), currentFrame.getRegionWidth() * scale, currentFrame.getRegionHeight() * scale);
        }

        vsFont.draw(batch, "Vs", 200-32, 160);
        batch.end();
        frameTime += delta;

        selectedButton.currenState = MenuButton.ButtonState.NORMAL;
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) selectedButton = selectedButton.left;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) selectedButton = selectedButton.right;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) selectedButton = selectedButton.up;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) selectedButton = selectedButton.down;
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z )) {
            selectedButton.click();
        } else {
            selectedButton.currenState = MenuButton.ButtonState.HOVERED;
        }
    }

    private class CharacterIcon extends MenuButton {

        boolean player = true;
        TextureRegion unitIcon;
        GameUnit unit;

        public CharacterIcon(String text, float x, float y, BitmapFont font, TextureRegion unitSprite, GameUnit unit, boolean player) {
            super(text, x, y, font, TEXTURE_SQUARE, TEXTURE_SQUARE_HOVERED, TEXTURE_SQUARE_HOVERED);
            this.unitIcon = unitSprite;
            this.player = player;
            this.unit = unit;
//            this.unitIcon.setRegion(0, 0, 40, 40);
        }

        @Override
        public void render(SpriteBatch batch) {
            if (this.currenState == ButtonState.HOVERED) {
                batch.draw(hovered, pos.x, pos.y);
            } else if (this.currenState == ButtonState.NORMAL) {
                batch.draw(normal, pos.x, pos.y);
            } else {
                batch.draw(pressed, pos.x, pos.y);
            }
            batch.draw(unitIcon, pos.x + 4, pos.y + 4);
            font.draw(batch, text, pos.x + normal.getRegionWidth() / 2f - layout.width / 2f, pos.y + layout.height + 4);
        }
    }

    private class BackButton extends MenuButton {

        public BackButton(float x, float y, TextureRegion normal) {
            super("", x, y, null, normal, normal, normal);
        }

        @Override
        public void render(SpriteBatch batch) {
            batch.end();

            batch.begin();
            if (currenState == ButtonState.HOVERED) {
                batch.setColor(Color.YELLOW);
            } else {
                batch.setColor(Color.WHITE);
            }

            batch.draw(normal, pos.x, pos.y);
            batch.end();
            batch.setColor(Color.WHITE);
            batch.begin();
        }
    }
}
