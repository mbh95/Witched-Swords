package com.comp460.launcher.practice.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class BattlePracticeAssets {

    public static final BitmapFont FONT_BATTLE_PORTRAIT = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.WHITE);
    public static final BitmapFont FONT_FIGHT = FontManager.getFont(FontManager.KEN_VECTOR_FUTURE, 16, Color.WHITE);
    public static final BitmapFont FONT_VS = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 48, Color.RED);
    public static final BitmapFont FONT_INFO = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.WHITE);

    public static final TextureRegion TEXTURE_SQUARE = SpriteManager.BATTLE_PRACTICE.findRegion("battle-square");
    public static final TextureRegion TEXTURE_SQUARE_HOVERED = SpriteManager.BATTLE_PRACTICE.findRegion("battle-square-hovered");

    public static final TextureRegion TEXTURE_BACK_BUTTON  = SpriteManager.BATTLE_PRACTICE.findRegion("back-button");
    public static final TextureRegion TEXTURE_BACK_BUTTON_HOVERED = SpriteManager.BATTLE_PRACTICE.findRegion("back-button-hovered");

    public static final TextureRegion TEXTURE_BG = SpriteManager.COMMON.findRegion("bg");;
    public static final NinePatch NP_INFO_BG = new NinePatch(SpriteManager.BATTLE_PRACTICE.findRegion("info-bg-ns"), 2, 2, 2, 2);

    public static final TextureRegion TEXTURE_PLAYER_AREA = SpriteManager.BATTLE_PRACTICE.findRegion("player-area");
    public static final TextureRegion TEXTURE_AI_AREA = SpriteManager.BATTLE_PRACTICE.findRegion("ai-area");

    public static final TextureRegion TEXTURE_FIGHT_BUTTON = SpriteManager.BATTLE_PRACTICE.findRegion("fight-button");;
    public static final TextureRegion TEXTURE_FIGHT_BUTTON_HOVER = SpriteManager.BATTLE_PRACTICE.findRegion("fight-button-hover");
    public static final TextureRegion TEXTURE_i = SpriteManager.BATTLE_PRACTICE.findRegion("i");
}
