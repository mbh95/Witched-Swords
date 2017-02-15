package com.comp460.archive.battle2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by matthewhammond on 1/29/17.
 */
public class BattleTile implements IRenderable {
    private float screenX, screenY;
    private TextureRegion sprite;
    private TextureRegion sideSprite;
    private int width, height;

    public BattleTile(float screenX, float screenY, TextureRegion sprite, TextureRegion sideSprite) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.sprite = sprite;
        this.sideSprite = sideSprite;
        this.width = sprite.getRegionWidth();
        this.height = sprite.getRegionHeight();
    }

    @Override
    public TextureRegion getSprite() {
        return sprite;
    }

    @Override
    public float getScreenX() {
        return screenX;
    }

    @Override
    public float getScreenY() {
        return screenY;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(getSprite(), getScreenX(), getScreenY());
        batch.draw(sideSprite, screenX, screenY - sideSprite.getRegionHeight());
        batch.end();
    }
}
