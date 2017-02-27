package com.comp460.screens.battle.units.protagonists.clarissa;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.assets.SpriteManager;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.DamageVector;

/**
 * Created by Belinda on 2/17/17.
 */
public class Ingredient {
    public enum IngredientID{
        NONE("", "", "attacks/arrow_normal", "attacks/impact", new DamageVector(10)),
        POISON("ingredients/poison_inv", "ingredients/poison_field", "attacks/arrow_poison", "attacks/impact",  new DamageVector(20)),
        FIRE("ingredients/fire_inv", "ingredients/fire_field", "attacks/arrow_fire", "attacks/impact", new DamageVector(30));

        public Animation<TextureRegion> fieldAnim;
        public Animation<TextureRegion> impactAnim;
        public TextureRegion inventorySprite;
        public TextureRegion arrowSprite;
        public DamageVector damageVector;

        IngredientID(String inventorySpritePath, String fieldAnimPath, String arrowSpritePath, String impactAnimPath, DamageVector damageVec) {
            inventorySprite = SpriteManager.BATTLE.findRegion(inventorySpritePath);
            fieldAnim = BattleAnimationManager.getBattleAnimation(fieldAnimPath);
            arrowSprite = SpriteManager.BATTLE.findRegion(arrowSpritePath);
            impactAnim = BattleAnimationManager.getBattleAnimation(impactAnimPath);

            this.damageVector = damageVec;
        }
    }

    public IngredientID id;
    public Animation<TextureRegion> animation;
    public TextureRegion invTexture;
    public TextureRegion arrowTexture;
    public float lifetime;
    public float animTimer;
    public int row, col;

    public Ingredient(IngredientID id, int row, int col) {
        this.id = id;
        this.animation = id.fieldAnim;
        this.invTexture = id.inventorySprite;
        this.arrowTexture = id.arrowSprite;
        lifetime = 5f;
        animTimer = 0f;
        this.row = row;
        this.col = col;
    }

    public void update(float delta) {
        this.animTimer += delta;
        lifetime -= delta;
    }

    public void render(BattleScreen screen) {
        screen.batch.draw(animation.getKeyFrame(animTimer), screen.colToScreenX(row, col), screen.rowToScreenY(row, col));
    }
}