package com.comp460.tactics.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;

/**
 * Created by matthewhammond on 1/26/17.
 */
public class MapCursorComponent implements Component {
    public int countdown, delay;
    public Entity selection;

    public MapCursorComponent populate(int delay) {
        this.delay = delay;
        if (this.countdown > this.delay) {
            this.countdown = this.delay;
        }
        return this;
    }
}
