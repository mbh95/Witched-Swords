package com.comp460.screens.battle.units.enemies.baddie.moves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.assets.SpriteManager;
import com.comp460.screens.battle.BattleAnimation;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.enemies.baddie.Baddie;

import java.util.Random;

/**
 * Created by Belinda on 4/10/2017.
 */
public class Vines extends BattleUnitAbility {

    public Baddie baddie;
    public static Animation<TextureRegion> vinesAnim = BattleAnimationManager.getBattleAnimation("attacks/vines");
    Random rng = new Random();

    public Vines(Baddie baddie) {
        super("vines", "Ensnare", "attack", "Vines ensnare the opponent.");
        this.baddie = baddie;
    }

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curEnergy >= 1;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        int col = rng.nextInt(3);
        VinesInstance vinesInstance = new VinesInstance(baddie.curRow, col, 2.0f, 10, baddie);
//        user.removeEnergy(1);
        baddie.vines.add(vinesInstance);
    }

    public class VinesInstance {
        public float warningTimer;
        public float timer;
        public boolean doneDamage = false;
        public int damageAmt;
        private Baddie baddie;
        public int row, col;
        public boolean shown = false;

        public VinesInstance(int row, int col, float duration, int damage, Baddie baddie) {
            this.row = row;
            this.col = col;
            this.warningTimer = 0.25f;
            this.timer = duration;

            this.doneDamage = false;
            this.baddie = baddie;
            this.damageAmt = damage;

        }

        public void update(BattleScreen screen, BattleUnit owner, float delta) {

            float x = screen.colToScreenX(baddie.curRow, col);
            float y = screen.rowToScreenY(baddie.curRow, col);
            if (warningTimer > 0) {
                warningTimer -= delta;
                return;
            } else if (!shown){
                screen.addAnimation(new BattleAnimation(vinesAnim, x, y, 2.0f));
                shown = true;
            }

            BattleUnit opponent = screen.p2Unit;
            if (opponent == owner) {
                opponent = screen.p1Unit;
            }
            timer -= delta;

            if (timer <= 0) {
                opponent.rooted.remove(this);
                return;
            }

            if (opponent.curCol == col && opponent.curRow == row && !doneDamage) {
                float dealt = opponent.applyDamage(new DamageVector(damageAmt, baddie));
                doneDamage = true;
                opponent.rooted.add(this);
            }
        }
    }
}
