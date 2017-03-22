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
 * Switches tactics unit animations if a unit becomes selected or deselected.
 */
public class UnitAnimatorSystem extends EntitySystem implements EntityListener {

    private Family selectedUnitFamily = Family.all(SelectedComponent.class, UnitStatsComponent.class, AnimationComponent.class).get();
    private Family unitFamily = Family.all(UnitStatsComponent.class, AnimationComponent.class).get();

    private ComponentMapper<UnitStatsComponent> unitM = ComponentMapper.getFor(UnitStatsComponent.class);
    private ComponentMapper<AnimationComponent> animM = ComponentMapper.getFor(AnimationComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(selectedUnitFamily, this);
    }

    @Override
    public void entityAdded(Entity entity) {
        // Switch to a selected animation.
        Animation<TextureRegion> selectedAnim = TacticsAnimationManager.getTacticsAnimation(unitM.get(entity).base.id, "select");
        animM.get(entity).animation = selectedAnim;
        animM.get(entity).timer = 0f;
        animM.get(entity).animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public void entityRemoved(Entity entity) {
        // Make sure a newly de-selected unit stil has animation and unitStats components.
        if (!unitFamily.matches(entity)) {
            return;
        }

        // Switch back to idle animation.
        Animation<TextureRegion> idleAnim = TacticsAnimationManager.getTacticsAnimation(unitM.get(entity).base.id, "idle");
        animM.get(entity).animation = idleAnim;
        animM.get(entity).timer = 0f;
        animM.get(entity).animation.setPlayMode(Animation.PlayMode.LOOP);

    }
}
