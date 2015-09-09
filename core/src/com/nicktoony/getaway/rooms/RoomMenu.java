package com.nicktoony.getaway.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nicktoony.getaway.Game;
import com.nicktoony.getaway.services.BetterResourceManager;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

/**
 * Created by Nick on 09/09/2015.
 */
public class RoomMenu extends Room {

    private Viewport viewport;
    private SceneLoader sceneLoader;

    public RoomMenu(Game game) {
        super(game);

        viewport = new ScalingViewport(Scaling.fill, game.getConfig().game_resolution_x, game.getConfig().game_resolution_y);
        BetterResourceManager betterResourcesManager = new BetterResourceManager("overlay2d/");
        sceneLoader = new SceneLoader(betterResourcesManager);
        sceneLoader.loadScene("MainScene", viewport);

        sceneLoader.addComponentsByTagName("button", ButtonComponent.class);

        ItemWrapper root = new ItemWrapper(sceneLoader.rootEntity);
        ButtonComponent buttonPlay = root.getChild("buttonPlay").getEntity().getComponent(ButtonComponent.class);
        buttonPlay.addListener(new ButtonComponent.ButtonListener() {
            @Override
            public void touchUp() {

            }

            @Override
            public void touchDown() {

            }

            @Override
            public void clicked() {
                playButton();
            }
        });

        ButtonComponent buttonQuit = root.getChild("buttonQuit").getEntity().getComponent(ButtonComponent.class);
        buttonQuit.addListener(new ButtonComponent.ButtonListener() {
            @Override
            public void touchUp() {

            }

            @Override
            public void touchDown() {

            }

            @Override
            public void clicked() {
                quitButton();
            }
        });
    }

    @Override
    public void show() {

    }

    private void playButton() {
        game.setScreen(new RoomGame(game));
    }

    private void quitButton() {
        Gdx.app.exit();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sceneLoader.getEngine().update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
}
