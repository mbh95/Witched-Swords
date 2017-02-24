package com.comp460.common.input;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by matth on 2/19/2017.
 */
public interface Controller {
    boolean button1Pressed();
    boolean button1JustPressed();

    boolean button2Pressed();
    boolean button2JustPressed();

    boolean startPressed();
    boolean startJustPressed();

    boolean endPressed();
    boolean endJustPressed();

    boolean upPressed();
    boolean upJustPressed();

    boolean downPressed();
    boolean downJustPressed();

    boolean leftPressed();
    boolean leftJustPressed();

    boolean rightPressed();
    boolean rightJustPressed();

    TextureRegion button1Sprite();
    TextureRegion button2Sprite();
    TextureRegion startSprite();
    TextureRegion endSprite();
    TextureRegion directionalSprite();
}
