package com.nicktoony.getaway;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nick on 23/03/15.
 */
public class Road extends Entity {

    private PolygonSpriteBatch polyBatch;
    private Texture textureSolid;
    private CatmullRomSpline<Vector2> catmull;
    private Random random;
    private ArrayList<UpcomingPoint> points;

    private final int ROAD_WIDTH = 300;

    class UpcomingPoint extends Vector2 {
        public int toX;

        public UpcomingPoint(float x, float y, int toX) {
            super(x, y);
            this.toX = toX;
        }


        UpcomingPoint() {
        }
    }

    @Override
    public void create() {
        // Generate a temporary texture
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.DARK_GRAY); // DE is red, AD is green and BE is blue.
        pix.fill();
        textureSolid = new Texture(pix);
        polyBatch = new PolygonSpriteBatch();

        // random
        random = new Random();
        points = new ArrayList<UpcomingPoint>();

        // define the initial road
        defineRoad();
    }

    @Override
    public void step() {

        if (!points.isEmpty()) {
            ArrayList<UpcomingPoint> toRemove = new ArrayList<UpcomingPoint>();
            for (UpcomingPoint point : points) {
                point.y -= 10;
                if (point.y < 100) {
                    if (point.x > Gdx.graphics.getWidth()/2) {
                        point.x -= 1;
                    } else if (point.x < Gdx.graphics.getWidth()/2) {
                        point.x += 1;
                    } else {
                        toRemove.add(point);
                    }
                } else {
                    if (point.x > point.toX) {
                        point.x -= 1;
                    } else if (point.x < point.toX) {
                        point.x += 1;
                    }
                }
            }

            for (UpcomingPoint point : toRemove) {
                points.remove(point);
            }
        } else {
            if (random.nextBoolean()) {
                int offset = random.nextInt(80) - 40;
                points.add(new UpcomingPoint(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() + 100, Gdx.graphics.getWidth() / 2 + offset));
            } else {
                int offset = random.nextInt(80) - 40;
                points.add(new UpcomingPoint(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() + 100, Gdx.graphics.getWidth() / 2 + offset));
                points.add(new UpcomingPoint(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() + 600, Gdx.graphics.getWidth() / 2 - offset));
            }
        }

        defineRoad();
    }

    private void defineRoad() {
        // Define an array of road points to render
        List<Vector2> data = new ArrayList<Vector2>();

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        data.add(new Vector2(width/2, -200));
        data.addAll(points);
//        data.add(new Vector2(width/2 - offset, height/4*3));
        data.add(new Vector2(width/2, height + 200));

        // Convert the arraylist to an array
        Vector2[] dataSet = new Vector2[data.size()];
        data.toArray(dataSet);

        // Create the catmull, setting its spancount manually
        catmull = new CatmullRomSpline<Vector2>(dataSet, true);
        catmull.spanCount = dataSet.length-1;
    }

    @Override
    public void render(SpriteBatch batch) {
        // The points array defines the circles to create between vectors
        ArrayList<Short> pointsToRender = new ArrayList<Short>();
        // The vectors represent positions on the maps
        ArrayList<Float> vectorsToRender = new ArrayList<Float>();
        // The pointPosition represents the position in the points array
        short pointPosition = 0;

        // Extract points of curve from spline
        int k = 100; // This sets the smoothness of the curve
        Vector2[] points = new Vector2[k];
        for (int i = 0; i < k - 1; ++i) {
            // Each point needs a vector2
            points[i] = new Vector2();
            // Create the values from the catmull, and assign to the point's vector
            catmull.valueAt(points[i], ((float) i) / ((float) k - 1));
        }

        // Render the road piece for each section
        for (int i = 0; i < k - 2; ++i) {
            Vector2 from = new Vector2(points[i].x, points[i].y);
            Vector2 to = new Vector2(points[i + 1].x, points[i + 1].y);

            // Render the actual road piece
            pointPosition += renderRoadPiece(from, to, (short) pointPosition, pointsToRender, vectorsToRender);
        }

        // Convert the float arraylist to a float array
        float[] floatArray = new float[vectorsToRender.size()];
        for (int i = 0; i < vectorsToRender.size(); i++) {
            floatArray[i] = vectorsToRender.get(i);
        }
        // Conver the short arraylist to a short array
        short[] shortArray = new short[pointsToRender.size()];
        for (int i = 0; i < pointsToRender.size(); i++) {
            shortArray[i] = pointsToRender.get(i);
        }

        // Generate the polygon
        PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid),
                floatArray, shortArray);
        PolygonSprite poly = new PolygonSprite(polyReg);
        poly.setOrigin(0, 0);

        // Finally, render
        batch.end();
        polyBatch.setProjectionMatrix(batch.getProjectionMatrix());
        polyBatch.begin();
        poly.draw(polyBatch);
        polyBatch.end();
        batch.begin();
    }

    private short renderRoadPiece(Vector2 from, Vector2 to, short pointPosition, ArrayList<Short> pointsToRender, ArrayList<Float> vectorsToRender) {

        // Get the mid point between the from and to
        Vector2 v2 = new Vector2(from).sub(to);
        // Calculate the angle between the two points
        float angle = (float) Math.toDegrees(Math.atan2(v2.y, v2.x)) + 90;

        // Setup variables
        Vector2 temp;
        float[] vectors = new float[8];
        // Create a vector from the origin to the left side of the road
        Vector2 left = new Vector2(-ROAD_WIDTH, 0);
        left.rotate(angle); // angle it to road angle
        // And again for right, calculating from origin to right side of road
        Vector2 right = new Vector2(ROAD_WIDTH, 0);
        right.rotate(angle); // angle to match

        // First
        temp = new Vector2(from);
        temp.add(left);
        vectors[0] = temp.x;
        vectors[1] = temp.y;

        // Second
        temp = new Vector2(from);
        //temp.rotate(angle);
        temp.add(right);
        vectors[2] = temp.x;
        vectors[3] = temp.y;

        // Third
        temp = new Vector2(to);
        temp.add(right);
        //temp.rotate(angle);
        vectors[4] = temp.x;
        vectors[5] = temp.y;

        // Forth
        temp = new Vector2(to);
        temp.add(left);
        //temp.rotate(angle);
        vectors[6] = temp.x;
        vectors[7] = temp.y;

        // Add all the vector coords to the render array
        for (float vector : vectors) {
            vectorsToRender.add(vector);
        }
        // Now connect them with triangles
        short[] points;
        // If there was a previous section of road
        if (pointPosition > 0) {
            // render all circles between, AND connect to the previous sections
            points = new short[]{
                    pointPosition, (short) (pointPosition + 1), (short) (pointPosition + 2),         // Two triangles using vertex indices.
                    pointPosition, (short) (pointPosition + 2), (short) (pointPosition + 3),
                    pointPosition, (short) (pointPosition + 1), (short) (pointPosition -2), // prev section
                    pointPosition, (short) (pointPosition + 1), (short) (pointPosition -1) // prev section
            };
        } else {
            points = new short[]{
                    pointPosition, (short) (pointPosition + 1), (short) (pointPosition + 2),         // Two triangles using vertex indices.
                    pointPosition, (short) (pointPosition + 2), (short) (pointPosition + 3)
            };
        }
        // Add all the points to the render array
        for (short point : points) {
            pointsToRender.add(point);
        }

        // Return how many vectors we added
        return (short) ((short) vectors.length/2);
    }
}
