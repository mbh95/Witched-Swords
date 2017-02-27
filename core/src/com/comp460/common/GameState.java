package com.comp460.common;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by matthewhammond on 2/25/17.
 */
public interface GameState {

    void init();

    void update(float delta);

    void render(SpriteBatch batch, SpriteBatch uiBatch);
}
