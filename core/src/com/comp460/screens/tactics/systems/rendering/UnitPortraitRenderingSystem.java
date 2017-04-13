package com.comp460.screens.tactics.systems.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.Settings;
import com.comp460.assets.FontManager;
import com.comp460.common.GameUnit;
import com.comp460.assets.BattleAnimationManager;
import com.comp460.screens.tactics.TacticsAssets;
import com.comp460.screens.tactics.TacticsScreen;
//import com.comp460.screens.tactics.components.cursor.MapCursorSelectionComponent;
import com.comp460.screens.tactics.components.cursor.MapCursorComponent;
import com.comp460.screens.tactics.components.cursor.MapCursorSelectionComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.AIControlledComponent;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;
import com.comp460.screens.tactics.components.unit.UnitStatsComponent;

/**
 * Created by Belinda on 2/20/17.
 */
public class UnitPortraitRenderingSystem extends IteratingSystem {

    private static final Family cursorFamily = Family.all(MapCursorComponent.class).get();

    private static final Family playerControlledFamily = Family.all(PlayerControlledComponent.class, UnitStatsComponent.class).get();
    private static final Family aiControlledFamily = Family.all(AIControlledComponent.class, UnitStatsComponent.class).get();

    private static final ComponentMapper<MapCursorSelectionComponent> selectionM = ComponentMapper.getFor(MapCursorSelectionComponent.class);
    private static final ComponentMapper<MapPositionComponent> posM = ComponentMapper.getFor(MapPositionComponent.class);
    private static final ComponentMapper<UnitStatsComponent> unitStatsM = ComponentMapper.getFor(UnitStatsComponent.class);

    private static BitmapFont hpFont = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.WHITE, Color.BLACK, 1);

    private TacticsScreen parentScreen;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private static final int x = Settings.INTERNAL_WIDTH - TacticsAssets.HOVER_PLAYER.getRegionWidth();
    private static final int y = Settings.INTERNAL_HEIGHT - TacticsAssets.HOVER_PLAYER.getRegionHeight() - 10;

    public UnitPortraitRenderingSystem(TacticsScreen tacticsScreen) {
        super(cursorFamily);
        this.parentScreen = tacticsScreen;
        batch = parentScreen.uiBatch;
        camera = parentScreen.uiCamera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
//        TextureRegion bg = BattlePracticeAssets.TEXTURE_PLAYER_AREA;
        Entity displayed = null;

        MapPositionComponent mapPos = posM.get(entity);
        displayed = parentScreen.getMap().getUnitAt(mapPos.row, mapPos.col);

        MapCursorSelectionComponent selectionComponent = selectionM.get(entity);
        if (displayed == null && selectionComponent != null && selectionComponent.selected != null) {
            displayed = selectionComponent.selected;
        }

        if (displayed == null) {
            return;
        }

        GameUnit unit = unitStatsM.get(displayed).base;
        TextureRegion unitIcon = BattleAnimationManager.getBattleUnitAnimation(unitStatsM.get(displayed).base.id, "attack").getKeyFrame(0f);

        batch.begin();

        if (playerControlledFamily.matches(displayed)) {
            batch.draw(TacticsAssets.HOVER_PLAYER, x, y);
        } else if (aiControlledFamily.matches(displayed)) {
            batch.draw(TacticsAssets.HOVER_AI, x, y);
        }

        batch.draw(unitIcon, x + 85, y + 5);

        batch.end();


//
////        Gdx.gl.glEnable(GL20.GL_BLEND);
////        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//
////        ShapeRenderer sr = new ShapeRenderer();
////        sr.setProjectionMatrix(parentScreen.getCamera().combined);
////        sr.begin(ShapeRenderer.ShapeType.Filled);
//
//        if (playerControlledFamily.matches(hovered)) {
//            x = 0;
//            y = 240;
//            bg = BattlePracticeAssets.TEXTURE_PLAYER_AREA;
//        } else if (aiControlledFamily.matches(hovered)) {
//            x = 400 - bg.getRegionWidth();
//            y = 240;
//            bg = BattlePracticeAssets.TEXTURE_AI_AREA;
//        }
//
//
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
//
        sr.begin(ShapeRenderer.ShapeType.Filled);
//
//        // draw black hp background box
//        sr.setColor(Color.BLACK);
//        sr.rect(x + 3, y - unitIcon.getRegionHeight() - hpLayout.height - 20 - 2, 56, 10);
//        // draw black portrait bordground box
////        sr.rect(x + 5, y - 5 - unitIcon.getRegionHeight(), 40, 40);
//
        // draw hp bar
        double percentHP = 1.0 * unit.curHP / unit.maxHP;
        if (percentHP > .45)
            sr.setColor(Color.GREEN);
        else if (percentHP > .25)
            sr.setColor(Color.GOLDENROD);
        else
            sr.setColor(Color.SCARLET);
        if (percentHP > 0)
            sr.rect(x + 6, y + 5, (int) (73 * percentHP), 10);
        sr.end();

        batch.begin();
//        // draw info box background
//        batch.draw(bg, x, y - bg.getRegionHeight());
//        // draw hp text
        String hpString = String.format("%03d/%03d", unit.curHP, unit.maxHP);
        GlyphLayout hpLayout = new GlyphLayout(hpFont, hpString);
        hpFont.draw(batch, hpLayout, x + 6 + 73 / 2 - hpLayout.width / 2, y + 5 + 9 - 1);

        // draw unit name
        hpFont.draw(batch, unit.name, x + 4, y + 28 + 8);
        batch.end();
//
//        // draw protrait and hp outlines
//        sr.begin(ShapeRenderer.ShapeType.Line);
//        sr.setColor(Color.WHITE);
////        sr.rect(x + 5, y - 5 - unitIcon.getRegionHeight(), 40, 40);
//        sr.rect(x + 3, y - unitIcon.getRegionHeight() - hpLayout.height - 20 - 2, 56, 10);
//        sr.end();
//
//        batch.begin();
//        // draw portrait
//        batch.draw(unitIcon, x + 5, y - 5 - unitIcon.getRegionHeight());
//        batch.end();
    }
}
