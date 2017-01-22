package com.comp460.tactics.map.systems.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.comp460.Mappers;
import com.comp460.tactics.map.components.*;
import com.comp460.tactics.map.MapPosition;

/**
 * Created by matthewhammond on 1/19/17.
 */
public class KeyboardMapSelectionSystem extends IteratingSystem {

    private Family toggledUnits = Family.all(ShowValidMovesComponent.class).get();

    public KeyboardMapSelectionSystem() {
        super(Family.all(CursorComponent.class, MapPositionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MapPosition cursorPos = Mappers.mapPosM.get(entity).mapPos;

        Entity selection;

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            if ((selection = cursorPos.map.getUnitAt(cursorPos.row, cursorPos.col)) != null) {
                entity.remove(CursorSelectionComponent.class);
                CursorSelectionComponent sel = new CursorSelectionComponent().populate(selection);
                entity.add(sel);

                if (toggledUnits.matches(selection) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                    selection.remove(ShowValidMovesComponent.class);
                } else {
                    if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                        clearToggledUnits();
                    }
                    ShowValidMovesComponent toggle = new ShowValidMovesComponent();
                    selection.add(toggle);
                }
            } else {
                if (Mappers.selectionM.has(entity)) {
                    selection = Mappers.selectionM.get(entity).selection;
                    if (selection == null) {
                        return;
                    }
                    MapPosition selectionPos = Mappers.mapPosM.get(selection).mapPos;
                    selectionPos.map.move(selection, cursorPos.row, cursorPos.col);
                    clearToggledUnits();
                    entity.remove(CursorSelectionComponent.class);
                }
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            clearToggledUnits();
            entity.remove(CursorSelectionComponent.class);
        }
    }

    private void clearToggledUnits() {
        this.getEngine().getEntitiesFor(toggledUnits).forEach((e)->{
            e.remove(ShowValidMovesComponent.class);
        });
    }
}
