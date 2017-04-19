package com.comp460.screens.battle.units.protagonists.andre;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.assets.FontManager;
import com.comp460.assets.SoundManager;
import com.comp460.assets.SpriteManager;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.FloatingText;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.protagonists.andre.moves.Shield;
import com.comp460.screens.battle.units.protagonists.andre.moves.Punch;
import com.comp460.screens.battle.units.protagonists.andre.moves.Smash;
import com.comp460.common.GameUnit;
import com.comp460.screens.battle.units.protagonists.clarissa.Ingredient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * Created by Belinda on 2/17/17.
 */
public class Andre extends BattleUnit {

    public static Animation<TextureRegion> shieldFlare = BattleAnimationManager.getBattleAnimation("attacks/shield_outline");

    public static final BitmapFont blockFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.CYAN);
    public static final TextureRegion ENERGY_0_SPRITE = SpriteManager.BATTLE.findRegion("andre-power/empty");
    public static final TextureRegion ENERGY_1_SPRITE = SpriteManager.BATTLE.findRegion("andre-power/1");
    public static final TextureRegion ENERGY_2_SPRITE = SpriteManager.BATTLE.findRegion("andre-power/2");
    public static final TextureRegion ENERGY_3_SPRITE = SpriteManager.BATTLE.findRegion("andre-power/3");

    private static BitmapFont yellowFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 10, Color.ORANGE, Color.BLACK, 2);
    private static GlyphLayout smashReadyLayout = new GlyphLayout(yellowFont, "SMASH READY!");
    private static BitmapFont hintFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.WHITE);
    private static GlyphLayout hintLayout = new GlyphLayout(hintFont, "Block to charge up!");

    public float invX = 400 / 2 - 2 * 40;
    public float invY = (int) screen.gridOffsetY + 3 * 40 + 2;
    public final int invSlotWidth = 12;

    public int maxCharges = 3;
    public int charges = 0;

    public float shieldDuration;
    public boolean shieldFresh;
    public List<Punch> punches = new ArrayList<>();

    public float smashTimer = 0f;
    public int smashCol = -1;
    public int smashPower = 50;

    public Andre(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);

        this.ability1 = new Punch(this);
        this.ability2 = new Shield(this);
    }

    public void smash() {
        if (smashCol >= 0) {
            return;
        }
        this.smashTimer = 0.1f;
        this.smashCol = 0;
        this.smashPower = 100;
        SoundManager.boom.play();
        this.screen.shakeCamera(20f);
    }

    public void raiseShield(float duration) {
        this.shieldDuration = duration;
        shieldFresh = true;
//        SoundManager.shieldSound.play(0.3f);
    }

    @Override
    public void update(float delta) {
        if (charges == 3 && !(ability1 instanceof Smash)) ability1 = new Smash(this);
        else if (charges < 3 && !(ability1 instanceof Punch)) ability1 = new Punch(this);

        super.update(delta);
        if (shieldDuration > 0) {
            shieldDuration -= delta;
            if (shieldDuration <= 0) {
                shieldDuration = 0;
                shieldFresh = false;
            }
        }

        for (Iterator<Punch> iter = punches.iterator(); iter.hasNext(); ) {
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

        if (shieldFresh)
            batch.draw(shieldFlare.getKeyFrame(0), screen.colToScreenX(this.curRow, this.curCol), screen.rowToScreenY(this.curRow, this.curCol));
        BattleUnit opponent = this.screen.p1Unit;
        if (this == opponent) {
            opponent = this.screen.p2Unit;
        }

        if (ability1 instanceof Smash) {
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
//       int x = invSlotWidth * (maxCharges - 1);
//       for (int i = 0; i < charges; i++) {
//           batch.draw(SpriteManager.BATTLE.findRegion("ingredients/fire_inv"), invX + x + 1, invY + 1);
//           x -= invSlotWidth;
//       }

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
                    opponent.applyDamage(new DamageVector(smashPower, this));
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
//            super.curEnergy += 2;
            charges = Math.min(3, charges + 1);
            shieldFresh = false;
            screen.addAnimation(new FloatingText("Block", blockFont, transform.x, transform.y + 40, 0.2f));
            SoundManager.block.play(0.4f);
        }
        return 0;
    }
}
