package com.comp460;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.comp460.experimental.AssetManager;
import com.comp460.experimental.GameUnit;
//import com.comp460.experimental.battle.BattleScreen;
import com.comp460.screens.BattleScreen;
import com.comp460.tactics.TacticsScreen;

public class Main extends Game {
	public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();

		Settings.load();
		Assets.load();
		AssetManager.load();

		TacticsScreen ts = new TacticsScreen(Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT, Assets.Maps.TEST);
		this.setScreen(new BattleScreen(this, ts));
//		this.setScreen(new TacticsScreen(Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT, Assets.Maps.TEST));
//		this.setScreen(new BattleScreen(GameUnit.loadFromJSON("common/units/ruby.json"), GameUnit.loadFromJSON("common/units/bulba.json")));
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
