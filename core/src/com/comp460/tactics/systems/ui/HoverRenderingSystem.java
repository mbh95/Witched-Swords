package com.comp460.tactics.systems.ui;

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
import com.comp460.assets.FontManager;
import com.comp460.common.GameUnit;
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

    private static BitmapFont hpFont = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.WHITE);

    private TacticsScreen parentScreen;
    private SpriteBatch batch;
    private OrthographicCamera camera;

    public HoverRenderingSystem(TacticsScreen tacticsScreen) {
        super(cursorFamily);
        this.parentScreen = tacticsScreen;
        batch = parentScreen.uiBatch;
        camera = parentScreen.uiCamera;
//        uiCamera.translate(200, 120);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        int x = 0;
        int y = 0;
        TextureRegion bg = BattlePracticeAssets.TEXTURE_PLAYER_AREA;
        Entity hovered = cursorM.get(entity).hovered;
        if (hovered == null) {
            return;
        }
        GameUnit unit = unitStatsM.get(hovered).base;
        TextureRegion unitIcon = BattleAnimationManager.getBattleUnitAnimation(unitStatsM.get(hovered).base.id, "attack").getKeyFrame(0f);

//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

//        ShapeRenderer sr = new ShapeRenderer();
//        sr.setProjectionMatrix(parentScreen.getCamera().combined);
//        sr.begin(ShapeRenderer.ShapeType.Filled);

        if (playerControlledFamily.matches(hovered)) {
            x = 0;
            y = 240;
            bg = BattlePracticeAssets.TEXTURE_PLAYER_AREA;
        } else if (aiControlledFamily.matches(hovered)) {
            x = 400 - bg.getRegionWidth();
            y = 240;
            bg = BattlePracticeAssets.TEXTURE_AI_AREA;
        }

        batch.begin();
        // draw info box background
        batch.draw(bg, x, y - bg.getRegionHeight());
        // draw hp text
        String hpString = "HP " + String.format("%03d/%03d", unit.curHP, unit.maxHP);
        GlyphLayout hpLayout = new GlyphLayout(hpFont, hpString);
        hpFont.draw(batch, hpString, x + 5, y - 10 - unitIcon.getRegionHeight());
        batch.end();

        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);

        sr.begin(ShapeRenderer.ShapeType.Filled);

        // draw black hp background box
        sr.setColor(Color.BLACK);
        sr.rect(x + 3, y - unitIcon.getRegionHeight() - hpLayout.height - 20 - 2, 56, 10);
        // draw black portrait bordground box
//        sr.rect(x + 5, y - 5 - unitIcon.getRegionHeight(), 40, 40);

        // draw hp bar
        double percentHP = 1.0 * unit.curHP / unit.maxHP;
        if (percentHP > .45)
            sr.setColor(Color.GREEN);
        else if (percentHP > .25)
            sr.setColor(Color.GOLDENROD);
        else
            sr.setColor(Color.SCARLET);
        if (percentHP > 0)
            sr.rect(x + 5, y - unitIcon.getRegionHeight() - hpLayout.height - 20, (int) (52 * percentHP), 6);
        sr.end();

        // draw protrait and hp outlines
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);
//        sr.rect(x + 5, y - 5 - unitIcon.getRegionHeight(), 40, 40);
        sr.rect(x + 3, y - unitIcon.getRegionHeight() - hpLayout.height - 20 - 2, 56, 10);
        sr.end();

        batch.begin();
        // draw portrait
        batch.draw(unitIcon, x + 5, y - 5 - unitIcon.getRegionHeight());
        batch.end();
    }
}
