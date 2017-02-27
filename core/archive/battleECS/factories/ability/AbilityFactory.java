package com.comp460.screens.battleECS.factories.ability;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.comp460.screens.battle.BattleScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matth on 2/14/2017.
 */
public class AbilityFactory {

    public String id;
    public String animID = "attack";
    public String name;
    public String description;
    public int energyCost;
    public int healthCost;
    public float castTime;

    public List<AbilityEntityFactory> entities = new ArrayList<>();

    public void addToScreen(BattleScreen screen, Entity owner) {
        for (AbilityEntityFactory entity : entities) {
            entity.addToScreen(screen, owner);
        }
    }

    public static AbilityFactory loadFromJSON(FileHandle jsonFile, Entity owner, BattleScreen screen) {
        Json json = new Json();
        AbilityFactory factory = json.fromJson(AbilityFactory.class, jsonFile);
        return factory;
    }
}
