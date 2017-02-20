package com.comp460.battle.units.protagonists.zane;

import com.comp460.battle.BattleScreen;
import com.comp460.battle.units.BattleUnit;
import com.comp460.battle.units.BattleUnitFactory;
import com.comp460.common.GameUnit;

/**
 * Created by Belinda on 2/17/17.
 */
public class ZaneFactory implements BattleUnitFactory {
    @Override
    public BattleUnit buildUnit(BattleScreen screen, int row, int col, GameUnit base) {
        return new Zane(screen, row, col, base);
    }
}
