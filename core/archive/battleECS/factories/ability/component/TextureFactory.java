package com.comp460.screens.battleECS.factories.ability.component;

import com.badlogic.ashley.core.Entity;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.battle.factories.ability.AbilityComponentFactory;
import com.comp460.common.SpriteManager;
import com.comp460.common.components.TextureComponent;

/**
 * Created by matth on 2/15/2017.
 */
public class TextureFactory implements AbilityComponentFactory {
    public String path;
    @Override
    public void addToEntity(Entity base, BattleScreen screen, Entity owner) {
        base.add(new TextureComponent().populate(SpriteManager.BATTLE.findRegion(path)));
    }
}
