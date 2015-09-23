package com.nicktoony.getaway.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.nicktoony.getaway.components.Entity;
import com.nicktoony.getaway.services.SpriteSheetReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nick on 23/03/15.
 */
public class Road extends Entity {

    // Constants
    private static final int TILE_SIZE = 128;
    private static final int ROAD_WIDTH = 3;
    private static final int ROAD_SPEED = 8;
    private static final int ROAD_OFFSET_HEIGHT = 512;
    private static final int ROAD_OFFSET_WIDTH = 256;
    private static final int ROAD_OFFSET_SIDES = 72;

    private static final int GENERATOR_MIN_CHANGE = 10;
    private static final int GENERATOR_MAX_CHANGE = 25;
    private static final int GENERATOR_MIN_SHARPNESS = 10;
    private static final int GENERATOR_MAX_SHARPNESS = 15;
    private static final int GENERATOR_RESET_MULTIPLIER = 6;
    private static final float GENERATOR_MIN_RATE = .2f;
    private static final float GENERATOR_MAX_RATE = .3f;

    // Rendering
    private SpriteSheetReader spriteSheet;
    private String middleDefault;
    private String sideDefault;

    // Logic
    private List<RoadCombo> roadCombos = new ArrayList<RoadCombo>();
    private List<UpcomingRoad> upcomingRoads = new ArrayList<UpcomingRoad>();
    private List<UpcomingRoad> deleteRoads = new ArrayList<UpcomingRoad>();
    private Random random;

    // Rotation
    private float currentAngle = 0;
    private float targetAngle = 10;
    private int lastChange = 0;
    private float rate = .05f;

    class UpcomingRoad {
        public int x;
        public int y;
        public RoadCombo roadCombo;
        public List<TextureRegion> textureRegions;

        public UpcomingRoad(int x, int y, RoadCombo roadCombo) {
            this.x = x;
            this.y = y;
            this.roadCombo = roadCombo;
            this.textureRegions = new ArrayList<TextureRegion>();

            // Add road left
            textureRegions.add(spriteSheet.getTexture(roadCombo.left));
            // Add road middle
            for (int i = 0; i < ROAD_WIDTH; i ++) {
                textureRegions.add(spriteSheet.getTexture(roadCombo.getRandomMiddle()));
            }
            // Add road right
            textureRegions.add(spriteSheet.getTexture(roadCombo.right));

            // Add side grass
            while (TILE_SIZE * textureRegions.size() < game.getWidth() + ROAD_OFFSET_WIDTH) {
                textureRegions.add(0, spriteSheet.getTexture(roadCombo.getRandomSide()));
                textureRegions.add(spriteSheet.getTexture(roadCombo.getRandomSide()));
            }
        }
    }

    class RoadCombo {
        public String left;
        public String right;
        public float offset = 0;
        public int angle = 0;
        private List<String> side = new ArrayList<String>();
        private List<String> middle = new ArrayList<String>();

        public RoadCombo(String left, String right) {
            this.left = left;
            this.right = right;
        }

        public RoadCombo setOffset(float offset) {
            this.offset = offset;
            return this;
        }

        public RoadCombo setAngle(int angle) {
            this.angle = angle;
            return this;
        }

        public RoadCombo addSide(String side) {
            this.side.add(side);
            return this;
        }

        public RoadCombo addMiddle(String side) {
            this.middle.add(side);
            return this;
        }

        public String getRandomSide() {
            return side.isEmpty() ? sideDefault : side.get(random.nextInt(side.size()));
        }

        public String getRandomMiddle() {
            return middle.isEmpty() ? middleDefault : middle.get(random.nextInt(middle.size()));
        }
    }

    class RoadPosition {
        public int x;
        public int y;
        public float angle;

        public RoadPosition(int x, int y, float angle) {
            this.x = x;
            this.y = y;
            this.angle = angle;
        }
    }

    @Override
    public void create() {
        // Read the sprite sheet
        spriteSheet = new SpriteSheetReader(Gdx.files.internal("graphics/spritesheet_tiles.xml"), Gdx.files.internal("graphics/spritesheet_tiles.png"));

        // Add a simple road
        roadCombos.add(new RoadCombo("road_asphalt21.png", "road_asphalt23.png")
                        .setOffset(0)
                        .setAngle(0)
                        .addSide("land_grass04.png")
                        .addSide("land_grass11.png")
                        .addMiddle("road_asphalt22.png")
        );

        // Define the road middle
        middleDefault = "road_asphalt22.png";
        sideDefault = "land_grass04.png";

        // Random
        random = new Random();
    }

    @Override
    public void step() {

        // Road each upcoming road
        for (UpcomingRoad upcomingRoad : this.upcomingRoads) {
            // Move the segment down
            upcomingRoad.y -= ROAD_SPEED;
            // If too far off the screen
            if (upcomingRoad.y < -ROAD_OFFSET_HEIGHT) {
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
        while ((upcomingRoads.size() - 1) * TILE_SIZE <= game.getHeight() + ROAD_OFFSET_HEIGHT * 2) {
            // Find the last road segment
            UpcomingRoad last = upcomingRoads.isEmpty() ? null : upcomingRoads.get(upcomingRoads.size() - 1);
            int y = -ROAD_OFFSET_HEIGHT;
            // If a last road segment was found, jump to just above it
            if (last != null) {
                y = last.y + TILE_SIZE;
            }

            // Add a new segment
            upcomingRoads.add(new UpcomingRoad(game.getWidth() / 2, y, roadCombos.get(random.nextInt(roadCombos.size()))));
        }

        if (Math.abs(targetAngle - currentAngle) < rate) {
            targetAngle = -targetAngle;
        } else if (targetAngle > currentAngle) {
            currentAngle += rate;
            game.getCamera().rotate(rate);
        } else if (targetAngle < currentAngle) {
            currentAngle -= rate;
            game.getCamera().rotate(-rate);
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        for (UpcomingRoad upcomingRoad : this.upcomingRoads) {
            float offset = (TILE_SIZE/2) * (upcomingRoad.textureRegions.size());

            // Render the sections
            for (TextureRegion textureRegion : upcomingRoad.textureRegions) {
                batch.draw(textureRegion, upcomingRoad.x - offset, upcomingRoad.y);
                offset -= 128;
            }
        }
    }

    @Override
    public void dispose() {
        
    }

    /**
     * Find the position on the screen, given the road position
     * @param x the x position of the road (from -1 to 1)
     * @param y the y position of the road (from 0 to 1)
     * @return
     */
    public RoadPosition findScreenPosition(float x, float y) {
        RoadPosition vector2 = new RoadPosition(0, -999, 0);
        for (UpcomingRoad road : upcomingRoads) {
            if (road.y <= game.getHeight()*y) {
                vector2.x = (int) (road.x + (getRoadWidth()/2 - ROAD_OFFSET_SIDES)*x);
                vector2.y = (int) (game.getHeight()*y);
                vector2.angle = currentAngle;
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
