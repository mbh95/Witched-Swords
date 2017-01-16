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

    public static TiledMap testMap;
    public static Texture bulbaMicro;
    public static Texture bulbaMacro;
    public static Texture cursor;
    public static Texture actionMenu;
    public static Texture battleTile;
    public static Texture hpBar;
    public static Texture mega;
    public static Texture scratch;
    public static void load() {
        testMap = new TmxMapLoader().load("testmap.tmx");
        bulbaMicro = new Texture(Gdx.files.internal("bulba_micro.png"));
        bulbaMacro = new Texture(Gdx.files.internal("bulba_macro.png"));
        cursor = new Texture(Gdx.files.internal("cursor.png"));
        actionMenu = new Texture(Gdx.files.internal("action_menu.png"));
        battleTile = new Texture(Gdx.files.internal("battle_tile.png"));
        hpBar = new Texture(Gdx.files.internal("hp_bar.png"));
        mega = new Texture(Gdx.files.internal("megadude.png"));
        scratch = new Texture(Gdx.files.internal("scratch.png"));
    }

    public static void dispose() {

    }
}
