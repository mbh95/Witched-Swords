package com.comp460.common;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

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

    private String action1;
    private String action2;

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

    public String getAction1() {
        return action1;
    }

    public String getAction2() {
        return action2;
    }

    public void setCurHP(int curHP) {
        if (curHP > maxHP || curHP < 0) {
            return;
        } else {
            this.curHP = curHP;
        }
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
