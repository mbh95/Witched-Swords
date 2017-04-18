package com.comp460.screens.battle;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.MainGame;
import com.comp460.assets.SpriteManager;
import com.comp460.common.GameScreen;
import com.comp460.common.GameUnit;
import com.comp460.common.ui.DialogueBox;
import com.comp460.screens.tactics.TacticsTutorialScreen;

/**
 * Created by matth on 4/17/2017.
 */
public class BattleTutorialScreen extends BattleScreen {

    enum TutorialState {ATTACK_Z, PICK_UP, TOOK_DAMAGE, WIN, DONE};
    private static TextureRegion tutorialSprite = SpriteManager.BATTLE.findRegion("attacks/puff");
    private TutorialState curTutState;

    public BattleTutorialScreen(MainGame game, GameScreen prevScreen, GameUnit p1UnitBase, GameUnit p2UnitBase, boolean p1Initiated, boolean exitAllowed, float time) {
        super(game, prevScreen, p1UnitBase, p2UnitBase, p1Initiated, exitAllowed, time);
        this.currentDialogueBox = DialogueBox.buildList(this, new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Welcome to your first combat battle! In these turn based battles you control a small band of heroes fighting a horde of monsters. Defeat all of the monsters to win. You lose if all of your heroes fall."),
                new DialogueBox.DialogueBoxTemplate(tutorialSprite, "The first thing you'll want to do is select a unit that you want to move. Move your cursor over one of your units and press 'z' to select it."));

        this.curTutState = TutorialState.ATTACK_Z;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        switch (curTutState) {
            case ATTACK_Z:
                if (game.controller.button1JustPressedDestructive()) {
                    this.currentDialogueBox = DialogueBox.buildList(this, new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Great job! When you have a unit selected blue squares on the map show tiles your unit can move to and red squares show where they can attack."), new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Now you need to tell your unit where to go. Move your cursor into one of the blue squares and press 'z' to order your unit to move and open the action menu."));
                    curTutState = TutorialState.PICK_UP;
                }
                break;
        }
    }
}
