package com.comp460.battle.units;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.comp460.battle.BattleObject;
import com.comp460.battle.BattleScreen;
import com.comp460.assets.AnimationManager;
import com.comp460.battle.buffs.BattleBuff;
import com.comp460.common.GameUnit;

import java.util.*;

/**
 * Created by matthewhammond on 2/15/17.
 */
public class BattleUnit implements BattleObject {

    public String id;
    public String name;
    public String description;

    public int maxHP;
    public int curHP;

    public int curEnergy;

    public int curRow;
    public int curCol;

    public Vector3 transform;

    public String curAnimId;

    private boolean idleAnim;
    private Map<String, Animation<TextureRegion>> animationCache = new HashMap<>();
    public Animation<TextureRegion> curAnim;
    public float animTimer;

    public BattleScreen screen;

    public GameUnit base;

    public boolean canMove = true;

    public Queue<BattleBuff> buffs = new PriorityQueue<>();

    public BattleUnit(BattleScreen screen, int row, int col, GameUnit base) {

        this.screen = screen;
        this.base = base;

        this.curRow = row;
        this.curCol = col;

        this.id = base.getId();
        this.name = base.getName();
        this.description = base.getDescription();

        this.maxHP = base.getMaxHP();
        this.curHP = base.getCurHP();

        this.curEnergy = 5;

        this.curAnimId = AnimationManager.defaultAnimID;
        this.curAnim = AnimationManager.getUnitAnimation(id, AnimationManager.defaultAnimID);
        this.animTimer = 0f;
    }

    public void render(SpriteBatch batch, float delta) {
        transform.slerp(new Vector3(screen.colToScreenX(curCol), screen.rowToScreenY(curRow), transform.z), 0.3f);
        animTimer += delta;
        if (!idleAnim && curAnim.isAnimationFinished(animTimer)) {
            curAnim = AnimationManager.getUnitAnimation(id, AnimationManager.defaultAnimID);
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
            }
        }
    }

    public void startAnimation(String animId) {
        if (animationCache.containsKey(animId)) {
            curAnim = animationCache.get(animId);
        } else {
            curAnim = AnimationManager.getUnitAnimation(id, animId);
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

    public void useAbility() {

    }

}
