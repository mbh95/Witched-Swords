package com.comp460.screens.battle.units;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;
import com.comp460.screens.battle.BattleObject;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.screens.battle.FloatingText;
import com.comp460.screens.battle.buffs.BattleBuff;
import com.comp460.common.GameUnit;
import com.comp460.screens.battle.units.enemies.baddie.moves.Vines;

import java.util.*;

/**
 * Created by matthewhammond on 2/15/17.
 */
public class BattleUnit implements BattleObject {

    public static TextureRegion CONFUSION_SPRITE = SpriteManager.BATTLE.findRegion("confusion");
    public static BitmapFont damageFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.RED, Color.BLACK, 1);
    public static BitmapFont healingFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.GREEN, Color.BLACK, 1);

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

    public float speed;

    public Vector3 transform;

    public Queue<BattleBuff> buffs = new PriorityQueue<>();

    private boolean idleAnim;
    private boolean fallenAnim;
    private boolean victoryAnim;
    private Map<String, Animation<TextureRegion>> animationCache = new HashMap<>();

    public Animation<TextureRegion> curAnim;
    public float animTimer;

    public float restoreCntd = 0;

    public float confuseTimer = 0f;

    public BattleScreen screen;

    public GameUnit base;
    public List<Vines.VinesInstance> rooted = new ArrayList<>();

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

        this.speed = base.speed;

        this.curEnergy = 5;

        if (screen != null)
            this.transform = new Vector3(screen.colToScreenX(row, col), screen.rowToScreenY(row, col), 0f);
        else
            this.transform = new Vector3(0, 0, 0f);


        this.startAnimation("idle");

        if (speed != 0)
            this.restoreCntd = 1f / speed;
    }

    public void render(SpriteBatch batch, float delta) {
        transform.slerp(new Vector3(screen.colToScreenX(curRow, curCol), screen.rowToScreenY(curRow, curCol), transform.z), 0.3f);
        animTimer += delta;
        if (!idleAnim && curAnim.isAnimationFinished(animTimer)) {
            startAnimation("idle");
        }
        batch.draw(curAnim.getKeyFrame(animTimer), transform.x, transform.y);

        if (confuseTimer > 0) {
            batch.draw(CONFUSION_SPRITE, transform.x, transform.y + 40);
        }
    }

    @Override
    public void update(float delta) {
        canMove = rooted.isEmpty();
        for (Iterator<BattleBuff> iterator = buffs.iterator(); iterator.hasNext(); ) {
            BattleBuff buff = iterator.next();
            buff.tick(delta);
            if (buff.isDone()) {
                iterator.remove();
                buff.post();
            }
        }
        if (speed != 0) {
            restoreCntd -= delta;
            if (restoreCntd < 0) {
                removeEnergy(-1);
                restoreCntd = 1f / speed;
            }
        }

        if (confuseTimer > 0) {
            confuseTimer -= delta;
        }
    }

    public void startAnimation(String animId) {
        if (fallenAnim || victoryAnim || (idleAnim && animId.equals("idle"))) {
            return;
        }

        animTimer = 0f;

        if (animationCache.containsKey(animId)) {
            curAnim = animationCache.get(animId);
        } else {
            curAnim = BattleAnimationManager.getBattleUnitAnimation(id, animId);
            animationCache.put(animId, curAnim);
        }

        if (animId.equals("idle")) {
            idleAnim = true;
        } else {
            idleAnim = false;
        }

        if (animId.equals("fallen")) {
            fallenAnim = true;
            curAnim.setPlayMode(Animation.PlayMode.NORMAL);
        } else {
            fallenAnim = false;
        }

        if (animId.equals("victory")) {
            victoryAnim = true;
            curAnim.setPlayMode(Animation.PlayMode.LOOP);

        } else {
            victoryAnim = false;
        }
    }

    public void move(int dr, int dc) {

        if (!canMove) {
            return;
        }

        int newRow = curRow + dr;
        int newCol = curCol + dc;

        if (confuseTimer > 0) {
            newRow = curRow - dc;
            newCol = curCol + dr;
        }

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

    public void confuse(float duration) {
        this.confuseTimer = duration;
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
            startAnimation("fallen");
            if (damageVector.source != null) {
                damageVector.source.startAnimation("victory");
            }
            return curHP - prevHP;

        }
        if (curHP > maxHP) {
            curHP = maxHP;
        }

        int deltaHP = curHP - prevHP;
        if (deltaHP < 0) {
            screen.addAnimation(new FloatingText(deltaHP + "", damageFont, (float) (transform.x + 5f), transform.y + 40, 0.5f));
            startAnimation("hurt");

        }

        if (deltaHP > 0) {
            screen.addAnimation(new FloatingText("+" + deltaHP, healingFont, (float) (transform.x + 16f), transform.y + 40, 0.5f));
        }
        return deltaHP;
    }

    public void removeEnergy(int amt) {
        this.curEnergy -= amt;
        if (this.curEnergy < 0) {
            curEnergy = 0;
        } else if (this.curEnergy > 5) {
            curEnergy = 5;
        }
    }

    public void updateBase() {
        this.base.curHP = this.curHP;
        this.base.maxHP = this.maxHP;
    }
}
