package com.comp460.launcher;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.comp460.FontManager;
import com.comp460.Settings;

import java.awt.*;
import java.util.Collection;

/**
 * Created by matthewhammond on 2/4/17.
 */
public class MainMenu extends ScreenAdapter {

    private static TextureRegion TEXTURE_TITLE = SplashScreen.TEXTURE_TITLE;
    private static TextureRegion TEXTURE_TITLE_BG = SplashScreen.TEXTURE_TITLE_BG;
    private static TextureRegion TEXTURE_BUTTON_NORMAL= new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/mm_button_normal.png")));
    private static TextureRegion TEXTURE_BUTTON_PRESSED= new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/mm_button_pressed.png")));
    private static TextureRegion TEXTURE_CURSOR= new TextureRegion(new Texture(Gdx.files.local("launcher/sprites/cursor.png")));

    protected static final Vector3 titleScale = new Vector3(.5f, .5f, 0f);
    protected static final Vector3 titlePos = new Vector3(400f / 2f - TEXTURE_TITLE.getRegionWidth() * titleScale.x/2f, 240f - TEXTURE_TITLE.getRegionHeight() * titleScale.y - 10f, 0f);

    private static BitmapFont menuFont = FontManager.getFont(FontManager.KEN_PIXEL, 16, Color.WHITE);
    enum AnimState {BUILD_MENU, DONE, POST, TRANSITION};

    private SpriteBatch batch;
    private Game game;
    private OrthographicCamera camera;
    private AnimState curAnimState;

    private Vector3 cursorPos = new Vector3(0,0,0);
    private MenuButton newGameButton = new MenuButton("--------", titlePos.x, titlePos.y-TEXTURE_BUTTON_NORMAL.getRegionHeight() - 10, menuFont, TEXTURE_BUTTON_NORMAL, TEXTURE_BUTTON_NORMAL, TEXTURE_BUTTON_PRESSED);
    private MenuButton loadGameButton = new MenuButton("--------", titlePos.x, titlePos.y-2*TEXTURE_BUTTON_NORMAL.getRegionHeight() - 10, menuFont, TEXTURE_BUTTON_NORMAL, TEXTURE_BUTTON_NORMAL, TEXTURE_BUTTON_PRESSED);
    private MenuButton battlePracticeButton = new MenuButton("Battle\nPractice", titlePos.x, titlePos.y-3*TEXTURE_BUTTON_NORMAL.getRegionHeight() - 10, menuFont, TEXTURE_BUTTON_NORMAL, TEXTURE_BUTTON_NORMAL, TEXTURE_BUTTON_PRESSED);

    MenuButton[] buttons = new MenuButton[] {newGameButton, loadGameButton, battlePracticeButton};

    MenuButton selectedButton = newGameButton;

    public MainMenu(Game game) {
        super();
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Settings.INTERNAL_WIDTH, Settings.INTERNAL_HEIGHT);
        curAnimState = AnimState.BUILD_MENU;

        newGameButton.down = loadGameButton;
        loadGameButton.down = battlePracticeButton;

        loadGameButton.up = newGameButton;
        battlePracticeButton.up = loadGameButton;

        battlePracticeButton.action = ()->{
            this.game.setScreen(new BattlePracticeMenu(game));
            dispose();
        };

        this.cursorPos = new Vector3(selectedButton.pos.x-4, selectedButton.pos.y-4, 0f);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(SplashScreen.BG, 0f, 0f);
        batch.draw(TEXTURE_TITLE, titlePos.x, titlePos.y, TEXTURE_TITLE.getRegionWidth() * titleScale.x, TEXTURE_TITLE.getRegionHeight() * titleScale.y);
        batch.draw(TEXTURE_TITLE, titlePos.x, titlePos.y, TEXTURE_TITLE_BG.getRegionWidth() * titleScale.x, TEXTURE_TITLE_BG.getRegionHeight() * titleScale.y);
        for (MenuButton butt : buttons) {
            butt.render(batch);
        }

        batch.draw(TEXTURE_CURSOR, cursorPos.x, cursorPos.y);

        cursorPos.slerp(new Vector3(selectedButton.pos.x-4, selectedButton.pos.y-4, 0f), 0.1f);
        batch.end();

        selectedButton.currenState = MenuButton.ButtonState.NORMAL;
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) selectedButton = selectedButton.left;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) selectedButton = selectedButton.right;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) selectedButton = selectedButton.up;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) selectedButton = selectedButton.down;
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z )) {
            selectedButton.click();
        } else {
            selectedButton.currenState = MenuButton.ButtonState.HOVERED;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.X )) {
            game.setScreen(new SplashScreen(game));
            dispose();
        }

        switch (curAnimState) {
            case BUILD_MENU:
                break;
            case DONE:
                break;
            case POST:
                break;
            case TRANSITION:
                break;
        }
    }
}
