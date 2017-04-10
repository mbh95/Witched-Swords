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

    private static BitmapFont notifyFontRed = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.RED, Color.BLACK, 1);
    private static BitmapFont notifyFontGreen = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.GREEN, Color.BLACK, 1);

    public Switch(Yvonne yvonne) {
        super("switch", "Rage", "attack", "Toggle that raises attack but increases incoming damage.");
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
            screen.addAnimation(new FloatingText("x2 ATK", notifyFontGreen, (float) (yvonne.transform.x + 20f), yvonne.transform.y, 0.5f));
            screen.addAnimation(new FloatingText("x2 DMG", notifyFontRed, (float) (yvonne.transform.x + 20f), yvonne.transform.y + 10, 0.5f));
        } else {
            screen.addAnimation(new FloatingText("x1 ATK", notifyFontRed, (float) (yvonne.transform.x + 20f), yvonne.transform.y, 0.5f));
            screen.addAnimation(new FloatingText("x1 DMG", notifyFontGreen, (float) (yvonne.transform.x + 20f), yvonne.transform.y + 10, 0.5f));
        }

    }
}
