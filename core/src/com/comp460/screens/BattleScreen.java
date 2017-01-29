//package com.comp460.screens;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input;
//import com.badlogic.gdx.ScreenAdapter;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.comp460.Assets;
//import com.comp460.GameDriver;
//import com.comp460.battle.BattleAttack;
//import com.comp460.battle.BattleUnit;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
///**
// * Created by Belinda on 1/15/17.
// */
//public class BattleScreen extends ScreenAdapter {
//
//    private int DISP_WIDTH = 400;
//    private int DISP_HEIGHT = 240;
//
//    private GameDriver game;
//    private OrthographicCamera camera;
//
//    private BattleUnit bulba;
//    private BattleUnit rogue;
//
//    private int bounceDelay = 120;
//    private int cursorDelay = 10;
//
//    private float t = 0f;
//
//    private List<BattleAttack> attacks = new ArrayList<BattleAttack>();
//
//    public BattleScreen(GameDriver parentGame) {
//        this.game = parentGame;
//        this.camera = new OrthographicCamera(DISP_WIDTH, DISP_HEIGHT);
//        this.camera.position.set(DISP_WIDTH/2, DISP_HEIGHT/2, 0);
//
//        bulba = new BattleUnit(new Texture[] {
//            Assets.Textures.BULBA_IDLE1_BATTLE,
//                Assets.Textures.BULBA_IDLE2_BATTLE,
//                Assets.Textures.BULBA_IDLE3_BATTLE,
//                Assets.Textures.BULBA_IDLE4_BATTLE}, null,null,null,null);
//        bulba.col = 3; bulba.row = 0;
//        bulba.maxHP = 100;
//        bulba.currHP = 100;
//
//        rogue = new BattleUnit(new Texture[] {
//            Assets.Textures.ROGUE,
//                Assets.Textures.ROGUE1,
//                Assets.Textures.ROGUE2
//        }, null,null,null,null);
//        rogue.player = true;
//        rogue.col = 0; rogue.row = 0;
//        rogue.maxHP = 10;
//        rogue.currHP = 10;
//    }
//
//    private void update(float delta) {
//        camera.update();
//
//        // move/attack with rogue! <3
//        if (cursorDelay == 0) {
//            int oldRow = rogue.row;
//            int oldCol = rogue.col;
//            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) rogue.col--;
//            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) rogue.col++;
//            if (Gdx.input.isKeyPressed(Input.Keys.UP)) rogue.row++;
//            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) rogue.row--;
//
//            if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
//                for (int i = 0; i < 3; i++)
//                    attacks.add(new BattleAttack(rogue.row, rogue.col + 1 + i, 10, Assets.Textures.LAZER,
//                    (att) -> {
//                        if (att.row == bulba.row && att.col == bulba.col) {
//                            bulba.currHP -= 1;
//                        }
//                    }));
//            }
//
//            if (rogue.row != oldRow || rogue.col != oldCol) {
//                cursorDelay = 8;
//            }
//        }
//        if (cursorDelay > 0)
//            cursorDelay--;
//
//        updateAI(delta);
//
//        if(rogue.row < 0) rogue.row = 0;
//        if(rogue.row >= 2) rogue.row = 3-1;
//        if(rogue.col < 0) rogue.col = 0;
//        if(rogue.col >= 2) rogue.col = 3-1;
//
//        if(bulba.row  < 0) bulba.row = 0;
//        if(bulba.row  >= 2) bulba.row = 3-1;
//        if(bulba.col - 3 < 0) bulba.col = 3;
//        if(bulba.col - 3 >= 2) bulba.col = 6-1;
//
//        List<BattleAttack> toDelete = new ArrayList<BattleAttack>();
//        for (BattleAttack att : attacks) {
//            att.update();
//            if (att.duration == 0) {
//                toDelete.add(att);
//            }
//        }
//        for (BattleAttack del : toDelete) {
//            attacks.remove(del);
//        }
//    }
//
//    private void drawMap() {
//        // draw battle tiles
//        for (int i = 2; i >= 0; i--) {
//            for (int j = 2; j >= 0; j--) {
//                game.batch.draw(Assets.Textures.BATTLE_TILE, DISP_WIDTH/2 - (i+1)*40, j*40 + 20);
//                game.batch.draw(Assets.Textures.BATTLE_TILE, DISP_WIDTH/2 + i*40, j*40 + 20);
//            }
//        }
//    }
//
//    private void drawMask() {
//        // draw battle tile mask
//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        ShapeRenderer sr = new ShapeRenderer();
//        sr.setProjectionMatrix(camera.combined);
//        sr.begin(ShapeRenderer.ShapeType.Filled);
//
//        sr.setColor(0.0f, 0.0f, 1.0f, 0.1f);
//        sr.rect(DISP_WIDTH/2 - 40*3, 20, 40*3, 40*3+9);
//        sr.setColor(1.0f, 0.0f, 0.0f, 0.1f);
//        sr.rect(DISP_WIDTH/2, 20, 40*3, 40*3+9);
//        sr.end();
//        Gdx.gl.glDisable(GL20.GL_BLEND);
//    }
//
//    private void drawHP(BattleUnit unit) {
//        int x, y;
//        if (unit.player) {
//            x = 4; y = DISP_HEIGHT-20;
//        } else {
//            x = DISP_WIDTH-64-4; y = DISP_HEIGHT-20;
//        }
//        game.batch.setProjectionMatrix(camera.combined);
//        game.batch.begin();
//        game.batch.draw(Assets.Textures.HP_BAR, x, y);
//        game.batch.end();
//
//        ShapeRenderer sr = new ShapeRenderer();
//        sr.setProjectionMatrix(camera.combined);
//        double percentHP = 1.0*unit.currHP/unit.maxHP;
//        if (percentHP > .45)
//            sr.setColor(Color.GREEN);
//        else if (percentHP > .25)
//            sr.setColor(Color.GOLDENROD);
//        else
//            sr.setColor(Color.SCARLET);
//        sr.begin(ShapeRenderer.ShapeType.Filled);
//        if (percentHP > 0)
//            sr.rect(x+9, y+3, (int) (52 * percentHP), 4);
//        sr.end();
//    }
//
//    @Override
//    public void render(float delta) {
//        update(delta);
//
//        game.batch.setProjectionMatrix(camera.combined);
//        game.batch.begin();
//        game.batch.draw(Assets.Textures.BATTLE_BG, 0f, 0f, 400, 240);
//        drawMap();
//        game.batch.end();
//        drawMask();
//
//        game.batch.setProjectionMatrix(camera.combined);
//        game.batch.begin();
//
//        // bounce bulba! <3
//        float bulbaHeight = bulba.row*40 + 29;
//        float rogueHeight = rogue.row*40 + 29;
////        if (bounceDelay <= 60) {
//            bulbaHeight += 2f*Math.sin(t) + 2;
//            rogueHeight += 2f*Math.sin(t) + 2;
////        }
//        if (bounceDelay == 0) bounceDelay = 120;
//        bounceDelay--;
//        bulba.updateSprite();
//        game.batch.draw(bulba.getSprite(), DISP_WIDTH/2 + (bulba.col - 3)*40, bulbaHeight);
//        rogue.updateSprite();
//        game.batch.draw(rogue.getSprite(), DISP_WIDTH/2 - 40*3 + rogue.col*40, rogueHeight);
//
//        for (BattleAttack attack : attacks) {
//            game.batch.draw(attack.sprite, DISP_WIDTH/2 + (attack.col - 3)*40, attack.row*40 + 29);
//        }
//        game.batch.end();
//
//        drawHP(bulba);
//
//        drawHP(rogue);
//        t+=0.05f;
//    }
//
//    public enum AiState {OFFENSE, DEFENSE};
//    public AiState curAiState = AiState.DEFENSE;
//    public int aiDelay = 30;
//    public Random rng = new Random();
//
//    public void updateAI(float delta) {
//        if (aiDelay == 0) {
//            aiDelay = 30;
//            switch(curAiState) {
//                case OFFENSE:
//                    if (rng.nextDouble() < .05) {
//                        curAiState = AiState.DEFENSE;
//                    }
//                    if (rogue.col != 2 && rng.nextDouble() < .2) {
//                        curAiState = AiState.DEFENSE;
//                    }
//                    if (bulba.row != rogue.row) {
//                        bulba.row += (int)((1.0*rogue.row - bulba.row) / 2.0);
//                    } else {
//                        if (rng.nextDouble() < .7) {
//
//                            attacks.add(new BattleAttack(bulba.row, bulba.col - 1, 20, Assets.Textures.SCRATCH, (e) -> {
//                                if (e.row == rogue.row && e.col == rogue.col) {
//                                    rogue.currHP -= 2;
//                                }
//                                e.effect = (ent)->{};
//                            }));
//                        }
//                    }
//                    bulba.col--;
//                    break;
//                case DEFENSE:
//                    if (rng.nextDouble() < .05) {
//                        curAiState = AiState.OFFENSE;
//                    }
//                    if (rogue.col == 2 && rng.nextDouble() < .2) {
//                        curAiState = AiState.OFFENSE;
//                    }
//                    bulba.col++;
//                    if (bulba.row == rogue.row) {
//                        bulba.row += rng.nextBoolean()?1:-1;
//                        if (bulba.row < 0) {
//                            bulba.row += 2;
//                        } else if (bulba.row >= 3) {
//                            bulba.row -= 2;
//                        }
//                    }
//
//                    break;
//            }
//        } else {
//            aiDelay--;
//        }
//    }
//}
