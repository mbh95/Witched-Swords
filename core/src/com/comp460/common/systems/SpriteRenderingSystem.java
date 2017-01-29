package com.comp460.common.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.comp460.common.components.InvisibleComponent;
import com.comp460.common.components.TransformComponent;
import com.comp460.common.components.TextureComponent;

import java.util.PriorityQueue;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class SpriteRenderingSystem extends IteratingSystem {

    private static final Family renderableFamily = Family.all(TextureComponent.class, TransformComponent.class).exclude(InvisibleComponent.class).get();

    private static final ComponentMapper<TextureComponent> textureM = ComponentMapper.getFor(TextureComponent.class);
    private static final ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);

    PriorityQueue<Entity> renderQueue;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    public SpriteRenderingSystem(SpriteBatch batch, OrthographicCamera camera) {
        super(renderableFamily);
        this.camera = camera;
        this.batch = batch;
        renderQueue = new PriorityQueue<>((Entity o1, Entity o2) ->
                Float.compare(transformM.get(o2).pos.z, transformM.get(o1).pos.z));
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Entity e: renderQueue) {
            TextureComponent texComp = textureM.get(e);
            TransformComponent t = transformM.get(e);
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
