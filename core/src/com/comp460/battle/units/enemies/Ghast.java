package com.comp460.battle.units.enemies;

import com.comp460.battle.BattleScreen;
import com.comp460.battle.units.BattleUnit;
import com.comp460.battle.units.BattleUnitFactory;
import com.comp460.battle.units.protagonists.Clarissa;
import com.comp460.common.GameUnit;

/**
 * Created by matth on 2/15/2017.
 */
public class Ghast extends BattleUnit {

    public Ghast(BattleScreen screen, int row, int col, GameUnit base) {
        super(screen, row, col, base);
    }

    class GhastFactory implements BattleUnitFactory {

        @Override
        public BattleUnit buildUnit(BattleScreen screen, int row, int col, GameUnit base) {
            return new Ghast(screen, row, col, base);
        }
    }
}
