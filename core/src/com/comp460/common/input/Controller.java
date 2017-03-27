package com.comp460.common.input;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by matth on 2/19/2017.
 */
public abstract class Controller {
    public abstract boolean button1Pressed();
    public abstract boolean button1JustPressed();

    private boolean button1Toggle = true;
    public boolean button1JustPressedDestructive() {
        boolean returnVal = button1Toggle && button1JustPressed();
        if (returnVal) {
            button1Toggle = false;
        }
        return returnVal;
    }

    public abstract boolean button2Pressed();
    public abstract boolean button2JustPressed();

    private boolean button2Toggle = true;
    public boolean button2JustPressedDestructive() {
        boolean returnVal = button2Toggle && button2JustPressed();
        if (returnVal) {
            button2Toggle = false;
        }
        return returnVal;
    }

    public abstract boolean startPressed();
    public abstract boolean startJustPressed();

    private boolean startToggle = true;
    public boolean startJustPressedDestructive() {
        boolean returnVal = startToggle && startJustPressed();
        if (returnVal) {
            startToggle = false;
        }
        return returnVal;
    }

    public abstract boolean endPressed();
    public abstract boolean endJustPressed();

    private boolean endToggle = true;
    public boolean endJustPressedDestructive() {
        boolean returnVal = endToggle && endJustPressed();
        if (returnVal) {
            endToggle = false;
        }
        return returnVal;
    }

    public abstract boolean upPressed();
    public abstract boolean upJustPressed();

    private boolean upToggle = true;
    public boolean upJustPressedDestructive() {
        boolean returnVal = upToggle && upJustPressed();
        if (returnVal) {
            upToggle = false;
        }
        return returnVal;
    }

    public abstract boolean downPressed();
    public abstract boolean downJustPressed();

    private boolean downToggle = true;
    public boolean downJustPressedDestructive() {
        boolean returnVal = downToggle && downJustPressed();
        if (returnVal) {
            downToggle = false;
        }
        return returnVal;
    }

    public abstract boolean leftPressed();
    public abstract boolean leftJustPressed();

    private boolean leftToggle = true;
    public boolean leftJustPressedDestructive() {
        boolean returnVal = leftToggle && leftJustPressed();
        if (returnVal) {
            leftToggle = false;
        }
        return returnVal;
    }


    public abstract boolean rightPressed();
    public abstract boolean rightJustPressed();

    private boolean rightToggle = true;
    public boolean rightJustPressedDestructive() {
        boolean returnVal = rightToggle && rightJustPressed();
        if (returnVal) {
            rightToggle = false;
        }
        return returnVal;
    }

    public abstract TextureRegion button1Sprite();
    public abstract TextureRegion button2Sprite();
    public abstract TextureRegion startSprite();
    public abstract TextureRegion endSprite();
    public abstract TextureRegion directionalSprite();

    public void update() {
        button1Toggle = true;
        button2Toggle = true;
        startToggle = true;
        endToggle = true;
        upToggle = true;
        downToggle = true;
        leftToggle = true;
        rightToggle = true;
    }
}
