package com.comp460.screens.launcher;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by matth on 2/20/2017.
 */
public class TexturedButton extends Button {

    public TextureRegion normalTexture;
    public TextureRegion hoveredTexture;

    public TexturedButton(float x, float y, TextureRegion texture, Runnable action) {
        this(x, y, texture, texture, action);
    }
    public TexturedButton(float x, float y, TextureRegion normalTexture, TextureRegion hoveredTexture, Runnable action) {
        super(x, y, normalTexture.getRegionWidth(), normalTexture.getRegionHeight(), action);

        this.normalTexture = normalTexture;
        this.hoveredTexture = hoveredTexture;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (curState == ButtonState.NORMAL) {
            batch.draw(normalTexture, pos.x, pos.y, width, height);
        } else if (curState == ButtonState.HOVERED){
            batch.draw(hoveredTexture, pos.x, pos.y, width, height);
        }
    }
}
