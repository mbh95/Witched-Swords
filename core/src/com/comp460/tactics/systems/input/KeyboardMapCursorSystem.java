package com.comp460.tactics.systems.input;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.comp460.battle.BattleScreen;
import com.comp460.common.GameUnit;
import com.comp460.tactics.TacticsScreen;
import com.comp460.tactics.components.*;
import com.comp460.tactics.components.unit.AIControlledComponent;
import com.comp460.tactics.components.unit.PlayerControlledComponent;
import com.comp460.tactics.components.unit.ReadyToMoveComponent;
import com.comp460.tactics.components.unit.UnitStatsComponent;
import com.comp460.tactics.map.MapPosition;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class KeyboardMapCursorSystem extends IteratingSystem {

    private static final Family mapCursorFamily = Family.all(MapCursorComponent.class, MapPositionComponent.class).get();
    private static final Family toggledUnitsFamily = Family.all(ShowValidMovesComponent.class).get();
    private static final Family readyPlayerControlledFamily = Family.all(PlayerControlledComponent.class, ReadyToMoveComponent.class).get();

    private static final Family playerControlledFamily = Family.all(PlayerControlledComponent.class).get();
    private static final Family aiControlledFamily = Family.all(AIControlledComponent.class).get();

    private static final ComponentMapper<MapCursorComponent> cursorM = ComponentMapper.getFor(MapCursorComponent.class);
    private static final ComponentMapper<MapPositionComponent> mapPosM = ComponentMapper.getFor(MapPositionComponent.class);
    private static final ComponentMapper<UnitStatsComponent> statsM = ComponentMapper.getFor(UnitStatsComponent.class);

    private TacticsScreen parentScreen;

    public KeyboardMapCursorSystem(TacticsScreen tacticsScreen) {
        super(mapCursorFamily);
        this.parentScreen = tacticsScreen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MapCursorComponent cursor = cursorM.get(entity);
        MapPositionComponent cursorPos = mapPosM.get(entity);

        if (cursor.countdown == 0) {
            int oldRow = cursorPos.row;
            int oldCol = cursorPos.col;
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) cursorPos.col--;
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) cursorPos.col++;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) cursorPos.row++;
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) cursorPos.row--;
            if (cursorPos.row < 0) cursorPos.row = 0;
            else if (cursorPos.row >= parentScreen.getMap().getHeight())
                cursorPos.row = parentScreen.getMap().getHeight() - 1;
            if (cursorPos.col < 0) cursorPos.col = 0;
            else if (cursorPos.col >= parentScreen.getMap().getWidth())
                cursorPos.col = parentScreen.getMap().getWidth() - 1;
            if (cursorPos.row != oldRow || cursorPos.col != oldCol) {
                cursor.countdown = cursor.delay;
            }
        } else if (cursor.countdown > 0) {
            cursor.countdown--;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            Entity newSelection = parentScreen.getMap().getUnitAt(cursorPos.row, cursorPos.col);

            if (newSelection != null) {
                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    if (toggledUnitsFamily.matches(newSelection)) {
                        newSelection.remove(ShowValidMovesComponent.class);
                    } else {
                        ShowValidMovesComponent toggle = new ShowValidMovesComponent();
                        newSelection.add(toggle);
                    }
                } else {

                    // This is a hack to get combat wired in
                    MapPositionComponent mapPosComp = mapPosM.get(newSelection);
                    if (cursor.selection != null && aiControlledFamily.matches(newSelection)/* && parentScreen.getMap().computeValidMoves(cursor.selection).contains(new MapPosition(parentScreen.getMap(), mapPosComp.row, mapPosComp.col))*/) {
                        System.out.println("STARTING COMBAT");
                        UnitStatsComponent playerUnitStats = statsM.get(cursor.selection);
                        UnitStatsComponent aiUnitStats = statsM.get(newSelection);
                        this.parentScreen.game.setScreen(new BattleScreen(this.parentScreen.game, this.parentScreen, playerUnitStats.base, aiUnitStats.base));

                    } else {
                        // This is the real selection code:
                        cursor.selection = newSelection;

                        clearToggledUnits();

                        // If the selected unit's moves were not already visible, make them visible
                        if (!toggledUnitsFamily.matches(cursor.selection)) {
                            ShowValidMovesComponent toggle = new ShowValidMovesComponent();
                            cursor.selection.add(toggle);
                        }
                    }

                }
            } else {
                if (cursor.selection != null && readyPlayerControlledFamily.matches(cursor.selection)) {
                    MapPositionComponent selectionPos = mapPosM.get(cursor.selection);
                    if (parentScreen.getMap().computeValidMoves(cursor.selection).contains(new MapPosition(parentScreen.getMap(), cursorPos.row, cursorPos.col))) {
                        parentScreen.getMap().move(cursor.selection, cursorPos.row, cursorPos.col);
                        clearToggledUnits();

                        cursor.selection = null;
                    } else {
                        //ERR ERR SOUND
                    }
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            clearToggledUnits();
        }
    }

    private void clearToggledUnits() {
        this.getEngine().getEntitiesFor(toggledUnitsFamily).forEach((e) -> {
            e.remove(ShowValidMovesComponent.class);
        });
    }
}
