package com.comp460.screens.battle.units.protagonists.andre.moves;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.screens.battle.BattleAnimation;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.protagonists.andre.Andre;

/**
 * Created by Belinda on 2/17/17.
 */
public class Shield extends BattleUnitAbility {

    public static float duration = 1f;

    public Andre andre;
    public Shield(Andre andre) {
        super("shield", "Energy Shield", "attack", "A powerful shield.");
        this.andre = andre;
    }

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curEnergy >= 1;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
//        screen.addAnimation(new BattleAnimation(shieldFlare, user.transform.x, user.transform.y, 0.2f));
        user.curEnergy-=1;
        andre.raiseShield(0.5f);
    }
}
