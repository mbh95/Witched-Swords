package com.comp460.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Belinda on 3/24/17.
 */
public class SoundManager {
    public static final Sound cursorMoveClick = Gdx.audio.newSound(Gdx.files.internal("sound/click2.wav"));
    public static final Sound selectionClick = Gdx.audio.newSound(Gdx.files.internal("sound/selection_click.ogg"));

}
