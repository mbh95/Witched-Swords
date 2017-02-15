package com.comp460.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by matth on 2/14/2017.
 */
public class SpriteManager {
    public static final TextureAtlas BATTLE = new TextureAtlas(Gdx.files.internal("sprites/battle.atlas"));

    public static final TextureAtlas BATTLE_PRACTICE = new TextureAtlas(Gdx.files.internal("sprites/battle_practice.atlas"));

    public static final TextureAtlas COMMON = new TextureAtlas(Gdx.files.internal("sprites/common.atlas"));

    public static final TextureAtlas MAIN_MENU = new TextureAtlas(Gdx.files.internal("sprites/main_menu.atlas"));

    public static final TextureAtlas SPLASH = new TextureAtlas(Gdx.files.internal("sprites/splash.atlas"));

    public static final TextureAtlas TACTICS = new TextureAtlas(Gdx.files.internal("sprites/tactics.atlas"));
}
