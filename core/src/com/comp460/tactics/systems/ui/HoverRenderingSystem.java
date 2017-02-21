package com.comp460.tactics.systems.ui;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.launcher.practice.battle.BattlePracticeAssets;
import com.comp460.tactics.TacticsScreen;
import com.comp460.tactics.components.cursor.MapCursorComponent;
import com.comp460.tactics.components.map.MapPositionComponent;
import com.comp460.tactics.components.unit.AIControlledComponent;
import com.comp460.tactics.components.unit.PlayerControlledComponent;
import com.comp460.tactics.components.unit.UnitStatsComponent;

/**
 * Created by Belinda on 2/20/17.
 */
public class HoverRenderingSystem extends IteratingSystem {

    private static final Family cursorFamily = Family.all(MapCursorComponent.class).get();

    private static final Family playerControlledFamily = Family.all(PlayerControlledComponent.class, MapPositionComponent.class).get();
    private static final Family aiControlledFamily = Family.all(AIControlledComponent.class, MapPositionComponent.class).get();
    private static final Family unitStatsFamily = Family.all(UnitStatsComponent.class).get();

    private static final ComponentMapper<MapCursorComponent> cursorM = ComponentMapper.getFor(MapCursorComponent.class);
    private static final ComponentMapper<MapPositionComponent> mapPosM = ComponentMapper.getFor(MapPositionComponent.class);
    private static final ComponentMapper<UnitStatsComponent> unitStatsM = ComponentMapper.getFor(UnitStatsComponent.class);

    private TacticsScreen parentScreen;
    private SpriteBatch batch;

    public HoverRenderingSystem(TacticsScreen tacticsScreen) {
        super(cursorFamily);
        this.parentScreen = tacticsScreen;
        batch = parentScreen.uiBatch;
//        uiCamera.translate(200, 120);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Entity hovered = cursorM.get(entity).hovered;
        if (hovered == null) {
            return;
        }
        TextureRegion unitIcon = BattleAnimationManager.getBattleUnitAnimation(unitStatsM.get(hovered).base.id, "attack").getKeyFrame(0f);

//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

//        ShapeRenderer sr = new ShapeRenderer();
//        sr.setProjectionMatrix(parentScreen.getCamera().combined);
//        sr.begin(ShapeRenderer.ShapeType.Filled);

        batch.begin();
        if (playerControlledFamily.matches(hovered)) {
            batch.draw(BattlePracticeAssets.TEXTURE_PLAYER_AREA, 0, 240 - 10 - BattlePracticeAssets.TEXTURE_PLAYER_AREA.getRegionHeight());
        } else if (aiControlledFamily.matches(hovered)) {
            batch.draw(BattlePracticeAssets.TEXTURE_AI_AREA, 400 - 0 - BattlePracticeAssets.TEXTURE_AI_AREA.getRegionWidth(), 240 - 10 - BattlePracticeAssets.TEXTURE_PLAYER_AREA.getRegionHeight());
        }

        batch.draw(unitIcon, 0, 240 - 10 - unitIcon.getRegionHeight());

//        MapPositionComponent pos = mapPosM.get(hovered);
//        sr.rect(pos.col * parentScreen.getMap().getTileWidth(), pos.row * parentScreen.getMap().getTileHeight(), parentScreen.getMap().getTileWidth(), parentScreen.getMap().getTileHeight());
//        sr.end();
//        sr.dispose();
//        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.end();
    }
}
