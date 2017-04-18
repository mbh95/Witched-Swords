package com.comp460.screens.battle.units.protagonists.clarissa.moves;

import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.protagonists.clarissa.Clarissa;
import com.comp460.screens.battle.units.protagonists.clarissa.Ingredient;

import java.util.Iterator;

import static com.comp460.assets.SoundManager.pickupSound;

/**
 * Created by Belinda on 2/17/17.
 */
public class PickUp extends BattleUnitAbility {

    public static final String idString = "pickup";
    public static final String nameString = "Pick Up";
    public static final String animationString = "pickup";
    public static final String descriptionString = "Pick up ingredient.";

    public Clarissa clarissa;

    public PickUp(Clarissa clarissa) {
        super(idString, nameString, animationString, descriptionString, 0, 0);
        this.clarissa = clarissa;
    }

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return clarissa.inventory.size() < clarissa.maxIngredients;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        if (clarissa.inventory.size() >= clarissa.maxIngredients) {
            return;
        }
        for (Iterator<Ingredient> iter = clarissa.spawnedIngredients.iterator(); iter.hasNext();) {
            Ingredient cur = iter.next();
            if (clarissa.curRow == cur.row && clarissa.curCol == cur.col) {
                iter.remove();
                clarissa.pushIngredient(cur);
                pickupSound.play();
            }
        }
    }
}