package com.nicktoony.getaway;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import java.util.ArrayList;
import java.util.List;

public class Game extends ApplicationAdapter {
    public static final int STATE_START = 0;
    public static final int STATE_MENU = 1;
    public static final int STATE_GAME = 2;
    public static final int STATE_OVER = 3;

	private SpriteBatch batch;
    private List<Entity> entityList = new ArrayList<Entity>();
    private List<Entity> newEntitiesList = new ArrayList<Entity>();
    private int state = STATE_START;
    private OrthographicCamera camera;
    private GameConfig config;
    private Road road;
    private ScalingViewport scalingViewport;
    private Player player;

    @Override
	public void create () {
		batch = new SpriteBatch();
        config = new GameConfig();

        Music music = Gdx.audio.newMusic(Gdx.files.internal("music/rocket.mp3"));
        music.play();
        music.setLooping(true);

        addEntity(new Menu());
        road = new Road();
        addEntity(road);
        player = (Player) addEntity(new Player());
        addEntity(new PoliceManager());

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        scalingViewport = new ScalingViewport(Scaling.fill, config.game_resolution_x, config.game_resolution_y, camera);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Sort the camera and viewport
        camera.position.set(config.game_resolution_x / 2, config.game_resolution_y / 2, 0);
        scalingViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Add new entities
        if (!newEntitiesList.isEmpty()) {
            entityList.addAll(newEntitiesList);
            newEntitiesList.clear();
        }

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
        newEntitiesList.add(entity);
        entity.create();
        return entity;
    }

    public Road getRoad() {
        return road;
    }

    public GameConfig getConfig() {
        return config;
    }

    public int getWidth() {
        return config.game_resolution_x;
    }

    public int getHeight() {
        return config.game_resolution_y;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Player getPlayer() {
        return player;
    }
}
