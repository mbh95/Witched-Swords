package com.comp460.tactics.map.systems.rendering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.Mappers;
import com.comp460.tactics.map.MapPosition;
import com.comp460.tactics.map.components.CursorSelectionComponent;

/**
 * Created by matthewhammond on 1/20/17.
 */
public class SelectionRenderingSystem extends IteratingSystem {

    private OrthographicCamera camera;

    public SelectionRenderingSystem(OrthographicCamera camera) {
        super(Family.all(CursorSelectionComponent.class).get());
        this.camera = camera;
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Entity selection = Mappers.selectionM.get(entity).selection;
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        int team  = Mappers.statsM.get(selection).team;
        if (team == 0) {
            sr.setColor(0f, 1f, 1f, .2f);
        } else {
            sr.setColor(1f, 1f, 0f, .2f);
        }
        MapPosition pos = Mappers.mapPosM.get(selection).mapPos;
        sr.rect(pos.col * pos.map.getTileWidth(), pos.row * pos.map.getTileHeight(), pos.map.getTileWidth(), pos.map.getTileHeight());
        sr.end();
        sr.dispose();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
