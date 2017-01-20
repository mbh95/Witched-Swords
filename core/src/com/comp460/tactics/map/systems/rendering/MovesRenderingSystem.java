package com.comp460.tactics.map.systems.rendering;

import com.badlogic.ashley.core.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.Mappers;
import com.comp460.tactics.map.components.ShowValidMovesComponent;
import com.comp460.tactics.map.components.units.UnitStatsComponent;
import com.comp460.tactics.map.components.units.ValidMovesComponent;
import com.comp460.tactics.map.MapPosition;
import com.comp460.tactics.map.TacticsMap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by matthewhammond on 1/20/17.
 */
public class MovesRenderingSystem  extends EntitySystem implements EntityListener {

    private TacticsMap map;
    private OrthographicCamera camera;

    private Map<Integer, Set<MapPosition>> teamToValidMoves;

    Family visible = Family.all(ShowValidMovesComponent.class, ValidMovesComponent.class, UnitStatsComponent.class).get();

    public MovesRenderingSystem(TacticsMap map, OrthographicCamera camera) {
        this.teamToValidMoves = new HashMap<>(2);
        this.map = map;
        this.camera = camera;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(visible, this);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        for (int team : teamToValidMoves.keySet()) {
            if (team == 0) {
                sr.setColor(0f, 0f, 1f, 0.2f);
            } else {
                sr.setColor(1f, 0f, 0f, 0.2f);
            }
            sr.begin(ShapeRenderer.ShapeType.Filled);
            for (MapPosition pos : teamToValidMoves.get(team)) {
                sr.rect(pos.col * map.getTileWidth(), pos.row * map.getTileHeight(), map.getTileWidth(), map.getTileHeight());
            }
            sr.end();
        }
        sr.dispose();
    }

    @Override
    public void entityAdded(Entity entity) {
        rebuildTeam(Mappers.statsM.get(entity).team);
    }

    @Override
    public void entityRemoved(Entity entity) {
        rebuildTeam(Mappers.statsM.get(entity).team);
    }

    public void rebuildTeam(int team) {
        if (!this.teamToValidMoves.containsKey(team)) {
            this.teamToValidMoves.put(team, new HashSet<>());
        }
        this.teamToValidMoves.get(team).clear();
        this.getEngine().getEntitiesFor(visible).forEach(e -> {
            if (Mappers.statsM.get(e).team == team) {
                teamToValidMoves.get(team).addAll(Mappers.movesM.get(e).validMoves);
            }
        });
    }
}
