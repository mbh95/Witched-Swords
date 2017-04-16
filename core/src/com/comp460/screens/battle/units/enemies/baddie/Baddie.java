package com.comp460.screens.battle.units.enemies.baddie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.common.GameUnit;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.enemies.shelly.moves.Spike;
import com.comp460.screens.battle.units.enemies.baddie.moves.Vines;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.comp460.assets.SoundManager.monsterHurtSound;

/**
 * Created by matthewhammond on 3/5/17.
 */
public class Baddie extends BattleUnit {

    public List<Spike.SpikeProjectile> activeSpikes = new ArrayList<>();
    public List<Vines.VinesInstance> vines = new ArrayList<>();

    public Baddie(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);
//        this.ability1 = new Spike(this);
        this.ability1 = new Vines(this);
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

        for (Iterator<Vines.VinesInstance> iter = vines.iterator(); iter.hasNext(); ) {
            Vines.VinesInstance vines = iter.next();
            vines.update(screen, this, delta);
            if (vines.timer <= 0) {
                iter.remove();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        super.render(batch, delta);
        for (Spike.SpikeProjectile spike : activeSpikes) {
            spike.render(batch);
        }
        for (Vines.VinesInstance vine : vines) {
            vine.render(batch, screen);
        }
    }
}