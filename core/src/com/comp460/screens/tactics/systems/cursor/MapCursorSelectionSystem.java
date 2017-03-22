package com.comp460.screens.tactics.systems.cursor;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.comp460.screens.battle.BattleScreen;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.MapCursorSelectionComponent;
import com.comp460.screens.tactics.components.cursor.MovementPathComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.cursor.MapCursorComponent;
import com.comp460.screens.tactics.components.unit.*;
import com.comp460.screens.tactics.factories.ActionMenuFactory;
import com.comp460.screens.tactics.systems.map.ValidMoveManagementSystem;

import java.util.Set;

/**
 * Created by matthewhammond on 1/15/17.
 */
public class MapCursorSelectionSystem extends IteratingSystem implements EntityListener {

    private static final Family mapCursorFamily = Family.all(MapCursorComponent.class, MapPositionComponent.class).get();

    private static final Family pathingFamily = Family.all(MapCursorSelectionComponent.class, MovementPathComponent.class).get();

    private static final Family selectedUnitsFamily = Family.all(SelectedComponent.class).get();
    private static final Family toggledUnitsFamily = Family.all(ShowValidMovesComponent.class).get();

    private static final Family playerControlledFamily = Family.all(PlayerControlledComponent.class).get();
    private static final Family aiControlledFamily = Family.all(AIControlledComponent.class).get();

    private static final Family readyPlayerControlledFamily = Family.all(PlayerControlledComponent.class, ReadyToMoveComponent.class).get();

    private static final Family queuedMoveFamily = Family.all(QueuedMoveComponent.class).get();

    private static final ComponentMapper<MapCursorSelectionComponent> selectionM = ComponentMapper.getFor(MapCursorSelectionComponent.class);
    private static final ComponentMapper<MapPositionComponent> mapPosM = ComponentMapper.getFor(MapPositionComponent.class);
    private static final ComponentMapper<UnitStatsComponent> statsM = ComponentMapper.getFor(UnitStatsComponent.class);
    private static final ComponentMapper<MovementPathComponent> pathM = ComponentMapper.getFor(MovementPathComponent.class);

    private TacticsScreen parentScreen;

    public MapCursorSelectionSystem(TacticsScreen tacticsScreen) {
        super(mapCursorFamily);
        this.parentScreen = tacticsScreen;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        engine.addEntityListener(queuedMoveFamily, this);
    }

