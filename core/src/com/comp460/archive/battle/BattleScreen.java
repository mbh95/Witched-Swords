package com.comp460.archive.battle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.comp460.Assets;
import com.comp460.archive.battle.BattleAttack;
import com.comp460.archive.battle.BattleUnit;
import com.comp460.Main;
import com.sun.javafx.binding.StringFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Belinda on 1/15/17.
 */
public class BattleScreen extends ScreenAdapter {

    private int DISP_WIDTH = 400;
    private int DISP_HEIGHT = 240;

    private float totalTime = 1100;

    private Main game;
    private ScreenAdapter tacticsScreen;
    private OrthographicCamera camera;
    private BitmapFont font = new BitmapFont(Gdx.files.internal("common/fonts/boss.fnt"));

    private BattleUnit bulba;
    private BattleUnit rogue;

    private int bounceDelay = 120;
    private boolean freeze = false;
    private int freezeDelay = 180;

    private float t = 0f;

    private List<BattleAttack> attacks = new ArrayList<BattleAttack>();

    public BattleScreen(Main parentGame, ScreenAdapter tacticsScreen) {
        this.game = parentGame;
        this.tacticsScreen = tacticsScreen;
        this.camera = new OrthographicCamera(DISP_WIDTH, DISP_HEIGHT);
        this.camera.position.set(DISP_WIDTH/2, DISP_HEIGHT/2, 0);

        bulba = new BattleUnit(
            new Texture[] {
                Assets.Textures.BULBA_IDLE0_BATTLE, Assets.Textures.BULBA_IDLE1_BATTLE
            },
            new Texture[] {
                Assets.Textures.BULBA_IDLE1_BATTLE,
                Assets.Textures.BULBA_IDLE2_BATTLE,
                Assets.Textures.BULBA_IDLE3_BATTLE,
                Assets.Textures.BULBA_IDLE4_BATTLE},
            new Texture[] {
                Assets.Textures.BULBA_ATTACK0_BATTLE
            }, new Texture[] {
                Assets.Textures.BULBA_HURT0_BATTLE
        }, new Texture[] {
                Assets.Textures.BULBA_FALLEN0_BATTLE,
                Assets.Textures.BULBA_FALLEN1_BATTLE
        });
        bulba.col = 5; bulba.row = 0;
        bulba.maxHP = 100;
        bulba.currHP = 100;
        bulba.startIdleAnimation();

        rogue = new BattleUnit(
            new Texture[] {
                Assets.Textures.ROGUE,
                Assets.Textures.ROGUE1,
                Assets.Textures.ROGUE2
            },
            new Texture[] {
                    Assets.Textures.ROGUE
            },
            new Texture[] {
                    Assets.Textures.ROGUE
            },
            null,null);
        rogue.player = true;
        rogue.col = 0; rogue.row = 0;
        rogue.maxHP = 10;
        rogue.currHP = 10;
        rogue.startIdleAnimation();
    }

    private void update(float delta) {
        // check if battle should end!!
        // check if both have no energy or either has no hp left or time is over
        // (later should check if no more energy can be spent)

        if (freeze) {
            if (freezeDelay < 0 && Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))
                game.setScreen(tacticsScreen);

            if (freezeDelay >= 0)
                freezeDelay--;
        }

        if ((rogue.currNRG == 0 && bulba.currNRG == 0) || rogue.currHP <= 0 || bulba.currHP <= 0 || totalTime < 0) {
            freeze = true;
        }

        camera.update();

