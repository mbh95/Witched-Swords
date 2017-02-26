package com.comp460.util;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Created by matth on 2/14/2017.
 */
public class TexturePackerUtil {
    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.combineSubdirectories = true;
        TexturePacker.process(settings, "./assets-raw/sprites/battle", "./assets/sprites", "battle");
        TexturePacker.process(settings, "./assets-raw/sprites/battle_practice", "./assets/sprites", "battle_practice");
        TexturePacker.process(settings, "./assets-raw/sprites/common", "./assets/sprites", "common");
        TexturePacker.process(settings, "./assets-raw/sprites/main_menu", "./assets/sprites", "main_menu");
        TexturePacker.process(settings, "./assets-raw/sprites/splash", "./assets/sprites", "splash");
        TexturePacker.process(settings, "./assets-raw/sprites/tactics", "./assets/sprites", "tactics");
    }
}
