package com.comp460.battle.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.AssetMgr;
import com.comp460.battle.BattleUnit;

/**
 * Created by matth on 2/13/2017.
 */
public class MoveTextureComponent implements Component{
    public String path;
    public transient TextureRegion texture;

    public MoveTextureComponent() {

    }

    public MoveTextureComponent(BattleUnit owner, MoveTextureComponent template) {
        this.path = template.path;
        this.texture = AssetMgr.getTexture(path);
    }
}
