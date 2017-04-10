package com.comp460.screens.battle.units.protagonists.yvonne;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.common.GameUnit;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.protagonists.yvonne.moves.Slice;
import com.comp460.screens.battle.units.protagonists.yvonne.moves.Switch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Belinda on 2/17/17.
 */
public class Yvonne extends BattleUnit {

    private static final TextureRegion TRANCE_SPRITE = SpriteManager.BATTLE.findRegion("attacks/yvonne_trance");

    private static final TextureRegion METER_EMPTY_SPRITE = SpriteManager.BATTLE.findRegion("yvonne-meter/empty");
    private static final TextureRegion METER_BAR_SPRITE = SpriteManager.BATTLE.findRegion("yvonne-meter/bar");

    private static final BitmapFont tranceFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 8, Color.CYAN);
    private static final BitmapFont hintFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.WHITE);

    public float tranceMeter = 0f;
    public float tranceMeterMax = 50f;
    public float tranceDuration = 3f;

    public boolean tranceActive = false;

    public boolean attackUpOn = false;

    public float hurtDelay = 0.05f;
    public float hurtTimer = 0f;
    public int hurtBuffer = 0;

    public Yvonne(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);
        this.ability1 = new Slice(this);
        this.ability2 = new Switch(this);
    }

    //    public static final BitmapFont blockFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.CYAN);
    public static final TextureRegion INVENTORY_SPRITE = SpriteManager.BATTLE.findRegion("ui/clarissa_inv");
    private static BitmapFont yellowFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 10, Color.ORANGE, Color.BLACK, 2);
    private static GlyphLayout smashReadyLayout = new GlyphLayout(yellowFont, "SMASH READY!");

    public float invX = 400 / 2 - 2 * 40;
    public float invY = (int) screen.gridOffsetY + 3 * 40 + 2;
    public final int invSlotWidth = 12;

    public int maxCharges = 3;
    public int charges = 0;

    public List<Slice.SliceInstance> slices = new ArrayList<>();

    @Override
    public void update(float delta) {

        super.update(delta);
        for (Iterator<Slice.SliceInstance> iter = slices.iterator(); iter.hasNext(); ) {
            Slice.SliceInstance slice = iter.next();
            slice.update(screen, this, delta);
            if (slice.timer <= 0) {
                iter.remove();
            }
        }

        if (attackUpOn) {
            hurtTimer -= delta;
            if (hurtTimer <= 0) {
                hurtBuffer += 1;
                if (hurtBuffer == 5) {
                    hurtBuffer = 0;
                    this.applyDamage(new DamageVector(5, this));
                }
                hurtTimer = hurtDelay;
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
        if (tranceActive) {
            tranceMeter -= delta * (tranceMeterMax / tranceDuration);
            if (tranceMeter <= 0) {
                tranceMeter = 0;
                tranceActive = false;
            }
            batch.draw(TRANCE_SPRITE, this.transform.x, this.transform.y);
        }

        batch.draw(METER_EMPTY_SPRITE, invX, invY);

        batch.end();

        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(this.screen.camera.combined);
        double percentTrance = 1.0 * tranceMeter / tranceMeterMax;
        sr.setColor(Color.SCARLET);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        if (percentTrance > 0)
            sr.rect(invX + 1, invY + 1, (int) ((METER_EMPTY_SPRITE.getRegionWidth() - 2) * percentTrance), 4);
        sr.end();

        batch.begin();

        if (tranceActive) {
            tranceFont.draw(batch, "Dmg = Healing", invX - 20, invY + 20);
        } else {
            hintFont.draw(batch, "Deal damage", invX - 8, invY + 14);
        }

//        batch.draw(METER_BAR_SPRITE, invX, invY, ((METER_BAR_SPRITE.getRegionWidth() - 2) * (tranceMeter / tranceMeterMax) + 2), METER_BAR_SPRITE.getRegionHeight());

//        batch.draw(INVENTORY_SPRITE, invX, invY);
//        int x = invSlotWidth * (maxCharges - 1);
//        for (int i = 0; i < charges; i++) {
//            batch.draw(SpriteManager.BATTLE.findRegion("ingredients/fire_inv"), invX + x + 1, invY + 1);
//        }

    }


    @Override
    public float applyDamage(DamageVector damageVector) {
        DamageVector adjusted = new DamageVector(damageVector.trueDamage * ((attackUpOn) ? 2 : 1), damageVector.source);
        if (tranceActive) {
            DamageVector reversed = new DamageVector(-adjusted.trueDamage, damageVector.source);
            return -super.applyDamage(reversed);
        } else {
            return super.applyDamage(adjusted);
        }
    }

    public void fillTranceMeter(float amt) {
        if (tranceActive) {
            return;
        }
        tranceMeter += amt;
        if (tranceMeter >= tranceMeterMax) {
            tranceMeter = tranceMeterMax;
            tranceActive = true;
        }
//        tranceTimer = 2f;
//        this.ability1 = new Disabled();
    }
}
