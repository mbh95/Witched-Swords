package com.comp460.util;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.comp460.screens.tactics.TacticsMap;
import com.comp460.screens.tactics.components.unit.PlayerControlledComponent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by matthewhammond on 4/10/17.
 */
public class MapIconGeneratorUtil {
    public static void generate() throws IOException {
//        TacticsMap map = new TacticsMap();

        String[] mapJSONFiles = new String[]{
                "maps/joe.json",
                "maps/bridge.json",
                "maps/cliffs.json",
                "maps/indoor.json"
        };

        Json json = new Json();
        for (String path : mapJSONFiles) {
            TacticsMap map = json.fromJson(TacticsMap.class, Gdx.files.internal(path));
            map.init(null);
            BufferedImage sprite = new BufferedImage(map.getWidth() * 3, map.getHeight() * 3, BufferedImage.TYPE_INT_RGB);
            Graphics g = sprite.getGraphics();
            g.setColor(new Color(149, 111, 44));
            g.fillRect(0, 0, sprite.getWidth(), sprite.getHeight());
            for (int r = map.getHeight() - 1; r >= 0; r--) {
                for (int c = 0; c < map.getWidth(); c++) {
                    Entity e = map.getUnitAt(r, c);
                    int x = c * 3;
                    int y = (map.getHeight() - r - 1) * 3;
                    if (e != null) {
                        if (PlayerControlledComponent.get(e) != null) {
                            g.setColor(new Color(50, 50, 172));

                            g.fillRect(x, y, 3, 3);

                            g.setColor(new Color(67, 67, 239));
                            g.fillRect(x + 1, y + 1, 1, 1);
                        } else {
                            g.setColor(new Color(172, 50, 50));
                            g.fillRect(x, y, 3, 3);

                            g.setColor(new Color(237, 72, 72));
                            g.fillRect(x + 1, y + 1, 1, 1);

                        }
                    } else if (!map.tiles[r][c].traversable) {
                        g.setColor(new Color(205, 187, 103));
                        g.fillRect(x, y, 3, 3);
                        g.setColor(new Color(76, 58, 14));
                        g.drawLine(x, y + 3, x + 2, y + 3);
                    }
                }
            }

            File outputfile = new File(path + ".png");
            ImageIO.write(sprite, "png", outputfile);

        }
    }
}
