package com.comp460.battle;

import com.badlogic.ashley.core.Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matth on 2/12/2017.
 */
public class BattleMove {
    public String id;
    public String name;
    public String description;
    public int energyCost = 0;
    public int healthCost = 0;
    public List<EntityTemplate> entities = new ArrayList<>();
    public transient BattleUnit owner;

    public void perform(Engine engine) {

        for (EntityTemplate template : entities) {
            template.addTo(owner, engine);
        }
    }
}
