package com.comp460.screens.battle.units.protagonists.zane.moves;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.assets.SpriteManager;
import com.comp460.screens.battle.BattleAnimation;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitAbility;
import com.comp460.screens.battle.units.DamageVector;
import com.comp460.screens.battle.units.protagonists.zane.Zane;

/**
 * Created by Belinda on 4/10/2017.
 */
public class Flurry extends BattleUnitAbility {

    Zane zane;
    public static Animation<TextureRegion> z0 = BattleAnimationManager.getBattleAnimation("attacks/za");
    public static Animation<TextureRegion> z1 = BattleAnimationManager.getBattleAnimation("attacks/zb");
    public static Animation<TextureRegion> z2 = BattleAnimationManager.getBattleAnimation("attacks/zc");

    public static TextureRegion[] zRegions = new TextureRegion[]{SpriteManager.BATTLE.findRegion("attacks/za"), SpriteManager.BATTLE.findRegion("attacks/zb"), SpriteManager.BATTLE.findRegion("attacks/zc")};
    public static Animation<TextureRegion> zcurr = z0;

    public float speed = 300f;
    public static TextureRegion sprite = SpriteManager.BATTLE.findRegion("attacks/bullet");
    public float animTimer;
    public int duration = 15;
    public boolean damage = true;
    private int row, col;
    private int state = -1;
    private GunbladeShot shot;

    public Flurry(Zane zane) {
        super("flurry", "Flurry!", "attack", "Insert description of flurry here.");
        this.zane = zane;
        this.animTimer = 0;
    }

    @Override
    public boolean canUse(BattleUnit user, BattleScreen screen) {
        return user.curEnergy >= 3 && zane.charges == 3;
    }

    @Override
    public void use(BattleUnit user, BattleScreen screen) {
        super.use(user, screen);
        user.removeEnergy(3);
        zane.charges = 0;
        this.row = zane.curRow;
        this.col = zane.curCol + 1;
        this.damage = true;
        state = 0;
        duration = 14;
    }

    public void update(BattleScreen screen, BattleUnit owner, float delta) {
        BattleUnit opponent = screen.p2Unit;
        if (opponent == owner) {
            owner = screen.p1Unit;
        }

        if (duration == 14 && state != 3 && state != -1) {
//            screen.addAnimation(new BattleAnimation(zcurr, screen.colToScreenX(0, 3), screen.rowToScreenY(0, 3), 0.25f));
//            System.out.println("making z " + state);
        }

        switch (state) {
            case -1:
                return;
            case 0:
                zcurr = z0;
                if (opponent.curRow == 2 && damage) {
                    opponent.applyDamage(new DamageVector(15, zane));
                    damage = false;
                }
                if (duration <= 0) {
                    state = 1;
                    duration = 15;
                }
                break;
            case 1:
                zcurr = z1;
                if (((opponent.curRow == 2 && opponent.curCol == 5)
                        || (opponent.curRow == 1 && opponent.curCol == 4)
                        || (opponent.curRow == 0 && opponent.curCol == 3)) && damage) {
                    opponent.applyDamage(new DamageVector(15, zane));
                    damage = false;
                }
                if (duration <= 0) {
                    state = 2;
                    duration = 15;
                }
                break;
            case 2:
                zcurr = z2;
                if (opponent.curRow == 0 && damage) {
                    opponent.applyDamage(new DamageVector(15, zane));
                    damage = false;
                }

                // making shot
                if (duration <= 0) {
                    state = 3;
                    duration = 15;
                    row = zane.curRow;
                    col = zane.curCol + 1;
                    shot = new GunbladeShot(screen.colToScreenX(row, col), screen.rowToScreenY(row, col));
                    System.out.println("making shot");
                }
                break;
            case 3:
                if (shot.updateAndReturnCollided(delta, screen)) {
                    damage = false;
                    state = -1;
                    shot = null;
                }
                duration++;
                break;
        }
        duration--;
    }

    public void render(SpriteBatch batch) {
        if (state == 3)
            shot.render(batch);
        if (state != -1 && state != 3) {
            batch.draw(zRegions[state], zane.screen.colToScreenX(0, 3), zane.screen.rowToScreenY(0, 3));
        }
    }

    public class GunbladeShot {
        public Vector3 bottomLeftPos;

        public GunbladeShot(float blx, float bly) {
            this.bottomLeftPos = new Vector3(blx, bly, 0);
        }

        public void render(SpriteBatch batch) {
            batch.draw(sprite, bottomLeftPos.x, bottomLeftPos.y);
        }

        public boolean updateAndReturnCollided(float delta, BattleScreen screen) {
            bottomLeftPos.x += speed * delta;
            BattleUnit opponent = screen.p2Unit;
            if (opponent == zane) {
                opponent = screen.p1Unit;
            }

            float tipPosX = bottomLeftPos.x + 40;
            float tipPosY = bottomLeftPos.y + 20;
            if (tipPosX >= opponent.transform.x + 15 && tipPosX <= opponent.transform.x + 30) {
                if (tipPosY >= opponent.transform.y && tipPosY <= opponent.transform.y + 40) {
                    opponent.applyDamage(new DamageVector(30, zane));
                    return true;
                }
            }
            if (tipPosX > zane.screen.colToScreenX(0, 5) + 80) {
                return true;
            }
            return false;
        }
    }
}