package com.comp460.battle.units.protagonists;

import com.comp460.battle.BattleScreen;
import com.comp460.battle.units.BattleUnit;
import com.comp460.battle.units.BattleUnitFactory;
import com.comp460.common.GameUnit;

/**
 * Created by matth on 2/15/2017.
 */
public class Clarissa extends BattleUnit {

    public Clarissa(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);
    }

    class ClarissaFactory implements BattleUnitFactory {

        @Override
        public BattleUnit buildUnit(BattleScreen screen, int row, int col, GameUnit base) {
            return new Clarissa(screen, row, col, base);
        }
    }
}
