package com.comp460.battle.units.protagonists.clarissa;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.comp460.assets.AnimationManager;
import com.comp460.assets.SpriteManager;
import com.comp460.battle.BattleAnimation;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.units.BattleUnit;
import com.comp460.battle.units.BattleUnitAbility;
import com.comp460.battle.units.DamageVector;
import com.comp460.common.GameUnit;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by matth on 2/15/2017.
 */
public class Clarissa extends BattleUnit {

    public static final TextureRegion INVENTORY_SPRITE = SpriteManager.BATTLE.findRegion("ui/clarissa_inv");

    public float invX = 400/2 - 2*40;
    public float invY =  (int)screen.gridOffsetY + 3 * 40 + 2;
    public final int invSlotWidth = 12;

    public int maxIngredients = 3;
    public Queue<Ingredient> inventory;

    public List<Ingredient> spawnedIngredients;

    public List<Crossbow.Arrow> arrows = new ArrayList<>();

    public float nextSpawnCountdown = getNewSpawnTime();

    public Random rng = new Random();
    public static Ingredient.IngredientID[] possibleIngredients = new Ingredient.IngredientID[] {Ingredient.IngredientID.POISON, Ingredient.IngredientID.FIRE};

    public Clarissa(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);
        this.ability1 = new Crossbow(this);
        this.ability2 = new PickUp(this);
        inventory = new ConcurrentLinkedQueue();
        spawnedIngredients = new ArrayList<>();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        nextSpawnCountdown -= delta;
        if (nextSpawnCountdown <= 0) {
            spawnIngredient();
            nextSpawnCountdown = getNewSpawnTime();
        }
        for (Iterator<Ingredient> iter = spawnedIngredients.iterator(); iter.hasNext(); ) {
            Ingredient cur = iter.next();
            cur.update(delta);
            if (cur.lifetime <= 0) {
                iter.remove();
            }
        }
        for (Iterator<Crossbow.Arrow> iter = arrows.iterator(); iter.hasNext(); ) {
            Crossbow.Arrow arrow = iter.next();
            if (arrow.updateAndReturnCollided(delta, screen)) {
                iter.remove();
            }
        }
    }


    public void render(SpriteBatch batch) {
        for (Crossbow.Arrow arrow : arrows) {
            arrow.render(batch);
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        for (Ingredient ingredient: spawnedIngredients) {
            ingredient.render(screen);
        }

//        invX = this.transform.x;
//        invY = this.transform.y - 13;
        batch.draw(INVENTORY_SPRITE, invX, invY);
        int x = invSlotWidth * (maxIngredients - 1);
        for (Ingredient ing : inventory) {
            batch.draw(ing.invTexture, invX + x + 1, invY + 1);
            x -= invSlotWidth;
        }
        super.render(batch, delta);
        for (Crossbow.Arrow arrow : arrows) {
            arrow.render(batch);
        }
    }

    public void spawnIngredient() {
        int choice = rng.nextInt(possibleIngredients.length);
        Ingredient.IngredientID newID = possibleIngredients[choice];
        Ingredient newIngredient = new Ingredient(newID, rng.nextInt(screen.numRows), rng.nextInt(screen.numCols/2 + (screen.isOnLHS(this.curRow, this.curCol)?0:screen.numCols/2)));
        for (Ingredient in : spawnedIngredients) {
            if (in.row == newIngredient.row && in.col == newIngredient.col) {
                return;
            }
        }
        spawnedIngredients.add(newIngredient);
    }

    public void pushIngredient(Ingredient newIngredient) {
        inventory.add(newIngredient);
        while (inventory.size() > maxIngredients) {
            inventory.remove();
        }
    }

    public Ingredient popIngredient() {
        return inventory.remove();
    }

    public float getNewSpawnTime() {
        return (float)(Math.random() + 2);
    }
}

class Crossbow extends BattleUnitAbility {

    public Clarissa clarissa;

    public float arrowSpeed = 300f;

    public Crossbow(Clarissa clarissa) {
        this.clarissa = clarissa;

        this.clarissa = clarissa;
        this.id = "crossbow";
        this.name = "Ready aim Fire!";
        this.animationId = "attack";
        this.description = "placeholder description";
    }

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curEnergy >= 1;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        if (clarissa.inventory.size() == 0) {
            clarissa.arrows.add(new Arrow(Ingredient.IngredientID.NONE, screen.colToScreenX(user.curCol) + 26, screen.rowToScreenY(user.curRow) + 22));
            return;
        }
        clarissa.arrows.add(new Arrow(clarissa.popIngredient().id, screen.colToScreenX(user.curCol) + 26, screen.rowToScreenY(user.curRow) + 22));
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
                    opponent.applyDamage(ingredient.damageVector);
                    screen.addAnimation(new BattleAnimation(ingredient.impactAnim, tipPos.x - 20, tipPos.y - 10, 0.1f));
                    return true;
                }
            }
            return false;
        }
    }
}

class PickUp extends BattleUnitAbility {

    public Clarissa clarissa;

    public PickUp(Clarissa clarissa) {
        this.clarissa = clarissa;
        this.id = "pickup";
        this.name = "PickUp";
        this.animationId = "attack";
        this.description = "placeholder description";
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
            }
        }
    }
}

class Ingredient {
    enum IngredientID{
        NONE("", "", "attacks/arrow_normal", "attacks/impact", new DamageVector(15)),
        POISON("ingredients/poison_inv", "ingredients/poison_field", "attacks/arrow_poison", "attacks/impact",  new DamageVector(20)),
        FIRE("ingredients/fire_inv", "ingredients/fire_field", "attacks/arrow_fire", "attacks/impact", new DamageVector(30));

        public Animation<TextureRegion> fieldAnim;
        public Animation<TextureRegion> impactAnim;
        public TextureRegion inventorySprite;
        public TextureRegion arrowSprite;
        public DamageVector damageVector;

        IngredientID(String inventorySpritePath, String fieldAnimPath, String arrowSpritePath, String impactAnimPath, DamageVector damageVec) {
            inventorySprite = SpriteManager.BATTLE.findRegion(inventorySpritePath);
            fieldAnim = AnimationManager.getBattleAnimation(fieldAnimPath);
            arrowSprite = SpriteManager.BATTLE.findRegion(arrowSpritePath);
            impactAnim = AnimationManager.getBattleAnimation(impactAnimPath);

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
        screen.batch.draw(animation.getKeyFrame(animTimer), screen.colToScreenX(col), screen.rowToScreenY(row));
    }
}