package com.comp460.screens.tactics.systems.rendering;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.comp460.assets.SpriteManager;
import com.comp460.screens.tactics.TacticsScreen;
import com.comp460.screens.tactics.components.cursor.MovementPathComponent;
import com.comp460.screens.tactics.components.map.MapPositionComponent;
import com.comp460.screens.tactics.components.unit.AIControlledComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Renders paths onthe map.
 */
public class PathRenderingSystem extends IteratingSystem {

    private static final Family pathingUnitFamily = Family.all(MovementPathComponent.class).get();
    private static final Family enemyUnitFamily = Family.all(AIControlledComponent.class).get();

    private static final ComponentMapper<MovementPathComponent> pathM = ComponentMapper.getFor(MovementPathComponent.class);

    private TacticsScreen screen;

    private TextureRegion ARROW_NORTH_NORTH = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_continue_vert");
    private TextureRegion ARROW_NORTH_EAST = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_bend_down_right");
    private TextureRegion ARROW_NORTH_WEST = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_bend_down_left");

    private TextureRegion ARROW_EAST_NORTH = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_bend_up_left");
    private TextureRegion ARROW_EAST_EAST = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_continue_horiz");
    private TextureRegion ARROW_EAST_SOUTH = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_bend_down_left");

    private TextureRegion ARROW_SOUTH_EAST = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_bend_up_right");
    private TextureRegion ARROW_SOUTH_SOUTH = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_continue_vert");
    private TextureRegion ARROW_SOUTH_WEST = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_bend_up_left");

    private TextureRegion ARROW_WEST_NORTH = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_bend_up_right");
    private TextureRegion ARROW_WEST_SOUTH = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_bend_down_right");
    private TextureRegion ARROW_WEST_WEST = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_continue_horiz");


    private TextureRegion ARROW_HEAD_NORTH = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_head_up");
    private TextureRegion ARROW_HEAD_EAST = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_head_right");
    private TextureRegion ARROW_HEAD_SOUTH = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_head_down");
    private TextureRegion ARROW_HEAD_WEST = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_head_left");

    private TextureRegion ARROW_END_MOVE = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_end_move");
    private TextureRegion ARROW_END_ATTACK = SpriteManager.TACTICS.findRegion("ui/arrow/arrow_end_attack");

    private enum Direction {NORTH, SOUTH, EAST, WEST}

    private Map<Direction, Map<Direction, TextureRegion>> spriteTable = new HashMap<>();

    public PathRenderingSystem(TacticsScreen screen) {
        super(pathingUnitFamily);
        this.screen = screen;

        spriteTable.put(Direction.NORTH, new HashMap<>());
        spriteTable.put(Direction.SOUTH, new HashMap<>());
        spriteTable.put(Direction.EAST, new HashMap<>());
        spriteTable.put(Direction.WEST, new HashMap<>());

        spriteTable.get(Direction.NORTH).put(Direction.NORTH, ARROW_NORTH_NORTH);
        spriteTable.get(Direction.NORTH).put(Direction.EAST, ARROW_NORTH_EAST);
        spriteTable.get(Direction.NORTH).put(Direction.WEST, ARROW_NORTH_WEST);

        spriteTable.get(Direction.EAST).put(Direction.EAST, ARROW_EAST_EAST);
        spriteTable.get(Direction.EAST).put(Direction.NORTH, ARROW_EAST_NORTH);
        spriteTable.get(Direction.EAST).put(Direction.SOUTH, ARROW_EAST_SOUTH);

        spriteTable.get(Direction.SOUTH).put(Direction.SOUTH, ARROW_SOUTH_SOUTH);
        spriteTable.get(Direction.SOUTH).put(Direction.EAST, ARROW_SOUTH_EAST);
        spriteTable.get(Direction.SOUTH).put(Direction.WEST, ARROW_SOUTH_WEST);

        spriteTable.get(Direction.WEST).put(Direction.WEST, ARROW_WEST_WEST);
        spriteTable.get(Direction.WEST).put(Direction.NORTH, ARROW_WEST_NORTH);
        spriteTable.get(Direction.WEST).put(Direction.SOUTH, ARROW_WEST_SOUTH);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        MovementPathComponent path = pathM.get(entity);
        screen.batch.begin();
        for (int i = 1; i < path.positions.size(); i++) {
            MapPositionComponent prev = path.positions.get(i - 1);
            MapPositionComponent curr = path.positions.get(i);
            Direction inDir = Direction.NORTH;
            TextureRegion sprite;

            if (prev.row == curr.row && prev.col == curr.col + 1) {
                inDir = Direction.WEST;
            } else if (prev.row == curr.row && prev.col == curr.col - 1) {
                inDir = Direction.EAST;
            } else if (prev.row == curr.row + 1 && prev.col == curr.col) {
                inDir = Direction.SOUTH;
            } else if (prev.row == curr.row - 1 && prev.col == curr.col) {
                inDir = Direction.NORTH;
            }
            if (i == path.positions.size() - 1) {
                switch (inDir) {
                    case NORTH:
                        screen.batch.draw(ARROW_HEAD_NORTH, screen.getMap().getTileX(curr.row, curr.col), screen.getMap().getTileY(curr.row, curr.col));
                        break;
                    case EAST:
                        screen.batch.draw(ARROW_HEAD_EAST, screen.getMap().getTileX(curr.row, curr.col), screen.getMap().getTileY(curr.row, curr.col));
                        break;
                    case SOUTH:
                        screen.batch.draw(ARROW_HEAD_SOUTH, screen.getMap().getTileX(curr.row, curr.col), screen.getMap().getTileY(curr.row, curr.col));
                        break;
                    case WEST:
                        screen.batch.draw(ARROW_HEAD_WEST, screen.getMap().getTileX(curr.row, curr.col), screen.getMap().getTileY(curr.row, curr.col));
                        break;
                }
                Entity entityAt = screen.getMap().getUnitAt(curr.row, curr.col);
                if (entityAt == null) {
//                    screen.batch.draw(ARROW_END_MOVE, screen.getMap().getTileX(curr.row, curr.col), screen.getMap().getTileY(curr.row, curr.col));
                } else if (enemyUnitFamily.matches(entityAt)) {
                    screen.batch.draw(ARROW_END_ATTACK, screen.getMap().getTileX(curr.row, curr.col), screen.getMap().getTileY(curr.row, curr.col));
                }
            } else {
                MapPositionComponent next = path.positions.get(i + 1);
                Direction outDir = Direction.NORTH;
                if (curr.row == next.row && curr.col == next.col + 1) {
                    outDir = Direction.WEST;
                } else if (curr.row == next.row && curr.col == next.col - 1) {
                    outDir = Direction.EAST;
                } else if (curr.row == next.row + 1 && curr.col == next.col) {
                    outDir = Direction.SOUTH;
                } else if (curr.row == next.row - 1 && curr.col == next.col) {
                    outDir = Direction.NORTH;
                }
                sprite = spriteTable.get(inDir).get(outDir);
                if (sprite == null) {
//                    System.out.println(inDir+ ", " + outDir);
                } else {
                    screen.batch.draw(sprite, screen.getMap().getTileX(curr.row, curr.col), screen.getMap().getTileY(curr.row, curr.col));
                }
            }
        }
        screen.batch.end();
    }
}
