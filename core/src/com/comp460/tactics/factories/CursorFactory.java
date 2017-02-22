package com.comp460.tactics.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.tactics.TacticsAssets;
import com.comp460.tactics.TacticsScreen;
import com.comp460.tactics.components.core.CameraTargetComponent;
import com.comp460.tactics.components.core.TextureComponent;
import com.comp460.tactics.components.core.TransformComponent;
import com.comp460.tactics.components.cursor.MapCursorComponent;
import com.comp460.tactics.components.map.MapPositionComponent;

/**
 * Created by matthewhammond on 2/21/17.
 */
public class CursorFactory {
    public static Entity makeCursor(TacticsScreen screen) {
        Entity cursor = new Entity();
        TextureComponent texture = new TextureComponent(new TextureRegion(TacticsAssets.CURSOR));
        CameraTargetComponent cameraTarget = new CameraTargetComponent(screen.getCamera(), 0.3f);
        MapPositionComponent selectedSquare = new MapPositionComponent(0, 0);
        TransformComponent transformComponent = new TransformComponent(0, 0, 0);

        MapCursorComponent cursorComponent = new MapCursorComponent(0.1f);

        cursor.add(texture);
        cursor.add(cameraTarget);
        cursor.add(selectedSquare);
        cursor.add(transformComponent);
        cursor.add(cursorComponent);
        return cursor;
    }
}
