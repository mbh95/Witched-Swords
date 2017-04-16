package com.comp460.screens.battle.units.protagonists.zane;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;
import com.comp460.common.GameUnit;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.FloatingText;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.protagonists.andre.moves.Punch;
import com.comp460.screens.battle.units.protagonists.andre.moves.Smash;
import com.comp460.screens.battle.units.protagonists.zane.moves.Counterattack;
import com.comp460.screens.battle.units.protagonists.zane.moves.Flurry;
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
    public static final TextureRegion ENERGY_0_SPRITE = SpriteManager.BATTLE.findRegion("zane-hits/empty");
    public static final TextureRegion ENERGY_1_SPRITE = SpriteManager.BATTLE.findRegion("zane-hits/1");
    public static final TextureRegion ENERGY_2_SPRITE = SpriteManager.BATTLE.findRegion("zane-hits/2");
    public static final TextureRegion ENERGY_3_SPRITE = SpriteManager.BATTLE.findRegion("zane-hits/3");

    public static final BitmapFont parryFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.CYAN);
    private static BitmapFont yellowFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 10, Color.ORANGE, Color.BLACK, 2);
    private static GlyphLayout smashReadyLayout = new GlyphLayout(yellowFont, "FLURRY READY!");
    private static BitmapFont hintFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.WHITE);
    private static GlyphLayout hintLayout = new GlyphLayout(hintFont, "Attack and parry, don't get hit!");
    public float invX = 400 / 2 - 2 * 40;
    public float invY = (int) screen.gridOffsetY + 3 * 40 + 2;
    public float parryDuration;
    public boolean parryFresh;
    public List<Slash.SlashInstance> slashes = new ArrayList<>();
    public List<Counterattack.CounterattackInstance> counterattacks = new ArrayList<>();
    public int charges = 0;
    public Flurry flurry;

    public Zane(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);
        this.ability1 = new Slash(this);
        this.ability2 = new Parry(this);
        flurry = new Flurry(this);
    }

    public void parry(float duration) {
        this.parryDuration = duration;
        parryFresh = true;
    }
    @Override
    public void update(float delta) {
        if (charges == 3 && !(ability1 instanceof Flurry)) ability1 = flurry;
        else if (charges < 3 && !(ability1 instanceof Slash)) ability1 = new Slash(this);
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

        flurry.update(screen, this, delta);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        super.render(batch, delta);
        flurry.render(batch);

        if (parryFresh)
            batch.draw(parryFlare.getKeyFrame(0), screen.colToScreenX(this.curRow, this.curCol), screen.rowToScreenY(this.curRow, this.curCol));
        BattleUnit opponent = this.screen.p1Unit;
        if (this == opponent) {
            opponent = this.screen.p2Unit;
        }
        if (ability1 instanceof Flurry) {
            yellowFont.draw(batch, smashReadyLayout,
                    screen.colToScreenX(0, 0) + (int) (screen.tileWidth * 1.5) - smashReadyLayout.width / 2,
                    screen.rowToScreenY(2, 0) + screen.tileHeight + ENERGY_0_SPRITE.getRegionHeight() + smashReadyLayout.height / 2 + 10);
        } else {
            hintFont.draw(batch, hintLayout,
                    screen.colToScreenX(0, 0) + (int) (screen.tileWidth * 1.5) - hintLayout.width / 2,
                    screen.rowToScreenY(2, 0) + screen.tileHeight + ENERGY_0_SPRITE.getRegionHeight() + hintLayout.height / 2 + 5);
        }

        switch (charges) {
            case 0:
                batch.draw(ENERGY_0_SPRITE, invX, invY);
                break;
            case 1:
                batch.draw(ENERGY_1_SPRITE, invX, invY);
                break;
            case 2:
                batch.draw(ENERGY_2_SPRITE, invX, invY);
                break;
            case 3:
                batch.draw(ENERGY_3_SPRITE, invX, invY);
                break;
        }
    }

    @Override
    public float applyDamage(DamageVector damageVector) {
        if (parryDuration <= 0) {
            charges = 0;
            return super.applyDamage(damageVector);
        } else if (parryFresh) {
            parryFresh = false;
            screen.addAnimation(new FloatingText("Parry", parryFont, transform.x, transform.y + 40, 0.2f));
            ((Parry)ability2).counterattack.use(this,screen);
        }
        return 0;
    }

    public void addCharge() {
        charges++;
        if (charges > 3) {
            charges = 3;
        }
    }
}
