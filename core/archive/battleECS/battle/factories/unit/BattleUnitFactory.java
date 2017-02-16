package com.comp460.battle.factories.unit;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.battle.AnimationManager;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.components.*;
import com.comp460.battle.factories.ability.AbilityFactory;
import com.comp460.common.GameUnit;
import com.comp460.common.components.AnimationComponent;
import com.comp460.common.components.TextureComponent;
import com.comp460.common.components.TransformComponent;

/**
 * Created by matth on 2/14/2017.
 */
public class BattleUnitFactory {
    public GameUnit base;

    public BattleUnitFactory(GameUnit base) {
        this.base = base;
    }

    public void addToBattleScreen(BattleScreen screen, int row, int col, Component... extras) {
        Entity unit = new Entity();

        unit.add(new HealthComponent(base.getMaxHP(), base.getCurHP()));
        unit.add(new EnergyComponent(5));
        unit.add(new UnitComponent(base.getId()));

        AbilitiesComponent abilitiesComponent = new AbilitiesComponent();
        for (String moveID : this.base.getMoves()) {
            abilitiesComponent.moves.add(AbilityFactory.loadFromJSON(Gdx.files.internal("json/moves/" + moveID + ".json"), unit, screen));
        }
        unit.add(abilitiesComponent);

        unit.add(new GridPositionComponent(row, col));
        unit.add(new TransformComponent().populate(screen.colToScreenX(col), screen.rowToScreenY(row), 0f));

        Animation<TextureRegion> defaultAnim = AnimationManager.getUnitAnimation(base.getId(), AnimationManager.defaultAnimID);
        unit.add(new TextureComponent().populate(defaultAnim.getKeyFrame(0f)));
        unit.add(new AnimationComponent().populate(defaultAnim));

        for (Component extra : extras) {
            unit.add(extra);
        }

        screen.engine.addEntity(unit);
    }
}
