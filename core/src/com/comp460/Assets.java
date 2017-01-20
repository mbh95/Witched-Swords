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
        public static Texture BULBA;
        public static TextureAtlas MENU;
    }

    public static class Maps {
        public static TiledMap TEST;
    }

    public static void load() {
        Textures.CURSOR = new Texture(Gdx.files.internal("cursor.png"));
        Textures.BULBA = new Texture(Gdx.files.internal("bulba_micro.png"));

        Maps.TEST = new TmxMapLoader().load("testmap.tmx");
    }

    public static void dispose() {
        Textures.CURSOR.dispose();
        Textures.BULBA.dispose();

        Maps.TEST.dispose();
    }
}
