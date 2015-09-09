package com.nicktoony.getaway;

import com.nicktoony.getaway.rooms.RoomGame;
import com.nicktoony.getaway.rooms.RoomMenu;

public class Game extends com.badlogic.gdx.Game {
    private GameConfig config;

    @Override
	public void create () {
        config = new GameConfig();
        setScreen(new RoomMenu(this));
	}

    public GameConfig getConfig() {
        return config;
    }
}
