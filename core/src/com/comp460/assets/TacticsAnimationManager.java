package com.comp460.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by matthewhammond on 2/21/17.
 */
public class TacticsAnimationManager {
    public static final String defaultTacticsAnimID = "idle";
    public static final String tacticsAnimBase = "units/%s/%s_%s";

    public static String getAnimString(String unitID, String animID) {
        return String.format(tacticsAnimBase, unitID, unitID, animID);
    }

    public static Animation<TextureRegion> getTacticsAnimation(String unitID, String animID) {
        Array<TextureAtlas.AtlasRegion> frames = SpriteManager.TACTICS.findRegions(getAnimString(unitID, animID));
        if (frames.size <= 0) {
            frames = SpriteManager.TACTICS.findRegions(getAnimString(unitID, defaultTacticsAnimID));
        }
        Animation<TextureRegion> anim = new Animation<TextureRegion>(0.25f, frames);
        if (animID.equals(defaultTacticsAnimID)) {
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }
        return anim;
    }

    public static Animation<TextureRegion> getTacticsAnimationAnimation(String path) {
        Array<TextureAtlas.AtlasRegion> frames = SpriteManager.TACTICS.findRegions(path);
        if (frames.size <= 0) {
            frames = SpriteManager.TACTICS.findRegions("missing");
            System.out.println("loading missing");
        }
        Animation<TextureRegion> anim = new Animation<TextureRegion>(0.25f, frames);
        anim.setPlayMode(Animation.PlayMode.LOOP);
        return anim;
    }
}
