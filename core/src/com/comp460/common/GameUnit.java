package com.comp460.common;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.comp460.battle.units.BattleUnitFactory;

import java.util.List;

/**
 * Created by matthewhammond on 1/29/17.
 */
public class GameUnit {

    private String id;
    private String name;
    private String description;

    private int maxHP;
    private int curHP;

    private int moveDist;

    private List<String> moves;

    private BattleUnitFactory battleFactory;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getCurHP() {
        return curHP;
    }

    public int getMoveDist() {
        return moveDist;
    }

    public BattleUnitFactory getBattleUnitFactory() {
        return battleFactory;
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
