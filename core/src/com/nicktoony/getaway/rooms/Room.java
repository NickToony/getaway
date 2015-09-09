package com.nicktoony.getaway.rooms;

import com.badlogic.gdx.Screen;
import com.nicktoony.getaway.Game;

/**
 * Created by Nick on 09/09/2015.
 */
public abstract class Room implements Screen {
    protected Game game;

    public Room(Game game) {
        this.game = game;
    }
}
