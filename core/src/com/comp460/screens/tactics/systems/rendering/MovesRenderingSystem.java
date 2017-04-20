package com.comp460.screens.tactics.systems.rendering;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;
import com.comp460.screens.tactics.components.unit.ShowValidMovesComponent;
import com.comp460.screens.tactics.components.unit.UnitStatsComponent;
import com.comp460.screens.tactics.systems.map.ValidMoveManagementSystem;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Renders the blue move squares.
 * commenting is my favorite!!!!!!!!!!!
 */
public class MovesRenderingSystem extends EntitySystem {

    private static final Family visible = Family.all(ShowValidMovesComponent.class, UnitStatsComponent.class, MapPositionComponent.class).get();

    private static final Family playerControlled = Family.all(PlayerControlledComponent.class).get();
    private static final Family aiControlled = Family.all(PlayerControlledComponent.class).get();

    private TacticsScreen parentScreen;

    public MovesRenderingSystem(TacticsScreen tacticsScreen, int priority) {
        super(priority);
        this.parentScreen = tacticsScreen;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(parentScreen.getCamera().combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);

        ValidMoveManagementSystem moveManager = getEngine().getSystem(ValidMoveManagementSystem.class);
        for (Entity e : this.getEngine().getEntitiesFor(visible)) {
            Set<MapPositionComponent> attackSquares = parentScreen.getMap().computeValidAttacks(e);
            Set<MapPositionComponent> moveSquares = parentScreen.getMap().computeValidMoves(e);
            for (MapPositionComponent pos : moveManager.getValidMoves(e)) {
                if (parentScreen.getMap().getUnitAt(pos.row, pos.col) != null) {
                    continue;
                }
                if (playerControlled.matches(e)) {
                    sr.setColor(0f, 0f, 1f, 0.2f);
                } else {
                    sr.setColor(1f, 0f, 0f, 0.2f);
                }
                sr.rect(pos.col * parentScreen.getMap().getTileWidth(), pos.row * parentScreen.getMap().getTileHeight(), parentScreen.getMap().getTileWidth(), parentScreen.getMap().getTileHeight());
            }
            attackSquares.removeAll(moveSquares);
            sr.setColor(1f, 0f, 0f, 0.2f);
            for (MapPositionComponent pos : attackSquares) {
                sr.rect(pos.col * parentScreen.getMap().getTileWidth(), pos.row * parentScreen.getMap().getTileHeight(), parentScreen.getMap().getTileWidth(), parentScreen.getMap().getTileHeight());
            }
        }
        sr.end();
        sr.dispose();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
