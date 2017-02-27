package com.comp460.screens.launcher;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by matth on 2/20/2017.
 */
public class NinePatchButton extends Button{

    public NinePatch ninePatchTexture;

    public NinePatchButton(float x, float y, float width, float height, NinePatch ninePatchTexture, Runnable action) {
        super(x, y, width, height, action);

        this.ninePatchTexture = ninePatchTexture;
    }

    @Override
    public void render(SpriteBatch batch) {
        this.ninePatchTexture.draw(batch, this.pos.x, this.pos.y, width, height);
    }
}
