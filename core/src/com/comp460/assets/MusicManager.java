package com.comp460.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Created by matthewhammond on 4/19/17.
 */
public class MusicManager {
    public static final Music MENU_THEME = Gdx.audio.newMusic(Gdx.files.internal("music/old-city-theme.ogg"));
    public static final Music TACTICS_THEME = Gdx.audio.newMusic(Gdx.files.internal("music/beeball.mp3"));
    public static final Music BATTLE_THEME = Gdx.audio.newMusic(Gdx.files.internal("music/battle-in-the-winter.mp3"));
    public static final Music VICTORY_THEME = Gdx.audio.newMusic(Gdx.files.internal("music/Victory.ogg"));
    public static final Music DEFEAT_THEME = Gdx.audio.newMusic(Gdx.files.internal("music/disconnected.ogg"));

    public static Music getMusicFromPath(String path) {
        switch (path) {
            case "music/old-city-theme.ogg":
                return MENU_THEME;
            case "music/beeball.mp3":
                return TACTICS_THEME;
            case "music/battle-in-the-winter.mp3":
                return BATTLE_THEME;
            case "music/Victory.ogg":
                return VICTORY_THEME;
            case "music/disconnected.ogg":
                return DEFEAT_THEME;
            default:
                return Gdx.audio.newMusic(Gdx.files.internal(path));
        }
    }
}
