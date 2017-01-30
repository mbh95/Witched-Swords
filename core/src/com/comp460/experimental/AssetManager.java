package com.comp460.experimental;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by matthewhammond on 1/12/17.
 */
public class AssetManager {

    public enum BattleAnimation {IDLE("_idle"), ATTACK("_attack"), HURT("_hurt"), FALLEN("_fallen");
        private String animId;
        private BattleAnimation(String id) {
            this.animId = id;
        }
        public String getAnimId() {
            return this.animId;
        }
    }

    private static Map<String, Animation<TextureRegion>> idAnimLookup = new HashMap<>();

    public static class Textures {

        public static Texture BATTLE_BG;

        public static Texture ACTION_MENU;
        public static TextureRegion BATTLE_TILE_BLUE;
        public static TextureRegion BATTLE_TILE_RED;

        public static Texture ENERGY;
        public static Texture HP_BAR;
        public static Texture MEGA;
        public static Texture SCRATCH;
        public static Texture LAZER;

        public static Texture ROGUE_MICRO_1;
        public static Texture ROGUE_MICRO_2;
        public static Texture ROGUE; // pls rename later
        public static Texture ROGUE1;
        public static Texture ROGUE2;
    }

    public static class Maps {
        public static TiledMap TEST;
    }

    public static void load() {

        loadBattleAnim("bulba", BattleAnimation.IDLE, 4);
        loadBattleAnim("bulba", BattleAnimation.ATTACK, 1);

        loadBattleAnim("ruby", BattleAnimation.IDLE, 3);

        Texture battleTilesTexture = new Texture(Gdx.files.internal("battle/sprites/ui/battle_tiles_plains.png"));
        Textures.BATTLE_TILE_BLUE = new TextureRegion(battleTilesTexture, battleTilesTexture.getWidth()/2, battleTilesTexture.getWidth()/2);
        Textures.BATTLE_TILE_RED = new TextureRegion(battleTilesTexture, battleTilesTexture.getWidth()/2, 0, battleTilesTexture.getWidth()/2, battleTilesTexture.getWidth()/2);

        Textures.BATTLE_BG = new Texture(Gdx.files.internal("battle/sprites/backgrounds/plains.png"));
        Textures.HP_BAR = new Texture(Gdx.files.internal("battle/sprites/ui/hp_bar.png"));
        Textures.ENERGY = new Texture(Gdx.files.internal("battle/sprites/ui/energy.png"));
        Textures.SCRATCH = new Texture(Gdx.files.internal("battle/sprites/attacks/scratch.png"));
        Textures.LAZER = new Texture(Gdx.files.internal("battle/sprites/attacks/lazer.png"));
//        Textures.ROGUE = new Texture(Gdx.files.internal("ruby_idle_0.png"));
//        Textures.ROGUE1 = new Texture(Gdx.files.internal("ruby_idle_0.png"));
//        Textures.ROGUE2 = new Texture(Gdx.files.internal("ruby_idle_1.png"));
//        Maps.TEST = new TmxMapLoader().load("testmap.tmx");
    }

    public static void loadBattleAnim(String unitId, BattleAnimation animId, int frames) {
        TextureRegion[] frameArray = new TextureRegion[frames];
        for (int i = 0; i < frames; i++) {
            frameArray[i] = new TextureRegion(new Texture(Gdx.files.internal("battle/sprites/units/"+unitId+ animId.getAnimId() + "_" + i + ".png")));
        }
        idAnimLookup.put(unitId+animId.getAnimId(), new Animation<>(0.25f, frameArray));
    }


    public static void dispose() {
    }

    public static Animation<TextureRegion> getAnimation(String id, BattleAnimation anim) {
        Animation result = idAnimLookup.get(id + anim.getAnimId());
        if (result == null) {
//            loadBattleAnim(id, anim, 1);
            return idAnimLookup.get(id + BattleAnimation.IDLE.getAnimId());
        }
        return result;
    }
}
