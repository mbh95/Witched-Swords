package com.comp460.screens.battle.units.protagonists.clarissa.moves;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.screens.battle.BattleAnimation;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.protagonists.clarissa.Clarissa;
import com.comp460.screens.battle.units.protagonists.clarissa.Ingredient;

/**
 * Created by Belinda on 2/17/17.
 */
public class Crossbow extends BattleUnitAbility {

    public static final String idString = "crossbow";
    public static final String nameString = "Ready aim Fire!";
    public static final String animationString = "attack";
    public static final String descriptionString = "Fire a crossbow bolt infused with highlighted ingredient.";

    public Clarissa clarissa;

    public float arrowSpeed = 300f;

    public Crossbow(Clarissa clarissa) {
        super(idString, nameString, animationString, descriptionString);
        this.clarissa = clarissa;
    }

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curEnergy > 1;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        clarissa.removeEnergy(2);
        if (clarissa.inventory.size() == 0) {
            clarissa.arrows.add(new Arrow(Ingredient.IngredientID.NONE, screen.colToScreenX(user.curRow, user.curCol) + 26, screen.rowToScreenY(user.curRow, user.curCol) + 22));
            return;
        }
        clarissa.arrows.add(new Arrow(clarissa.popIngredient().id, screen.colToScreenX(user.curRow, user.curCol) + 26, screen.rowToScreenY(user.curRow, user.curCol) + 22));
    }

    public class Arrow {
        public Vector3 tipPos;
        public Ingredient.IngredientID ingredient;

        public Arrow(Ingredient.IngredientID ingredient, float x, float y) {
            this.ingredient = ingredient;
            this.tipPos = new Vector3(x, y, 0);
        }

        public void render(SpriteBatch batch) {
            batch.draw(ingredient.arrowSprite, tipPos.x - ingredient.arrowSprite.getRegionWidth(), tipPos.y - ingredient.arrowSprite.getRegionHeight()/2);
        }

        public boolean updateAndReturnCollided(float delta, BattleScreen screen) {
            tipPos.x += arrowSpeed * delta;
            BattleUnit opponent = screen.p1Unit;
            if (opponent == clarissa) {
                opponent = screen.p2Unit;
            }
            if (tipPos.x >= opponent.transform.x + 15 && tipPos.x <= opponent.transform.x + 30) {
                if (tipPos.y >= opponent.transform.y && tipPos.y <= opponent.transform.y + 40) {
                    DamageVector dmg = new DamageVector(ingredient.damageVector);
                    dmg.source = clarissa;
                    opponent.applyDamage(dmg);
                    screen.addAnimation(new BattleAnimation(ingredient.impactAnim, tipPos.x - 20, tipPos.y - 10, 0.1f));
                    return true;
                }
            }
            return false;
        }
    }
}