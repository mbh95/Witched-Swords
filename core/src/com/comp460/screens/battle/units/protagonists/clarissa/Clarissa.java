package com.comp460.screens.battle.units.protagonists.clarissa;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.SpriteManager;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.common.GameUnit;
import com.comp460.screens.battle.units.protagonists.clarissa.moves.*;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by matth on 2/15/2017.
 */
public class Clarissa extends BattleUnit {

    public static final TextureRegion INVENTORY_SPRITE = SpriteManager.BATTLE.findRegion("rendering/clarissa_inv");

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