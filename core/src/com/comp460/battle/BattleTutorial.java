package com.comp460.battle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Rectangle;
import com.comp460.common.GameUnit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matthewhammond on 2/6/17.
 */
public class BattleTutorial extends BattleScreen {

    enum TutorialState {INTRO};

    private boolean controlEnabled;
    private TutorialState tutState;

    private List<Rectangle> focusRects;

    public BattleTutorial(Game parent, int width, int height, GameUnit basePlayerUnit, GameUnit baseAiUnit) {
        super(parent, width, height, basePlayerUnit, baseAiUnit);
        controlEnabled = false;
        tutState = TutorialState.INTRO;
        focusRects = new ArrayList<>();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        drawFocus();
    }

    private void drawFocus() {
//        Gdx.gl.glEnable(GL20.GL_BLEND)
    }

    @Override
    protected void takeInput() {
        if (controlEnabled) {
            super.takeInput();
        }
    }
}
