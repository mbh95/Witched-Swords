package com.comp460.screens.battle.units.protagonists.andre;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.comp460.assets.FontManager;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.FloatingText;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.protagonists.andre.moves.Shield;
import com.comp460.screens.battle.units.protagonists.andre.moves.Punch;
import com.comp460.screens.battle.units.protagonists.andre.moves.Smash;
import com.comp460.common.GameUnit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Belinda on 2/17/17.
 */
public class Andre extends BattleUnit {

    public static final BitmapFont blockFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.CYAN);
    public float shieldDuration;
    public boolean shieldFresh;
    public List<Punch> punches = new ArrayList<>();

    public float smashTimer = 0f;
    public int smashCol = -1;
    public int smashPower = 50;

    public Andre(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);

        this.ability1 = new Smash(this);
        this.ability2 = new Shield(this);
    }

    public void smash() {
        if (smashCol >= 0) {
            return;
        }
        this.smashTimer = 0.1f;
        this.smashCol = 0;
        this.smashPower = 50;
    }

    public void raiseShield(float duration) {
        this.shieldDuration = duration;
        shieldFresh = true;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (shieldDuration > 0) {
            shieldDuration -= delta;
            if (shieldDuration <= 0) {
                shieldDuration = 0;
                shieldFresh = false;
            }
        }

        for (Iterator<Punch> iter = punches.iterator(); iter.hasNext();) {
            Punch punch = iter.next();
            punch.update(screen, this, delta);
            if (punch.duration <= 0) {
                iter.remove();
            }
        }
    }

   @Override
    public void render(SpriteBatch batch, float delta) {
        super.render(batch, delta);
        for (Punch punch : punches)  {
            punch.render(batch, screen);
        }

        BattleUnit opponent = this.screen.p1Unit;
        if (this == opponent) {
            opponent = this.screen.p2Unit;
        }

        if (smashCol >= 0) {
            smashTimer -= delta;
            if (smashTimer <= 0) {
                smashTimer = 0.1f;
                smashCol++;
                smashPower = Math.round(0.9f * smashPower);
                if (smashCol >= this.screen.numCols) {
                    smashCol = -1;
                    return;
                }
                if (opponent.curCol == smashCol) {
                    opponent.applyDamage(new DamageVector(smashPower));
                }
                for (int r = 0; r < this.screen.numRows; r++) {
                    this.screen.tileOffsets[r][smashCol].y = smashPower;
                }
            }
        }
    }

    @Override
    public float applyDamage(DamageVector damageVector) {
        if (shieldDuration <= 0) {
            return super.applyDamage(damageVector);
        } else if (shieldFresh) {
            super.curEnergy += 2;
            shieldFresh = false;
            screen.addAnimation(new FloatingText("Block", blockFont, transform.x, transform.y + 40, 0.2f));
        }
        return 0;
    }
}
