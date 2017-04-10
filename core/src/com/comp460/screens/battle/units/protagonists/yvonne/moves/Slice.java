package com.comp460.screens.battle.units.protagonists.yvonne.moves;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.screens.battle.BattleAnimation;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.protagonists.yvonne.Yvonne;

/**
 * Created by matthewhammond on 4/7/17.
 */
public class Slice extends BattleUnitAbility {

    public static Animation<TextureRegion> sliceAnim = BattleAnimationManager.getBattleAnimation("attacks/slice");

    public Yvonne yvonne;

    public Slice(Yvonne yvonne) {
        super("slice", "Slice", "attack", "A bloodthirsty swing from a scythe spanning an entire column.");
        this.yvonne = yvonne;
    }

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curEnergy >= 1;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        SliceInstance sliceInstance = new SliceInstance(yvonne.curRow, yvonne.curCol + 3, .2f, 10, yvonne, true);
        user.removeEnergy(1);
        yvonne.slices.add(sliceInstance);
        screen.addAnimation(new BattleAnimation(sliceAnim, screen.colToScreenX(0, yvonne.curCol + 3), screen.rowToScreenY(0, yvonne.curCol + 3), 0.2f));
    }

    public class SliceInstance {
        public float timer;
        public boolean doneDamage = false;
        public int damageAmt;
        private Yvonne yvonne;
        public int row, col;
        public boolean lifesteal;

        public SliceInstance(int row, int col, float duration, int damage, Yvonne yvonne, boolean lifesteal) {
            this.row = row;
            this.col = col;
            this.timer = duration;
            this.doneDamage = false;
            this.yvonne = yvonne;
            this.lifesteal = lifesteal;
            this.damageAmt = yvonne.attackUpOn ? damage * 2 : damage;

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

            if (opponent.curCol == col && !doneDamage) {
                float dealt = opponent.applyDamage(new DamageVector(damageAmt, yvonne));
                yvonne.fillTranceMeter(-dealt);
                if (lifesteal) {
                    yvonne.applyDamage(new DamageVector((int) dealt, yvonne));
                }
                doneDamage = true;
            }
        }
    }
}


