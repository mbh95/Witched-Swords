package com.comp460.battle;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Belinda on 1/16/17.
 */
public class BattleUnit {
    public int col, row;
    public Texture sprite;
    public int maxHP, currHP;
    public boolean player;

    public BattleUnit(Texture sprite) {
        this.sprite = sprite;
        player = false;
    }
}
