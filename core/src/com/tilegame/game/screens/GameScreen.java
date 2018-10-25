package com.tilegame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tilegame.game.TileGame;

import static com.tilegame.game.TileGame.V_HEIGHT;
import static com.tilegame.game.TileGame.V_WIDTH;

/**
 * Created by: Harrison on 24 Oct 2018
 */
public class GameScreen extends AbstractScreen {


    private SpriteBatch batch;
    private OrthographicCamera camera;
    private FitViewport viewport;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public GameScreen(TileGame app) {
        super(app);
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, camera);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("16x16.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/16f);

        float width = viewport.getWorldWidth();
        float height = viewport.getWorldHeight();

        //Set our camera
        // '/2' centers our game , '/16' scales to unit scale no we can get our tiles :)
        camera.position.set((width / 2) / 16, (height / 2) / 16, 0);

        //Zoom in our camera by the scaled amount multiplied by 2
        camera.zoom /= 32f; //This is what we use to zoom our game
    }

    public void update(float delta){
        //Check for any input first
        handleInput(delta);

        //Update our camera after inputs
        camera.update();

        //update camera every frame
        renderer.setView(camera);
    }

    @Override
    public void render(float delta) {
        update(delta);

        batch.setProjectionMatrix(camera.combined);

        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void handleInput(float delta){
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            camera.position.x -= 100 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            camera.position.x += 100 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            camera.position.y += 100 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            camera.position.y -= 100 * delta;
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {

            //Gets coordinates click on screen(relative to pixels)
            Vector3 input = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

            //Unproject input coordinates from screen to our game
            Vector3 u = camera.unproject(input);

            //Display
            //Reminder: 'Math.floor' rounds down (eg 99.99999 == 99)
            System.out.print("Clicked tile: " + Math.floor(u.x) + " : " + Math.floor(u.y) + "\n");
        }
    }

}
