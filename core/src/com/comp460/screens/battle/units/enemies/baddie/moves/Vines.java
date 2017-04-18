package com.comp460.screens.battle.units.enemies.baddie.moves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.assets.SpriteManager;
import com.comp460.screens.battle.BattleAnimation;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.enemies.baddie.Baddie;

import java.util.Random;

/**
 * Created by Belinda on 4/10/2017.
 */
public class Vines extends BattleUnitAbility {

    public Baddie baddie;
    public static TextureRegion vineSprite = SpriteManager.BATTLE.findRegion("attacks/vines");
    Random rng = new Random();

    public Vines(Baddie baddie) {
        super("vines", "Ensnare", "attack", "Vines ensnare the opponent.", 1, 0);
        this.baddie = baddie;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        int col = rng.nextInt(3);
        VinesInstance vinesInstance = new VinesInstance(baddie.curRow, col, 2.0f, 10, baddie);
        baddie.vines.add(vinesInstance);
    }

    public class VinesInstance {
        public float warningTimer;
        public float timer;
        public boolean doneDamage = false;
        public int damageAmt;
        private Baddie baddie;
        public int row, col;

        public VinesInstance(int row, int col, float duration, int damage, Baddie baddie) {
            this.row = row;
            this.col = col;
            this.warningTimer = 0.4f;
            this.timer = duration;

            this.doneDamage = false;
            this.baddie = baddie;
            this.damageAmt = damage;

        }

        public void update(BattleScreen screen, BattleUnit owner, float delta) {

            if (warningTimer > 0) {
                warningTimer -= delta;
                return;
            }

            BattleUnit opponent = screen.p2Unit;
            if (opponent == owner) {
                opponent = screen.p1Unit;
            }
            timer -= delta;

            if (opponent.curCol == col && opponent.curRow == row && !doneDamage) {
                float dealt = opponent.applyDamage(new DamageVector(damageAmt, baddie));
                doneDamage = true;
                if (dealt < 0) {
                    opponent.root(timer);
                } else {
                    timer = 0;
                }
            }
        }

        public void render(SpriteBatch batch, BattleScreen screen) {

            if (warningTimer > 0) {
                float x = screen.colToScreenX(row, col);
                float y = screen.rowToScreenY(row, col);
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
            } else if (timer > 0){
                float x = screen.colToScreenX(row, col);
                float y = screen.rowToScreenY(row, col);

                batch.draw(vineSprite, x, y);
            }
        }
    }
}
