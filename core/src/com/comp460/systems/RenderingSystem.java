package com.comp460.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.comp460.components.TransformComponent;
import com.comp460.components.TextureComponent;
import com.comp460.tactics.TacticsMap;

import java.util.Comparator;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class RenderingSystem extends IteratingSystem {

    private TacticsMap map;

    private Array<Entity> renderQueue;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<TransformComponent> transformM;

    private Comparator<Entity> comparator;

    public RenderingSystem(SpriteBatch batch, TacticsMap map, OrthographicCamera camera) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get());

        this.map = map;
        textureM = ComponentMapper.getFor(TextureComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);

        this.renderQueue = new Array<Entity>();
        this.camera = camera;
        this.batch = batch;

        this.comparator = new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {
                TransformComponent t1 = transformM.get(e1);
                TransformComponent t2 = transformM.get(e2);
                return (int)Math.signum(t2.pos.z - t1.pos.z);
            }
        };
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        renderQueue.sort(comparator);
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
