package com.comp460.screens.battle.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.comp460.screens.battle.units.BattleUnit;

/**
 * Created by matthewhammond on 2/15/17.
 */
public class HumanPlayer implements BattlePlayer {

    public BattleUnit myUnit;

    public HumanPlayer(BattleUnit myUnit) {
        this.myUnit = myUnit;
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) myUnit.move(0, -1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) myUnit.move(0, 1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) myUnit.move(1, 0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) myUnit.move(-1, 0);

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) myUnit.useAbility1();
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) myUnit.useAbility2();
    }
}
