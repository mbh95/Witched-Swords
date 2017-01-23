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
        public static Texture BATTLE_BG;
        public static Texture CURSOR;

        public static Texture BULBA_MICRO_1;
        public static Texture BULBA_MICRO_2;
        public static Texture BULBA_IDLE1_BATTLE;
        public static Texture BULBA_IDLE2_BATTLE;
        public static Texture BULBA_IDLE3_BATTLE;
        public static Texture BULBA_IDLE4_BATTLE;

        public static Texture ACTION_MENU;
        public static Texture BATTLE_TILE;
        public static Texture HP_BAR;
        public static Texture MEGA;
        public static Texture SCRATCH;
        public static Texture LAZER;
        public static Texture ROGUE;

        public static Texture ROGUE_MICRO_1;
        public static Texture ROGUE_MICRO_2;
    }

    public static class Maps {
        public static TiledMap TEST;
    }

    public static void load() {
        Textures.CURSOR = new Texture(Gdx.files.internal("cursor.png"));

        Textures.BULBA_MICRO_1 = new Texture(Gdx.files.internal("bulba_micro.png"));
        Textures.BULBA_MICRO_2 = new Texture(Gdx.files.internal("bulba_micro2.png"));
        TextureRegion[] BULBA_MICRO_ANIM = new TextureRegion[] {new TextureRegion(Textures.BULBA_MICRO_1), new TextureRegion(Textures.BULBA_MICRO_2)};
        animLookup.put("bulba_micro", BULBA_MICRO_ANIM);

        Textures.ROGUE_MICRO_1 = new Texture(Gdx.files.internal("ruby.png"));
        Textures.ROGUE_MICRO_2 = new Texture(Gdx.files.internal("ruby2.png"));
        TextureRegion[] ROGUE_MICRO_ANIM = new TextureRegion[] {new TextureRegion(Textures.ROGUE_MICRO_1), new TextureRegion(Textures.ROGUE_MICRO_2)};
        animLookup.put("rogue_micro", ROGUE_MICRO_ANIM);

        Textures.BULBA_IDLE1_BATTLE = new Texture(Gdx.files.internal("bulba/bulba1_battle.png"));
        Textures.BULBA_IDLE2_BATTLE = new Texture(Gdx.files.internal("bulba/bulba2_battle.png"));
        Textures.BULBA_IDLE3_BATTLE = new Texture(Gdx.files.internal("bulba/bulba3_battle.png"));
        Textures.BULBA_IDLE4_BATTLE = new Texture(Gdx.files.internal("bulba/bulba4_battle.png"));
        Textures.CURSOR = new Texture(Gdx.files.internal("cursor.png"));
        Textures.ACTION_MENU = new Texture(Gdx.files.internal("action_menu.png"));
        Textures.BATTLE_TILE = new Texture(Gdx.files.internal("battle_tile.png"));
        Textures.BATTLE_BG = new Texture(Gdx.files.internal("plains_blur.png"));
        Textures.HP_BAR = new Texture(Gdx.files.internal("hp_bar.png"));
        Textures.MEGA = new Texture(Gdx.files.internal("megadude.png"));
        Textures.SCRATCH = new Texture(Gdx.files.internal("scratch.png"));
        Textures.LAZER = new Texture(Gdx.files.internal("lazer.png"));
        Textures.ROGUE = new Texture(Gdx.files.internal("ruby_macro.png"));
        Maps.TEST = new TmxMapLoader().load("testmap.tmx");
    }

    public static void dispose() {
    }
}
