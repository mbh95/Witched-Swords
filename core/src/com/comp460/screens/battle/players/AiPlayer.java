package com.comp460.screens.battle.players;

import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.players.ai.GhastAi;
import com.comp460.screens.battle.units.BattleUnit;

/**
 * Created by matthewhammond on 2/15/17.
 */
public abstract class AiPlayer implements BattlePlayer {

    public BattleUnit myUnit;
    public BattleUnit opponentUnit;
    public BattleScreen screen;

    public AiPlayer(BattleUnit myUnit, BattleUnit opponentUnit, BattleScreen screen) {
        this.myUnit = myUnit;
        this.opponentUnit = opponentUnit;
        this.screen = screen;
    }

    public static AiPlayer getAiFor(BattleUnit myUnit, BattleUnit opponentUnit, BattleScreen screen) {
        switch (myUnit.id) {
            case "ghast":
                return new GhastAi(myUnit, opponentUnit, screen);
            default:
                return new GhastAi(myUnit, opponentUnit, screen);
        }
    }
}
