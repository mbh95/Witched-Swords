package com.comp460.systems.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.comp460.Mappers;
import com.comp460.components.TransformComponent;
import com.comp460.components.TextureComponent;
import com.comp460.tactics.map.TacticsMap;

import java.util.PriorityQueue;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class SpriteRenderingSystem extends IteratingSystem {

    PriorityQueue<Entity> renderQueue;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    public SpriteRenderingSystem(SpriteBatch batch, TacticsMap map, OrthographicCamera camera) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get());

        this.camera = camera;
        this.batch = batch;
        renderQueue = new PriorityQueue<>((Entity o1, Entity o2) ->
                Float.compare(Mappers.transformM.get(o2).pos.z, Mappers.transformM.get(o1).pos.z));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Entity e: renderQueue) {
            TextureComponent texComp = Mappers.textureM.get(e);
            TransformComponent t = Mappers.transformM.get(e);
            batch.draw(texComp.texture, t.pos.x, t.pos.y);
        }
        renderQueue.clear();
        batch.end();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }
}
