package com.comp460.screens.battle.units.protagonists.zane.moves;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.screens.battle.BattleAnimation;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.DamageVector;

import com.comp460.screens.battle.units.protagonists.zane.Zane;

/**
 * Created by matthewhammond on 4/7/17.
 */
public class Slash extends BattleUnitAbility {

    public static Animation<TextureRegion> slashAnim = BattleAnimationManager.getBattleAnimation("attacks/slash");

    public Zane zane;

    public Slash(Zane zane) {
        super("slash", "Slash", "attack", "A quick and strong sword slash spanning an entire row on the enemy side.");
        this.zane = zane;
    }

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curEnergy >= 1;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        SlashInstance slashInstance = new SlashInstance(zane.curRow, zane.curCol + 1, .2f, 10, zane);
        user.removeEnergy(2);
        zane.slashes.add(slashInstance);
        screen.addAnimation(new BattleAnimation(slashAnim, screen.colToScreenX(zane.curRow, zane.curCol + 1), screen.rowToScreenY(zane.curRow, zane.curCol + 1), 0.2f));
    }

    public class SlashInstance {
        public float timer;
        public boolean doneDamage = false;
        public int damageAmt;
        private Zane zane;
        public int row, col;

        public SlashInstance(int row, int col, float duration, int damage, Zane zane) {
            this.row = row;
            this.col = col;
            this.timer = duration;
            this.doneDamage = false;
            this.zane = zane;
            this.damageAmt = damage;

        }

        public void update(BattleScreen screen, BattleUnit owner, float delta) {
            BattleUnit opponent = screen.p2Unit;
            if (opponent == owner) {
                opponent = screen.p1Unit;
            }
            timer -= delta;

            if (timer <= 0) {
                return;
            }

            if (opponent.curRow == row && !doneDamage) {
                float dealt = opponent.applyDamage(new DamageVector(damageAmt, zane));
                doneDamage = true;
            }
        }
    }
}


