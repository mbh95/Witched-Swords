package com.comp460.archive.battle2.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.comp460.archive.battle2.BattleScreen;
import com.comp460.archive.battle2.BattleTile;
import com.comp460.archive.battle2.components.LocationComponent;
import com.comp460.archive.battle2.components.MoveTextureComponent;
import com.comp460.archive.battle2.components.WarningComponent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by matth on 2/13/2017.
 */
public class MoveRenderingSystem extends IteratingSystem {

    private static final Family visibleFamily = Family.all(MoveTextureComponent.class, LocationComponent.class).exclude(WarningComponent.class).get();

    private static final ComponentMapper<MoveTextureComponent> texM = ComponentMapper.getFor(MoveTextureComponent.class);
    private static final ComponentMapper<LocationComponent> locM = ComponentMapper.getFor(LocationComponent.class);

    private BattleScreen screen;

    private Queue<Entity> renderQueue;

    public MoveRenderingSystem(BattleScreen screen) {
        super(visibleFamily);
        this.screen = screen;
        this.renderQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        screen.batch.begin();
        for (Entity e: renderQueue) {
            MoveTextureComponent texComp = texM.get(e);
            LocationComponent locComp = locM.get(e);
            BattleTile tile = screen.grid.getTile(locComp.row, locComp.col);
            screen.batch.draw(texComp.texture, tile.getScreenX(), tile.getScreenY());
        }
        renderQueue.clear();
        screen.batch.end();
    }
}
