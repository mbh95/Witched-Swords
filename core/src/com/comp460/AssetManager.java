package com.comp460;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.sun.javafx.binding.StringFormatter;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by matthewhammond on 1/12/17.
 */
public class AssetManager {

    public static final String BATTLE_SPRITES_UNITS_PATH = "./battle/sprites/units/";
    public static final String MAPS_PATH = "./tactics/maps/";

    public static final Pattern getFileNamePatternFor(String unitID, BattleAnimation animID) {
        return Pattern.compile(StringFormatter.format("%s%s_(\\d+).png", unitID, animID.getAnimId()).getValue());
    }

    public enum BattleAnimation {
        IDLE("_idle"), ATTACK("_attack"), HURT("_hurt"), FALLEN("_fallen");
        private String animId;

        BattleAnimation(String id) {
            this.animId = id;
        }

        public String getAnimId() {
            return this.animId;
        }
    }

    private static Map<String, Animation<TextureRegion>> idAnimLookup = new HashMap<>();

    public static class Textures {


        public static Texture BATTLE_BG;

        public static Texture ACTION_MENU;
        public static TextureRegion BATTLE_TILE_BLUE;
        public static TextureRegion BATTLE_TILE_RED;
        public static TextureRegion BATTLE_TILE_BLUE_SIDE;
        public static TextureRegion BATTLE_TILE_RED_SIDE;

        public static Texture ENERGY;
        public static Texture HP_BAR;
        public static Texture MEGA;
        public static Texture SCRATCH;
        public static Texture LAZER;
        public static Texture SHIELD;
        public static Texture SPIKE;
        public static Texture ARROW;
        public static Texture PUFF;
    }

    public static class Maps {
        public static TiledMap TEST;
    }

    public static void load() {

        loadAllBattleAnims("bulba");
        loadAllBattleAnims("ghast");
        loadAllBattleAnims("ruby");
        loadAllBattleAnims("shieldman");
        loadAllBattleAnims("clarissa");
        getAnimation("shieldman", BattleAnimation.ATTACK).setFrameDuration(.5f);

        Texture battleTilesTexture = new Texture(Gdx.files.internal("battle/sprites/ui/battle_tiles_plains.png"));
        Textures.BATTLE_TILE_BLUE = new TextureRegion(battleTilesTexture, 0, 0, battleTilesTexture.getWidth()/2, battleTilesTexture.getWidth()/2);
        Textures.BATTLE_TILE_BLUE_SIDE = new TextureRegion(battleTilesTexture, 0, battleTilesTexture.getWidth()/2, battleTilesTexture.getWidth()/2, battleTilesTexture.getHeight() - battleTilesTexture.getWidth()/2);

        Textures.BATTLE_TILE_RED = new TextureRegion(battleTilesTexture, battleTilesTexture.getWidth()/2, 0, battleTilesTexture.getWidth()/2, battleTilesTexture.getWidth()/2);
        Textures.BATTLE_TILE_RED_SIDE = new TextureRegion(battleTilesTexture, battleTilesTexture.getWidth()/2, battleTilesTexture.getWidth()/2, battleTilesTexture.getWidth()/2, battleTilesTexture.getHeight() - battleTilesTexture.getWidth()/2);

        Textures.BATTLE_BG = new Texture(Gdx.files.internal("battle/sprites/backgrounds/plains.png"));
        Textures.HP_BAR = new Texture(Gdx.files.internal("battle/sprites/ui/hp_bar.png"));
        Textures.ENERGY = new Texture(Gdx.files.internal("battle/sprites/ui/energy.png"));
        Textures.SCRATCH = new Texture(Gdx.files.internal("battle/sprites/attacks/scratch.png"));
        Textures.LAZER = new Texture(Gdx.files.internal("battle/sprites/attacks/lazer.png"));
        Textures.SHIELD = new Texture(Gdx.files.internal("battle/sprites/attacks/shield_outline.png"));
        Textures.SPIKE = new Texture(Gdx.files.internal("battle/sprites/attacks/spike.png"));
        Textures.ARROW = new Texture(Gdx.files.internal("battle/sprites/attacks/arrow.png"));
        Textures.PUFF = new Texture(Gdx.files.internal("battle/sprites/attacks/puff.png"));

        Maps.TEST = new TmxMapLoader().load(MAPS_PATH + "testmap.tmx");
    }

    public static void dispose() {
    }

    public static void loadAllBattleAnims(String... unitIDs) {
        for (String unitID : unitIDs) {
            loadBattleAnim(unitID, BattleAnimation.IDLE);
            loadBattleAnim(unitID, BattleAnimation.ATTACK);
            loadBattleAnim(unitID, BattleAnimation.HURT);
            loadBattleAnim(unitID, BattleAnimation.FALLEN);
        }
    }

    public static void loadBattleAnim(String unitID, BattleAnimation animID) {
        File path = new File(BATTLE_SPRITES_UNITS_PATH + unitID + "/");
        if (path == null || path.listFiles() == null || path.listFiles().length == 0) {
            System.err.printf("Error loading battle animation (%s, %s): No such files\n", unitID, animID.getAnimId());
            return;
        }
        Pattern filenamePattern = getFileNamePatternFor(unitID, animID);
        Map<File, Integer> indexicalMap = new HashMap<>();
        Queue<File> frameFiles = new PriorityQueue<>((o1, o2) -> indexicalMap.get(o1).compareTo(indexicalMap.get(o2)));
        for (File f : path.listFiles()) {
            Matcher m;
            if ((m = filenamePattern.matcher(f.getName())).matches()) {
                indexicalMap.put(f, Integer.parseInt(m.group(1)));
                frameFiles.add(f);
            }
        }
        if (frameFiles.size() == 0) {
            System.err.printf("Error loading battle animation (%s, %s): No such files\n", unitID, animID.getAnimId());
            return;
        }
        TextureRegion[] frameArray = new TextureRegion[frameFiles.size()];
        int i = 0;
        while (!frameFiles.isEmpty()) {
            frameArray[i++] = new TextureRegion(new Texture(new FileHandle(frameFiles.poll())));
        }
        idAnimLookup.put(unitID + animID.getAnimId(), new Animation<>(.25f, frameArray));
        System.out.printf("Successfully loaded battle animation (%s, %s): %d frames\n", unitID, animID.getAnimId(), frameArray.length);
    }

    public static Animation<TextureRegion> getAnimation(String id, BattleAnimation anim) {
        if (!idAnimLookup.containsKey(id + anim.getAnimId())) {
            loadBattleAnim(id, anim);
            if (!idAnimLookup.containsKey(id + anim.getAnimId()) && anim != BattleAnimation.IDLE) {
                return getAnimation(id, BattleAnimation.IDLE);
            }
        }
        return idAnimLookup.get(id + anim.getAnimId());
    }
}
