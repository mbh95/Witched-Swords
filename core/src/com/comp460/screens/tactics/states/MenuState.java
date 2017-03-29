//package com.comp460.screens.tactics.states;
//
//import com.badlogic.gdx.graphics.g2d.GlyphLayout;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.comp460.common.GameState;
//import com.comp460.screens.launcher.NinePatchTextButton;
//import com.comp460.screens.launcher.main.MainMenuAssets;
//import com.comp460.screens.tactics.TacticsScreen;
//import com.comp460.screens.tactics.systems.cursor.MapCursorMovementSystem;
//import com.comp460.screens.tactics.systems.cursor.MapCursorSelectionSystem;
//
//import java.util.ArrayList;
//
//import static com.comp460.screens.tactics.TacticsScreen.TacticsState.PLAYER_TURN;
//
///**
// * Created by Belinda on 3/27/17.
// */
//public class MenuState implements GameState{
//    @Override
//    public void init() {
//        buttonX = (int) (width/2f - buttonWidth / 2f);
//        topButtonY = (height - 2*buttonHeight);
//        menuButtons = new ArrayList<>(menuButtonTemplates.length);
//
//        for (int i = 0; i < menuButtonTemplates.length; i++) {
//            TacticsScreen.TemplateRow template = menuButtonTemplates[i];
//            NinePatchTextButton newButton = new NinePatchTextButton(buttonX, topButtonY - i * buttonHeight, buttonWidth, buttonHeight, new GlyphLayout(MainMenuAssets.FONT_MENU_ITEM, template.text), MainMenuAssets.FONT_MENU_ITEM, MainMenuAssets.NINEPATCH_BUTTON, template.action);
//            menuButtons.add(newButton);
//        }
//
//        for (int i = 0; i < menuButtons.size(); i++) {
//            if (i > 0)
//                menuButtons.get(i).up = menuButtons.get(i - 1);
//            if (i < menuButtons.size() - 1)
//                menuButtons.get(i).down = menuButtons.get(i + 1);
//        }
//    }
//
//    @Override
//    public void update(float delta) {
//        renderMenu(delta);
//        engine.getSystem(MapCursorMovementSystem.class).setProcessing(false);
//        engine.getSystem(MapCursorSelectionSystem.class).setProcessing(false);
////                    engine.getSystem(TurnManagementSystem.class).endTurn();
//        if (game.controller.leftJustPressed()) curSelectedButton = curSelectedButton.left;
//        if (game.controller.rightJustPressed()) curSelectedButton = curSelectedButton.right;
//        if (game.controller.upJustPressed()) curSelectedButton = curSelectedButton.up;
//        if (game.controller.downJustPressed()) curSelectedButton = curSelectedButton.down;
//        if (game.controller.button1JustPressed()) {
////                    System.out.println(curSelectedButton.pos);
//            curSelectedButton.click();
//        }
//        if (game.controller.button2JustPressed()) {
//            engine.getSystem(MapCursorMovementSystem.class).setProcessing(true);
//            engine.getSystem(MapCursorSelectionSystem.class).setProcessing(true);
//            curState = PLAYER_TURN;
//        }
//    }
//
//    @Override
//    public void render(SpriteBatch batch, SpriteBatch uiBatch) {
//
//    }
//}
