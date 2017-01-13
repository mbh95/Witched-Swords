package com.comp460;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Created by matthewhammond on 1/12/17.
 */
public class Assets {

    public static TiledMap testMap;
    public static void load() {
        testMap = new TmxMapLoader().load("testmap.tmx");
    }

    public static void dispose() {

    }
}
