package com.comp460.screens.battle.units.enemies.trixie;

import com.comp460.common.GameUnit;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitFactory;
import com.comp460.screens.battle.units.enemies.ghast.Ghast;

/**
 * Created by matthewhammond on 4/10/17.
 */
public class TrixieFactory implements BattleUnitFactory {

    @Override
    public BattleUnit buildUnit(BattleScreen screen, int row, int col, GameUnit base) {
        return  new Trixie(screen, row, col, base);
    }
}
