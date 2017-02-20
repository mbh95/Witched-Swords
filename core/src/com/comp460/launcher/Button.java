package com.comp460.launcher;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by matth on 2/8/2017.
 */
public abstract class Button {

    enum ButtonState {NORMAL, HOVERED}

    public ButtonState curState;

    public Vector3 pos;
    public float width, height;

    public Button up;
    public Button down;
    public Button left;
    public Button right;

    public Runnable action = ()->{};

    public Button(float x, float y, float width, float height, Runnable action) {

        this.curState = ButtonState.NORMAL;

        this.pos = new Vector3(x, y, 0);
        this.width = width;
        this.height = height;

        this.up = this;
        this.down = this;
        this.left = this;
        this.right = this;

        this.action = action;
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

    public abstract void render(SpriteBatch batch);
}
