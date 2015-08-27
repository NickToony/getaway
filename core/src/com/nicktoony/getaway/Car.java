package com.nicktoony.getaway;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by nick on 27/08/15.
 */
public class Car extends Entity {
    private Texture texture;

    @Override
    public void create() {
        texture = new Texture("graphics/car.png");
    }

    @Override
    public void step() {

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(texture, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
    }
}
