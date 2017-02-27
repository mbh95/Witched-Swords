package com.comp460.screens.tactics.systems.ui;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.Settings;
import com.comp460.assets.FontManager;
import com.comp460.screens.tactics.TacticsAssets;
import com.comp460.screens.tactics.TacticsScreen;

/**
 * Created by matth on 2/24/2017.
 */
public class TurnRenderingSystem extends EntitySystem {

    private static final BitmapFont turnFont = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.WHITE);
    private static final GlyphLayout playerTurnLayout = new GlyphLayout(turnFont, "Player Turn");
    private static final GlyphLayout aiTurnLayout = new GlyphLayout(turnFont, "Enemy Turn");
    private static final GlyphLayout toEndLayout = new GlyphLayout(turnFont, " to end");

    private TacticsScreen screen;

    public TurnRenderingSystem(TacticsScreen screen) {
        this.screen = screen;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        TextureRegion bg;
        GlyphLayout layout;
        if (screen.curState == TacticsScreen.TacticsState.PLAYER_TURN) {
            bg = TacticsAssets.TURN_PLAYER;
            layout = playerTurnLayout;
        } else if (screen.curState == TacticsScreen.TacticsState.AI_TURN) {
            bg = TacticsAssets.TURN_AI;
            layout = aiTurnLayout;
        } else {
            return;
        }
        int x = Settings.INTERNAL_WIDTH - bg.getRegionWidth();
        int y = 0;
        screen.uiBatch.begin();
        screen.uiBatch.draw(bg, x, y);
        turnFont.draw(screen.uiBatch, layout, x + 4, y + bg.getRegionHeight() / 2 + 8 + 1);

        if (screen.curState == TacticsScreen.TacticsState.PLAYER_TURN) {
            screen.uiBatch.draw(screen.game.controller.startSprite(), x + 2, y + 2);
            turnFont.draw(screen.uiBatch, toEndLayout, x + 2 + screen.game.controller.startSprite().getRegionWidth() + 2, 2 + 8);
        }
        screen.uiBatch.end();
    }
}
