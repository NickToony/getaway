package com.nicktoony.getaway;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nick on 23/03/15.
 */
public class Road extends Entity {

    // Constants
    private static final int TILE_SIZE = 128;
    private static final int ROAD_WIDTH = 3;
    private static final int ROAD_SPEED = 5;
    private static final int ROAD_OFFSET = 256;

    // Rendering
    private SpriteSheetReader spriteSheet;
    private String roadMiddle;

    // Logic
    private List<RoadCombo> roadCombos = new ArrayList<RoadCombo>();
    private List<UpcomingRoad> upcomingRoads = new ArrayList<UpcomingRoad>();
    private List<UpcomingRoad> deleteRoads = new ArrayList<UpcomingRoad>();

    class UpcomingRoad {
        public int x;
        public int y;
        public RoadCombo roadCombo;

        public UpcomingRoad(int x, int y, RoadCombo roadCombo) {
            this.x = x;
            this.y = y;
            this.roadCombo = roadCombo;
        }
    }

    class RoadCombo {
        public String left;
        public String right;
        public float offset = 0;
        public int angle = 0;

        public RoadCombo(String left, String right) {
            this.left = left;
            this.right = right;
        }

        public RoadCombo offset(float offset) {
            this.offset = offset;
            return this;
        }

        public RoadCombo angle(int angle) {
            this.angle = angle;
            return this;
        }
    }

    class RoadPosition {
        public int x;
        public int y;
        public int angle;

        public RoadPosition(int x, int y, int angle) {
            this.x = x;
            this.y = y;
            this.angle = angle;
        }
    }

    @Override
    public void create() {
        // Read the sprite sheet
        spriteSheet = new SpriteSheetReader(Gdx.files.internal("graphics/spritesheet_tiles.xml"), Gdx.files.internal("graphics/spritesheet_tiles.png"));

        // Add road parts
        roadCombos.add(new RoadCombo("road_asphalt21.png", "road_asphalt23.png").offset(0));

        // Define the road middle
        roadMiddle = "road_asphalt22.png";
    }

    @Override
    public void step() {

        // Road each upcoming road
        for (UpcomingRoad upcomingRoad : this.upcomingRoads) {
            // Move the segment down
            upcomingRoad.y -= ROAD_SPEED;
            // If too far off the screen
            if (upcomingRoad.y < -ROAD_OFFSET) {
                // destroy it
                deleteRoads.add(upcomingRoad);
            }
        }

        // For each road to be removed
        for (UpcomingRoad toDelete : deleteRoads) {
            // remove it from the upcoming array
            upcomingRoads.remove(toDelete);
        }
        // clear roads to be deleted - they already have been
        deleteRoads.clear();

        // While there is empty space on the screen for road
        while ((upcomingRoads.size()-1) * TILE_SIZE <= game.getHeight() + ROAD_OFFSET) {
            // Find the last road segment
            UpcomingRoad last = upcomingRoads.isEmpty() ? null : upcomingRoads.get(upcomingRoads.size()-1);
            int y = -ROAD_OFFSET;
            // If a last road segment was found, jump to just above it
            if (last != null) {
                y = last.y + TILE_SIZE;
            }

            // Add a new segment
            upcomingRoads.add(new UpcomingRoad(game.getWidth()/2, y, roadCombos.get(0)));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        for (UpcomingRoad upcomingRoad : this.upcomingRoads) {
            float offset = getRoadWidth()/2;

            // Left
            batch.draw(spriteSheet.getTexture(upcomingRoad.roadCombo.left), upcomingRoad.x - offset, upcomingRoad.y);
            offset -= 128;

            // Middle
            for (int i = 0; i < ROAD_WIDTH; i ++) {
                batch.draw(spriteSheet.getTexture(roadMiddle), upcomingRoad.x - offset, upcomingRoad.y);
                offset -= 128;
            }

            // Right
            batch.draw(spriteSheet.getTexture(upcomingRoad.roadCombo.right), upcomingRoad.x - offset, upcomingRoad.y);
            offset -= 128;
        }
    }

    /**
     * Find the position on the screen, given the road position
     * @param x the x position of the road (from -1 to 1)
     * @param y the y position of the road (from 0 to 1)
     * @return
     */
    public RoadPosition findScreenPosition(float x, float y) {
        RoadPosition vector2 = new RoadPosition(0, 0, 0);
        for (UpcomingRoad road : upcomingRoads) {
            if (road.y <= game.getHeight()*y) {
                vector2.x = (int) (road.x + (getRoadWidth()/2)*x);
                vector2.y = (int) (game.getHeight()*y);
                vector2.angle = road.roadCombo.angle;
            } else {
                break;
            }
        }
        return vector2;
    }

    /**
     * Calculate the road width
     * @return
     */
    public float getRoadWidth() {
        return (TILE_SIZE) * (ROAD_WIDTH + 2);
    }
}
