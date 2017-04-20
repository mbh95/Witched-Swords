package com.comp460.screens.tactics.systems.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.FontManager;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.MapCursorSelectionComponent;
import com.comp460.screens.tactics.components.cursor.MapCursorComponent;
import com.comp460.screens.tactics.components.unit.AIControlledComponent;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;

/**
 * Displays context based control tooltips.
 */
public class ControlsRenderingSystem extends IteratingSystem {

    private static final Family cursorFamily = Family.all(MapCursorComponent.class).get();

    private static final Family playerControlledFamily = Family.all(PlayerControlledComponent.class).get();
    private static final Family aiControlledFamily = Family.all(AIControlledComponent.class).get();

    private static final ComponentMapper<MapCursorSelectionComponent> selectionM = ComponentMapper.getFor(MapCursorSelectionComponent.class);

    private static BitmapFont controlsFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.WHITE, Color.BLACK, 1);

    TacticsScreen screen;

    public ControlsRenderingSystem(TacticsScreen tacticsScreen, int priority) {
        super(cursorFamily, priority);

        this.screen = tacticsScreen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (screen.cursorLocked() || screen.curState != TacticsScreen.TacticsState.PLAYER_TURN) {
            return;
        }
        MapCursorSelectionComponent selection = selectionM.get(entity);

        int x = 2;
        int y = 2;
        TextureRegion button1Icon = screen.game.controller.button1Sprite();
        TextureRegion button2Icon = screen.game.controller.button2Sprite();
        String button1Action = "Select";
        String button2Action = "Deselect";

        if (selection != null) { // You have a unit selected
            if (playerControlledFamily.matches(selection.selected)) { // You have one of your units selected
                button1Action = "Confirm";
            } else if (aiControlledFamily.matches(selection.selected)) { // You have an enemy unit selected

            }
        }

        screen.uiBatch.begin();

//        screen.uiBatch.draw(button2Icon, x, y);
//        controlsFont.draw(screen.uiBatch, button2Action, x + button2Icon.getRegionWidth() + 2, y + 8);
//
//        y += button1Icon.getRegionHeight() + 2;
//
//        screen.uiBatch.draw(button1Icon, x, y);
//        controlsFont.draw(screen.uiBatch, button1Action, x + button1Icon.getRegionWidth() + 2, y + 8);

        int inputHintX = 2;
        int inputHintY = 2;
        int inputHintLineHeight = 16;

        screen.uiBatch.draw(screen.game.controller.button1Sprite(), inputHintX, inputHintY + 2 * inputHintLineHeight);
        controlsFont.draw(screen.uiBatch, button1Action, inputHintX + screen.game.controller.button1Sprite().getRegionWidth() + 2, inputHintY + 2 * inputHintLineHeight + 8);

        screen.uiBatch.draw(screen.game.controller.button2Sprite(), inputHintX, inputHintY + inputHintLineHeight);
        controlsFont.draw(screen.uiBatch, button2Action, inputHintX + screen.game.controller.button1Sprite().getRegionWidth() + 2, inputHintY + 1 * inputHintLineHeight + 8);

        screen.uiBatch.draw(screen.game.controller.directionalSprite(), inputHintX, inputHintY);
        controlsFont.draw(screen.uiBatch, "Cursor", inputHintX + screen.game.controller.directionalSprite().getRegionWidth() + 2, inputHintY + 8);

        screen.uiBatch.end();
    }
}
