package com.comp460.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.comp460.screens.tactics.TacticsMap;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by matthewhammond on 4/10/17.
 */
public class MapIconGeneratorUtil {
    public static void main(String[] args) {
//        TacticsMap map = new TacticsMap();

        String[] mapJSONFiles = new String[]{

        };

        Json json = new Json();
        for (String path : mapJSONFiles) {
            TacticsMap map = json.fromJson(TacticsMap.class, Gdx.files.internal(path));
            map.init(null);
            BufferedImage sprite = new BufferedImage(map.getWidth() * 3, map.getHeight() * 3, BufferedImage.TYPE_INT_RGB);
            Graphics g = sprite.getGraphics();
            g.setColor(new Color(0xff00ff));
            g.fillRect(0, 0, sprite.getWidth(), sprite.getHeight());
            for (int r = 0; r < map.getHeight(); r++) {
                for (int c = 0; c < map.getWidth(); c++) {
                    Entity e = map.getUnitAt(r, c);
                    if (e != null) {
                        if (PlayerControlledComponent.get(e) != null) {
                            g.setColor(Color.BLUE);
                            g.fillRect();
                        } else if () {

                        }
                    }
                }
            }
            g.setColor(Color.RED);
        }

    }
}
