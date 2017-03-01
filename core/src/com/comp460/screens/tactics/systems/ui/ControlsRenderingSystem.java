package com.comp460.screens.tactics.systems.ui;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.FontManager;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.MapCursorComponent;
import com.comp460.screens.tactics.components.unit.AIControlledComponent;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;

/**
 * Created by matthewhammond on 2/27/17.
 */
public class ControlsRenderingSystem extends IteratingSystem {

    private static final Family cursorFamily = Family.all(MapCursorComponent.class).get();

    private static final Family playerControlledFamily = Family.all(PlayerControlledComponent.class).get();
    private static final Family aiControlledFamily = Family.all(AIControlledComponent.class).get();

    private static final ComponentMapper<MapCursorComponent> cursorM = ComponentMapper.getFor(MapCursorComponent.class);

    private static BitmapFont controlsFont = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.WHITE, Color.BLACK, 1);

    TacticsScreen screen;

    public ControlsRenderingSystem(TacticsScreen tacticsScreen) {
        super(cursorFamily);

        this.screen = tacticsScreen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MapCursorComponent cursor = cursorM.get(entity);

        int x = 2;
        int y = 2;
        TextureRegion button1Icon = screen.game.controller.button1Sprite();
        TextureRegion button2Icon = screen.game.controller.button2Sprite();
        String button1Action = "Select";
        String button2Action = "Deselect";

        if (cursor.selection != null) { // You have a unit selected

            if (playerControlledFamily.matches(cursor.selection)) { // You have one of your units selected
                button1Action = "Move";
            } else if (aiControlledFamily.matches(cursor.selection)) { // You have an enemy unit selected

            }
        }

        screen.uiBatch.begin();

        screen.uiBatch.draw(button2Icon, x, y);
        controlsFont.draw(screen.uiBatch, button2Action, x + button2Icon.getRegionWidth() + 2, y + 8);

        y += button1Icon.getRegionHeight() + 2;

        screen.uiBatch.draw(button1Icon, x, y);
        controlsFont.draw(screen.uiBatch, button1Action, x + button1Icon.getRegionWidth() + 2, y + 8);

        screen.uiBatch.end();
    }
}
