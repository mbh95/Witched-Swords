package com.comp460;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by matthewhammond on 1/12/17.
 */
public class Assets {

    public static Map<String, TextureRegion[]> animLookup = new HashMap<>();

    public static class Textures {
        public static TextureRegion BATTLE_TILE_RED;
        public static TextureRegion BATTLE_TILE_BLUE;
        public static Texture BATTLE_BG;
        public static Texture CURSOR;

        public static Texture BULBA_MICRO_1;
        public static Texture BULBA_MICRO_2;
        public static Texture BULBA_IDLE0_BATTLE;
        public static Texture BULBA_IDLE1_BATTLE;
        public static Texture BULBA_IDLE2_BATTLE;
        public static Texture BULBA_IDLE3_BATTLE;
        public static Texture BULBA_IDLE4_BATTLE;
        public static Texture BULBA_ATTACK0_BATTLE;
        public static Texture BULBA_HURT0_BATTLE;
        public static Texture BULBA_FALLEN0_BATTLE;
        public static Texture BULBA_FALLEN1_BATTLE;

        public static Texture ACTION_MENU;
        public static Texture BATTLE_TILE;
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
        Textures.CURSOR = new Texture(Gdx.files.internal("tactics/sprites/rendering/cursor.png"));

        Textures.BULBA_MICRO_1 = new Texture(Gdx.files.internal("tactics/sprites/units/bulba.png"));
        Textures.BULBA_MICRO_2 = new Texture(Gdx.files.internal("tactics/sprites/units/bulba_idle_1.png"));
        TextureRegion[] BULBA_MICRO_ANIM = new TextureRegion[] {new TextureRegion(Textures.BULBA_MICRO_1), new TextureRegion(Textures.BULBA_MICRO_2)};
        animLookup.put("bulba_tactics_idle", BULBA_MICRO_ANIM);

        Textures.ROGUE_MICRO_1 = new Texture(Gdx.files.internal("tactics/sprites/units/ruby.png"));
        Textures.ROGUE_MICRO_2 = new Texture(Gdx.files.internal("tactics/sprites/units/ruby.png"));
        TextureRegion[] ROGUE_MICRO_ANIM = new TextureRegion[] {new TextureRegion(Textures.ROGUE_MICRO_1), new TextureRegion(Textures.ROGUE_MICRO_2)};
        animLookup.put("rogue_micro", ROGUE_MICRO_ANIM);

//        Textures.BULBA_IDLE0_BATTLE = new Texture(Gdx.files.internal("battle/sprites/units/bulba1_idle.png"));
//        Textures.BULBA_IDLE1_BATTLE = new Texture(Gdx.files.internal("battle/sprites/units/bulba_idle_0.png"));
//        Textures.BULBA_IDLE2_BATTLE = new Texture(Gdx.files.internal("battle/sprites/units/bulba_idle_1.png"));
//        Textures.BULBA_IDLE3_BATTLE = new Texture(Gdx.files.internal("battle/sprites/units/bulba_idle_2.png"));
//        Textures.BULBA_IDLE4_BATTLE = new Texture(Gdx.files.internal("battle/sprites/units/bulba_idle_3.png"));
//        Textures.BULBA_ATTACK0_BATTLE = new Texture(Gdx.files.internal("battle/sprites/units/bulba_attack_0.png"));
//        Textures.BULBA_HURT0_BATTLE = new Texture(Gdx.files.internal("battle/sprites/units/bulba_hurt_0.png"));
//        Textures.BULBA_FALLEN0_BATTLE = new Texture(Gdx.files.internal("battle/sprites/units/bulba_fallen_0.png"));
//        Textures.BULBA_FALLEN1_BATTLE = new Texture(Gdx.files.internal("battle/sprites/units/bulba_fallen_1.png"));

        Textures.ACTION_MENU = new Texture(Gdx.files.internal("tactics/sprites/rendering/action_menu.png"));
//        Texture tempTiles = new Texture(Gdx.files.internal("battle/sprites/rendering/battle_tiles_plains.png"));
//        Textures.BATTLE_TILE = new Texture(Gdx.files.internal("battle/sprites/rendering/battle_tile.png"));
//        Textures.BATTLE_TILE_BLUE = new TextureRegion(tempTiles, 0,0,tempTiles.getWidth()/2, tempTiles.getHeight());
//        Textures.BATTLE_TILE_RED = new TextureRegion(tempTiles, tempTiles.getWidth()/2, 0, tempTiles.getWidth()/2, tempTiles.getHeight());
//        Textures.BATTLE_BG = new Texture(Gdx.files.internal("battle/sprites/backgrounds/plains.png"));
//        Textures.HP_BAR = new Texture(Gdx.files.internal("battle/sprites/rendering/hp_bar.png"));
//        Textures.ENERGY = new Texture(Gdx.files.internal("battle/sprites/rendering/energy.png"));
//        Textures.MEGA = new Texture(Gdx.files.internal("battle/sprites/units/megadude.png"));
//        Textures.SCRATCH = new Texture(Gdx.files.internal("battle/sprites/attacks/scratch.png"));
//        Textures.LAZER = new Texture(Gdx.files.internal("battle/sprites/attacks/lazer.png"));
//        Textures.ROGUE = new Texture(Gdx.files.internal("battle/sprites/units/ruby.png"));
//        Textures.ROGUE1 = new Texture(Gdx.files.internal("battle/sprites/units/ruby_idle_1.png"));
//        Textures.ROGUE2 = new Texture(Gdx.files.internal("battle/sprites/units/ruby_idle_2.png"));
        Maps.TEST = new TmxMapLoader().load("tactics/maps/testmap.tmx");
    }

    public static void dispose() {
    }
}
