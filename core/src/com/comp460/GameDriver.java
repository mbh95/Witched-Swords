package com.comp460;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.comp460.common.GameScreen;
import com.comp460.tactics.TacticsScreen;

import java.util.Stack;

public class GameDriver extends Game {

	private Stack<GameScreen> screenStack;

	@Override
	public void create () {
		Settings.load();
		Assets.load();

		this.screenStack = new Stack<>();
		pushScreen(new TacticsScreen(Settings.INTERNAL_WIDTH,
				Settings.INTERNAL_HEIGHT,
				Assets.Maps.TEST,
				this));
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
	}

	public void pushScreen(GameScreen newScreen) {
		if (!this.screenStack.isEmpty()) {
			this.screenStack.peek().pause();
		}
		this.screenStack.push(newScreen);
		this.setScreen(newScreen);
		this.screenStack.peek().resume();
	}

	public GameScreen popScreen() {
		if (this.screenStack.isEmpty()) {
			return null;
		}
		GameScreen old = this.screenStack.pop();
		old.pause();
		if (!this.screenStack.isEmpty()) {
			this.setScreen(this.screenStack.peek());
			this.screenStack.peek().resume();
		} else {
			this.dispose();
		}
		return old;
	}
}
