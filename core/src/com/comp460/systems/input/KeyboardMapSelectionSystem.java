package com.comp460.systems.input;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.comp460.Mappers;
import com.comp460.components.*;
import com.comp460.tactics.map.MapPosition;

/**
 * Created by matthewhammond on 1/19/17.
 */
public class KeyboardMapSelectionSystem extends IteratingSystem {

    private Family selectedUnits = Family.all(SelectedUnitComponent.class).get();
    private Family toggledUnits = Family.all(ShowValidMovesComponent.class).get();


    public KeyboardMapSelectionSystem() {
        super(Family.all(TacticsCursorComponent.class, MapPositionComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MapPosition mapPos = Mappers.mapPosM.get(entity).mapPos;

        Entity selection;

        this.getEngine().getEntitiesFor(selectedUnits);

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            if ((selection = mapPos.map.getUnitAt(mapPos.row, mapPos.col)) != null) {
                clearAllSelectedUnits();
                SelectedUnitComponent sel = new SelectedUnitComponent();
                selection.add(sel);
                ShowValidMovesComponent toggle = new ShowValidMovesComponent();
                selection.add(toggle);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            clearToggledUnits();
        }
    }

    private void clearAllSelectedUnits() {
        this.getEngine().getEntitiesFor(selectedUnits).forEach((e)->{
            e.remove(SelectedUnitComponent.class);
        });
    }

    private void clearToggledUnits() {
        this.getEngine().getEntitiesFor(selectedUnits).forEach((e)->{
            e.remove(SelectedUnitComponent.class);
        });
    }
}
