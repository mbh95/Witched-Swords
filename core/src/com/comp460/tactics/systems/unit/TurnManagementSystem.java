package com.comp460.tactics.systems.unit;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;

/**
 * Created by matthewhammond on 2/20/17.
 */
public class TurnManagementSystem extends EntitySystem implements EntityListener {


    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

//        engine.addEntityListener();
    }

    @Override
    public void entityAdded(Entity entity) {

    }

    @Override
    public void entityRemoved(Entity entity) {

    }
}
