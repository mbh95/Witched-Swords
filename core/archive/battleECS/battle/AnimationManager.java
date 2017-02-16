package com.comp460.battle;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.comp460.common.SpriteManager;


/**
 * Created by matth on 2/14/2017.
 */
public class AnimationManager {
    public static final String defaultAnimID = "idle";
    public static final String animBase = "units/%s/%s_%s";

    public static String getAnimString(String unitID, String animID) {
        return String.format(animBase, unitID, unitID, animID);
    }

    public static Animation<TextureRegion> getUnitAnimation(String unitID, String animID) {
        Array<TextureAtlas.AtlasRegion> frames = SpriteManager.BATTLE.findRegions(getAnimString(unitID, animID));
        if (frames.size == 0) {
            frames = SpriteManager.BATTLE.findRegions(getAnimString(unitID, defaultAnimID));
        }
        Animation<TextureRegion> anim = new Animation<TextureRegion>(0.25f, frames);
        if (animID.equals(defaultAnimID)) {
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }
        return anim;
    }
}