    @Override
    protected void processEntity(Entity cursor, float deltaTime) {
        MapPositionComponent cursorPos = mapPosM.get(cursor);

        if (parentScreen.game.controller.button1JustPressed()) {
            Entity newSelection = parentScreen.getMap().getUnitAt(cursorPos.row, cursorPos.col);
            Entity prevSelection = null;
            if (selectionM.get(cursor) != null) {
                prevSelection = selectionM.get(cursor).selected;
            }

            // You just clicked on a unit different from the one already selected
            if (newSelection != null && newSelection != prevSelection) {
                cursor.remove(MapCursorSelectionComponent.class);

                MapCursorSelectionComponent newSelectionComponent = new MapCursorSelectionComponent();
                newSelectionComponent.selected = newSelection;
                cursor.add(newSelectionComponent);

                clearToggledUnits();
                newSelection.add(new SelectedComponent());
                newSelection.add(new ShowValidMovesComponent());

                if (readyPlayerControlledFamily.matches(newSelection)) {
                    MovementPathComponent path = new MovementPathComponent();
                    MapPositionComponent unitPos = mapPosM.get(newSelection);
                    path.positions.add(new MapPositionComponent(unitPos.row, unitPos.col));
                    cursor.add(path);
                } else {
                    cursor.remove(MovementPathComponent.class);
                }
                return;
            }

            // If the cursor is already drawing a path
            if (pathingFamily.matches(cursor)) {
                MapCursorSelectionComponent selectionComponent = selectionM.get(cursor);

                Set<MapPositionComponent> validMoves = this.getEngine().getSystem(ValidMoveManagementSystem.class).getValidMoves(selectionComponent.selected);
                // You clicked on a square that you can move to
                if (validMoves.contains(cursorPos)) {
                    QueuedMoveComponent queuedMove = ActionMenuFactory.makeActionMenu(pathM.get(cursor).positions, selectionComponent.selected, parentScreen);
                    selectionComponent.selected.add(queuedMove);
                } else {
                    cursor.remove(MapCursorSelectionComponent.class);
                    clearToggledUnits();
                    cursor.remove(MovementPathComponent.class);
                }
            }
        }

        if (parentScreen.game.controller.button2JustPressed()) {
            clearToggledUnits();
            cursor.remove(MapCursorSelectionComponent.class);
            cursor.remove(MovementPathComponent.class);
        }




//         else{ // You just clicked on an empty square
//            // You currently have a unit selected
//            if (selectionComponent != null) {
//                Set<MapPositionComponent> validMoves = this.getEngine().getSystem(ValidMoveManagementSystem.class).getValidMoves(selectionComponent.selected);
//                // You clicked on a square that you can move to
//                if (validMoves.contains(mapPosM.get(cursor))) {
//
//                }
//            } else {
//                clearToggledUnits();
//            }
//        }
//
//        // HACKY WORKAROUND FOR NOW TO GET BATTLES WORKING:
//        if (newSelection != null && playerControlledFamily.matches(newSelection)) {
//            clearToggledUnits();
//            cursor.selection = newSelection;
//            cursor.selection.add(new ShowValidMovesComponent());
//            newSelection.add(new SelectedComponent());
//
//            return;
//        }
//
//        // You currently have a unit selected
//        if (cursor.selection != null) {
//
//            // The unit you have selected is ready to move
//            if (readyPlayerControlledFamily.matches(cursor.selection)) {
//
//                // The unit can move the space you clicked on
//                if (parentScreen.getMap().computeValidMoves(cursor.selection).contains(new MapPositionComponent(cursorPos.row, cursorPos.col))) {
//
//                    // HACK TO GET COMBAT WORKING:
//                    Entity prevUnit = parentScreen.getMap().getUnitAt(cursorPos.row, cursorPos.col);
//                    if (prevUnit != null) {
//                        // If you clicked on an enemy unit start combat
//                        if (aiControlledFamily.matches(prevUnit)) {
//                            System.out.println("STARTING COMBAT");
//                            UnitStatsComponent playerUnitStats = statsM.get(cursor.selection);
//                            UnitStatsComponent aiUnitStats = statsM.get(newSelection);
//                            MovementPathComponent path = pathM.get(cursor.selection);
//                            if (path != null) {
//                                int index = path.positions.size() - 2;
//                                MapPositionComponent pathPos = path.positions.get(index);
//                                while (index > 0 && parentScreen.getMap().getUnitAt(pathPos.row, pathPos.col) != null && parentScreen.getMap().getUnitAt(pathPos.row, pathPos.col) != cursor.selection) {
//                                    index--;
//                                    pathPos = path.positions.get(index);
//                                }
//                                parentScreen.getMap().move(cursor.selection, pathPos.row, pathPos.col);
//                            }
//                            this.parentScreen.game.setScreen(new BattleScreen(this.parentScreen.game, this.parentScreen, playerUnitStats.base, aiUnitStats.base, false, 10f));
//                            return;
//                        }
//                    } else {
//                        parentScreen.getMap().move(cursor.selection, cursorPos.row, cursorPos.col);
//                        clearToggledUnits();
//                        cursor.selection = null;
//                    }
//                } else {
//                    clearToggledUnits();
//                    cursor.selection = null;
//                }
//            } else {
//                clearToggledUnits();
//                cursor.selection = null;
//            }
//        }
//    }
//
//    // HACKY WORKAROUND TO ALLOW SELECTING ENEMY UNITS. DELETE THIS WHEN THE ABOVE HACK IS REMOVED
//        if(newSelection !=null)
//
//    {
//        clearToggledUnits();
//        cursor.selection = newSelection;
//        cursor.selection.add(new ShowValidMovesComponent());
//        newSelection.add(new SelectedComponent());
//    }
//
//        if(Gdx.input.isKeyJustPressed(Input.Keys.X))
//
//    {
//        cursor.selection = null;
//        clearToggledUnits();
//    }

    }

    private void clearToggledUnits() {
        this.getEngine().getEntitiesFor(toggledUnitsFamily).forEach((e) -> {
            e.remove(ShowValidMovesComponent.class);
        });
        this.getEngine().getEntitiesFor(selectedUnitsFamily).forEach((e) -> {
            e.remove(SelectedComponent.class);
        });
    }

    @Override
    public void entityAdded(Entity entity) {
        this.setProcessing(false);
    }

    @Override
    public void entityRemoved(Entity entity) {
        if (getEngine().getEntitiesFor(queuedMoveFamily).size() == 0 ) {
            this.setProcessing(true);
        }
    }
}
