package com.comp460.screens.launcher;

import com.badlogic.gdx.graphics.g2d.*;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class NinePatchTextButton extends NinePatchButton {

    public BitmapFont font;
    private GlyphLayout layout;

    public NinePatchTextButton(float x, float y, GlyphLayout layout, BitmapFont font, NinePatch ninePatch, Runnable action) {
        this(x, y, layout.width, layout.height, layout, font, ninePatch, action);
    }

    public NinePatchTextButton(float x, float y, float width, float height, GlyphLayout layout, BitmapFont font, NinePatch ninePatch, Runnable action) {
        super(x, y, width, height, ninePatch, action);
        this.font = font;
        this.layout = layout;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        font.draw(batch, layout, pos.x + width/2f - layout.width/2f, pos.y + height/2f + layout.height/2f);
    }
}
