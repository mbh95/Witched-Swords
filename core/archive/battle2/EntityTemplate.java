package com.comp460.archive.battle2;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matth on 2/13/2017.
 */
public class EntityTemplate {
    public List<Component> components = new ArrayList<>();

    public void addTo(BattleUnit owner, Engine engine) {
        Entity move = new Entity();
        for (Component component : components) {
            try {
                Constructor<?> constructor = component.getClass().getConstructor(BattleUnit.class, component.getClass());
                Component copy = (Component) constructor.newInstance(owner, component);
                move.add(copy);
            } catch (Exception e) {
                e.printStackTrace();
                move.add(component);
            }
        }
        engine.addEntity(move);
    }
}
