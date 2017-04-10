package com.comp460.screens.battle.units.protagonists.yvonne.moves;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.comp460.assets.FontManager;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.FloatingText;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.protagonists.yvonne.Yvonne;

/**
 * Created by matth on 4/9/2017.
 */
public class Switch extends BattleUnitAbility {

    private Yvonne yvonne;

    private static BitmapFont notifyFontRed = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.RED);
    private static BitmapFont notifyFontGreen = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.GREEN);

    public Switch(Yvonne yvonne) {
        super("switch", "Rage", "attack", "Raise attack, drains health.");
        this.yvonne = yvonne;
    }

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curEnergy >= 1;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        yvonne.removeEnergy(1);
        yvonne.attackUpOn = !yvonne.attackUpOn;
//        yvonne.startTrance();
        if (yvonne.attackUpOn) {
            screen.addAnimation(new FloatingText("+ATK", notifyFontGreen, (float) (yvonne.transform.x + 16f), yvonne.transform.y, 0.5f));
            screen.addAnimation(new FloatingText("+HP DRAIN", notifyFontRed, (float) (yvonne.transform.x + 16f), yvonne.transform.y + 10, 0.5f));

        } else {
            screen.addAnimation(new FloatingText("-ATK", notifyFontRed, (float) (yvonne.transform.x + 16f), yvonne.transform.y, 0.5f));
            screen.addAnimation(new FloatingText("-HP DRAIN", notifyFontGreen, (float) (yvonne.transform.x + 16f), yvonne.transform.y + 10, 0.5f));
        }

    }
}
