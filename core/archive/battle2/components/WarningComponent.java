package com.comp460.archive.battle2.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.comp460.archive.battle2.BattleUnit;

/**
 * Created by matth on 2/13/2017.
 */
public class WarningComponent implements Component {
    public float duration;
    public transient Color color;

    public WarningComponent() {

    }

    public WarningComponent(BattleUnit owner, WarningComponent template) {

        this.duration = template.duration;
        this.color = owner.getGrid().isOnLHS(owner.getGridRow(), owner.getGridCol())?new Color(0f,0f,1f, 0.3f):new Color(1f,0f,0f, 0.3f);
    }
}
