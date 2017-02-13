package com.comp460.battle;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.comp460.battle.components.LocationComponent;

import java.lang.reflect.Constructor;
import java.util.Random;

/**
 * Created by matth on 2/13/2017.
 */
public class ScratchEntityTemplate extends EntityTemplate {

    @Override
    public void addTo(BattleUnit owner, Engine engine) {
        Random rng = new Random();
        for (int i = 0; i < 3; i++) {
            int col = rng.nextInt(3);
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
            puffLoc.col = col;
            puffLoc.relative = false;
            move.add(puffLoc);
            engine.addEntity(move);
        }
    }
}
