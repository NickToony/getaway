package com.nicktoony.getaway;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by nick on 27/08/15.
 */
public class Car extends Entity {
    private Texture texture;
    private Sprite sprite;

    @Override
    public void create() {
        texture = new Texture("graphics/car.png");
        sprite = new Sprite(texture);
        sprite.setOriginCenter();
    }

    @Override
    public void step() {
        Vector2 pos = game.getRoad().getVectorAt(new Vector2(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2));
        sprite.setCenter(pos.x, Gdx.graphics.getHeight()/2);
        sprite.setRotation((pos.angle()));
    }

    @Override
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
