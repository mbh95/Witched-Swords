package com.comp460.screens.battle;

import com.comp460.MainGame;
import com.comp460.common.GameScreen;
import com.comp460.common.GameUnit;

/**
 * Created by matth on 4/17/2017.
 */
public class BattleTutorialScreen extends BattleScreen {

    public BattleTutorialScreen(MainGame game, GameScreen prevScreen, GameUnit p1UnitBase, GameUnit p2UnitBase, boolean p1Initiated, boolean exitAllowed, float time) {
        super(game, prevScreen, p1UnitBase, p2UnitBase, p1Initiated, exitAllowed, time);
    }


}
