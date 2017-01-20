package com.comp460;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Belinda on 1/16/17.
 */
public class BattleAttack {
    public int row, col;
    public int duration;
    public int damage;
    public Texture sprite;

    public BattleAttack(int row, int col, int duration, int damage, Texture sprite) {
        this.row = row;
        this.col = col;
        this.duration = duration;
        this.damage = damage;
        this.sprite = sprite;
    }
    public void update(){
        duration--;
    }
}
