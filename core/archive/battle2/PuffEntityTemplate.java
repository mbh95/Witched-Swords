package com.comp460.archive.battle2;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.comp460.archive.battle2.components.LocationComponent;

import java.lang.reflect.Constructor;
import java.util.Random;

/**
 * Created by matth on 2/13/2017.
 */
public class PuffEntityTemplate extends EntityTemplate {

    @Override
    public void addTo(BattleUnit owner, Engine engine) {
        Random rng = new Random();
        int hole = rng.nextInt(3);
        for (int i = 0; i < 3; i++) {
            if (i == hole) {
                continue;
            }
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
            LocationComponent puffLoc = new LocationComponent();
            puffLoc.row = i;
            puffLoc.col = owner.getGridCol();
            puffLoc.relative = false;
            move.add(puffLoc);
            engine.addEntity(move);
        }
    }
}
