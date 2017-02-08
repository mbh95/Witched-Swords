package com.comp460.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.AssetManager;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by matthewhammond on 2/1/17.
 */
public class ActionFactory {

    public static BattleAction buildAction(int actionID) {
        switch (actionID) {
            case 0:
                return buildLazer();
            case 1:
                return buildScratches();
            case 2:
                return buildShield();
            case 3:
                return buildSpike();
            case 4:
                return buildArrow();
            default:
                return (owner) -> {};

        }
    }
    public static BattleAction buildLazer() {
        return ((owner) -> {
            if (owner.getEnergy() <= 0) {
                return;
            }
            BattleGrid grid = owner.getGrid();
            owner.setEnergy(owner.getEnergy() - 1);
            owner.setAnimAttack();
            for (int i = 0; i < 3; i++) {
                grid.addEffect(buildWarning(20, 0f, 0f, 1f, 0.4f, new BattleEffect(owner, owner.getGridRow(), owner.getGridCol() + i + 1, 0) {

                    boolean active = true;
                    @Override
                    public boolean tick() {
                        if (this.numTicks > 10) {
                            return false;
                        }
                        if (active) {
                            grid.getUnits().forEach((u) -> {
                                if (u == owner) {
                                    return;
                                } else {
                                    if (u.getGridRow() == this.row && u.getGridCol() == this.col) {
                                        u.hurt(30);
                                        active = false;
                                    }
                                }
                            });
                        }
                        return true;
                    }

                    @Override
                    public void render(SpriteBatch batch) {
                        batch.begin();
                        batch.draw(AssetManager.Textures.LAZER, grid.getTile(this.row, this.col).getScreenX(), grid.getTile(this.row, this.col).getScreenY());
                        batch.end();
                    }
                }));
            }
        });
    }

    public static BattleAction buildScratches() {
        return ((owner) -> {
            if (owner.getEnergy() <= 0) {
                return;
            }
            BattleGrid grid = owner.getGrid();
            Random rng = new Random();
            owner.setEnergy(owner.getEnergy() - 1);
            owner.setAnimAttack();
            for (int i = 0; i < grid.getNumCols(); i++) {
                grid.addEffect(buildWarning(30, 1f, 0f, 0f, 0.4f, new BattleEffect(owner, i, grid.getNumCols()/2 - 1 - rng.nextInt(grid.getNumCols()/2), 0) {
                    boolean active = true;
                    @Override
                    public boolean tick() {
                        if (this.numTicks > 10) {
                            return false;
                        }
                        if (active) {
                        grid.getUnits().forEach((u)->{
                            if (u == owner) {
                                return;
                            } else {
                                if (u.getGridRow() == this.row && u.getGridCol() == this.col) {
                                    u.hurt(20);
                                    active = false;
                                }
                            }
                        });
                        }
                        return true;
                    }

                    @Override
                    public void render(SpriteBatch batch) {
                        batch.begin();
                        batch.draw(AssetManager.Textures.SCRATCH, grid.getTile(this.row, this.col).getScreenX(), grid.getTile(this.row, this.col).getScreenY());
                        batch.end();
                    }
                }));
            }
        });
    }

    public static BattleAction buildShield() {
        return ((owner) -> {
            if (owner.getEnergy() <= 0) {
                return;
            }
            BattleGrid grid = owner.getGrid();
            owner.setEnergy(owner.getEnergy() - 1);
            owner.setAnimAttack();
            grid.addEffect(new BattleEffect(owner, owner.getGridRow(), owner.getGridCol(), 1) {
                @Override
                public boolean tick() {
                    if (this.numTicks > 30) {
                        return false;
                    }
                    Set<BattleEffect> toRemove = new HashSet<>();
                    grid.getEffects().forEach((e)->{
                        if (e != this && e.row == this.row && e.col == this.col) {
                            toRemove.add(e);
                            owner.addFloatingText("Block!");
                        }
                    });
                    toRemove.forEach((e)->{
                        grid.removeEffect(e);
                    });
                    return true;
                }

                @Override
                public void render(SpriteBatch batch) {
                    batch.begin();
                    batch.draw(AssetManager.Textures.SHIELD, grid.getTile(this.row, this.col).getScreenX(), grid.getTile(this.row, this.col).getScreenY());
                    batch.end();
                }
            });
        });
    }

