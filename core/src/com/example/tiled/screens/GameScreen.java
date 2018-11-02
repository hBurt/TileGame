package com.example.tiled.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.tiled.ExampleTiledGame;
import com.example.tiled.Settings;
import com.example.tiled.handler.InputHandler;
import com.example.tiled.models.Player;

/**
 * Created by: Harrison on 24 Oct 2018
 */
public class GameScreen extends AbstractScreen {

    private Viewport viewport;
    private OrthographicCamera camera;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private TiledMapTileLayer layer;
    private OrthogonalTiledMapRenderer renderer;

    private TextureAtlas atlas;

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

        //Create a Orthographic cam
        camera = new OrthographicCamera();

        //Create a new viewport ser to fixed size.
        viewport = new FitViewport(ExampleTiledGame.V_WIDTH, ExampleTiledGame.V_HEIGHT, camera);

        //Create a new map loader
        mapLoader = new TmxMapLoader();

        //Load a new map
        map = mapLoader.load("16xnew.tmx");

        //Load our tmx and set our scale
        renderer = new OrthogonalTiledMapRenderer(map, 1/16f);

        //Grab our viewport values
        float width = viewport.getWorldWidth();
        float height = viewport.getWorldHeight();

        //Add a new player to our map
        player = new Player(map, 2,2, app);

        //Properly scale our camera
        camera.setToOrtho(false, (width / Settings.tileSize) / Settings.gameScale, (height / Settings.tileSize) / Settings.gameScale);

        //Zoom in our camera by the scaled amount
        camera.zoom /= Settings.gameScale * Settings.tileSize; //This is what we use to zoom our game

        //Create the new inputHandler
        ih = new InputHandler(map, player, camera, viewport);
    }

    @Override
    public void show() {
        //Link the inputHandler
        Gdx.input.setInputProcessor(ih);
    }


    private void update(float delta){
        ih.update(delta);

        //Interpolate the player
        player.update(delta);

        //Position our camera on player every frame
        camera.position.set(player.getWorldX(), player.getWorldY(), 0);

        //Update our camera after inputs
        camera.update();

        //Update our hud
        hud.update(delta, player);

        //update camera every frame
        renderer.setView(camera);


    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(hud.stage.getCamera().combined);

        //Call our update method
        update(delta);

        //Display the tileMap
        renderer.render();

        //Grab our world coordinates
        float worldStartX = viewport.getWorldWidth() / 2f - camera.position.x * 32f;
        float worldStartY = viewport.getWorldHeight() / 2f - camera.position.y * 32f;

        //Start our batch
        batch.begin();
        if(player != null) {
            batch.draw(player.getTexture(),
                    worldStartX + player.getWorldX() * 32f, //Players x
                    worldStartY + player.getWorldY() * 32f, //Players Y
                    Settings.gameScale * Settings.tileSize, //tile size
                    Settings.gameScale * Settings.tileSize  //tile size
            );
        }
        batch.end();

        //Draw hud
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
