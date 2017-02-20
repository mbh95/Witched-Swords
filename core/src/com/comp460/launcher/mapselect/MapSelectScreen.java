package com.comp460.launcher.mapselect;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.comp460.MainGame;
import com.comp460.Settings;
import com.comp460.assets.SpriteManager;
import com.comp460.common.GameScreen;
import com.comp460.launcher.Button;
import com.comp460.launcher.TexturedButton;
import com.comp460.launcher.practice.battle.BattlePracticeAssets;
import com.comp460.tactics.TacticsScreen;

/**
 * Created by Belinda on 2/19/17.
 */
public class MapSelectScreen extends GameScreen {

    private Button[] buttons;
    private Button selectedButton;

    private Vector3 cursorPos;

    private NinePatch cursorSprite = new NinePatch(SpriteManager.MAIN_MENU.findRegion("cursor-tiny"), 2, 2, 2, 2);

    public MapSelectScreen(MainGame game, GameScreen prevScreen) {
        super(game, prevScreen);

        MapButton map1Button = new MapButton(new TmxMapLoader().load("maps/testmap.tmx"), "Map 1", 0, 50, ()->{});

        map1Button.action = () -> {
            game.setScreen(new TacticsScreen(game, prevScreen, Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT, map1Button.map));
        };

        Button[][] buttonMap = new Button[][] {{map1Button}};

        for (int r = 0; r < buttonMap.length; r++) {
            for (int c = 0; c < buttonMap[0].length; c++) {
                if (r < buttonMap.length - 1) {
                    Button down = buttonMap[r + 1][c];
                    buttonMap[r][c].down = down;
                }
                if (r > 0) {
                    Button up = buttonMap[r-1][c];
                    buttonMap[r][c].up = up;
                }

                if (c > 0) {
                    Button left = buttonMap[r][c-1];
                    buttonMap[r][c].left = left;
                }
                if (c < buttonMap[0].length - 1) {
                    Button right = buttonMap[r][c + 1];
                    buttonMap[r][c].right = right;
                }
            }
        }
        buttons = new Button[] {map1Button};
        selectedButton = map1Button;
        cursorPos = new Vector3(selectedButton.pos);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();
        batch.draw(BattlePracticeAssets.TEXTURE_BG, 0, 0);
        for (Button button : buttons) {
            button.render(batch);
        }

        float scale = 3.0f;

        cursorSprite.draw(batch, cursorPos.x, cursorPos.y, selectedButton.width, selectedButton.height);

        batch.end();

        selectedButton.setNormal();
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) selectedButton = selectedButton.left;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) selectedButton = selectedButton.right;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) selectedButton = selectedButton.up;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) selectedButton = selectedButton.down;
        selectedButton.setHovered();

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            selectedButton.click();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            this.previousScreen();
        }

        cursorPos.slerp(selectedButton.pos, 0.3f);
    }

    private class MapButton extends TexturedButton {
        public TiledMap map;
        private GlyphLayout layout;

        public MapButton(TiledMap map, String name, float x, float y, Runnable action) {
            super(x, y, BattlePracticeAssets.TEXTURE_SQUARE, action);
            System.out.println(name);
            this.map = map;
            this.layout = new GlyphLayout(BattlePracticeAssets.FONT_BATTLE_PORTRAIT, name);
        }

        @Override
        public void render(SpriteBatch batch) {
            super.render(batch);
//            batch.draw(unitIcon, pos.x + 4, pos.y + 4);
            BattlePracticeAssets.FONT_BATTLE_PORTRAIT.draw(batch, layout, pos.x + this.normalTexture.getRegionWidth() / 2f - layout.width / 2f, pos.y + layout.height + 4);
        }
    }

}
