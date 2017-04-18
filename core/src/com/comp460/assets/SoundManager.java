package com.comp460.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Belinda on 3/24/17.
 */
public class SoundManager {
    public static final Sound cursorMoveSound = Gdx.audio.newSound(Gdx.files.internal("sound/click2.wav"));
    public static final Sound selectionSound = Gdx.audio.newSound(Gdx.files.internal("sound/selection_click.ogg"));
    public static final Sound arrowSound = Gdx.audio.newSound(Gdx.files.internal("sound/swing.wav"));
    public static final Sound pickupSound = Gdx.audio.newSound(Gdx.files.internal("sound/vial.wav"));
    public static final Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("sound/hit.ogg"));
    public static final Sound monsterHurtSound = Gdx.audio.newSound(Gdx.files.internal("sound/monster_hurt.ogg"));
    public static final Sound monsterFallenSound = Gdx.audio.newSound(Gdx.files.internal("sound/monster_death.ogg"));
    public static final Sound failSound = Gdx.audio.newSound(Gdx.files.internal("sound/fail.wav"));
    public static final Sound battleTransition = Gdx.audio.newSound(Gdx.files.internal("sound/battle-transition.wav"));
    public static final Sound impactSound = Gdx.audio.newSound(Gdx.files.internal("sound/impact.wav"));
    public static final Sound metalImpactSound = Gdx.audio.newSound(Gdx.files.internal("sound/metal-impact.wav"));
    public static final Sound shieldSound = Gdx.audio.newSound(Gdx.files.internal("sound/magicshield.wav"));
    public static final Sound boom = Gdx.audio.newSound(Gdx.files.internal("sound/atari_boom.wav"));
    public static final Sound block = Gdx.audio.newSound(Gdx.files.internal("sound/spring.wav"));

}
