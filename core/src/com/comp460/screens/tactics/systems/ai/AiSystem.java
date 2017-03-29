package com.comp460.screens.tactics.systems.ai;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.AIControlledComponent;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;
import com.comp460.screens.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.screens.tactics.components.unit.UnitStatsComponent;
import com.comp460.screens.tactics.systems.map.ValidMoveManagementSystem;

import java.util.HashSet;
import java.util.Map;
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

            System.out.println(moveQueue.size());

            Set<MapPositionComponent> movePositions = screen.getMap().computeValidMoves(toMove);
            Set<MapPositionComponent> attackPositions = screen.getMap().computeValidAttacks(toMove, movePositions);

            Entity target = null;
            int targetHP = Integer.MAX_VALUE;

            for (Entity e : getEngine().getEntitiesFor(playerUnitsFamily)) {
                MapPositionComponent playerPosition = posM.get(e);
                UnitStatsComponent playerStats = statsM.get(e);
                if (attackPositions.contains(playerPosition)) {
                    if (target == null || playerStats.base.curHP < targetHP) {
                        target = e;
                        targetHP = playerStats.base.curHP;
                    }
                }
            }
            if (target != null) {
                MapPositionComponent targetPos = posM.get(target);
                MapPositionComponent curPos = MapPositionComponent.get(toMove);
                Set<MapPositionComponent> options = new HashSet<>();
                MapPositionComponent up = new MapPositionComponent(targetPos.row + 1, targetPos.col);
                MapPositionComponent down = new MapPositionComponent(targetPos.row - 1, targetPos.col);
                MapPositionComponent left = new MapPositionComponent(targetPos.row, targetPos.col - 1);
                MapPositionComponent right = new MapPositionComponent(targetPos.row, targetPos.col + 1);

                if (movePositions.contains(up) || curPos.equals(up)) {
                    options.add(up);
                } else if (movePositions.contains(down) || curPos.equals(down)) {
                    options.add(down);
                } else if (movePositions.contains(left) || curPos.equals(left)) {
                    options.add(left);
                } else if (movePositions.contains(right) || curPos.equals(right)) {
                    options.add(right);
                }

                Map<MapPositionComponent, Integer> distances = screen.getMap().distanceMap(toMove);

                MapPositionComponent closest = null;
                int closestDist = Integer.MAX_VALUE;

                for (MapPositionComponent option : options) {
                    int dist = distances.getOrDefault(option, Integer.MAX_VALUE);
                    if (dist <= closestDist) {
                        closestDist = dist;
                        closest = option;
                    }
                }

                if (closest != null) {
                    screen.getMap().move(toMove, closest.row, closest.col);
                    screen.transitionToBattleView(target, toMove, false);
                }
            }


            toMove.remove(ReadyToMoveComponent.class);

            moveQueue.clear();
        }
    }
}
