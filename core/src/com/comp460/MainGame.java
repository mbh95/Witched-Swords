package com.comp460;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import com.comp460.assets.FontManager;
import com.comp460.battle.BattleScreen;
import com.comp460.common.GameUnit;
import com.comp460.common.input.Controller;
import com.comp460.common.input.KeyboardController;
import com.comp460.launcher.splash.SplashScreen;


public class MainGame extends Game {

	private SpriteBatch batch;
	private FrameBuffer buffer;
	private TextureRegion bufferRegion;

	private boolean showFPS = true;
	private BitmapFont fpsFont;

	public Controller controller;

	@Override
	public void create () {
		batch = new SpriteBatch();

		Settings.load();

		buffer = new FrameBuffer(Pixmap.Format.RGB888, Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT, false);
		buffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		bufferRegion = new TextureRegion(buffer.getColorBufferTexture());
		bufferRegion.flip(false, true);

		fpsFont = FontManager.getFont(FontManager.KEN_PIXEL, 8, Color.YELLOW);
		fpsFont.setColor(Color.YELLOW);

//		TacticsScreen ts = new TacticsScreen(Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT, Assets.Maps.TEST);
//		this.setScreen(new BattleScreen(this, ts));

//		this.setScreen(new TacticsScreen(Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT, Assets.Maps.TEST));
//		this.setScreen(new BattleScreen(this, null, GameUnit.loadFromJSON("json/units/protagonists/clarissa.json"), GameUnit.loadFromJSON("json/units/enemies/ghast.json")));

//		this.setScreen(new MainMenu(this));

		controller = new KeyboardController();
		this.setScreen(new SplashScreen(this));
//		this.setScreen(ts);

//		this.setScreen(new BattleScreen(this, null, new BattleUnitFactory(GameUnit.loadFromJSON("json/units/clarissa.json")), new BattleUnitFactory(GameUnit.loadFromJSON("json/units/ghast.json"))));
	}
// i'm matt and i want to be mean to ms poops <3
	@Override
	public void render () {
		buffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.render();
		buffer.end();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(bufferRegion, 0, 0, Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
		if (showFPS) {
			fpsFont.draw(batch, Gdx.graphics.getFramesPerSecond() + "", 10, Settings.WINDOW_HEIGHT - 10);
		}
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
