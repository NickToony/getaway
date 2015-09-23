package com.nicktoony.getaway.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.nicktoony.getaway.Game;
import com.nicktoony.getaway.components.Entity;
import com.nicktoony.getaway.entities.*;
import com.nicktoony.getaway.services.BetterResourceManager;
import com.uwsoft.editor.renderer.SceneLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 09/09/2015.
 */
public class RoomGame extends Room implements ContactListener, ContactFilter {

    private SpriteBatch batch;
    private List<Entity> entityList = new ArrayList<Entity>();
    private List<Entity> newEntitiesList = new ArrayList<Entity>();
    private OrthographicCamera camera;
    private Road road;
    private ScalingViewport gameViewport;
    private ScalingViewport uiViewport;
    private Player player;
    private SceneLoader sceneLoader;
    private World world;
    private PoliceManager policeManager;
    private Box2DDebugRenderer debugRenderer;
    private List<Body> bodiesToDestroy = new ArrayList<Body>();

    public RoomGame(Game game) {
        super(game);
        batch = new SpriteBatch();

        // If music is enabled
        if (game.getConfig().sound_music) {
            Music music = Gdx.audio.newMusic(Gdx.files.internal("music/rocket.mp3"));
            music.play();
            music.setLooping(true);
        }

        // Create the physics world
        world = new World(new Vector2(0, 0), true);
        world.setContactFilter(this);
        world.setContactListener(this);

        // Create the game entities
        road = new Road();
        addEntity(road);
        player = (Player) addEntity(new Player());
        policeManager = new PoliceManager();
        addEntity(policeManager);

        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        gameViewport = new ScalingViewport(Scaling.stretch, game.getConfig().game_resolution_x, game.getConfig().game_resolution_y, camera);

        // Setup the UI viewport
        uiViewport = new ScalingViewport(Scaling.stretch, game.getConfig().game_resolution_x, game.getConfig().game_resolution_y);
        // Load the UI
        BetterResourceManager betterResourcesManager = new BetterResourceManager("overlay2d/");
        sceneLoader = new SceneLoader(betterResourcesManager);
        sceneLoader.loadScene("Game", uiViewport);

        // debug
        debugRenderer = new Box2DDebugRenderer();
        debugRenderer.setDrawBodies(true);
        debugRenderer.setDrawInactiveBodies(true);
        debugRenderer.setDrawJoints(true);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Clear the screen
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
        // render box2d debug
//        debugRenderer.render(world, camera.combined);
        batch.end();

        // Render UI (need to update to UIViewport first)
        uiViewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sceneLoader.getEngine().update(delta);

        // update box2d
        world.step(1, 6, 2);

        for (Body body : bodiesToDestroy) {
            world.destroyBody(body);
            body.setUserData(null);
        }
        bodiesToDestroy.clear();
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
        for (Entity entity : entityList) {
            entity.dispose();
        }
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

    public World getWorld() {
        return world;
    }

    @Override
    public void beginContact(Contact contact) {
        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if (bodyA.getUserData() instanceof Player) {
            handlePlayerCollision((Player) bodyA.getUserData(), bodyB);
        } else if (bodyB.getUserData() instanceof Player) {
            handlePlayerCollision((Player) bodyB.getUserData(), bodyA);
        }
    }

    private void handlePlayerCollision(Player player, Body other) {
        if (other.getUserData() instanceof Police) {
            Police police = (Police) other.getUserData();
            destroyEntity(police);
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        if (fixtureA.getBody().getUserData() instanceof Player || fixtureB.getBody().getUserData() instanceof Player) {
            return true;
        }
        return true;
    }

    private void destroyEntity(Entity entity) {
        entity.dispose();
        entityList.remove(entity);
    }

    public PoliceManager getPoliceManager() {
        return policeManager;
    }

    public void destroyBody(Body body) {
        bodiesToDestroy.add(body);
    }
}
