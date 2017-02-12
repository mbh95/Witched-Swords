package com.comp460.launcher;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by matthewhammond on 2/11/17.
 */
public class TextButton extends Button {

    public String text;
    public BitmapFont font;
    private GlyphLayout layout;

    public TextButton(float x, float y, String text, BitmapFont font, TextureRegion texture, TextureRegion textureHovered, Runnable action) {
        super(x, y, texture, textureHovered, action);

        this.text = text;
        this.font = font;
        layout = new GlyphLayout(font, text);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        font.draw(batch, layout, pos.x + width/2f - layout.width/2f, pos.y + height/2f + layout.height/2f);
    }
}
