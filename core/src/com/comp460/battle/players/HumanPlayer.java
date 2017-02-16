package com.comp460.battle.players;

import com.comp460.battle.units.BattleUnit;

/**
 * Created by matthewhammond on 2/15/17.
 */
public class HumanPlayer implements BattlePlayer {

    public BattleUnit myUnit;

    public HumanPlayer(BattleUnit myUnit) {
        this.myUnit = myUnit;
    }

    @Override
    public void update(float delta) {

    }
}
