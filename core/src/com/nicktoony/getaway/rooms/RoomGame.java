package com.nicktoony.getaway.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.nicktoony.getaway.Game;
import com.nicktoony.getaway.components.Entity;
import com.nicktoony.getaway.entities.Player;
import com.nicktoony.getaway.entities.PoliceManager;
import com.nicktoony.getaway.entities.Road;
import com.nicktoony.getaway.services.BetterResourceManager;
import com.uwsoft.editor.renderer.SceneLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 09/09/2015.
 */
public class RoomGame extends Room {

    private SpriteBatch batch;
    private List<Entity> entityList = new ArrayList<Entity>();
    private List<Entity> newEntitiesList = new ArrayList<Entity>();
    private OrthographicCamera camera;
    private OrthographicCamera camera2;
    private Road road;
    private ScalingViewport gameViewport;
    private ScalingViewport uiViewport;
    private Player player;
    private SceneLoader sceneLoader;

    public RoomGame(Game game) {
        super(game);
        batch = new SpriteBatch();

        if (game.getConfig().sound_music) {
            Music music = Gdx.audio.newMusic(Gdx.files.internal("music/rocket.mp3"));
            music.play();
            music.setLooping(true);
        }

        road = new Road();
        addEntity(road);
        player = (Player) addEntity(new Player());
        addEntity(new PoliceManager());

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        gameViewport = new ScalingViewport(Scaling.fill, game.getConfig().game_resolution_x, game.getConfig().game_resolution_y, camera);

        // Setup the UI viewport
        uiViewport = new ScalingViewport(Scaling.stretch, game.getConfig().game_resolution_x, game.getConfig().game_resolution_y);
        // Load the UI
        BetterResourceManager betterResourcesManager = new BetterResourceManager("overlay2d/");
        sceneLoader = new SceneLoader(betterResourcesManager);
        sceneLoader.loadScene("Game", uiViewport);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Sort the camera and viewport
        gameViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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

        // Render UI (need to update to UIViewport first)
        uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sceneLoader.getEngine().update(delta);
    }

    @Override
    public void resize(int width, int height) {
        camera.position.set(game.getConfig().game_resolution_x / 2, game.getConfig().game_resolution_y / 2, 0);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public Entity addEntity(Entity entity) {
        entity.setGame(this);
        newEntitiesList.add(entity);
        entity.create();
        return entity;
    }

    public int getWidth() {
        return game.getConfig().game_resolution_x;
    }

    public int getHeight() {
        return game.getConfig().game_resolution_y;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Road getRoad() {
        return road;
    }

    public Player getPlayer() {
        return player;
    }
}
