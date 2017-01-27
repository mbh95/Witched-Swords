package com.comp460.battle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.Assets;

/**
 * Created by Belinda on 1/16/17.
 */
public class BattleUnit {
    public int col, row;
    private Texture idleSprites[], idleChangeSprites[], attackSprites[], hurtSprites[], fallenSprites[];
    private int idleIndex, idleChangeIndex, idleCycles, attackIndex, hurtIndex, fallenIndex;
    private Texture defaultSprite = Assets.Textures.LAZER;
    private Texture[] currentAnimation;
    private int currentAnimationIndex;

    public int maxHP, currHP, currNRG;
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
        this.idleChangeIndex = 0;
        this.idleCycles = 0;
        player = false;
        moveDelay = 15;
        castDelay = 0;
        spriteDelay = 10;
        currNRG = 5;
    }

    public void startIdleAnimation() {
        this.currentAnimation = idleSprites;
        this.currentAnimationIndex = 0;
    }
    private void startIdleChangeAnimation() {
        this.currentAnimation = idleChangeSprites;
        this.currentAnimationIndex = 0;
    }
    public void startAttackAnimation() {
        this.currentAnimation = attackSprites;
        this.currentAnimationIndex = 0;
    }

    public void updateSprite() {
        spriteDelay--;
        if (spriteDelay == 0) {
            spriteDelay = 10;

            // switch to idle change sprites if enough cycles pass
            // switch to idle sprites after one cycle of idle change sprites
            if (currentAnimation == idleSprites) {
                if (currentAnimationIndex == currentAnimation.length-1) {idleCycles++;} // done with 1 cycle
                if (idleCycles == 4) {startIdleChangeAnimation(); idleCycles = 0;}
            } else if (currentAnimation == idleChangeSprites) {
                if (currentAnimationIndex == currentAnimation.length-1) {idleCycles++;} // done with 1 cycle
                if (idleCycles == 1) {startIdleAnimation(); idleCycles = 0;}
            }
            currentAnimationIndex = (currentAnimationIndex+1) % currentAnimation.length;
        }
    }

    public Texture getSprite() {
        return currentAnimation[currentAnimationIndex];
    }
}
