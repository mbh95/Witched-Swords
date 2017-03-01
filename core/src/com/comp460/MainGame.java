package com.comp460;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import com.comp460.assets.FontManager;
import com.comp460.common.input.Controller;
import com.comp460.common.input.KeyboardController;
import com.comp460.screens.launcher.splash.SplashScreen;


public class MainGame extends Game {

    private float goalAspect;
    private int width, height;
    private int bufferX, bufferY;

    private SpriteBatch batch;
    private FrameBuffer buffer;
    private TextureRegion bufferRegion;

    private OrthographicCamera windowCamera = new OrthographicCamera();

    private boolean showFPS = true;
    private BitmapFont fpsFont;

    public Controller controller;

    public Music music;
    public String playingNow = "";

    @Override
    public void create() {

        batch = new SpriteBatch();

        Settings.load();

        this.goalAspect = 1f * Settings.INTERNAL_WIDTH / Settings.INTERNAL_HEIGHT;

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
    public void render() {
        // Render the game to the frame-buffer
        buffer.begin();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
        buffer.end();

        // Render the frame-buffer to the screen adjusted for window size
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(bufferRegion, bufferX, bufferY, buffer.getWidth(), buffer.getHeight());
        if (showFPS) {
            fpsFont.draw(batch, Gdx.graphics.getFramesPerSecond() + "", Gdx.graphics.getWidth() - 15, Gdx.graphics.getHeight() - 10);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.windowCamera.setToOrtho(false, width, height);
        this.windowCamera.update();
        this.batch.setProjectionMatrix(this.windowCamera.combined);


        float aspect = 1f * width / height;

        if (aspect >= goalAspect) {
            this.height = height;
            this.width = Math.round(goalAspect * height);
        } else {
            this.width = width;
            this.height = Math.round(width / goalAspect);
        }

        this.bufferX = width/2 - this.width/2;
        this.bufferY = height/2 - this.height/2;

        if (buffer != null) {
            buffer.dispose();
        }
        buffer = new FrameBuffer(Pixmap.Format.RGB888, this.width, this.height, false);
        buffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        bufferRegion = new TextureRegion(buffer.getColorBufferTexture());
        bufferRegion.flip(false, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void playMusic(String string) {
        if (playingNow.equals(string)) {
            return;
        }
        if (music != null) {
            music.stop();
            music.dispose();
        }
        music = Gdx.audio.newMusic(Gdx.files.internal(string));
        music.setLooping(true);
        music.play();
        playingNow = string;
    }
}
