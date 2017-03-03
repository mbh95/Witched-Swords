package com.comp460.screens.tactics.systems.unit;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.TacticsAnimationManager;
import com.comp460.common.components.AnimationComponent;
import com.comp460.screens.tactics.components.unit.SelectedComponent;
import com.comp460.screens.tactics.components.unit.UnitStatsComponent;

import java.awt.event.ComponentListener;

/**
 * Created by matthewhammond on 3/1/17.
 */
public class UnitAnimatorSystem extends EntitySystem implements EntityListener {

    private Family selectedUnitFamily = Family.all(SelectedComponent.class, UnitStatsComponent.class, AnimationComponent.class).get();

    private ComponentMapper<UnitStatsComponent> unitM = ComponentMapper.getFor(UnitStatsComponent.class);
    private ComponentMapper<AnimationComponent> animM = ComponentMapper.getFor(AnimationComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(selectedUnitFamily, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        Animation<TextureRegion> selectedAnim = TacticsAnimationManager.getTacticsAnimation(unitM.get(entity).base.id, "select");

        animM.get(entity).animation = selectedAnim;
        animM.get(entity).timer = 0f;
    }

    @Override
    public void entityRemoved(Entity entity) {
        Animation<TextureRegion> idleAnim = TacticsAnimationManager.getTacticsAnimation(unitM.get(entity).base.id, "idle");

        animM.get(entity).animation = idleAnim;
        animM.get(entity).timer = 0f;
    }
}
