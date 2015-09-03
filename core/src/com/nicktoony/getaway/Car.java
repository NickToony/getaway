package com.nicktoony.getaway;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by nick on 27/08/15.
 */
public class Car extends Entity {
    private Texture texture;
    private Sprite sprite;

    @Override
    public void create() {
        texture = new Texture("graphics/vehicles/car_black_1.png");
        sprite = new Sprite(texture);
        sprite.setOriginCenter();
    }

    @Override
    public void step() {
        Road.RoadPosition pos = game.getRoad().findScreenPosition(0, 0.25f);
        sprite.setCenter(pos.x, pos.y);
        sprite.setRotation(pos.angle);
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
