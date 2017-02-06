package com.comp460.archive.battle;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Belinda on 1/16/17.
 */
public class BattleAttack {
    public int row, col;
    public int duration;
    public int warning;
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
        warning = 20;
    }
    public void update(){
        if (warning > 0) {
            warning--;
            return;
        }
        this.effect.tick(this);
        duration--;
    }
}
