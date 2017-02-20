package com.comp460.common.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.SpriteManager;

/**
 * Created by matth on 2/19/2017.
 */
public class KeyboardController implements Controller {

    public TextureRegion zSprite = SpriteManager.COMMON.findRegion("keyboard/z");
    public TextureRegion xSprite = SpriteManager.COMMON.findRegion("keyboard/x");
    public TextureRegion dirSprite = SpriteManager.COMMON.findRegion("keyboard/arrows");


    @Override
    public boolean button1Pressed() {
        return Gdx.input.isKeyPressed(Input.Keys.Z);
    }

    @Override
    public boolean button1JustPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.Z);
    }

    @Override
    public boolean button2Pressed() {
        return Gdx.input.isKeyPressed(Input.Keys.X);
    }

    @Override
    public boolean button2JustPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.X);
    }

    @Override
    public boolean upPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.UP);
    }

    @Override
    public boolean upJustPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.UP);
    }

    @Override
    public boolean downPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.DOWN);
    }

    @Override
    public boolean downJustPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
    }

    @Override
    public boolean leftPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT);
    }

    @Override
    public boolean leftJustPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.LEFT);
    }

    @Override
    public boolean rightPressed() {
        return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    @Override
    public boolean rightJustPressed() {
        return Gdx.input.isKeyJustPressed(Input.Keys.RIGHT);
    }

    @Override
    public TextureRegion button1Sprite() {
        return zSprite;
    }

    @Override
    public TextureRegion button2Sprite() {
        return xSprite;
    }

    @Override
    public TextureRegion directionalSprite() {
        return dirSprite;
    }
}