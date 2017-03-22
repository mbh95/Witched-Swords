package com.comp460.screens.battleECS;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.screens.battle.components.*;
import com.comp460.screens.battle.factories.ability.AbilityFactory;
import com.comp460.screens.battle.systems.*;
import com.comp460.common.FontManager;
import com.comp460.common.GameScreen;
import com.comp460.screens.battle.factories.unit.BattleUnitFactory;
import com.comp460.common.SpriteManager;
import com.comp460.common.components.AnimationComponent;
import com.comp460.common.systems.SpriteAnimationSystem;
import com.comp460.common.systems.SpriteRenderingSystem;
import com.sun.javafx.binding.StringFormatter;

/**
 * Created by matth on 2/13/2017.
 */
public class BattleScreen extends GameScreen {

    public static final TextureAtlas sprites = SpriteManager.BATTLE;

    public final int numRows = 3;
    public final int numCols = 6;

    public final TextureRegion background = sprites.findRegion("bg/plains");
    public final TextureRegion tileLHS = sprites.findRegion("bg/tile_plains_blue");
    public final TextureRegion tileRHS = sprites.findRegion("bg/tile_plains_red");

    public final TextureRegion tileSideLHS = sprites.findRegion("bg/tile_plains_blue_side");
    public final TextureRegion tileSideRHS = sprites.findRegion("bg/tile_plains_red_side");

    public final TextureRegion hpBar = sprites.findRegion("rendering/hp_bar_new");
    public final TextureRegion energyBar = sprites.findRegion("rendering/energy");

    public final int tileWidth = tileLHS.getRegionWidth();
    public final int tileHeight = tileLHS.getRegionHeight();
    public final int tileSideHeight = tileSideLHS.getRegionHeight();

    public final float gridOffsetX = 0;
    public final float gridOffsetY = tileHeight;

