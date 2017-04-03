package com.comp460.screens.battle.units.enemies.baddie;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.comp460.assets.SpriteManager;
import com.comp460.common.GameUnit;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.DamageVector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by matthewhammond on 3/5/17.
 */
public class Baddie extends BattleUnit {

    public List<Spike.SpikeProjectile> activeSpikes = new ArrayList<>();

    public Baddie(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);
        this.ability1 = new Spike(this);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        for (Iterator<Spike.SpikeProjectile> iter = activeSpikes.iterator(); iter.hasNext();) {
            Spike.SpikeProjectile spike = iter.next();
            boolean collide = spike.updateAndReturnCollided(delta, screen);
            if (spike.tipPos.x < -Spike.sprite.getRegionWidth() || collide) {
                iter.remove();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        super.render(batch, delta);
        for (Spike.SpikeProjectile spike : activeSpikes)  {
            spike.render(batch);
        }
    }
}

class Spike extends BattleUnitAbility {

    public Baddie baddie;
    public float speed = 300f;
    public static TextureRegion sprite = SpriteManager.BATTLE.findRegion("attacks/spike");

    public Spike(Baddie baddie) {
        super("spike", "Spike Shot", "attack", "Shoot a razor sharp spike.");
        this.baddie = baddie;
    }

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curEnergy >= 1;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);

        baddie.activeSpikes.add(new SpikeProjectile(screen.colToScreenX(user.curRow, user.curCol) + 26, screen.rowToScreenY(user.curRow, user.curCol) + 22));
    }

    class SpikeProjectile {
        public Vector3 tipPos;

        public SpikeProjectile(float x, float y) {
            this.tipPos = new Vector3(x, y, 0);
        }

        public void render(SpriteBatch batch) {
            batch.draw(sprite, tipPos.x, tipPos.y - sprite.getRegionHeight()/2);
        }

        public boolean updateAndReturnCollided(float delta, BattleScreen screen) {
            tipPos.x -= speed * delta;
            BattleUnit opponent = screen.p1Unit;
            if (opponent == baddie) {
                opponent = screen.p2Unit;
            }
            if (tipPos.x >= opponent.transform.x + 15 && tipPos.x <= opponent.transform.x + 30) {
                if (tipPos.y >= opponent.transform.y && tipPos.y <= opponent.transform.y + 40) {
                    opponent.applyDamage(new DamageVector(20, baddie));
                    return true;
                }
            }
            return false;
        }
    }
}

