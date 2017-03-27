package com.comp460.screens.tactics.systems.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.comp460.Settings;
import com.comp460.assets.FontManager;
import com.comp460.screens.tactics.TacticsAssets;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.cursor.QueuedMoveComponent;

/**
 * Created by matth on 3/22/2017.
 */
public class ActionMenuRenderingSystem extends IteratingSystem {

    private static final Family actingUnitFamily = Family.all(QueuedMoveComponent.class, MapPositionComponent.class).get();

    private static final ComponentMapper<QueuedMoveComponent> actionM = ComponentMapper.getFor(QueuedMoveComponent.class);

    private static final BitmapFont actionFont = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.WHITE);

    public TacticsScreen screen;

    public ActionMenuRenderingSystem(TacticsScreen screen) {
        super(actingUnitFamily);

        this.screen = screen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        QueuedMoveComponent queuedMove = actionM.get(entity);

        float x = 0f;
        float y = Settings.INTERNAL_HEIGHT;
        y -= TacticsAssets.ACTION_ROW.getRegionHeight() * queuedMove.actions.size();

        screen.uiBatch.begin();
        for (int i = 0; i < queuedMove.actions.size(); i++) {
            if (i == queuedMove.selectedAction) {
                screen.uiBatch.draw(TacticsAssets.ACTION_ROW_SELECTED, x, y + i * TacticsAssets.ACTION_ROW.getRegionHeight());
            } else {
                screen.uiBatch.draw(TacticsAssets.ACTION_ROW, x, y + i * TacticsAssets.ACTION_ROW.getRegionHeight());
            }
            actionFont.draw(screen.uiBatch, queuedMove.actions.get(i).toString(), x + 4, y + 8 + 4 + i * TacticsAssets.ACTION_ROW.getRegionHeight());
        }

        screen.uiBatch.end();
    }
}
