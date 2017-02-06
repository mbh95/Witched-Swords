package com.comp460.archive.battle;

import com.badlogic.gdx.graphics.Texture;
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
    private boolean idead = false;

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
        currNRG = 500;
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
        if (idead) return;
        this.currentAnimation = attackSprites;
        this.currentAnimationIndex = 0;
    }
    public void startHurtAnimation() {
        this.currentAnimation = hurtSprites;
        this.currentAnimationIndex = 0;
    }
    public void startFallenAnimation() {
        this.currentAnimation = fallenSprites;
        this.currentAnimationIndex = 0;
        idead= true;
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

            if (currentAnimation==idleChangeSprites || currentAnimation==idleSprites)
                currentAnimationIndex = (currentAnimationIndex+1) % currentAnimation.length;
            else if (currentAnimation == fallenSprites) {
                if (currentAnimationIndex != currentAnimation.length-1)
                    currentAnimationIndex++;
            }
            else {
                currentAnimationIndex = (currentAnimationIndex+1);
                if (currentAnimationIndex == currentAnimation.length && currentAnimation != fallenSprites)
                    startIdleAnimation();
            }
        }
    }

    public Texture getSprite() {
        return currentAnimation[currentAnimationIndex];
    }
}
