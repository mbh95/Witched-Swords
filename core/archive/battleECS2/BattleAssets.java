package com.comp460.screens.battleECS2;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;

/**
 * Created by matthewhammond on 2/25/17.
 */
public class BattleAssets {
    static class Textures {
        public static final TextureRegion BACKGROUND = SpriteManager.BATTLE.findRegion("bg/plains");
        public static final TextureRegion TILE_LHS = SpriteManager.BATTLE.findRegion("bg/tile_plains_blue");
        public static final TextureRegion TILE_RHS = SpriteManager.BATTLE.findRegion("bg/tile_plains_red");

        public static final TextureRegion TILE_LHS_SIDE = SpriteManager.BATTLE.findRegion("bg/tile_plains_blue_side");
        public static final TextureRegion TILE_RHS_SIDE = SpriteManager.BATTLE.findRegion("bg/tile_plains_red_side");

        public static final TextureRegion HP_BAR = SpriteManager.BATTLE.findRegion("rendering/hp_bar_new");
    }

    public static class Fonts {
        public static BitmapFont greenFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.GREEN, Color.BLACK, 2);
        public static BitmapFont yellowFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.ORANGE, Color.BLACK, 2);
        public static BitmapFont redFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED, Color.BLACK, 2);

        public static BitmapFont timerFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED);
        public static BitmapFont resultsFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 32, Color.WHITE, Color.BLACK, 2);
        public static BitmapFont continueFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 16, Color.WHITE);

        public static BitmapFont hpFont = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.WHITE);
        public static BitmapFont movesFont = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.WHITE);

    }
}
