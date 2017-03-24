package com.comp460.screens.tactics.systems.ai;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.AIControlledComponent;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;
import com.comp460.screens.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.screens.tactics.components.unit.UnitStatsComponent;
import com.comp460.screens.tactics.systems.map.ValidMoveManagementSystem;

import java.util.PriorityQueue;
import java.util.Set;

/**
 * Created by matth on 2/22/2017.
 */
public class AiSystem extends IteratingSystem {

    private static final Family readyAiUnitsFamily = Family.all(AIControlledComponent.class, ReadyToMoveComponent.class).get();

    private static final Family playerUnitsFamily = Family.all(PlayerControlledComponent.class, MapPositionComponent.class).get();

    private static ComponentMapper<MapPositionComponent> posM = ComponentMapper.getFor(MapPositionComponent.class);
    private static ComponentMapper<UnitStatsComponent> statsM = ComponentMapper.getFor(UnitStatsComponent.class);

    public TacticsScreen screen;

    private PriorityQueue<Entity> moveQueue;

    public AiSystem(TacticsScreen screen) {
        super(readyAiUnitsFamily);
        this.screen = screen;

        moveQueue = new PriorityQueue<>((e1, e2) -> {
            return 1;
        });
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        moveQueue.add(entity);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (!moveQueue.isEmpty()) {

            Entity toMove = moveQueue.poll();

            Set<MapPositionComponent> positions = getEngine().getSystem(ValidMoveManagementSystem.class).getValidMoves(toMove);

            Entity target = null;
            int targetHP = Integer.MAX_VALUE;

            for (Entity e : getEngine().getEntitiesFor(playerUnitsFamily)) {
                MapPositionComponent playerPosition = posM.get(e);
                UnitStatsComponent playerStats = statsM.get(e);
                if (positions.contains(posM.get(e))) {
                    if (target == null || playerStats.base.curHP < targetHP) {
                        target = e;
                        targetHP = playerStats.base.curHP;
                    }
                }
            }
            if (target != null) {
                MapPositionComponent targetPos = posM.get(target);
                screen.getMap().move(toMove, targetPos.row, targetPos.col);
            }


            toMove.remove(ReadyToMoveComponent.class);

            moveQueue.clear();
        }
    }
}
