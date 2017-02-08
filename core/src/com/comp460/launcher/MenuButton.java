package com.comp460.launcher;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import static com.comp460.launcher.MenuButton.ButtonState.NORMAL;

/**
 * Created by matth on 2/8/2017.
 */
public class MenuButton {

    enum ButtonState {NORMAL, HOVERED, PRESSED};

    public MenuButton up;
    public MenuButton down;
    public MenuButton left;
    public MenuButton right;

    public TextureRegion normal;
    public TextureRegion hovered;
    public TextureRegion pressed;

    public ButtonState currenState;

    public Vector3 pos;

    public String text;
    public BitmapFont font;
    public GlyphLayout layout;

    public Runnable action = ()->{};

    public MenuButton(String text, float x, float y, BitmapFont font, TextureRegion normal, TextureRegion hovered, TextureRegion pressed) {
        this.up = this;
        this.down = this;
        this.left = this;
        this.right = this;
        this.currenState = NORMAL;
        this.pos = new Vector3(x, y, 0);
        this.text = text;
        this.font = font;
        if (font != null) {
            this.layout = new GlyphLayout(font, text);
        }
        this.normal = normal;
        this.hovered = hovered;
        this.pressed = pressed;
    }

    public void render(SpriteBatch batch) {
        switch (currenState) {
            case NORMAL:
                if (normal != null) {
                    batch.draw(normal, pos.x, pos.y);
                }
                break;
            case HOVERED:
                if (hovered != null) {
                    batch.draw(hovered, pos.x, pos.y);
                }                break;
            case PRESSED:
                if (pressed != null) {
                    batch.draw(pressed, pos.x, pos.y);
                }
                break;
        }
        font.draw(batch, text, pos.x + normal.getRegionWidth()/2f - layout.width/2f, pos.y + normal.getRegionHeight()/2f + layout.height/2f);
    }

    public void click() {
        this.currenState = ButtonState.PRESSED;
        this.action.run();
    }
}
