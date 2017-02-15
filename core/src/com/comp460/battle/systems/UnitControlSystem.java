package com.comp460.battle.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.Mappers;
import com.comp460.battle.components.*;

/**
 * Created by matth on 2/15/2017.
 */
public class UnitControlSystem extends IteratingSystem {

    private static final Family playerUnitFamily = Family.all(UnitComponent.class, GridPositionComponent.class, PlayerControlledComponent.class).exclude(StunComponent.class).get();

    private BattleScreen screen;

    public UnitControlSystem(BattleScreen screen) {
        super(playerUnitFamily);
        this.screen = screen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        GridPositionComponent pos = Mappers.gridPosM.get(entity);

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) screen.move(entity, 0, -1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) screen.move(entity, 0, 1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) screen.move(entity, 1, 0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) screen.move(entity, -1, 0);

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) screen.useAbility(entity, 0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) screen.useAbility(entity, 1);

    }
}
