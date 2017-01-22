package com.comp460;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.comp460.screens.BattleScreen;
import com.comp460.screens.TacticsScreenECS;

public class Main extends Game {
	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();

		Settings.load();
		Assets.load();

		this.setScreen(new TacticsScreenECS(Settings.INTERNAL_WIDTH,
											Settings.INTERNAL_HEIGHT,
											batch,
                                            Assets.Maps.TEST));

//		this.setScreen(new BattleScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
	}
	
	@Override
	public void dispose () {
		Assets.dispose();
		batch.dispose();
	}
}
