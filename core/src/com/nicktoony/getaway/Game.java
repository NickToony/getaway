package com.nicktoony.getaway;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends ApplicationAdapter {
    public static final int STATE_START = 0;
    public static final int STATE_MENU = 1;
    public static final int STATE_GAME = 2;
    public static final int STATE_OVER = 3;

	private SpriteBatch batch;
    private List<Entity> entityList = new ArrayList<Entity>();
    private int state = STATE_START;
    private OrthographicCamera camera;
    private GameConfig config;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        config = new GameConfig();

        Music music = Gdx.audio.newMusic(Gdx.files.internal("music/rocket.mp3"));
        music.play();
        music.setLooping(true);

        addEntity(new Menu());
        addEntity(new Road());

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = (float) config.game_resolution_x / (float) Gdx.graphics.getWidth();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // Step event
        for (Entity entity : entityList) {
            entity.step();
        }

        // render everything
        batch.setProjectionMatrix(camera.combined);
		batch.begin();
        for (Entity entity : entityList) {
            entity.render(batch);
        }
		batch.end();
	}

    public Entity addEntity(Entity entity) {
        entity.setGame(this);
        entityList.add(entity);
        entity.create();
        return entity;
    }
}
