package com.nicktoony.getaway.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.nicktoony.getaway.components.Entity;

/**
 * Created by nick on 27/08/15.
 */
public class Player extends Car {
    private final static int MAX_ROTATE = 15;
    private final static float SPEED_X = 0.04f;
    private final static float SPEED_ROTATION = 0.5f;
    private final static int INPUT_OFFSET = 150;

    private Texture texture;
    private float xPercentage = 0;
    private float rotation = 0;

    @Override
    public void create() {
        texture = new Texture("graphics/vehicles/car_black_1.png");
        sprite = new Sprite(texture);
        sprite.setOriginCenter();

        // Setup body
        setupBody();
    }

    @Override
    public void step() {
        Road.RoadPosition pos = game.getRoad().findScreenPosition(xPercentage, 0.4f);
        sprite.setCenter(pos.x, pos.y);
        sprite.setRotation(-rotation);

        // update Box2d angle
        body.setTransform(pos.x, pos.y, (float) Math.toRadians(-rotation));
        // wake the body!
        body.setLinearVelocity(0.1f, 0.1f);

        float speed = 0;
        if (Gdx.input.isTouched()) {
            if (Gdx.input.getX() > Gdx.graphics.getWidth()/2 + INPUT_OFFSET) {
                speed += SPEED_X;
            } else if (Gdx.input.getX() < Gdx.graphics.getWidth()/2 - INPUT_OFFSET) {
                speed -= SPEED_X;
            }
        }

        boolean reset = false;
        if (xPercentage + speed < 1f && xPercentage + speed > -1f) {
            xPercentage += speed;

            if (speed > 0 && rotation < MAX_ROTATE) {
                rotation += SPEED_ROTATION;
                game.getCamera().rotate(SPEED_ROTATION);
            } else if (speed < 0 && rotation > -MAX_ROTATE) {
                rotation -= SPEED_ROTATION;
                game.getCamera().rotate(-SPEED_ROTATION);
            } else if (speed == 0) {
                reset = true;
            }
        } else {
            reset = true;
        }

        if (reset) {
            if (Math.abs(rotation) < SPEED_ROTATION * 2) {
                game.getCamera().rotate(-rotation);
                rotation = 0;
            } else if (rotation < 0) {
                rotation += SPEED_ROTATION*2;
                game.getCamera().rotate(SPEED_ROTATION*2);
            } else if (rotation > 0) {
                rotation -= SPEED_ROTATION*2;
                game.getCamera().rotate(-SPEED_ROTATION*2);
            }
        }


    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public float getXPercentage() {
        return xPercentage;
    }

    @Override
    public void dispose() {

    }
}
