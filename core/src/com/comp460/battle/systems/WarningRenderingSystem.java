package com.comp460.battle.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.battle.BattleScreen;
import com.comp460.battle.Mappers;
import com.comp460.battle.components.GridPositionComponent;
import com.comp460.battle.components.WarningComponent;

/**
 * Created by matth on 2/13/2017.
 */
public class WarningRenderingSystem extends IteratingSystem {
    private static final Family visibleWarningFamily = Family.all(WarningComponent.class, GridPositionComponent.class).get();

    private BattleScreen screen;

    public WarningRenderingSystem(BattleScreen screen) {
        super(visibleWarningFamily);
        this.screen = screen;
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(screen.camera.combined);
        Color c = Mappers.warningM.get(entity).color;
        GridPositionComponent locComp = Mappers.gridPosM.get(entity);

        float x = screen.colToScreenX(locComp.col);
        float y = screen.rowToScreenY(locComp.row);
        sr.setColor(c);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.rect(x, y, screen.tileWidth, screen.tileHeight);
        sr.end();
        sr.dispose();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
