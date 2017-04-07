package com.comp460.screens.battle.units.protagonists.yvonne.moves;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.screens.battle.BattleAnimation;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.protagonists.yvonne.Yvonne;

/**
 * Created by matthewhammond on 4/7/17.
 */
public class Slice extends BattleUnitAbility {
    public Slice(Yvonne yvonne) {
        super("slice", "Slice", "attack", "A reckless and deadly swing from a scythe.");
        this.animTimer = 0;
        this.yvonne = yvonne;
    }
    public static Animation<TextureRegion> slice = BattleAnimationManager.getBattleAnimation("attacks/slice");
    public float animTimer;
    public int duration = 15;
    public boolean damage = true;
    private Yvonne yvonne;
    private int row, col;

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curEnergy >= 1;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        this.row = yvonne.curRow;
        this.col = yvonne.curCol+3;
        user.curEnergy -= 1;
        screen.addAnimation(new BattleAnimation(slice, screen.colToScreenX(0, col), screen.rowToScreenY(0, col), 0.2f));
        yvonne.slices.add(this);
        this.damage = true;
    }

    public void update(BattleScreen screen, BattleUnit owner, float delta) {
        BattleUnit opponent = screen.p2Unit;
        if (opponent == owner) {
            owner = screen.p1Unit;
        }
        duration--;

        if (opponent.curCol == col && damage) {
            opponent.applyDamage(new DamageVector(20, yvonne));
            damage = false;
        }
    }
}
