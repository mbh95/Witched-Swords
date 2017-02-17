package com.comp460.battle.units.protagonists.clarissa;

import com.comp460.battle.BattleScreen;
import com.comp460.battle.units.BattleUnit;
import com.comp460.battle.units.BattleUnitFactory;
import com.comp460.common.GameUnit;

/**
 * Created by matth on 2/16/2017.
 */
public class ClarissaFactory implements BattleUnitFactory {
    @Override
    public BattleUnit buildUnit(BattleScreen screen, int row, int col, GameUnit base) {
        return new Clarissa(screen, row, col, base);
    }

}
