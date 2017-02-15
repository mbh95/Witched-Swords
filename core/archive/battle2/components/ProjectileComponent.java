package com.comp460.archive.battle2.components;

import com.badlogic.ashley.core.Component;
import com.comp460.archive.battle2.BattleUnit;

/**
 * Created by matth on 2/13/2017.
 */
public class ProjectileComponent implements Component {
    public int dr;
    public int dc;
    public float delay;
    public transient float countdown;

    public ProjectileComponent() {

    }

    public ProjectileComponent(BattleUnit owner, ProjectileComponent template) {
        this.dr = template.dr;
        this.dc = template.dc;
        this.delay = template.delay;
        this.countdown = delay;
    }
}
