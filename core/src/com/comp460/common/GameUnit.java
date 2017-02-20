package com.comp460.common;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.units.BattleUnit;
import com.comp460.battle.units.BattleUnitFactory;
import com.comp460.tactics.components.unit.UnitStatsComponent;

import java.util.List;

/**
 * Created by matthewhammond on 1/29/17.
 */
public class GameUnit {

    public String id;
    public String name;
    public String description;

    public int maxHP;
    public int curHP;

    public int moveDist;

    public List<String> moves;

    public BattleUnitFactory battleFactory;


    public BattleUnit buildBattleUnit(BattleScreen screen, int row, int col) {
        return battleFactory.buildUnit(screen, row, col, this);
    }

    public UnitStatsComponent buildTacticsUnitStatsComponent(int team) {
        return new UnitStatsComponent().populate(team, this);
    }

    public void writeToJSON(String filename) {
        Json json = new Json();
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(true);
        json.setOutputType(JsonWriter.OutputType.json);
        FileHandle out = new FileHandle(filename);
        out.writeString(json.prettyPrint(this), false);
    }

    public static GameUnit loadFromJSON(String filename) {
        Json jsonReader = new Json();
        GameUnit unit = jsonReader.fromJson(GameUnit.class, new FileHandle(filename));
        return unit;
    }

}
