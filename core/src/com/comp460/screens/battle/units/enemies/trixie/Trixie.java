package com.comp460.screens.battle.units.enemies.trixie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.assets.SpriteManager;
import com.comp460.common.GameUnit;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.DamageVector;

import java.util.*;

/**
 * Created by matthewhammond on 4/10/17.
 */
public class Trixie extends BattleUnit {

    public static Animation<TextureRegion> scratchAnimation = BattleAnimationManager.getBattleAnimation("attacks/scratch");

    public List<Scratch.ScratchInstance> scratches = new ArrayList<>();

    public Trixie(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);

        this.ability1 = new Scratch(this);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        for (Iterator<Scratch.ScratchInstance> iter = scratches.iterator(); iter.hasNext();) {
            Scratch.ScratchInstance scratch = iter.next();
            scratch.update(screen, this, delta);
            if (scratch.damageTimer <= 0) {
                iter.remove();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        super.render(batch, delta);

        for (Scratch.ScratchInstance s : scratches) {
            s.render(batch, this.screen);
        }
    }
}


class Scratch extends BattleUnitAbility {

    private static final TextureRegion SCRATCH_SPRITE = SpriteManager.BATTLE.findRegion("attacks/scratch");

    public Trixie trixie;
    public Random rng = new Random();

    public Scratch(Trixie trixie) {
        super("scratch", "Scratch", "attack", "A scratch attack that confuses the target.");
        this.trixie = trixie;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);

        for (int r = 0; r < screen.numRows; r++) {
            int col = rng.nextInt(screen.numRows);

            trixie.scratches.add(new ScratchInstance(r, col));
        }

    }

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curEnergy > 1;
    }

    class ScratchInstance {
        public int row, col;

        public float warningTimer;
        public float damageTimer;
        public boolean active;
        public int damage = 10;
        public float confuseDuration = 2f;

        public ScratchInstance(int row, int col) {
            this.row = row;
            this.col = col;

            this.warningTimer = 0.5f;
            this.damageTimer = 0.25f;
        }

        public void update(BattleScreen screen, BattleUnit owner, float delta) {
            if (this.warningTimer > 0) {
                this.warningTimer -= delta;
                if (this.warningTimer <= 0) {
                    this.active = true;
                }
            } else {
                this.damageTimer -= delta;
            }

            if (active) {
                BattleUnit opponent = screen.p1Unit;
                if (opponent == owner) {
                    opponent = screen.p2Unit;
                }

                if (opponent.curRow == this.row && opponent.curCol == this.col) {
                    opponent.applyDamage(new DamageVector(damage, owner));
                    opponent.confuse(confuseDuration);
                    this.active = false;
                }
            }
        }

        public void render(SpriteBatch batch, BattleScreen screen) {

            float x = screen.colToScreenX(row, col);
            float y = screen.rowToScreenY(row, col);

            if (warningTimer > 0) {
                batch.end();
                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                ShapeRenderer sr = new ShapeRenderer();
                sr.setProjectionMatrix(screen.camera.combined);
                sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.setColor(1f, 0f, 0f, 0.2f);
                sr.rect(x, y, screen.tileWidth, screen.tileHeight);
                sr.end();
                sr.dispose();
                Gdx.gl.glDisable(GL20.GL_BLEND);
                batch.begin();
            } else if (active) {
                batch.draw(SCRATCH_SPRITE, x, y);
            }
        }
    }
}

