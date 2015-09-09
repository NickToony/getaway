package com.nicktoony.getaway.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nicktoony.getaway.components.Entity;

import java.util.Random;

/**
 * Created by Nick on 03/09/2015.
 */
public class Police extends Entity {
    private Texture texture;
    private Sprite sprite;
    private float x;
    private float y;
    private float targetY;
    private int targetDelay = 0;
    private Random random;
    private float speed = SPEED;

    private static final float TARGET_Y_MIN = 0.1f;
    private static final float TARGET_Y_MAX = 0.35f;
    private static final int TARGET_DELAY_MIN = 20;
    private static final int TARGET_DELAY_MAX = 40;
    private static final float SPEED = 0.01f;

    public Police(float x) {
        this.x = x;
        this.y = -1;
        this.random = new Random();
    }

    @Override
    public void create() {
        texture = new Texture("graphics/vehicles/car_blue_1.png");
        sprite = new Sprite(texture);
        sprite.setOriginCenter();
    }

    @Override
    public void step() {
        Road.RoadPosition pos = game.getRoad().findScreenPosition(x, y);
        sprite.setCenter(pos.x, pos.y);
        sprite.setRotation(-pos.angle/2);

        if (targetDelay == 0) {
            targetY = TARGET_Y_MIN + (random.nextFloat() * (TARGET_Y_MAX - TARGET_Y_MIN));
            targetDelay = TARGET_DELAY_MIN + random.nextInt(TARGET_DELAY_MAX - TARGET_DELAY_MIN);
        }

        if (Math.abs(y - targetY) < SPEED) {
            targetDelay -= 1;
            speed = SPEED / 5;
        } else if (y < targetY) {
            y += speed;
        } else if (y > targetY) {
            y -= speed;
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
