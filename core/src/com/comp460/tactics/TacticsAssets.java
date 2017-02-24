package com.comp460.tactics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
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


}
