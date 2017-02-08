package com.comp460.launcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.comp460.AssetManager;
import com.comp460.Settings;
import com.comp460.battle.BattleScreen;
import com.comp460.common.GameUnit;

/**
 * Created by matthewhammond on 2/6/17.
 */
public class BattlePracticeMenu extends ScreenAdapter {
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;
    private Game game;
    OrthographicCamera camera;

    GameUnit playerUnit;
    GameUnit aiUnit;

    Animation<TextureRegion> playerUnitIdle;
    Animation<TextureRegion> aiUnitIdle;

    float frameTime = 0.0f;

    public BattlePracticeMenu(Game parent) {
        super();
        batch = new SpriteBatch();
        camera = new OrthographicCamera(400, 240);
        camera.setToOrtho(false, 400, 240);
        skin = new Skin(Gdx.files.internal("launcher/ui/uiskin.json"));
        stage = new Stage();
        game = parent;

        addPlayerButton("common/units/ruby.json", 0, 0, 100, 100);
        addPlayerButton("common/units/shieldman.json", 100, 0, 100, 100);
        addPlayerButton("common/units/bulba.json", 200, 0, 100, 100);

        addAiButton("common/units/bulba.json", Gdx.graphics.getWidth()-100, 0, 100, 100);

        final TextButton battleButton = new TextButton("Fight!", skin, "default");
        battleButton.setSize(200f, 100f);
        battleButton.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 - 10f);

        battleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (playerUnit == null || aiUnit == null) {
                    return;
                }
                game.setScreen(new BattleScreen(game, Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT, playerUnit, aiUnit));
                dispose();
            }
        });
        stage.addActor(battleButton);

        Gdx.input.setInputProcessor(stage);
    }

    private void addPlayerButton(String jsonFile, int x, int y, int width, int height) {

        GameUnit unit = GameUnit.loadFromJSON(jsonFile);
        TextButton button = new TextButton(unit.getName(), skin, "default");
        button.setSize(width, height);
        button.setPosition(x, y);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                playerUnit = unit;
                playerUnitIdle = com.comp460.AssetManager.getAnimation(unit.getId(), AssetManager.BattleAnimation.IDLE);
            }
        });
        stage.addActor(button);
    }

    private void addAiButton(String jsonFile, int x, int y, int width, int height) {

        GameUnit unit = GameUnit.loadFromJSON(jsonFile);
        TextButton button = new TextButton(unit.getName(), skin, "default");
        button.setSize(width, height);
        button.setPosition(x, y);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                aiUnit = unit;
                aiUnitIdle = com.comp460.AssetManager.getAnimation(unit.getId(), AssetManager.BattleAnimation.IDLE);
            }
        });
        stage.addActor(button);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        stage.draw();
        batch.end();

        batch.begin();

        float scale = 4.0f;
        if (playerUnitIdle != null) {
            TextureRegion currentFrame = playerUnitIdle.getKeyFrame(frameTime, true);
            batch.draw(currentFrame, 0, 0 , currentFrame.getRegionWidth(), currentFrame.getRegionHeight());
        }
        if (aiUnitIdle != null) {
            TextureRegion currentFrame = aiUnitIdle.getKeyFrame(frameTime, true);
            batch.draw(currentFrame, Gdx.graphics.getWidth() - currentFrame.getRegionWidth() * scale, Gdx.graphics.getHeight() - currentFrame.getRegionHeight() * scale , currentFrame.getRegionWidth() * scale, currentFrame.getRegionHeight() * scale);
        }
        batch.end();
        frameTime += delta;
    }
}
