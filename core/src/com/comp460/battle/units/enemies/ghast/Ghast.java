package com.comp460.battle.units.enemies.ghast;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.AnimationManager;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.units.BattleUnit;
import com.comp460.battle.units.BattleUnitAbility;
import com.comp460.battle.units.DamageVector;
import com.comp460.common.GameUnit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by matth on 2/15/2017.
 */
public class Ghast extends BattleUnit {

    public static Animation<TextureRegion> puffAnimation = AnimationManager.getBattleAnimation("attacks/poof");
    public static final float cloudSpeed = 0.25f;

    public List<Puff.PuffCloud> clouds = new ArrayList<>();

    public Ghast(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);

        this.ability1 = new Puff(this);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        for (Iterator<Puff.PuffCloud> iter = clouds.iterator(); iter.hasNext();) {
            Puff.PuffCloud cloud = iter.next();
            cloud.update(screen, this, delta);
            if (cloud.col < 0) {
                iter.remove();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        super.render(batch, delta);
        for (Puff.PuffCloud cloud : clouds)  {
            cloud.render(batch, screen);
        }
    }
}

class Puff extends BattleUnitAbility {

    public Ghast ghast;
    public Random rng = new Random();

    public Puff(Ghast ghast) {
        super("puff", "Noxious Fumes", "attack", "Release toxic fumes that damage enemies over time and restore user's health.");
        this.ghast = ghast;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        user.applyDamage(new DamageVector(10));

        int gap = rng.nextInt(screen.numRows);

        for (int r = 0; r < screen.numRows; r++) {
            if (r == gap) {
                continue;
            }
            ghast.clouds.add(new PuffCloud(r, user.curCol));
        }

    }

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curHP > 10;
    }

    class PuffCloud {

        public float animTimer;
        public int row;
        public int col;
        public float moveTimer = Ghast.cloudSpeed;

        public PuffCloud(int row, int col) {
            this.row = row;
            this.col = col;
            this.animTimer = 0;
        }

        public void update(BattleScreen screen, BattleUnit owner, float delta) {
            moveTimer -= delta;
            if (moveTimer <= 0) {
                moveTimer = Ghast.cloudSpeed;
                this.col -= 1;
            }

            BattleUnit opponent = screen.p1Unit;
            if (opponent == owner) {
                owner = screen.p2Unit;
            }

            if (opponent.curRow == row && opponent.curCol == col) {
                float damageDealt = opponent.applyDamage(new DamageVector(1));
                owner.applyDamage(new DamageVector((int)(2*damageDealt)));
            }
        }

        public void render(SpriteBatch batch, BattleScreen screen) {
            batch.draw(Ghast.puffAnimation.getKeyFrame(animTimer), screen.colToScreenX(col), screen.rowToScreenY(row));
        }
    }
}
