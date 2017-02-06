package com.comp460.launcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by matthewhammond on 2/4/17.
 */
public class MainMenu extends ScreenAdapter {

    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;
    private Game game;

    public MainMenu(Game parent) {
        super();
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("launcher/ui/uiskin.json"));
        stage = new Stage();
        game = parent;

        final TextButton battleButton = new TextButton("Battle Practice", skin, "default");
        battleButton.setSize(200f, 100f);
        battleButton.setPosition(Gdx.graphics.getWidth() /2 - 100f, Gdx.graphics.getHeight()/2 - 10f);

        battleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                game.setScreen(new BattlePracticeMenu(game));
                dispose();
            }
        });

        stage.addActor(battleButton);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        stage.draw();
        batch.end();
    }
}
