package com.comp460.battle;

import com.badlogic.gdx.graphics.Texture;
import com.comp460.Assets;

/**
 * Created by Belinda on 1/16/17.
 */
public class BattleUnit {
    public int col, row;
    private Texture idleSprites[], idleChangeSprites[], attackSprites[], hurtSprites[], fallenSprites[];
    private int idleIndex, idleChangeIndex, attackIndex, hurtIndex, fallenIndex;
    private Texture defaultSprite = Assets.Textures.LAZER;

    public int maxHP, currHP;
    public boolean player;
    public int moveDelay, castDelay, spriteDelay;

    public BattleUnit(Texture idleSprites[],
                      Texture idleChangeSprites[],
                      Texture attackSprites[],
                      Texture hurtSprites[],
                      Texture fallenSprites[]) {

        this.idleSprites = idleSprites;
        this.idleChangeSprites = idleChangeSprites;
        this.attackSprites = attackSprites;
        this.hurtSprites = hurtSprites;
        this.fallenSprites = fallenSprites;

        this.idleIndex = 0;
        player = false;
        moveDelay = 15;
        castDelay = 0;
        spriteDelay = 10;
    }

    public void updateSprite() {
        spriteDelay--;
        if (spriteDelay == 0) {
            spriteDelay = 15;
            idleIndex = (idleIndex+1) % idleSprites.length;
        }
    }

    public Texture getSprite() {return idleSprites[idleIndex];}
}
