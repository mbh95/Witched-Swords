//package com.comp460.screens.battle.systems;
//
//import com.badlogic.ashley.core.ComponentMapper;
//import com.badlogic.ashley.core.Entity;
//import com.badlogic.ashley.core.Family;
//import com.badlogic.ashley.systems.IteratingSystem;
//import com.comp460.archive.battle2.components.BlockMoveComponent;
//import com.comp460.archive.battle2.components.LocationComponent;
//import com.comp460.archive.battle2.components.OwnerComponent;
//import com.comp460.archive.battle2.components.WarningComponent;
//import com.comp460.screens.battle.components.*;
//
///**
// * Created by matth on 2/13/2017.
// */
//public class BlockingSystem extends IteratingSystem {
//
//    private static final Family blockingFamily = Family.all(LocationComponent.class, BlockMoveComponent.class).exclude(WarningComponent.class).get();
//    private static final Family blockableFamily = Family.all(LocationComponent.class).exclude(WarningComponent.class).get();
//
//    private static final ComponentMapper<LocationComponent> locM = ComponentMapper.getFor(LocationComponent.class);
//
//
//    public BlockingSystem() {
//        super(blockingFamily);
//    }
//
//    @Override
//    protected void processEntity(Entity entity, float deltaTime) {
//        LocationComponent blockerLoc = locM.get(entity);
//        for (Entity e : this.getEngine().getEntitiesFor(blockableFamily)) {
//            if (e == entity) {
//                continue;
//            }
//            LocationComponent loc = locM.get(e);
//            if (loc.row == blockerLoc.row && loc.col == blockerLoc.col) {
//                this.getEngine().removeEntity(e);
//                OwnerComponent ownerComp;
//                if ((ownerComp = entity.getComponent(OwnerComponent.class)) != null) {
//                    ownerComp.owner.addFloatingText("Block!");
//                }
//            }
//        }
//    }
//}
