package com.comp460.launcher;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by matth on 2/8/2017.
 */
public class Button {

    enum ButtonState {NORMAL, HOVERED}

    public ButtonState curState;

    public Vector3 pos;
    public float width, height;

    public TextureRegion normalTexture;
    public TextureRegion hoveredTexture;

    public Button up;
    public Button down;
    public Button left;
    public Button right;

    public Runnable action = ()->{};

    public Button(float x, float y, float width, float height, TextureRegion texture, Runnable action) {
        this(x, y, width, height, texture, texture, action);
    }
    public Button(float x, float y, float width, float height, TextureRegion normalTexture, TextureRegion hoveredTexture, Runnable action) {
        this.pos = new Vector3(x, y, 0);
        this.width = width;
        this.height = height;

        this.curState = ButtonState.NORMAL;

        this.normalTexture = normalTexture;
        this.hoveredTexture = hoveredTexture;

        this.up = this;
        this.down = this;
        this.left = this;
        this.right = this;

        this.action = action;
    }

    public Button(float x, float y, TextureRegion texture, Runnable action) {
        this(x, y, texture, texture, action);
    }
    public Button(float x, float y, TextureRegion texture, TextureRegion hoveredTexture, Runnable action) {
        this(x, y, texture.getRegionWidth(), texture.getRegionHeight(), texture, hoveredTexture, action);
    }

    public void render(SpriteBatch batch) {
        if (curState == ButtonState.NORMAL) {
            batch.draw(normalTexture, pos.x, pos.y, width, height);
        } else if (curState == ButtonState.HOVERED){
            batch.draw(hoveredTexture, pos.x, pos.y, width, height);
        }
    }

    public void click() {
        this.action.run();
    }

    public void setHovered() {
        this.curState = ButtonState.HOVERED;
    }

    public void setNormal() {
        this.curState = ButtonState.NORMAL;
    }
}
