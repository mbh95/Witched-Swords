package com.comp460.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;


/**
 * Created by matth on 2/14/2017.
 */
public class BattleAnimationManager {
    public static final String defaultBattleAnimID = "idle";
    public static final String battleAnimBase = "units/%s/%s_%s";

    public static String getAnimString(String unitID, String animID) {
        return String.format(battleAnimBase, unitID, unitID, animID);
    }

    public static Animation<TextureRegion> getBattleUnitAnimation(String unitID, String animID) {
        Array<TextureAtlas.AtlasRegion> frames = SpriteManager.BATTLE.findRegions(getAnimString(unitID, animID));
        if (frames.size <= 0) {
            frames = SpriteManager.BATTLE.findRegions(getAnimString(unitID, defaultBattleAnimID));
        }
        Animation<TextureRegion> anim = new Animation<TextureRegion>(0.25f, frames);
        if (animID.equals(defaultBattleAnimID)) {
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }
        return anim;
    }

    public static Animation<TextureRegion> getBattleAnimation(String path) {
        Array<TextureAtlas.AtlasRegion> frames = SpriteManager.BATTLE.findRegions(path);
        if (frames.size <= 0) {
            frames = SpriteManager.BATTLE.findRegions("missing");
            System.out.println("loading missing");
        }
        Animation<TextureRegion> anim = new Animation<TextureRegion>(0.25f, frames);
        anim.setPlayMode(Animation.PlayMode.LOOP);
        return anim;
    }
}
