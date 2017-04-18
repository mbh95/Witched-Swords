package com.comp460.screens.battle.units.enemies.shelly.moves;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.comp460.assets.SpriteManager;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.enemies.baddie.Baddie;
import com.comp460.screens.battle.units.enemies.shelly.Shelly;

public class Spike extends BattleUnitAbility {

    public Shelly baddie;
    public float speed = 300f;
    public static TextureRegion sprite = SpriteManager.BATTLE.findRegion("attacks/spike");

    public Spike(Shelly baddie) {
        super("spike", "Spike Shot", "attack", "Shoot a razor sharp spike.", 1, 0);
        this.baddie = baddie;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);

        baddie.activeSpikes.add(new SpikeProjectile(screen.colToScreenX(user.curRow, user.curCol) + 26, screen.rowToScreenY(user.curRow, user.curCol) + 22));
    }

    public class SpikeProjectile {
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
