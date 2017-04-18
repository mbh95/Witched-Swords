package com.comp460.screens.tactics.systems.rendering;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.FontManager;
import com.comp460.assets.SpriteManager;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.HealAnimComponent;

/**
 * Created by matthewhammond on 4/18/17.
 */
public class HealAnimRenderingSystem extends IteratingSystem {
    private static final Family healAnimFamily = Family.all(HealAnimComponent.class, MapPositionComponent.class).get();
    private static final TextureRegion sparkleSprite = SpriteManager.TACTICS.findRegion("ui/sparkle");

    private static final BitmapFont healFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 8, Color.GREEN, Color.BLACK, 1);

    TacticsScreen parentScreen;

    public HealAnimRenderingSystem(TacticsScreen parentScreen) {
        super(healAnimFamily);
        this.parentScreen = parentScreen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HealAnimComponent healAnim = HealAnimComponent.get(entity);
        MapPositionComponent mapPos = MapPositionComponent.get(entity);

        float x = parentScreen.getMap().getTileX(mapPos.row, mapPos.col);
        float y = parentScreen.getMap().getTileY(mapPos.row, mapPos.col);
        parentScreen.batch.begin();

        parentScreen.batch.draw(sparkleSprite, x, y);
        healFont.draw(parentScreen.batch, "+" + healAnim.amt, x, y + parentScreen.getMap().getHeight() + 10);

        parentScreen.batch.end();

        healAnim.duration -= deltaTime;
        if (healAnim.duration <= 0) {
            entity.remove(HealAnimComponent.class);
        }
    }
}
