package com.comp460.screens.battle.units.enemies.shelly;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.comp460.common.GameUnit;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.enemies.shelly.moves.Spike;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Belinda on 4/10/2017.
 */
public class Shelly extends BattleUnit {
    public List<Spike.SpikeProjectile> activeSpikes = new ArrayList<>();

    public Shelly(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);
        this.ability1 = new Spike(this);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        for (Iterator<Spike.SpikeProjectile> iter = activeSpikes.iterator(); iter.hasNext(); ) {
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
