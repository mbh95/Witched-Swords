package com.comp460.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by matth on 2/8/2017.
 */
public class FontManager {
    public static final FreeTypeFontGenerator KEN_PIXEL = new FreeTypeFontGenerator(Gdx.files.local("fonts/kenpixel.ttf"));
    public static final FreeTypeFontGenerator KEN_PIXEL_BLOCKS = new FreeTypeFontGenerator(Gdx.files.local("fonts/kenpixel_blocks.ttf"));
    public static final FreeTypeFontGenerator KEN_PIXEL_MINI = new FreeTypeFontGenerator(Gdx.files.local("fonts/kenpixel_mini.ttf"));
    public static final FreeTypeFontGenerator KEN_VECTOR_FUTURE = new FreeTypeFontGenerator(Gdx.files.local("fonts/kenvector_future.ttf"));

    public static BitmapFont getFont(FreeTypeFontGenerator generator, int size, Color color) {
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = size;
        param.color = color;
        return generator.generateFont(param);
    }

    public static BitmapFont getFont(FreeTypeFontGenerator generator, int size, Color color, Color borderColor, float borderWidth) {
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = size;
        param.color = color;
        param.borderWidth = borderWidth;
        param.borderColor = borderColor;
        return generator.generateFont(param);
    }

}
