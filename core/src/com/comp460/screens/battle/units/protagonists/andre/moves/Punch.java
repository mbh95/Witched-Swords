package com.comp460.screens.battle.units.protagonists.andre.moves;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.screens.battle.BattleAnimation;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.protagonists.andre.Andre;

/**
 * Created by Belinda on 2/17/17.
 */
public class Punch extends BattleUnitAbility {
    public Punch(Andre andre) {
        super("smash", "Smash!", "attack", "A rocket powered punch.");
        this.animTimer = 0;
        this.andre = andre;
    }
    public static Animation<TextureRegion> punch = BattleAnimationManager.getBattleAnimation("attacks/punch");
    public float animTimer;
    public int duration = 15;
    private Andre andre;
    private int row, col;

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curEnergy >= 1 && andre.curCol == 2;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        this.row = andre.curRow;
        this.col = andre.curCol+1;
        user.curEnergy -= 1;
        screen.addAnimation(new BattleAnimation(punch, user.transform.x+40, user.transform.y, 0.2f));
        andre.punches.add(this);
    }

    public void update(BattleScreen screen, BattleUnit owner, float delta) {
        BattleUnit opponent = screen.p2Unit;
        if (opponent == owner) {
            owner = screen.p1Unit;
        }
        duration--;

        if (opponent.curRow == row && opponent.curCol == col) {
            opponent.applyDamage(new DamageVector(20));
            System.out.println("damage");
        }
    }

    public void render(SpriteBatch batch, BattleScreen screen) {
        batch.draw(punch.getKeyFrame(animTimer), screen.colToScreenX(row, col), screen.rowToScreenY(row, col));
    }
}
