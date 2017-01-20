package com.comp460.tactics.map;

/**
 * Created by matthewhammond on 1/18/17.
 */
public class MapTile {
    private boolean traversable;

    public MapTile(boolean traversable) {
        this.traversable = traversable;
    }

    public boolean isTraversable() {
        return this.traversable;
    }
}
