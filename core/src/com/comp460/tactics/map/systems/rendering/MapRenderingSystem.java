package com.comp460.tactics.map.systems.rendering;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.comp460.tactics.map.TacticsMap;

/**
 * Created by matthewhammond on 1/18/17.
 */
public class MapRenderingSystem extends EntitySystem {

    private TacticsMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    private boolean gridLinesVisible;

    public MapRenderingSystem(TacticsMap map, OrthographicCamera camera) {

        this.map = map;
        this.renderer = new OrthogonalTiledMapRenderer(map.getTiledMap());
        this.camera = camera;
        this.gridLinesVisible = true;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        renderer.setView(camera);
        renderer.render(map.visibleLayers);

        if (gridLinesVisible) {
            renderGridLines();
        }
    }

    public void setGridLinesVisible(boolean b) {
        this.gridLinesVisible = b;
    }

    private void renderGridLines() {
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        sr.setColor(Color.BLACK);
        sr.begin(ShapeRenderer.ShapeType.Line);
        for(int x = 0; x < map.getWidth() * map.getTileWidth(); x += map.getTileWidth())
            sr.line(x, 0, x, map.getHeight() * map.getTileHeight());
        for(int y = 0; y < map.getHeight() * map.getTileHeight(); y += map.getTileHeight())
            sr.line(0, y, map.getWidth() * map.getTileWidth(), y);
        sr.end();
        sr.dispose();
    }
}
