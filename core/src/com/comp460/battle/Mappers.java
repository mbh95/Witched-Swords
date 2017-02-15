package com.comp460.battle;

import com.badlogic.ashley.core.ComponentMapper;
import com.comp460.battle.components.*;
import com.comp460.common.components.AnimationComponent;
import com.comp460.common.components.TransformComponent;

/**
 * Created by matth on 2/15/2017.
 */
public class Mappers {
    public static final ComponentMapper<AbilitiesComponent> abilitiesM = ComponentMapper.getFor(AbilitiesComponent.class);
    public static final ComponentMapper<DamageComponent> damageM = ComponentMapper.getFor(DamageComponent.class);
    public static final ComponentMapper<EnergyComponent> energyM = ComponentMapper.getFor(EnergyComponent.class);
    public static final ComponentMapper<ExpiringComponent> expiringM = ComponentMapper.getFor(ExpiringComponent.class);
    public static final ComponentMapper<GridPositionComponent> gridPosM = ComponentMapper.getFor(GridPositionComponent.class);
    public static final ComponentMapper<HealthComponent> healthM = ComponentMapper.getFor(HealthComponent.class);
    public static final ComponentMapper<OwnerComponent> ownerM = ComponentMapper.getFor(OwnerComponent.class);
    public static final ComponentMapper<PlayerControlledComponent> playerControlM = ComponentMapper.getFor(PlayerControlledComponent.class);
    public static final ComponentMapper<ProjectileComponent> projectileM = ComponentMapper.getFor(ProjectileComponent.class);
    public static final ComponentMapper<StunComponent> stunM = ComponentMapper.getFor(StunComponent.class);
    public static final ComponentMapper<UnitComponent> unitM = ComponentMapper.getFor(UnitComponent.class);
    public static final ComponentMapper<WarningComponent> warningM = ComponentMapper.getFor(WarningComponent.class);

    public static final ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<AnimationComponent> animM = ComponentMapper.getFor(AnimationComponent.class);

}
