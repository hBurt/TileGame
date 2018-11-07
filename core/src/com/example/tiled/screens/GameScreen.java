package com.example.tiled.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.example.tiled.ExampleTiledGame;
import com.example.tiled.handler.InputHandler;
import com.example.tiled.models.Player;
import com.example.tiled.models.WorldLoader;

/**
 * Created by: Harrison on 24 Oct 2018
 */
public class GameScreen extends AbstractScreen {

    private WorldLoader worldLoader;

    private SpriteBatch batch;

    private Hud hud;
    private Player player;
    private InputHandler ih;

    public GameScreen(ExampleTiledGame app) {
        super(app);

        //Create a new SpriteBatch
        batch = new SpriteBatch();

        //New HUD
        hud = new Hud(batch);

        worldLoader = new WorldLoader("16xnew.tmx", app);

        //Add a new player to our map
        //Subtract this players position from our font batch draw player x and y to calculate proper node positions.
        player = new Player(worldLoader.getMap(), 2,2, app, worldLoader.getSimpleNodes());

        worldLoader.addPlayer(player);

        //Create the new inputHandler
        ih = new InputHandler(worldLoader.getMap(), player, worldLoader.getCamera(), worldLoader.getViewport(), worldLoader);
    }

    @Override
    public void show() {
        //Link the inputHandler
        Gdx.input.setInputProcessor(ih);
    }

    private void update(float delta){
        //Respond to player movement
        ih.update(delta);

        worldLoader.update(delta);

        //Update our hud
        hud.update(delta, player);
    }
    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(hud.stage.getCamera().combined);

        update(delta);

        worldLoader.render(batch);

        //Draw hud
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        worldLoader.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
