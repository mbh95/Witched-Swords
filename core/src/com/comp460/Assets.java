package com.comp460;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Created by matthewhammond on 1/12/17.
 */
public class Assets {

    public static class Textures {
        public static Texture CURSOR;

        public static Texture BULBA_MICRO;
        public static Texture BULBA_MACRO;

        public static Texture ACTION_MENU;
        public static Texture BATTLE_TILE;
        public static Texture HP_BAR;
        public static Texture MEGA;
        public static Texture SCRATCH;
        public static Texture LAZER;
    }

    public static class Maps {
        public static TiledMap TEST;
    }

    public static void load() {
        Textures.CURSOR = new Texture(Gdx.files.internal("cursor.png"));
        Textures.BULBA_MICRO = new Texture(Gdx.files.internal("bulba_micro.png"));

        Textures.BULBA_MACRO = new Texture(Gdx.files.internal("bulba_macro.png"));
        Textures.CURSOR = new Texture(Gdx.files.internal("cursor.png"));
        Textures.ACTION_MENU = new Texture(Gdx.files.internal("action_menu.png"));
        Textures.BATTLE_TILE = new Texture(Gdx.files.internal("battle_tile.png"));
        Textures.HP_BAR = new Texture(Gdx.files.internal("hp_bar.png"));
        Textures.MEGA = new Texture(Gdx.files.internal("megadude.png"));
        Textures.SCRATCH = new Texture(Gdx.files.internal("scratch.png"));
        Textures.LAZER = new Texture(Gdx.files.internal("lazer.png"));
        Maps.TEST = new TmxMapLoader().load("testmap.tmx");
    }

    public static void dispose() {
    }
}
