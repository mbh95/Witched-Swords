package com.comp460.screens.battle.units.protagonists.zane;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.assets.FontManager;
import com.comp460.common.GameUnit;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.FloatingText;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.protagonists.zane.moves.Counterattack;
import com.comp460.screens.battle.units.protagonists.zane.moves.Parry;
import com.comp460.screens.battle.units.protagonists.zane.moves.Slash;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Belinda on 2/17/17.
 */
public class Zane extends BattleUnit {
    public static Animation<TextureRegion> parryFlare = BattleAnimationManager.getBattleAnimation("attacks/parry");
    public static final BitmapFont parryFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.CYAN);
    public float parryDuration;
    public boolean parryFresh;
    public List<Slash.SlashInstance> slashes = new ArrayList<>();
    public List<Counterattack.CounterattackInstance> counterattacks = new ArrayList<>();

    public Zane(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);
        this.ability1 = new Slash(this);
        this.ability2 = new Parry(this);
    }

    public void parry(float duration) {
        this.parryDuration = duration;
        parryFresh = true;
    }
    @Override
    public void update(float delta) {
        super.update(delta);

        if (parryDuration > 0) {
            parryDuration -= delta;
            if (parryDuration <= 0) {
                parryDuration = 0;
                parryFresh = false;
            }
        }

        for (Iterator<Slash.SlashInstance> iter = slashes.iterator(); iter.hasNext(); ) {
            Slash.SlashInstance slice = iter.next();
            slice.update(screen, this, delta);
            if (slice.timer <= 0) {
                iter.remove();
            }
        }

        for (Iterator<Counterattack.CounterattackInstance> iter = counterattacks.iterator(); iter.hasNext(); ) {
            Counterattack.CounterattackInstance counterattack = iter.next();
            counterattack.update(screen, this, delta);
            if (counterattack.timer <= 0) {
                iter.remove();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        super.render(batch, delta);

        if (parryFresh)
            batch.draw(parryFlare.getKeyFrame(0), screen.colToScreenX(this.curRow, this.curCol), screen.rowToScreenY(this.curRow, this.curCol));
        BattleUnit opponent = this.screen.p1Unit;
        if (this == opponent) {
            opponent = this.screen.p2Unit;
        }
    }

    @Override
    public float applyDamage(DamageVector damageVector) {
        if (parryDuration <= 0) {
            return super.applyDamage(damageVector);
        } else if (parryFresh) {
            parryFresh = false;
            screen.addAnimation(new FloatingText("Parry", parryFont, transform.x, transform.y + 40, 0.2f));
            ((Parry)ability2).counterattack.use(this,screen);
        }
        return 0;
    }
}
