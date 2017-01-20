package com.comp460;

import com.badlogic.ashley.core.ComponentMapper;
import com.comp460.tactics.map.components.*;
import com.comp460.tactics.map.components.units.UnitStatsComponent;
import com.comp460.tactics.map.components.units.ValidMovesComponent;

/**
 * Created by matthewhammond on 1/19/17.
 */
public class Mappers {
    public static final ComponentMapper<CameraTargetComponent> cameraTargetM = ComponentMapper.getFor(CameraTargetComponent.class);
    public static final ComponentMapper<KeyboardMapMovementComponent> kbdMapMovementM = ComponentMapper.getFor(KeyboardMapMovementComponent.class);
    public static final ComponentMapper<MapPositionComponent> mapPosM = ComponentMapper.getFor(MapPositionComponent.class);
    public static final ComponentMapper<MovedAlreadyComponent> movedAlreadyM = ComponentMapper.getFor(MovedAlreadyComponent.class);
    public static final ComponentMapper<SelectedUnitComponent> selectedUnitM = ComponentMapper.getFor(SelectedUnitComponent.class);
    public static final ComponentMapper<ShowValidMovesComponent> showMovesM = ComponentMapper.getFor(ShowValidMovesComponent.class);
    public static final ComponentMapper<SnapToComponent> snapToM = ComponentMapper.getFor(SnapToComponent.class);
    public static final ComponentMapper<TacticsCursorComponent> tacticsCursorM = ComponentMapper.getFor(TacticsCursorComponent.class);
    public static final ComponentMapper<TextureComponent> textureM = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<UnitStatsComponent> statsM = ComponentMapper.getFor(UnitStatsComponent.class);
    public static final ComponentMapper<ValidMovesComponent> movesM = ComponentMapper.getFor(ValidMovesComponent.class);
}
