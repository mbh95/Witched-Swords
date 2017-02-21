package com.comp460.battle.units;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.comp460.assets.FontManager;
import com.comp460.battle.BattleObject;
import com.comp460.battle.BattleScreen;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.battle.FloatingText;
import com.comp460.battle.buffs.BattleBuff;
import com.comp460.common.GameUnit;

import java.util.*;

/**
 * Created by matthewhammond on 2/15/17.
 */
public class BattleUnit implements BattleObject {

    public static BitmapFont damageFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.RED);
    public static BitmapFont healingFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.GREEN);

    public String id;
    public String name;
    public String description;

    public boolean canMove = true;

    public BattleUnitAbility ability1 = BattleUnitAbility.getNullMove();
    public BattleUnitAbility ability2 = BattleUnitAbility.getNullMove();

    public int maxHP;
    public int curHP;

    public int curEnergy;

    public int curRow;
    public int curCol;

    public Vector3 transform;

    public Queue<BattleBuff> buffs = new PriorityQueue<>();

    private boolean idleAnim;
    private Map<String, Animation<TextureRegion>> animationCache = new HashMap<>();
    public Animation<TextureRegion> curAnim;
    public float animTimer;

    public BattleScreen screen;

    public GameUnit base;

    public BattleUnit(BattleScreen screen, int row, int col, GameUnit base) {

        this.screen = screen;
        this.base = base;

        this.curRow = row;
        this.curCol = col;

        this.id = base.id;
        this.name = base.name;
        this.description = base.description;

        this.maxHP = base.maxHP;
        this.curHP = base.curHP;

        this.curEnergy = 5;

        if (screen != null)
            this.transform = new Vector3(screen.colToScreenX(col), screen.rowToScreenY(row), 0f);
        else
            this.transform = new Vector3(0, 0, 0f);


        this.curAnim = BattleAnimationManager.getBattleUnitAnimation(id, BattleAnimationManager.defaultBattleAnimID);
        this.animTimer = 0f;
    }

    public void render(SpriteBatch batch, float delta) {
        transform.slerp(new Vector3(screen.colToScreenX(curCol), screen.rowToScreenY(curRow), transform.z), 0.3f);
        animTimer += delta;
        if (!idleAnim && curAnim.isAnimationFinished(animTimer)) {
            curAnim = BattleAnimationManager.getBattleUnitAnimation(id, BattleAnimationManager.defaultBattleAnimID);
            curAnim.setPlayMode(Animation.PlayMode.LOOP);
            animTimer = 0f;
            idleAnim = true;

        }
        batch.draw(curAnim.getKeyFrame(animTimer), transform.x, transform.y);
    }

    @Override
    public void update(float delta) {
        for (Iterator<BattleBuff> iterator = buffs.iterator(); iterator.hasNext(); ) {
            BattleBuff buff = iterator.next();
            buff.tick(delta);
            if (buff.isDone()) {
                iterator.remove();
                buff.post();
            }
        }
    }

    public void startAnimation(String animId) {
        if (animationCache.containsKey(animId)) {
            curAnim = animationCache.get(animId);
        } else {
            curAnim = BattleAnimationManager.getBattleUnitAnimation(id, animId);
            animationCache.put(animId, curAnim);
        }
        animTimer = 0f;
        idleAnim = false;
    }

    public void move(int dr, int dc) {
        if (!canMove) {
            return;
        }
        int newRow = curRow + dr;
        int newCol = curCol + dc;
        if (!screen.isOnGrid(newRow, newCol)) {
            // Can't move off the grid
            return;
        }
        if (screen.isOnLHS(curRow, curCol) != screen.isOnLHS(newRow, newCol)) {
            // Can't move to the other side of the grid
            return;
        }
        this.curRow = newRow;
        this.curCol = newCol;
    }

    public void useAbility1() {
        if (this.ability1.canUse(this, screen)) {
            this.ability1.use(this, screen);
            this.startAnimation(this.ability1.animationId);
        }
    }

    public void useAbility2() {
        if (this.ability2.canUse(this, screen)) {
            this.ability2.use(this, screen);
            this.startAnimation(this.ability2.animationId);
        }
    }

    public boolean canUseAbility() {
        return ability1.canUse(this, screen) || ability2.canUse(this, screen);
    }

    public float applyDamage(DamageVector damageVector) {
        int prevHP = curHP;
        this.curHP -= damageVector.trueDamage;
        if (curHP <= 0) {
            curHP = 0;
        }
        if (curHP > maxHP) {
            curHP = maxHP;
        }

        int deltaHP = curHP - prevHP;
        if (deltaHP < 0) {
            screen.addAnimation(new FloatingText(deltaHP+"", damageFont, (float)(transform.x + 5f), transform.y + 40, 0.5f));
        }

        if (deltaHP > 0) {
            screen.addAnimation(new FloatingText("+"+deltaHP, healingFont, (float)(transform.x + 16f), transform.y + 40, 0.5f));
        }
        return deltaHP;
    }

    public void updateBase() {
        this.base.curHP = this.curHP;
        this.base.maxHP = this.maxHP;
    }
}