        if (!freeze) {
            // move/attack with rogue! <3
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) rogue.col--;
            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) rogue.col++;
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) rogue.row++;
            if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) rogue.row--;

            if (Gdx.input.isKeyJustPressed(Input.Keys.Z) && rogue.currNRG != 0) {
                rogue.startAttackAnimation();
                rogue.currNRG--;
                for (int i = 0; i < 3; i++)
                    attacks.add(new BattleAttack(rogue.row, rogue.col + 1 + i, 10, rogue, Assets.Textures.LAZER,
                            (att) -> {
                                if (att.row == bulba.row && att.col == bulba.col) {
                                    bulba.currHP -= 1;
                                    if (bulba.currHP <= 0)
                                        bulba.startFallenAnimation();
                                    else
                                        bulba.startHurtAnimation();
                                }
                            }));
            }
            updateAI(delta);
        }

        bulba.updateSprite();
        rogue.updateSprite();

        if(rogue.row < 0) rogue.row = 0;
        if(rogue.row >= 2) rogue.row = 3-1;
        if(rogue.col < 0) rogue.col = 0;
        if(rogue.col >= 2) rogue.col = 3-1;

        if(bulba.row  < 0) bulba.row = 0;
        if(bulba.row  >= 2) bulba.row = 3-1;
        if(bulba.col - 3 < 0) bulba.col = 3;
        if(bulba.col - 3 >= 2) bulba.col = 6-1;

        List<BattleAttack> toDelete = new ArrayList<BattleAttack>();
        for (BattleAttack att : attacks) {
            att.update();
            if (att.duration == 0) {
                toDelete.add(att);
            }
        }
        for (BattleAttack del : toDelete) {
            attacks.remove(del);
            del.attacker.startIdleAnimation();
        }
    }

    private void drawMap() {
        // draw battle tiles
        for (int i = 2; i >= 0; i--) {
            for (int j = 2; j >= 0; j--) {
                game.batch.draw(Assets.Textures.BATTLE_TILE_BLUE, DISP_WIDTH/2 - (i+1)*40, j*40 + 20);
                game.batch.draw(Assets.Textures.BATTLE_TILE_RED, DISP_WIDTH/2 + i*40, j*40 + 20);
            }
        }
    }

    private void drawMask() {
        // draw battle tile mask
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);

