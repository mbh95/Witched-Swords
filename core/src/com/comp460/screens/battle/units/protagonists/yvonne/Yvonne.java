package com.comp460.screens.battle.units.protagonists.yvonne;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.common.GameUnit;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.protagonists.andre.moves.Punch;
import com.comp460.screens.battle.units.protagonists.yvonne.moves.Slice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Belinda on 2/17/17.
 */
public class Yvonne extends BattleUnit {

    public Yvonne(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);
        this.ability1 = new Slice(this);
    }

//    public static final BitmapFont blockFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.CYAN);
    public static final TextureRegion INVENTORY_SPRITE = SpriteManager.BATTLE.findRegion("ui/clarissa_inv");
    private static BitmapFont yellowFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 10, Color.ORANGE, Color.BLACK, 2);
    private static GlyphLayout smashReadyLayout = new GlyphLayout(yellowFont, "SMASH READY!");

    public float invX = 400/2 - 2*40;
    public float invY =  (int)screen.gridOffsetY + 3 * 40 + 2;
    public final int invSlotWidth = 12;

    public int maxCharges = 3;
    public int charges = 0;

    public List<Slice> slices = new ArrayList<>();

    @Override
    public void update(float delta) {

        super.update(delta);
        for (Iterator<Slice> iter = slices.iterator(); iter.hasNext();) {
            Slice slice = iter.next();
            slice.update(screen, this, delta);
            if (slice.duration <= 0) {
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

//        batch.draw(INVENTORY_SPRITE, invX, invY);
//        int x = invSlotWidth * (maxCharges - 1);
//        for (int i = 0; i < charges; i++) {
//            batch.draw(SpriteManager.BATTLE.findRegion("ingredients/fire_inv"), invX + x + 1, invY + 1);
//            x -= invSlotWidth;
//        }

    }
}
