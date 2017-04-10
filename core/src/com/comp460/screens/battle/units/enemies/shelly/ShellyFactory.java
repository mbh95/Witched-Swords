package com.comp460.screens.battle.units.enemies.shelly;

import com.comp460.common.GameUnit;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.units.BattleUnit;
import com.comp460.screens.battle.units.BattleUnitFactory;

/**
 * Created by Belinda on 4/10/2017.
 */
public class ShellyFactory implements BattleUnitFactory {

    @Override
    public BattleUnit buildUnit(BattleScreen screen, int row, int col, GameUnit base) {
        return new Shelly(screen, row, col, base);
    }
}
