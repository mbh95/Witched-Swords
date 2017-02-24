package com.comp460.tactics.systems.cursor;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.comp460.battle.BattleScreen;
import com.comp460.tactics.TacticsScreen;
import com.comp460.tactics.components.map.MapPositionComponent;
import com.comp460.tactics.components.cursor.MapCursorComponent;
import com.comp460.tactics.components.unit.*;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class MapCursorSelectionSystem extends IteratingSystem {

    private static final Family mapCursorFamily = Family.all(MapCursorComponent.class, MapPositionComponent.class).get();
    private static final Family toggledUnitsFamily = Family.all(ShowValidMovesComponent.class).get();
    private static final Family readyPlayerControlledFamily = Family.all(PlayerControlledComponent.class, ReadyToMoveComponent.class).get();

    private static final Family playerControlledFamily = Family.all(PlayerControlledComponent.class).get();
    private static final Family aiControlledFamily = Family.all(AIControlledComponent.class).get();

    private static final ComponentMapper<MapCursorComponent> cursorM = ComponentMapper.getFor(MapCursorComponent.class);
    private static final ComponentMapper<MapPositionComponent> mapPosM = ComponentMapper.getFor(MapPositionComponent.class);
    private static final ComponentMapper<UnitStatsComponent> statsM = ComponentMapper.getFor(UnitStatsComponent.class);

    private TacticsScreen parentScreen;

    public MapCursorSelectionSystem(TacticsScreen tacticsScreen) {
        super(mapCursorFamily);
        this.parentScreen = tacticsScreen;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MapCursorComponent cursor = cursorM.get(entity);
        MapPositionComponent cursorPos = mapPosM.get(entity);


        Entity newSelection = null;

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            newSelection = parentScreen.getMap().getUnitAt(cursorPos.row, cursorPos.col);

            // You just clicked on a unit
            // FINAL VERSION WILL LOOK LIKE THIS:
//            if (newSelection != null) {
//                clearToggledUnits();
//                cursor.selection = newSelection;
//                cursor.selection.add(new ShowValidMovesComponent());
//                return;
//            }
            // HACKY WORKAROUND FOR NOW TO GET BATTLES WORKING:
            if (newSelection != null && playerControlledFamily.matches(newSelection)) {
                clearToggledUnits();
                cursor.selection = newSelection;
                cursor.selection.add(new ShowValidMovesComponent());
                return;
            }

            // You currently have a unit selected
            if (cursor.selection != null) {

                // The unit you have selected is ready to move
                if (readyPlayerControlledFamily.matches(cursor.selection)) {

                    // The unit can move the space you clicked on
                    if (parentScreen.getMap().computeValidMoves(cursor.selection).contains(new MapPositionComponent(cursorPos.row, cursorPos.col))) {

                        // HACK TO GET COMBAT WORKING:
                        Entity prevUnit = parentScreen.getMap().getUnitAt(cursorPos.row, cursorPos.col);
                        if (prevUnit != null) {
                            // If you clicked on an enemy unit start combat
                            if (aiControlledFamily.matches(prevUnit)) {
                                System.out.println("STARTING COMBAT");
                                UnitStatsComponent playerUnitStats = statsM.get(cursor.selection);
                                UnitStatsComponent aiUnitStats = statsM.get(newSelection);
                                cursor.selection.remove(ReadyToMoveComponent.class);
                                this.parentScreen.game.setScreen(new BattleScreen(this.parentScreen.game, this.parentScreen, playerUnitStats.base, aiUnitStats.base, false));
                                return;
                            }
                        } else {
                            parentScreen.getMap().move(cursor.selection, cursorPos.row, cursorPos.col);
                            clearToggledUnits();
                            cursor.selection = null;
                        }
                    } else {
                        clearToggledUnits();
                        cursor.selection = null;
                    }
                } else {
                    clearToggledUnits();
                    cursor.selection = null;
                }
            }
        }

        // HACKY WORKAROUND TO ALLOW SELECTING ENEMY UNITS. DELETE THIS WHEN THE ABOVE HACK IS REMOVED
        if (newSelection != null) {
            clearToggledUnits();
            cursor.selection = newSelection;
            cursor.selection.add(new ShowValidMovesComponent());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            cursor.selection = null;
            clearToggledUnits();
        }
    }

    private void clearToggledUnits() {
        this.getEngine().getEntitiesFor(toggledUnitsFamily).forEach((e) -> {
            e.remove(ShowValidMovesComponent.class);
        });
    }
}
