package com.comp460.screens.tactics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.MainGame;
import com.comp460.assets.SpriteManager;
import com.comp460.assets.TacticsAnimationManager;
import com.comp460.common.GameScreen;
import com.comp460.common.ui.DialogueBox;
import com.comp460.screens.launcher.main.MainMenuScreen;
import com.comp460.screens.tactics.components.cursor.ActionMenuComponent;
import com.comp460.screens.tactics.components.cursor.MapCursorSelectionComponent;
import com.comp460.screens.tactics.components.cursor.MovementPathComponent;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;
import com.comp460.screens.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.screens.tactics.systems.ai.AiSystem;
import com.comp460.screens.tactics.systems.ai.PassiveAiSystem;

/**
 * Created by matth on 4/16/2017.
 */
public class TacticsTutorialScreen extends TacticsScreen {

    enum TutorialState {SELECT_A_UNIT, OPEN_ACTION_MENU, MAKE_FIRST_MOVE, END_YOUR_TURN, ATTACK, RESUME, WIN, DONE};

    private Entity waitingOnThisUnit = null;
    private static TextureRegion tutorialSprite = SpriteManager.BATTLE.findRegion("attacks/puff");

    private static Family readyPlayerFamily = Family.all(PlayerControlledComponent.class, ReadyToMoveComponent.class).get();

    private TutorialState curTutState;

    public TacticsTutorialScreen(MainGame game, GameScreen prevScreen, String mapJSONFile) {
        this(game, prevScreen, prevScreen, mapJSONFile);
    }

    public TacticsTutorialScreen(MainGame game, GameScreen onWinScreen, GameScreen onLoseScreen, String mapJSONFile) {
        super(game, onWinScreen, onLoseScreen, mapJSONFile);

        AiSystem aiSystem = engine.getSystem(AiSystem.class);
        engine.removeSystem(aiSystem);
        engine.addSystem(new PassiveAiSystem(this, aiSystem.priority));
        this.currentDialogueBox = DialogueBox.buildList(this, new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Welcome to your first tactics battle! In these turn based battles you control a small band of heroes fighting a horde of monsters. Defeat all of the monsters to win. You lose if all of your heroes fall."),
                new DialogueBox.DialogueBoxTemplate(tutorialSprite, "The first thing you'll want to do is select a unit that you want to move. Move your cursor over one of your units and press 'z' to select it."));

        this.curTutState = TutorialState.SELECT_A_UNIT;
    }

    @Override
    public void update(float delta) {
        super.update(delta);

//        System.out.println(curTutState);
        switch (curTutState) {
            case SELECT_A_UNIT:
                if (cursor.getComponent(MovementPathComponent.class) != null) {
                    this.currentDialogueBox = DialogueBox.buildList(this, new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Great job! When you have a unit selected blue squares on the map show tiles your unit can move to and red squares show where they can attack."), new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Now you need to tell your unit where to go. Move your cursor into one of the blue squares and press 'z' to order your unit to move and open the action menu."));
                    curTutState = TutorialState.OPEN_ACTION_MENU;
                }
                break;
            case OPEN_ACTION_MENU:
                if (cursor.getComponent(ActionMenuComponent.class) != null) {
                    waitingOnThisUnit = MapCursorSelectionComponent.get(cursor).selected;
                    this.currentDialogueBox = DialogueBox.buildList(this, new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Awesome! The action menu is the little menu that just popped up in the top left corner of the screen. You use it to tell a unit what to do once it gets to the square you told it to move to."), new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Tell your unit to 'Wait' and it will simply move to the new square and end its action."));
                    curTutState = TutorialState.MAKE_FIRST_MOVE;
                }
                break;
            case MAKE_FIRST_MOVE:
                if (waitingOnThisUnit != null && waitingOnThisUnit.getComponent(ReadyToMoveComponent.class) == null) {
                    this.currentDialogueBox = DialogueBox.buildList(this, new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Nice one! Now end your turn by either moving all of your units or pressing 'esc' to open the pause menu and selecting 'End Turn'"));
                    curTutState = TutorialState.END_YOUR_TURN;
                }
                break;
            case END_YOUR_TURN:
                if (engine.getEntitiesFor(readyPlayerFamily).size() == 0) {
                    this.currentDialogueBox = DialogueBox.buildList(this, new DialogueBox.DialogueBoxTemplate(tutorialSprite, "It would normally be the enemy's turn now, but for this tutorial the enemy AI is completely passive."), new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Your next goal is to attack an enemy. Move one of your units adjacent to the enemy unit and select the attack command from the action menu."));
                    curTutState = TutorialState.ATTACK;
                }
                break;
            case RESUME:
                this.currentDialogueBox = DialogueBox.buildList(this, new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Damage dealt during the battle is reflected in the unit's health bars here on the tactics map. When a unit's HP reaches zero it is removed from the tactics map altogether."), new DialogueBox.DialogueBoxTemplate(tutorialSprite, "When your units get low on HP Clarissa can heal herself and any adjacent, damaged, units others when she moves next to them."), new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Now keep attacking the enemy unit until you defeat it."));
                curTutState = TutorialState.WIN;
            case WIN:
                if (this.curState == TacticsState.PLAYER_WIN) {
                    this.currentDialogueBox = DialogueBox.buildList(this, new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Congrats! You just won your first battle!"));
                    curTutState = TutorialState.DONE;
                } else if (this.curState == TacticsState.AI_WIN){
                    this.currentDialogueBox = DialogueBox.buildList(this, new DialogueBox.DialogueBoxTemplate(tutorialSprite, "Ouch! Better luck next time."));
                    curTutState = TutorialState.DONE;
                }
                break;
        }
    }

    @Override
    public void transitionToBattleView(Entity playerEntity, Entity aiEntity, boolean playerInitiated) {
        super.transitionToBattleView(playerEntity, aiEntity, playerInitiated);
        if (curTutState == TutorialState.ATTACK) {
            curTutState = TutorialState.RESUME;
        }
    }
}
