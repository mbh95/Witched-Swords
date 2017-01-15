package com.comp460;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Created by matthewhammond on 1/12/17.
 */
public class Assets {

    public static TiledMap testMap;
    public static Texture bulbaImage;
    public static Texture cursor;
    public static void load() {
        testMap = new TmxMapLoader().load("testmap.tmx");
        bulbaImage = new Texture(Gdx.files.internal("bulba_micro.png"));
        cursor = new Texture(Gdx.files.internal("cursor.png"));
    }

    public static void dispose() {

    }
}
