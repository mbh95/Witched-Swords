package com.comp460.battle;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by matthewhammond on 2/15/17.
 */
public interface BattleObject {
    void render(SpriteBatch batch, float delta);
    void update(float delta);
}