    private static class SpikeEffect extends BattleEffect {

        boolean hit = false;

        public SpikeEffect(BattleUnit owner, int row, int col, int priority) {
            super(owner, row, col, priority);
        }

        @Override
        public boolean tick() {
            if (this.numTicks > 5) {
                if (col > 0) {
                    this.owner.getGrid().addEffect(new SpikeEffect(owner, row, col - 1, 0));
                }
                return false;
            }
            owner.getGrid().getUnits().forEach((u)->{
                if (u == owner) {
                    return;
                } else {
                    if (u.getGridRow() == this.row && u.getGridCol() == this.col) {
                        u.hurt(20);
                        hit = true;
                    }
                }
            });
            return !hit;
        }

        @Override
        public void render(SpriteBatch batch) {
            batch.begin();
            batch.draw(AssetManager.Textures.SPIKE, owner.getGrid().getTile(this.row, this.col).getScreenX(), owner.getGrid().getTile(this.row, this.col).getScreenY());
            batch.end();
        }
    }
    public static BattleAction buildSpike() {
        return ((owner) -> {
            if (owner.getEnergy() <= 0) {
                return;
            }
            BattleGrid grid = owner.getGrid();
            owner.setEnergy(owner.getEnergy() - 1);
            owner.setAnimAttack();

            grid.addEffect(new SpikeEffect(owner, owner.getGridRow(), owner.getGridCol() - 1, 1));
        });
    }

    private static class ArrowEffect extends BattleEffect {

        boolean hit = false;

        public ArrowEffect(BattleUnit owner, int row, int col, int priority) {
            super(owner, row, col, priority);
        }

        @Override
        public boolean tick() {
            if (this.numTicks > 3) {
                if (col < owner.getGrid().getNumCols()-1) {
                    this.owner.getGrid().addEffect(new ArrowEffect(owner, row, col + 1, 0));
                }
                return false;
            }
            owner.getGrid().getUnits().forEach((u)->{
                if (u == owner) {
                    return;
                } else {
                    if (u.getGridRow() == this.row && u.getGridCol() == this.col) {
                        u.hurt(15);
                        hit = true;
                    }
                }
            });
            return !hit;
        }

        @Override
        public void render(SpriteBatch batch) {
            batch.begin();
            batch.draw(AssetManager.Textures.ARROW, owner.getGrid().getTile(this.row, this.col).getScreenX(), owner.getGrid().getTile(this.row, this.col).getScreenY());
            batch.end();
        }
    }
    public static BattleAction buildArrow() {
        return ((owner) -> {
            if (owner.getEnergy() <= 0) {
                return;
            }
            BattleGrid grid = owner.getGrid();
            owner.setEnergy(owner.getEnergy() - 1);
            owner.setAnimAttack();

            grid.addEffect(new ArrowEffect(owner, owner.getGridRow(), owner.getGridCol() + 1, 1));
        });
    }

    public static BattleEffect buildWarning(int duration, float r, float g, float b, float a, BattleEffect replacement) {
        BattleGrid grid = replacement.owner.getGrid();
        int row = replacement.row;
        int col = replacement.col;
        if (!grid.isOnGrid(row, col)) {
            return null;
        }
        return new BattleEffect(replacement.owner, row, col, replacement.priority) {
            @Override
            public boolean tick() {
                if (numTicks > duration) {
                    grid.addEffect(replacement);
                    return false;
                }
                return true;
            }

            @Override
            public void render(SpriteBatch batch) {
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                ShapeRenderer sr = new ShapeRenderer();
                sr.setProjectionMatrix(batch.getProjectionMatrix());
                sr.begin(ShapeRenderer.ShapeType.Filled);
                BattleTile tile = grid.getTile(row, col);
                sr.setColor(r, g, b, a);
                sr.rect(tile.getScreenX(), tile.getScreenY(), tile.getWidth(), tile.getHeight());
                sr.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
            }
        };
    }
}
