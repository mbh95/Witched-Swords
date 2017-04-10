package com.comp460.screens.battle.units.protagonists.zane;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.comp460.common.GameUnit;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.protagonists.zane.moves.Slash;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Belinda on 2/17/17.
 */
public class Zane extends BattleUnit {
    public Zane(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);
        this.ability1 = new Slash(this);
    }

    public List<Slash.SlashInstance> slashes = new ArrayList<>();

    @Override
    public void update(float delta) {
        super.update(delta);
        for (Iterator<Slash.SlashInstance> iter = slashes.iterator(); iter.hasNext(); ) {
            Slash.SlashInstance slice = iter.next();
            slice.update(screen, this, delta);
            if (slice.timer <= 0) {
                iter.remove();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        super.render(batch, delta);

        BattleUnit opponent = this.screen.p1Unit;
        if (this == opponent) {
            opponent = this.screen.p2Unit;
        }

    }
}
