//package com.comp460.tactics.systems.rendering;
//
//import com.badlogic.ashley.core.Entity;
//import com.badlogic.ashley.core.Family;
//import com.badlogic.ashley.systems.IteratingSystem;
//import com.comp460.Mappers;
//import com.comp460.tactics.components.cursor.CursorActionMenuComponent;
//
///**
// * Created by matthewhammond on 1/25/17.
// */
//public class ActionMenuRenderingSystem extends IteratingSystem {
//
//    public ActionMenuRenderingSystem() {
//        super(Family.all(CursorActionMenuComponent.class).get());
//    }
//
//    @Override
//    protected void processEntity(Entity entity, float deltaTime) {
//        Entity actor = Mappers.actionMenuM.get(entity).actor;
//
//    }
//}
