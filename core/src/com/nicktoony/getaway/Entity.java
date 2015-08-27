package com.nicktoony.getaway;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by nick on 17/03/15.
 */
public abstract class Entity {
    protected Game game;

    public Entity setGame(Game game) {
        this.game = game;
        return this;
    }

    public abstract void create();
    public abstract void step();
    public abstract void render(SpriteBatch batch);
}
