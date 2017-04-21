package com.comp460.screens.battle.players.ai;

import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.players.AiPlayer;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.enemies.ghast.Ghast;

import java.util.Random;

/**
 * Created by matthewhammond on 2/15/17.
 */
public class GhastAi extends AiPlayer {

    private float attackDelay;
    private float attackTimer = attackDelay;
    private float moveTimer = getNewMoveTimer();
    private Random rng = new Random();

    public GhastAi(BattleUnit myUnit, BattleUnit opponentUnit, BattleScreen screen) {
        super(myUnit, opponentUnit, screen);
        attackDelay = 1f/myUnit.speed;
    }

    @Override
    public void update(float delta) {
        attackTimer -= delta;
        if (attackTimer <= 0) {
            myUnit.ability1.attempt(myUnit, screen);
            attackTimer = attackDelay;
        }

        moveTimer -= delta;
        if (moveTimer <= 0) {
            myUnit.move(rng.nextInt(3) - 1, rng.nextInt(3) - 1);
            moveTimer = getNewMoveTimer();
        }
    }

    private float getNewMoveTimer() {
        return (float) (Math.random() * 1.5f + .1f);
    }
}
