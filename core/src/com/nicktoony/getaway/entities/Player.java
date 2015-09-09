package com.nicktoony.getaway.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nicktoony.getaway.components.Entity;

/**
 * Created by nick on 27/08/15.
 */
public class Player extends Entity {
    private final static int MAX_ROTATE = 25;
    private final static float SPEED_X = 0.04f;
    private final static float SPEED_ROTATION = 0.5f;

    private Texture texture;
    private Sprite sprite;
    private float x = 0;
    private float rotation = 0;

    @Override
    public void create() {
        texture = new Texture("graphics/vehicles/car_black_1.png");
        sprite = new Sprite(texture);
        sprite.setOriginCenter();
    }

    @Override
    public void step() {
        Road.RoadPosition pos = game.getRoad().findScreenPosition(x, 0.4f);
        sprite.setCenter(pos.x, pos.y);
        sprite.setRotation(-rotation);

        float speed = 0;
        if (Gdx.input.isTouched()) {
            if (Gdx.input.getX() > Gdx.graphics.getWidth()/2) {
                speed += SPEED_X;
            } else if (Gdx.input.getX() < Gdx.graphics.getWidth()/2) {
                speed -= SPEED_X;
            }
        }

        boolean reset = false;
        if (x + speed < 1f && x + speed > -1f) {
            x += speed;

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
                game.getCamera().rotate(rotation);
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

    public float getX() {
        return x;
    }
}
