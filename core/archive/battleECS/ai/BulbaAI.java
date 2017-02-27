package com.comp460.screens.battleECS.ai;

import com.badlogic.ashley.core.Entity;

import java.util.Random;

/**
 * Created by matth on 2/15/2017.
 */
public class BulbaAI implements AI {
    public enum AiState {OFFENSE, DEFENSE};
    public AiState curAiState = AiState.OFFENSE;
    public int aiDelay = 30;
    public Random rng = new Random();

    @Override
    public void tick(Entity aiUnit, float delta) {
//        if (aiDelay == 0) {
//            aiDelay = 30;
//            if (rng.nextDouble() < .1) {
//                if (curAiState == AiState.DEFENSE) {
//                    curAiState = AiState.OFFENSE;
//                } else {
//                    curAiState = AiState.DEFENSE;
//                }
//            }
//            if (playerUnit.getEnergy() < aiUnit.getEnergy() && rng.nextDouble() < .4) {
//                curAiState = AiState.OFFENSE;
//            }
//            if (aiUnit.getEnergy() <= 0) {
//                curAiState = AiState.DEFENSE;
//            }
////            boolean isEnemyAttacking = false;
////            for (BattleEffect att : grid.getEffects()) {
////                if (att. == playerUnit) {
////                    isEnemyAttacking = true;
////                }
////            }
////            if (isEnemyAttacking && rng.nextDouble() < 0.2) {
////                curAiState = AiState.DEFENSE;
////            }
//
//            switch(curAiState) {
//                case OFFENSE:
//                    if (aiUnit.getGridRow() != playerUnit.getGridRow()) {
////                        aiUnit.setGridRow(aiUnit.getGridRow()+(int)((1.0*playerUnit.getGridRow() - aiUnit.getGridRow())));
//                        aiUnit.move(rng.nextBoolean()?1:-1, 0);
//                        if (aiUnit.getGridRow() < playerUnit.getGridRow()) {
//                            aiUnit.move(1, 0);
//                        } else if (aiUnit.getGridRow() > playerUnit.getGridRow()) {
//                            aiUnit.move(-1, 0);
//                        }
////                        System.out.println((int)(1.0*playerUnit.getGridRow() - aiUnit.getGridRow()));
//                    } //else {
//                    if (aiUnit.getGridCol() == 3 && rng.nextDouble() < .7 && aiUnit.getEnergy() != 0) {
//                        aiUnit.action1();
//                    }
////                    }
//                    aiUnit.setGridCol(aiUnit.getGridCol() - 1);
//                    break;
//                case DEFENSE:
//                    aiUnit.setGridCol(aiUnit.getGridCol() + 1);
//                    if (aiUnit.getGridRow() == playerUnit.getGridRow()) {
//                        aiUnit.move(rng.nextBoolean()?1:-1, 0);
//                        if (aiUnit.getGridRow() < 0) {
//                            aiUnit.move(2, 0);
//                        } else if (aiUnit.getGridRow() >= 3) {
//                            aiUnit.move(-2, 0);
//                        }
//                    }
//                    break;
//            }
//        } else {
//            aiDelay--;
//        }
    }
}
