package com.comp460.screens.tactics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;

/**
 * Created by matth on 2/15/2017.
 */
public class TacticsAssets {
    public static final TextureRegion CURSOR = SpriteManager.TACTICS.findRegion("ui/cursor");

    public static final TextureRegion HOVER_PLAYER = SpriteManager.TACTICS.findRegion("ui/player-hover");
    public static final TextureRegion HOVER_AI = SpriteManager.TACTICS.findRegion("ui/enemy-hover");

    public static final TextureRegion TURN_PLAYER = SpriteManager.TACTICS.findRegion("ui/player-turn");
    public static final TextureRegion TURN_AI = SpriteManager.TACTICS.findRegion("ui/enemy-turn");

    public static final TextureRegion ACTION_ROW = SpriteManager.TACTICS.findRegion("ui/action-menu-line");
    public static final TextureRegion ACTION_ROW_SELECTED = SpriteManager.TACTICS.findRegion("ui/action-menu-line-selected");

    public static final BitmapFont FONT_HELP_ITEM = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.WHITE, Color.BLACK, 1);


}