    private static BitmapFont greenFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.GREEN, Color.BLACK, 2);
    private static BitmapFont yellowFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.ORANGE, Color.BLACK, 2);
    private static BitmapFont redFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED, Color.BLACK, 2);

    private static BitmapFont timerFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 16, Color.RED);
    private static BitmapFont resultsFont = FontManager.getFont(FontManager.KEN_PIXEL_BLOCKS, 32, Color.WHITE, Color.BLACK, 2);
    private static BitmapFont continueFont = FontManager.getFont(FontManager.KEN_PIXEL_MINI, 16, Color.WHITE);

    private static BitmapFont hpFont = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.WHITE);


    private static GlyphLayout readyLayout = new GlyphLayout(redFont, "READY");
    private static GlyphLayout setLayout = new GlyphLayout(yellowFont, "SET");
    private static GlyphLayout fightLayout = new GlyphLayout(greenFont, "FIGHT!");

    private static GlyphLayout tieLayout = new GlyphLayout(resultsFont, "DRAW");
    private static GlyphLayout p1WinsLayout = new GlyphLayout(resultsFont, "YOU WIN!");
    private static GlyphLayout p2WinsLayout = new GlyphLayout(resultsFont, "YOU LOSE!");
    private static GlyphLayout stalemateLayout = new GlyphLayout(resultsFont, "STALEMATE");
    private static GlyphLayout outOfTimeLayout = new GlyphLayout(resultsFont, "OUT OF TIME");

    private static Family movesToKeepAliveFamily = Family.all(DamageComponent.class).get();
    private static Family playerUnitsFamily = Family.all(PlayerControlledComponent.class).get();
    private static Family aiUnitsFamily = Family.all(AIControlledComponent.class).get();

    private enum BattleState {COUNTOFF, RUNNING, END_STALEMATE, END_PLAYER_DIED, END_AI_DIED, END_BOTH_DIED, END_TIME}

    private float countOffTimer = 3;
    private float countdownTimer = 30;

    private BattleState curState = BattleState.COUNTOFF;

    private float endDelay = 2.0f;

    public Engine engine;

    public BattleScreen(Game game, GameScreen prevScreen, BattleUnitFactory playerUnit, BattleUnitFactory aiUnit) {
        super(game, prevScreen);

        this.engine = new Engine();

        this.engine.addSystem(new UnitControlSystem(this));
        this.engine.addSystem(new UnitAnimationSystem());

        this.engine.addSystem(new ExpiringSystem());
        this.engine.addSystem(new GridToScreenSystem(this));
        this.engine.addSystem(new ProjectileSystem(this));
        this.engine.addSystem(new ProjectileTrackingSystem(this));
        this.engine.addSystem(new StunSystem());
        this.engine.addSystem(new DamageSystem(this));

        this.engine.addSystem(new WarningRenderingSystem(this));
        this.engine.addSystem(new WarningSystem());

        this.engine.addSystem(new SpriteAnimationSystem());
        this.engine.addSystem(new SpriteRenderingSystem(this.batch, this.camera));

        this.engine.getSystem(UnitControlSystem.class).setProcessing(false);

        playerUnit.addToBattleScreen(this, 0, 0, new PlayerControlledComponent());
        aiUnit.addToBattleScreen(this, 0, numCols - 1, new AIControlledComponent(aiUnit.base.getId()));
    }

    public float rowToScreenY(int row) {
        return (tileHeight * row) + gridOffsetY;
    }

    public float colToScreenX(int col) {
        return ((this.width / 2f) + (col - numCols / 2f) * tileWidth) + gridOffsetX;
    }

    public boolean isOnGrid(int row, int col) {
        return (row >= 0 && row < numRows) && (col >= 0 && col < numCols);
    }

    public boolean isOnLHS(int row, int col) {
        return isOnGrid(row, col) && (col < numCols / 2);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        renderScene();

        engine.update(delta);

        renderUI(delta);

        switch (curState) {
            case COUNTOFF:
                countoff(delta);
                break;
            case RUNNING:
                tickTimer(delta);
                checkEndConditions(delta);
                break;
            case END_STALEMATE:
            case END_BOTH_DIED:
            case END_PLAYER_DIED:
            case END_AI_DIED:
            case END_TIME:
                renderEnd(delta);
                endDelay -= delta;
                if (endDelay <= 0) {
                    if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                        previousScreen();
                    }
                }
                break;
        }
    }

    private void tickTimer(float delta) {
        countdownTimer -= delta;
    }

    private void renderUI(float delta) {

        for (Entity e : engine.getEntitiesFor(playerUnitsFamily)) {
            HealthComponent health = Mappers.healthM.get(e);
            EnergyComponent energy = Mappers.energyM.get(e);
            if (health != null && energy != null) {
                renderHealthBar(health, energy, width / 2 - hpBar.getRegionWidth() - 10, 3);
            }
        }

        for (Entity e : engine.getEntitiesFor(aiUnitsFamily)) {
            HealthComponent health = Mappers.healthM.get(e);
            EnergyComponent energy = Mappers.energyM.get(e);
            if (health != null && energy != null) {
                renderHealthBar(health, energy, width / 2 + 10, 3);
            }
        }

        renderTimer(width / 2, 200);
    }

    private void renderTimer(float x, float y) {
        int seconds = ((int) countdownTimer) % 60;
        String timerString = StringFormatter.format("%02d", seconds).getValue();
        GlyphLayout timerLayout = new GlyphLayout(timerFont, timerString);
        batch.begin();
        timerFont.draw(batch, timerLayout, x - timerLayout.width / 2, y);
        batch.end();
    }

    private void renderHealthBar(HealthComponent health, EnergyComponent energy, float x, float y) {
        batch.begin();

        batch.draw(hpBar, x, y);

        for (int i = energy.curEnergy; i > 0; i--)
            batch.draw(energyBar, 51 + x - (i - 1) * 11, y + 2);

        String hpString = String.format("%03d/%03d", health.curHP, health.maxHP);
        GlyphLayout hpLayout = new GlyphLayout(hpFont, hpString);
        hpFont.draw(batch, hpString, x + hpBar.getRegionWidth() - hpLayout.width - 2, y + 22);
        batch.end();

        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        double percentHP = 1.0 * health.curHP / health.maxHP;
        if (percentHP > .45)
            sr.setColor(Color.GREEN);
        else if (percentHP > .25)
            sr.setColor(Color.GOLDENROD);
        else
            sr.setColor(Color.SCARLET);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        if (percentHP > 0)
            sr.rect(x + 9, y + 8, (int) (52 * percentHP), 4);
        sr.end();
    }

    private void checkEndConditions(float delta) {
        if (engine.getEntitiesFor(movesToKeepAliveFamily).size() > 0) {
            return;
        }

        boolean allPlayersDead = true;
        for (Entity e : engine.getEntitiesFor(playerUnitsFamily)) {
            HealthComponent health = Mappers.healthM.get(e);
            if (health.curHP > 0) {
                allPlayersDead = false;
                break;
            }
        }

        boolean allAiDead = true;
        for (Entity e : engine.getEntitiesFor(aiUnitsFamily)) {
            HealthComponent health = Mappers.healthM.get(e);
            if (health.curHP > 0) {
                allAiDead = false;
                break;
            }
        }

        if (allPlayersDead && allAiDead) {
            curState = BattleState.END_BOTH_DIED;
            return;
        } else if (allPlayersDead) {
            curState = BattleState.END_PLAYER_DIED;
            return;
        } else if (allAiDead) {
            curState = BattleState.END_AI_DIED;
            return;
        }

        if (countdownTimer <= 0) {
            curState = BattleState.END_TIME;
            return;
        }

        boolean allPlayersIdle = true;
        for (Entity e : engine.getEntitiesFor(playerUnitsFamily)) {
            if (canUseAnyAbilities(e)) {
                allPlayersIdle = false;
                break;
            }
        }
        boolean allAiIdle = true;
        for (Entity e : engine.getEntitiesFor(aiUnitsFamily)) {
            if (canUseAnyAbilities(e)) {
                allAiIdle = false;
                break;
            }
        }

        if (allPlayersIdle && allAiIdle) {
            curState = BattleState.END_STALEMATE;
        }

        if (curState != BattleState.RUNNING) {
            engine.getSystem(UnitControlSystem.class).setProcessing(false);
        }
    }

    private void renderEnd(float delta) {
        GlyphLayout layout = tieLayout;
        batch.begin();
        switch (curState) {
            case END_BOTH_DIED:
                layout = tieLayout;
                break;
            case END_PLAYER_DIED:
                layout = p2WinsLayout;
                break;
            case END_AI_DIED:
                layout = p1WinsLayout;
                break;
            case END_TIME:
                layout = outOfTimeLayout;
                break;
            case END_STALEMATE:
                layout = stalemateLayout;
                break;
        }
        resultsFont.draw(batch, layout, width / 2 - layout.width / 2, 100);


        if (this.endDelay <= 0) {
            layout = new GlyphLayout(continueFont, "any key to continue");
            continueFont.draw(batch, "any key to continue", width / 2 - layout.width / 2, 50);
        }
        batch.end();
    }

    public void countoff(float delta) {
        countOffTimer -= delta;
        int seconds = ((int) countOffTimer) % 60;
        batch.begin();
        switch (seconds) {
            case 2:
                redFont.draw(batch, readyLayout, 400 / 2 - readyLayout.width / 2, 240 / 2 - readyLayout.height / 2);
                break;
            case 1:
                yellowFont.draw(batch, setLayout, 400 / 2 - setLayout.width / 2, 240 / 2 - setLayout.height / 2);
                break;
            case 0:
                greenFont.draw(batch, fightLayout, 400 / 2 - fightLayout.width / 2, 240 / 2 - fightLayout.height / 2);
                break;
        }
        if (countOffTimer <= 0) {
            curState = BattleState.RUNNING;
            engine.getSystem(UnitControlSystem.class).setProcessing(true);
        }
        batch.end();
    }

    private void renderScene() {
        batch.begin();
        batch.draw(background, 0, 0);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (c < numCols / 2) {
                    batch.draw(tileLHS, colToScreenX(c), rowToScreenY(r));
                } else {
                    batch.draw(tileRHS, colToScreenX(c), rowToScreenY(r));
                }
            }
        }
        for (int c = 0; c < numCols; c++) {
            float x = colToScreenX(c);
            float y = rowToScreenY(0) - tileSideHeight;
            if (c < numCols / 2) {
                batch.draw(tileSideLHS, x, y);
            } else {
                batch.draw(tileSideRHS, x, y);
            }
        }
        batch.end();
    }

    public void move(Entity unit, int dr, int dc) {
        GridPositionComponent pos = Mappers.gridPosM.get(unit);
        if (pos == null) {
            return;
        }

        if (isOnGrid(pos.row + dr, pos.col + dc) && isOnLHS(pos.row, pos.col) == isOnLHS(pos.row + dr, pos.col + dc)) {
            pos.row += dr;
            pos.col += dc;
        }
    }

    public boolean canUse(Entity unit, AbilityFactory bmf) {
        if (bmf.healthCost != 0) {
            HealthComponent health = Mappers.healthM.get(unit);
            if (health == null || health.curHP < bmf.healthCost) {
                return false;
            }
        }
        if (bmf.energyCost != 0) {
            EnergyComponent energy = Mappers.energyM.get(unit);
            if (energy == null || energy.curEnergy < bmf.energyCost) {
                return false;
            }
        }
        return true;
    }

    public boolean canUseAnyAbilities(Entity unit) {
        AbilitiesComponent abilities = Mappers.abilitiesM.get(unit);
        if (abilities == null) {
            return false;
        }
        for (AbilityFactory ability : abilities.moves) {
            if (canUse(unit, ability)) {
                return true;
            }
        }
        return false;
    }

    public void useAbility(Entity unit, int moveIndex) {

        AbilitiesComponent moves = Mappers.abilitiesM.get(unit);
        if (moves == null || moveIndex >= moves.moves.size()) {
            return;
        }

        AbilityFactory bmf = moves.moves.get(moveIndex);
        if (!canUse(unit, bmf)) {
            return;
        }

        if (bmf.animID != null) {
            startUnitAnimation(unit, bmf.animID);
        }

        if (bmf.healthCost != 0) {
            HealthComponent health = Mappers.healthM.get(unit);
            health.curHP -= bmf.healthCost;
        }
        if (bmf.energyCost != 0) {
            EnergyComponent energy = Mappers.energyM.get(unit);
            energy.curEnergy -= bmf.energyCost;
        }

        stunUnit(unit, bmf.castTime);

        bmf.addToScreen(this, unit);
    }


    public void startUnitAnimation(Entity unit, String newAnim) {
        AnimationComponent animationComponent = Mappers.animM.get(unit);
        UnitComponent unitComponent = Mappers.unitM.get(unit);
        if (animationComponent == null || unitComponent == null) {
            return;
        }
        animationComponent.animation = AnimationManager.getUnitAnimation(unitComponent.unitID, newAnim);
        animationComponent.timer = 0f;
    }

    public void stunUnit(Entity unit, float duration) {
        StunComponent prevStun = Mappers.stunM.get(unit);

        if (prevStun == null) {
            unit.add(new StunComponent(duration));
        } else if (prevStun.duration < duration) {
            prevStun.duration = duration;
        }
    }
}