//        sr.setColor(0.0f, 0.0f, 1.0f, 0.1f);
//        sr.rect(DISP_WIDTH/2 - 40*3, 20, 40*3, 40*3+9);
//        sr.setColor(1.0f, 0.0f, 0.0f, 0.1f);
//        sr.rect(DISP_WIDTH/2, 20, 40*3, 40*3+9);

        // draw attack warning
        for (BattleAttack attack : attacks) {
            if (attack.attacker == rogue)
                sr.setColor(0.0f, 0.0f, 1.0f, 0.5f);
            else if (attack.attacker == bulba)
                sr.setColor(1.0f, 0.0f, 0.0f, 0.5f);
            sr.rect(DISP_WIDTH/2 + (attack.col - 3)*40, attack.row*40 + 29, 40, 40);
        }

        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawHP(BattleUnit unit) {
        int x, y;
        if (unit.player) {
            x = DISP_WIDTH/2 - 64 - 10;
            y = 3;
        } else {
            x = DISP_WIDTH/2 + 10;
            y = 3;
        }
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(Assets.Textures.HP_BAR, x, y);
        game.batch.end();

        ShapeRenderer sr = new ShapeRenderer();
        sr.setProjectionMatrix(camera.combined);
        double percentHP = 1.0*unit.currHP/unit.maxHP;
        if (percentHP > .45)
            sr.setColor(Color.GREEN);
        else if (percentHP > .25)
            sr.setColor(Color.GOLDENROD);
        else
            sr.setColor(Color.SCARLET);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        if (percentHP > 0)
            sr.rect(x+9, y+8, (int) (52 * percentHP), 4);
        sr.end();

        game.batch.begin();
        for (int i = unit.currNRG; i > 0 ; i--)
            game.batch.draw(Assets.Textures.ENERGY, 51 + x - (i-1)*11, y+2);
        game.batch.end();
    }

    private void countdown() {
        if (!freeze) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            totalTime -= deltaTime;
        }
        int seconds = ((int) totalTime) % 60;
        String timerString = StringFormatter.format("%02d", seconds).getValue();
        font.draw(game.batch, timerString, DISP_WIDTH/2 - font.getSpaceWidth()*2, DISP_HEIGHT-55);
    }

    @Override
    public void render(float delta) {
        update(delta);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(Assets.Textures.BATTLE_BG, 0f, 0f, 400, 240);
        drawMap();
        game.batch.end();
        drawMask();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        countdown();

        // bounce bulba! <3
        float bulbaHeight = bulba.row*40 + 29;
        float rogueHeight = rogue.row*40 + 29;
//        if (bounceDelay <= 60) {
            bulbaHeight += 2f*Math.sin(t) + 2;
            rogueHeight += 2f*Math.sin(t) + 2;
//        }
        if (bounceDelay == 0) bounceDelay = 120;
        bounceDelay--;

        game.batch.draw(bulba.getSprite(), DISP_WIDTH/2 + (bulba.col - 3)*40, bulbaHeight);
        game.batch.draw(rogue.getSprite(), DISP_WIDTH/2 - 40*3 + rogue.col*40, rogueHeight);

        for (BattleAttack attack : attacks) {
            if (attack.warning <= 0)
                game.batch.draw(attack.sprite, DISP_WIDTH/2 + (attack.col - 3)*40, attack.row*40 + 29);
        }

        game.batch.end();

        drawHP(bulba);
        drawHP(rogue);

        if (freeze) {
            drawResults();
        }
        t+=0.05f;
    }

    private void drawResults() {
        /*
        if ((rogue.currNRG == 0 && bulba.currNRG == 0) || rogue.currHP <= 0 || bulba.currHP <= 0 || totalTime < 0) {
            freeze = true;
        }
         */

        game.batch.begin();
        if (rogue.currHP <= 0 && bulba.currHP <= 0) {
            // DRAW
            GlyphLayout layout = new GlyphLayout(font, "YOU BOTH DIED");
            font.draw(game.batch, "YOU BOTH DIED", DISP_WIDTH/2 - layout.width/2, 100);
        } else if (rogue.currHP <= 0) {
            // YOU LOSE
            GlyphLayout layout = new GlyphLayout(font, "YOU DIED");
            font.draw(game.batch, "YOU DIED", DISP_WIDTH/2 - layout.width/2, 100);

        } else if (bulba.currHP <= 0) {
            // YOU WIN
            GlyphLayout layout = new GlyphLayout(font, "ENEMY DIED");
            font.draw(game.batch, "ENEMY DIED", DISP_WIDTH/2 - layout.width/2, 100);

        } else if (rogue.currNRG == 0 && bulba.currNRG == 0) {
            // ENERGY DRAW
            GlyphLayout layout = new GlyphLayout(font, "OUT OF ENERGY");
            font.draw(game.batch, "OUT OF ENERGY", DISP_WIDTH/2 - layout.width/2, 100);

        } else if(totalTime < 0) {
            // TIME DRAW
            GlyphLayout layout = new GlyphLayout(font, "OUT OF TIME");
            font.draw(game.batch, "OUT OF TIME", DISP_WIDTH/2 - layout.width/2, 100);
        }
        if (freezeDelay < 0) {
            GlyphLayout layout = new GlyphLayout(font, "any key to continue");
            font.draw(game.batch, "any key to continue", DISP_WIDTH/2 - layout.width/2, 50);
        }
        game.batch.end();
    }

    public enum AiState {OFFENSE, DEFENSE};
    public AiState curAiState = AiState.OFFENSE;
    public int aiDelay = 30;
    public Random rng = new Random();

    public void updateAI(float delta) {
        if (aiDelay == 0) {
            aiDelay = 30;
            if (rng.nextDouble() < .1) {
                if (curAiState == AiState.DEFENSE) {
                    curAiState = AiState.OFFENSE;
                } else {
                    curAiState = AiState.DEFENSE;
                }
            }
            if (rogue.currNRG < bulba.currNRG && rng.nextDouble() < .4) {
                curAiState = AiState.OFFENSE;
            }
            if (bulba.currNRG <= 0) {
                curAiState = AiState.DEFENSE;
            }
            boolean isEnemyAttacking = false;
            for (BattleAttack att : attacks) {
                if (att.attacker == rogue) {
                    isEnemyAttacking = true;
                }
            }
            if (isEnemyAttacking && rng.nextDouble() < 0.2) {
                curAiState = AiState.DEFENSE;
            }

            switch(curAiState) {
                case OFFENSE:
//                    if (bulba.row != rogue.row) {
//                        bulba.row += (int)((1.0*rogue.row - bulba.row) / 2.0);
//                    } else {
                        if (bulba.col == 3 && rng.nextDouble() < .7 && bulba.currNRG != 0) {
                            bulba.startAttackAnimation();
                            bulba.currNRG--;
                            for (int i = 0; i < 3; i++) {
                                attacks.add(new BattleAttack(i, bulba.col - rng.nextInt(3)-1, 20, bulba, Assets.Textures.SCRATCH, (e) -> {
                                    if (e.row == rogue.row && e.col == rogue.col) {
                                        rogue.currHP -= 2;
                                    }
                                    e.effect = (ent)->{};
                                }));
                            }

                        }
//                    }
                    bulba.col--;
                    break;
                case DEFENSE:
                    bulba.col++;
                    if (bulba.row == rogue.row) {
                        bulba.row += rng.nextBoolean()?1:-1;
                        if (bulba.row < 0) {
                            bulba.row += 2;
                        } else if (bulba.row >= 3) {
                            bulba.row -= 2;
                        }
                    }
                    break;
            }
        } else {
            aiDelay--;
        }
    }
}
