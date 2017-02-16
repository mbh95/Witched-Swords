package com.comp460.battle.factories.ability.component;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.Mappers;
import com.comp460.battle.components.PlayerControlledComponent;
import com.comp460.battle.components.WarningComponent;
import com.comp460.battle.factories.ability.AbilityComponentFactory;

/**
 * Created by matth on 2/15/2017.
 */
public class WarningFactory implements AbilityComponentFactory {
    public float duration = 0.25f;
    @Override
    public void addToEntity(Entity base, BattleScreen screen, Entity owner) {
        Color warningColor;
        if (Mappers.playerControlM.get(owner) != null) {
            warningColor = new Color(0,0,1,0.3f);
        } else {
            warningColor = new Color(1,0,0,0.3f);
        }
        base.add(new WarningComponent(duration, warningColor));
    }
}
