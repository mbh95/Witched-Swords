package com.comp460.experimental.battle;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.comp460.experimental.AssetManager;
import com.comp460.experimental.GameUnit;

import java.util.Random;

/**
 * Created by matthewhammond on 1/29/17.
 */
public class BattleUnit implements IRenderable {

    private BattleGrid grid;
    private GameUnit base;

    private int gridRow, gridCol;
    private Vector3 transform;

    private int energy = 5;

    private Animation<TextureRegion> animIdle;
    private Animation<TextureRegion> animAttack;
    private Animation<TextureRegion> animHurt;
    private Animation<TextureRegion> animFallen;

    private Animation<TextureRegion> currentAnim;
    private float animTimer;

    private boolean invlunerable;

    private BattleAction action1 = (owner)->{};
    private BattleAction action2 = (owner)->{};

    public BattleUnit( BattleGrid grid, GameUnit base, int row, int col) {
        this.grid = grid;
        this.base = base;

        animIdle = AssetManager.getAnimation(base.getId(), AssetManager.BattleAnimation.IDLE);
        animIdle.setPlayMode(Animation.PlayMode.LOOP);

        animAttack = AssetManager.getAnimation(base.getId(), AssetManager.BattleAnimation.ATTACK);
        animAttack.setPlayMode(Animation.PlayMode.NORMAL);

        animHurt = AssetManager.getAnimation(base.getId(), AssetManager.BattleAnimation.HURT);
        animHurt.setPlayMode(Animation.PlayMode.NORMAL);

        animFallen = AssetManager.getAnimation(base.getId(), AssetManager.BattleAnimation.FALLEN);
        animFallen.setPlayMode(Animation.PlayMode.NORMAL);

        this.currentAnim = animIdle;
        this.animTimer = 0.0f;
        this.gridRow = row;
        this.gridCol = col;
        this.transform = new Vector3(this.getTileX(), this.getTileY(), 0.0f);
        this.invlunerable = false;

        System.out.println(this.base.getAction1());
        if (this.base.getAction1() != -1) {
            this.action1 = (owner)-> {
                if (owner.getEnergy() <= 0) {
                    return;
                }
                owner.setEnergy(owner.getEnergy() - 1);
                for (int i = 0; i < 3; i++) {
                    grid.addEffect(BattleEffect.buildWarning(20, 0f, 0f, 1f, 0.4f, new BattleEffect(grid, gridRow, gridCol + i + 1, this) {
                        @Override
                        public void tick() {
                            if (this.numTicks >= 10) {
                                grid.removeEffect(this);
                            }
                            this.grid.getUnits().forEach((u)->{
                                if (u == owner) {
                                    return;
                                } else {
                                    if (u.getGridRow() == this.row && u.getGridCol() == this.col) {
                                        u.hurt(2);
                                    }
                                }
                            });
                        }

                        @Override
                        public void render(SpriteBatch batch) {
                            batch.draw(AssetManager.Textures.LAZER, grid.getTile(this.row, this.col).getScreenX(), grid.getTile(this.row, this.col).getScreenY());
                        }
                    }));
                }
            };
        }
        if (this.base.getAction2() != -1) {
            this.action2 = (owner)-> {
                if (owner.getEnergy() <= 0) {
                    return;
                }
                Random rng = new Random();
                owner.setEnergy(owner.getEnergy() - 1);
                for (int i = 0; i < grid.getNumCols(); i++) {
                    grid.addEffect(BattleEffect.buildWarning(10, 1f, 0f, 0f, 0.4f, new BattleEffect(grid, i, grid.getNumCols()/2 - 1 - rng.nextInt(grid.getNumCols()/2), this) {
                        @Override
                        public void tick() {
                            if (this.numTicks >= 10) {
                                grid.removeEffect(this);
                            }
                            this.grid.getUnits().forEach((u)->{
                                if (u == owner) {
                                    return;
                                } else {
                                    if (u.getGridRow() == this.row && u.getGridCol() == this.col) {
                                        u.hurt(3);
                                    }
                                }
                            });
                        }

                        @Override
                        public void render(SpriteBatch batch) {
                            batch.draw(AssetManager.Textures.SCRATCH, grid.getTile(this.row, this.col).getScreenX(), grid.getTile(this.row, this.col).getScreenY());
                        }
                    }));
                }
            };
        }
        this.grid.addUnit(this);
    }

    public void update(float delta) {
        animTimer += delta;
        if (this.currentAnim.isAnimationFinished(animTimer)) {
            this.currentAnim = animIdle;
            this.animTimer = 0.0f;
            this.invlunerable = false;
        }
        this.transform.slerp(new Vector3(this.getTileX(), this.getTileY(), 0.0f), 0.4f);
    }

    public void setAnimIdle() {
        this.currentAnim = animIdle;
        this.animTimer = 0.0f;
    }

    public void setAnimAttack() {
        this.currentAnim = animAttack;
        this.animTimer = 0.0f;
    }

    public void setAnimHurt() {
        this.currentAnim = animHurt;
        this.animTimer = 0.0f;
        this.invlunerable = true;
    }

    public void setAnimFallen() {
        this.currentAnim = animFallen;
        this.animTimer = 0.0f;
    }

    @Override
    public TextureRegion getSprite() {
        return this.currentAnim.getKeyFrame(animTimer);
    }

    @Override
    public float getScreenX() {
        return this.transform.x;
    }

    @Override
    public float getScreenY() {
        return this.transform.y;
    }

    public int getGridRow() {
        return this.gridRow;
    }

    public int getGridCol() {
        return this.gridCol;
    }

    public void setGridRow(int newRow) {
        if (this.grid.isOnLHS(gridRow, gridCol) && !this.grid.isOnLHS(newRow, gridCol)) {
            return;
        }
        if (this.grid.isOnRHS(gridRow, gridCol) && !this.grid.isOnRHS(newRow, gridCol)) {
            return;
        }
        this.gridRow = newRow;
    }

    public void setGridCol(int newCol) {
        if (this.grid.isOnLHS(gridRow, gridCol) && !this.grid.isOnLHS(gridRow, newCol)) {
            return;
        }
        if (this.grid.isOnRHS(gridRow, gridCol) && !this.grid.isOnRHS(gridRow, newCol)) {
            return;
        }
        this.gridCol = newCol;
    }

    public void move(int dr, int dc) {
        setGridRow(gridRow+dr);
        setGridCol(gridCol+dc);
    }

    public void action1() {
        action1.perform(this);
    }

    public void action2() {
        action2.perform(this);
    }

    private float getTileX() {
        return grid.getTile(gridRow, gridCol).getScreenX();
    }

    private float getTileY() {
        return grid.getTile(gridRow, gridCol).getScreenY() + grid.getTile(gridRow, gridCol).getSprite().getRegionHeight()/3;
    }

    public void hurt(int amt) {
        this.setAnimHurt();
        this.base.setCurHP(this.base.getCurHP() - amt);
    }

    public int getCurHP() {
        return this.base.getCurHP();
    }

    public int getMaxHP() {
        return this.base.getMaxHP();
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int newEnergy) {
        this.energy = newEnergy;
    }
}
