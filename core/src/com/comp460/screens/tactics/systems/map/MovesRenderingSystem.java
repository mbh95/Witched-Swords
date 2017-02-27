package com.comp460.screens.tactics.systems.map;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.ShowValidMovesComponent;
import com.comp460.screens.tactics.components.unit.UnitStatsComponent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by matthewhammond on 1/20/17.
 *
 * Renders the blue move squares.
 * commenting is my favorite!!!!!!!!!!!
 */
public class MovesRenderingSystem  extends EntitySystem implements EntityListener {

    private static final Family visible = Family.all(ShowValidMovesComponent.class, UnitStatsComponent.class).get();

    private static final ComponentMapper<UnitStatsComponent> statsM = ComponentMapper.getFor(UnitStatsComponent.class);

    private Map<Integer, Set<MapPositionComponent>> teamToValidMoves;
    private TacticsScreen parentScreen;

    public MovesRenderingSystem(TacticsScreen tacticsScreen) {
        this.teamToValidMoves = new HashMap<>(2);
        this.parentScreen = tacticsScreen;
    }


    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(visible, this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(parentScreen.getCamera().combined);
        for (int team : teamToValidMoves.keySet()) {
            if (team == 0) {
                sr.setColor(0f, 0f, 1f, 0.2f);
            } else {
                sr.setColor(1f, 0f, 0f, 0.2f);
            }
            sr.begin(ShapeRenderer.ShapeType.Filled);
            for (MapPositionComponent pos : teamToValidMoves.get(team)) {
                sr.rect(pos.col * parentScreen.getMap().getTileWidth(), pos.row * parentScreen.getMap().getTileHeight(), parentScreen.getMap().getTileWidth(), parentScreen.getMap().getTileHeight());
            }
            sr.end();
        }
        sr.dispose();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void entityAdded(Entity entity) {
        rebuildTeam(statsM.get(entity).team);
    }

    @Override
    public void entityRemoved(Entity entity) {
        rebuildTeam(statsM.get(entity).team);
    }

    public void rebuildTeam(int team) {
        if (!this.teamToValidMoves.containsKey(team)) {
            this.teamToValidMoves.put(team, new HashSet<>());
        }
        this.teamToValidMoves.get(team).clear();

        this.getEngine().getEntitiesFor(visible).forEach(e -> {
            if (statsM.get(e).team == team) {
                teamToValidMoves.get(team).addAll(parentScreen.getMap().computeValidMoves(e));
            }
        });
    }
}
