package com.comp460.screens.launcher.mapselect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.comp460.MainGame;
import com.comp460.Settings;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;
import com.comp460.common.GameScreen;
import com.comp460.screens.launcher.Button;
import com.comp460.screens.launcher.TexturedButton;
import com.comp460.screens.launcher.practice.battle.BattlePracticeAssets;
import com.comp460.screens.tactics.TacticsMap;
import com.comp460.screens.tactics.TacticsScreen;

import static com.comp460.Settings.INTERNAL_WIDTH;

public class MapSelectScreen extends GameScreen {

    private Button[] buttons;
    private Button selectedButton;
    private BitmapFont hintFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.WHITE);

    private int inputHintX = 2;
    private int inputHintY = 2;
    private int inputHintLineHeight = 16;

    private Vector3 cursorPos;

    private NinePatch cursorSprite = new NinePatch(SpriteManager.MAIN_MENU.findRegion("cursor-tiny"), 2, 2, 2, 2);

    public MapSelectScreen(MainGame game, GameScreen prevScreen) {
        super(game, prevScreen);

        MapButton bridgemapButton = new MapButton("maps/bridge.json", Settings.INTERNAL_WIDTH/2-50, 50, ()->{});
        MapButton smallmapButton = new MapButton("maps/small.json", Settings.INTERNAL_WIDTH/2-100, 50, ()->{});
//        MapButton canyonmapButton = new MapButton("maps/bridge.json", Settings.INTERNAL_WIDTH/2, 50, ()->{});
        MapButton cliffsmapButton = new MapButton("maps/cliffs.json", Settings.INTERNAL_WIDTH/2, 50, ()->{});
        MapButton indoormapButton = new MapButton("maps/indoor.json", Settings.INTERNAL_WIDTH/2+50, 50, ()->{});

        // set button actions
        bridgemapButton.action = () -> {
            game.setScreen(new TacticsScreen(game, prevScreen, bridgemapButton.mapJSONFile));
        };
        smallmapButton.action = () -> {
            game.setScreen(new TacticsScreen(game, prevScreen, smallmapButton.mapJSONFile));
        };
//        canyonmapButton.action = () -> {
//            game.setScreen(new TacticsScreen(game, prevScreen, canyonmapButton.map));
//        };
        cliffsmapButton.action = () -> {
            game.setScreen(new TacticsScreen(game, prevScreen, cliffsmapButton.mapJSONFile));
        };
        indoormapButton.action = () -> {
            game.setScreen(new TacticsScreen(game, prevScreen, indoormapButton.mapJSONFile));
        };

        // set up button mapping
        Button[][] buttonMap = new Button[][] {{smallmapButton, bridgemapButton, cliffsmapButton, indoormapButton}, {null, null, null, null}};
        for (int r = 0; r < buttonMap.length; r++) {
            for (int c = 0; c < buttonMap[0].length; c++) {
                if (buttonMap[r][c] == null) continue;
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
        buttons = new Button[] {smallmapButton, bridgemapButton, /*canyonmapButton,*/ indoormapButton, cliffsmapButton};
        selectedButton = smallmapButton;
        cursorPos = new Vector3(selectedButton.pos);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        GlyphLayout layout = new GlyphLayout(BattlePracticeAssets.FONT_BATTLE_PORTRAIT, "SELECT A MAP!");
        batch.begin();

        batch.draw(BattlePracticeAssets.TEXTURE_BG, 0, 0);
        BattlePracticeAssets.FONT_BATTLE_PORTRAIT.draw(batch, layout, INTERNAL_WIDTH/2 - layout.width/2, Settings.INTERNAL_HEIGHT - 50);

        // draw controls
        batch.draw(game.controller.button1Sprite(), inputHintX, inputHintY + 2 * inputHintLineHeight);
        hintFont.draw(batch, "Confirm", inputHintX + game.controller.button1Sprite().getRegionWidth() + 2, inputHintY + 2 * inputHintLineHeight + 8);

        batch.draw(game.controller.button2Sprite(), inputHintX, inputHintY + inputHintLineHeight);
        hintFont.draw(batch, "Back", inputHintX + game.controller.button1Sprite().getRegionWidth() + 2, inputHintY + 1 * inputHintLineHeight + 8);

        batch.draw(game.controller.directionalSprite(), inputHintX, inputHintY);
        hintFont.draw(batch, "Select", inputHintX + game.controller.directionalSprite().getRegionWidth() + 2, inputHintY + 8);

        for (Button button : buttons) {
            button.render(batch);
        }
        cursorSprite.draw(batch, cursorPos.x, cursorPos.y, selectedButton.width, selectedButton.height);

        batch.end();

        // set button/cursor selection logic
        selectedButton.setNormal();
        if (game.controller.leftJustPressed() && selectedButton.left != null) selectedButton = selectedButton.left;
        if (game.controller.rightJustPressed() && selectedButton.right != null) selectedButton = selectedButton.right;
        if (game.controller.upJustPressed() && selectedButton.up != null) selectedButton = selectedButton.up;
        if (game.controller.downJustPressed() && selectedButton.down != null) selectedButton = selectedButton.down;
        selectedButton.setHovered();

        if (game.controller.button1JustPressed()) {
            selectedButton.click();
        }
        if (game.controller.button2JustPressed() || game.controller.endJustPressed()) {
            this.previousScreen();
        }

        cursorPos.slerp(selectedButton.pos, 0.3f);
    }

    private class MapButton extends TexturedButton {
        public String mapJSONFile;
        private GlyphLayout layout;

        public MapButton(String mapJSONFile, float x, float y, Runnable action) {
            super(x, y, BattlePracticeAssets.TEXTURE_SQUARE, action);
            this.mapJSONFile = mapJSONFile;
            Json json = new Json();
            String name = json.fromJson(TacticsMap.class, Gdx.files.internal(mapJSONFile)).title;
            this.layout = new GlyphLayout(BattlePracticeAssets.FONT_BATTLE_PORTRAIT, name);
        }

        @Override
        public void render(SpriteBatch batch) {
            super.render(batch);
//            batch.draw(unitIcon, pos.x + 4, pos.y + 4);
            BattlePracticeAssets.FONT_BATTLE_PORTRAIT.draw(batch, layout, pos.x + this.normalTexture.getRegionWidth() / 2f - layout.width / 2f, pos.y + layout.height + 4);
        }
    }
    @Override
    public void show() {
        super.show();
        game.playMusic("music/old-city-theme.ogg");
    }
}
