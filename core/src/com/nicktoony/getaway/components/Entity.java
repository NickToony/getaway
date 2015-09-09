package com.nicktoony.getaway.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nicktoony.getaway.Game;
import com.nicktoony.getaway.rooms.RoomGame;

/**
 * Created by nick on 17/03/15.
 */
public abstract class Entity {
    protected RoomGame game;

    public Entity setGame(RoomGame game) {
        this.game = game;
        return this;
    }

    public abstract void create();
    public abstract void step();
    public abstract void render(SpriteBatch batch);
}
