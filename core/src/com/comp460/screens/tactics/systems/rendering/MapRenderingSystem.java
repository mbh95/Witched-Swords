package com.comp460.screens.tactics.systems.rendering;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.TacticsMap;

/**
 * Created by matthewhammond on 1/18/17.
 */
public class MapRenderingSystem extends EntitySystem {

    private TacticsScreen parentScreen;
    private OrthogonalTiledMapRenderer renderer;
    private boolean gridLinesVisible = true;

    public MapRenderingSystem(TacticsScreen tacticsScreen) {
        this.parentScreen = tacticsScreen;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (renderer == null) {
            this.renderer = new OrthogonalTiledMapRenderer(parentScreen.getMap().getTiledMap());
        }
        renderer.setView(parentScreen.getCamera());
        renderer.render();

        if (gridLinesVisible) {
            renderGridLines();
        }
    }

    private void renderGridLines() {
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(parentScreen.getCamera().combined);
        sr.setColor(Color.BLACK);
        sr.begin(ShapeRenderer.ShapeType.Line);
        TacticsMap map = parentScreen.getMap();
        for(int x = 0; x < map.getWidth() * map.getTileWidth(); x += map.getTileWidth())
            sr.line(x, 0, x, map.getHeight() * map.getTileHeight());
        for(int y = 0; y < map.getHeight() * map.getTileHeight(); y += map.getTileHeight())
            sr.line(0, y, map.getWidth() * map.getTileWidth(), y);
        sr.end();
        sr.dispose();
    }
}
