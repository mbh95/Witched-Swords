package com.comp460.archive.battle2.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.archive.battle2.BattleScreen;
import com.comp460.archive.battle2.BattleTile;
import com.comp460.archive.battle2.components.LocationComponent;
import com.comp460.archive.battle2.components.WarningComponent;

/**
 * Created by matth on 2/13/2017.
 */
public class WarningRenderingSystem extends IteratingSystem {
    private static final Family visibleWarningFamily = Family.all(WarningComponent.class, LocationComponent.class).get();

    private static final ComponentMapper<WarningComponent> warningM = ComponentMapper.getFor(WarningComponent.class);
    private static final ComponentMapper<LocationComponent> locM = ComponentMapper.getFor(LocationComponent.class);

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
        Color c = warningM.get(entity).color;
        LocationComponent locComp = locM.get(entity);
        BattleTile tile = screen.grid.getTile(locComp.row, locComp.col);

        sr.setColor(c);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.rect(tile.getScreenX(), tile.getScreenY(), tile.getWidth(), tile.getHeight());
        sr.end();
        sr.dispose();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
