package com.comp460.battle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.comp460.assets.FontManager;

/**
 * Created by matth on 2/17/2017.
 */
public class FloatingText extends BattleAnimation {

    public Vector3 vel = new Vector3(0, 10, 0);
    public BitmapFont font;
    public String text;

    public FloatingText(String text, BitmapFont font, float x, float y, float duration) {
        super(null, x, y, duration);
        this.text = text;
        this.font = font;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        this.transform.y += delta * vel.y;
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        font.draw(batch, text, transform.x, transform.y);
    }


}
