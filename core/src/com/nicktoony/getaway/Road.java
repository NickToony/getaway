package com.nicktoony.getaway;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nick on 23/03/15.
 */
public class Road extends Entity {

    PolygonSpriteBatch polyBatch = new PolygonSpriteBatch(); // To assign at the beginning
    Texture textureSolid;
    ShapeRenderer shaper = new ShapeRenderer();
    CatmullRomSpline<Vector2> myCatmull;
    ArrayList<Short> pointsToRender = new ArrayList<Short>();
    ArrayList<Float> vectorsToRender = new ArrayList<Float>();
    short number = 0;


    @Override
    public void create() {
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.DARK_GRAY); // DE is red, AD is green and BE is blue.
        pix.fill();
        textureSolid = new Texture(pix);
        polyBatch = new PolygonSpriteBatch();


        List<Vector2> data = new ArrayList<Vector2>();

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();
        data.add(new Vector2(width/2, -200));
        data.add(new Vector2(width/2 + 20, height/2));
        data.add(new Vector2(width/2, height + 200));
//        data.add(new Vector2(800, 900));

        Vector2[] dataSet = new Vector2[data.size()];
        data.toArray(dataSet);

        myCatmull = new CatmullRomSpline<Vector2>(dataSet, true);
        myCatmull.spanCount = dataSet.length-1;

    }

    @Override
    public void step() {

    }

    @Override
    public void render(SpriteBatch batch) {
        vectorsToRender.clear();
        pointsToRender.clear();

        // Extract points of curve from spline
        int k = 100; // This sets the smoothness of the curve
        Vector2[] points = new Vector2[k];
        for (int i = 0; i < k - 1; ++i) {
            points[i] = new Vector2();
            myCatmull.valueAt(points[i], ((float) i) / ((float) k - 1));
        }

        // Draw straight lines between each curve point to the pixmap.
        number = 0;
        for (int i = 0; i < k - 2; ++i) {
            Vector2 from = new Vector2(points[i].x, points[i].y);
            Vector2 to = new Vector2(points[i + 1].x, points[i + 1].y);

            renderRoadPiece(from, to, false);
        }

        float[] floatArray = new float[vectorsToRender.size()];
        for (int i = 0; i < vectorsToRender.size(); i++) {
            floatArray[i] = vectorsToRender.get(i);
        }
        short[] shortArray = new short[pointsToRender.size()];
        for (int i = 0; i < pointsToRender.size(); i++) {
            shortArray[i] = pointsToRender.get(i);
        }

        PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid),
                floatArray, shortArray);
        PolygonSprite poly = new PolygonSprite(polyReg);
        poly.setOrigin(0, 0);

        polyBatch.begin();
        poly.draw(polyBatch);
        polyBatch.end();
    }

    private void renderRoadPiece(Vector2 from, Vector2 to, boolean withLines) {

        Vector2 v2 = new Vector2(from).sub(to);

        float angle = (float) Math.toDegrees(Math.atan2(v2.y, v2.x)) + 90;
        Vector2 temp;
        float[] vectors = new float[8];
        int roadWidth = 100;
        Vector2 left = new Vector2(-roadWidth, 0);
        left.rotate(angle);
        Vector2 right = new Vector2(roadWidth, 0);
        right.rotate(angle);

        // First
        temp = new Vector2(from);
        //temp.rotate(angle);
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


//        PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid),
//                vectors,
//        });
//        PolygonSprite poly = new PolygonSprite(polyReg);
//        poly.setOrigin(0, 0);

//        polyBatch.begin();
//        poly.draw(polyBatch);
//        polyBatch.end();

        for (float vector : vectors) {
            vectorsToRender.add(vector);
        }
        short[] points = new short[]{
                number, (short) (number + 1), (short) (number + 2),         // Two triangles using vertex indices.
                number, (short) (number + 2), (short) (number + 3)
        };
        if (number > 0) {
            points = new short[]{
                    number, (short) (number + 1), (short) (number + 2),         // Two triangles using vertex indices.
                    number, (short) (number + 2), (short) (number + 3),
                    number, (short) (number + 1), (short) (number -2),
                    number, (short) (number + 1), (short) (number -1)
            };
        }
        number += 4;
        for (short point : points) {
            pointsToRender.add(point);
        }

        if (withLines) {
            shaper.setAutoShapeType(true);
            shaper.begin();
            shaper.setColor(Color.BLACK);
            shaper.line(vectors[0], vectors[1], vectors[2], vectors[3]);
//            shaper.setColor(Color.RED);
            shaper.line(vectors[2], vectors[3], vectors[4], vectors[5]);
//            shaper.setColor(Color.GREEN);
            shaper.line(vectors[4], vectors[5] - 1, vectors[6], vectors[7] - 1);
//            shaper.setColor(Color.PURPLE);
            shaper.line(vectors[0], vectors[1], vectors[6], vectors[7]);
            shaper.end();
        }
    }
}
