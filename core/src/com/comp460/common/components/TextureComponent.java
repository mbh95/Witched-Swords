package com.comp460.common.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Attached to entities to give them a sprite and a tint.
 */
public class TextureComponent implements Component {
    public TextureRegion texture = null;
    public Color tint;

    public TextureComponent(TextureRegion texture) {
        this(texture, Color.WHITE);
    }

    public TextureComponent(TextureRegion texture, Color tint) {
        this.texture = texture;
        this.tint = tint;
    }

    private static final ComponentMapper<TextureComponent> mapper = ComponentMapper.getFor(TextureComponent.class);
    public static TextureComponent get(Entity e) {
        return mapper.get(e);
    }
}
