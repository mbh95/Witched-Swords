package com.comp460.tactics.factories;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.comp460.assets.TacticsAnimationManager;
import com.comp460.common.GameUnit;
import com.comp460.tactics.TacticsMap;
import com.comp460.tactics.components.core.AnimationComponent;
import com.comp460.tactics.components.core.TextureComponent;
import com.comp460.tactics.components.core.TransformComponent;
import com.comp460.tactics.components.map.MapPositionComponent;
import com.comp460.tactics.components.unit.AIControlledComponent;
import com.comp460.tactics.components.unit.PlayerControlledComponent;
import com.comp460.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.tactics.components.unit.UnitStatsComponent;

/**
 * Created by matthewhammond on 2/21/17.
 */
public class UnitFactory {
    private static final ComponentMapper<AnimationComponent> animM = ComponentMapper.getFor(AnimationComponent.class);
    public static Entity makeUnit(TacticsMap map, String id, int team, int r, int c) {
        Entity unit = new Entity();

        unit.add(new AnimationComponent(TacticsAnimationManager.getTacticsAnimation(id, "idle")));
        unit.add(new TextureComponent(animM.get(unit).animation.getKeyFrame(0f)));

        unit.add(new MapPositionComponent(r, c));
        unit.add(new TransformComponent(map.getTileX(r, c), map.getTileY(r, c), 0));

        if (team == 0) {
            unit.add(new PlayerControlledComponent());
            unit.add(new UnitStatsComponent(team, GameUnit.loadFromJSON("json/units/protagonists/" + id + ".json")));
            unit.add(new ReadyToMoveComponent());
        } else {
            unit.add(new AIControlledComponent());
            unit.add(new UnitStatsComponent(team, GameUnit.loadFromJSON("json/units/enemies/" + id + ".json")));
        }
        return unit;
    }
}
