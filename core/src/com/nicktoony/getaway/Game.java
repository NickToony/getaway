package com.nicktoony.getaway;

import com.nicktoony.getaway.rooms.RoomGame;

public class Game extends com.badlogic.gdx.Game {
    private GameConfig config;

    @Override
	public void create () {
        config = new GameConfig();
        setScreen(new RoomGame(this));
	}

    public GameConfig getConfig() {
        return config;
    }
}
