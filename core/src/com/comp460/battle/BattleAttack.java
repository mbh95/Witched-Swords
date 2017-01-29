package com.comp460.battle;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Belinda on 1/16/17.
 */
public class BattleAttack {
    public int row, col;
    public int duration;
    public Texture sprite;
    public BattleEffect effect;
    public BattleUnit attacker;

    public BattleAttack(int row, int col, int duration, BattleUnit attacker, Texture sprite, BattleEffect effect) {
        this.row = row;
        this.col = col;
        this.duration = duration;
        this.effect = effect;
        this.sprite = sprite;
        this.attacker = attacker;
    }
    public void update(){
        this.effect.tick(this);
        duration--;
    }
}
