package com.tilegame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
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

    private Hud hud;
    private int[] count = {0, 0};  //Count output #

    private Vector2 coords = new Vector2(0,0);

    BitmapFont font = new BitmapFont();

    //Vad stuff

    public GameScreen(TileGame app) {
        super(app);

        //Create a new SpriteBatch
        batch = new SpriteBatch();

        //New HUD
        hud = new Hud(batch);

        //Create a Orthographic cam
        camera = new OrthographicCamera();

        //Create a new viewport ser to fixed size.
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, camera);

        //Create a new map loader
        mapLoader = new TmxMapLoader();

        //Load a new map
        map = mapLoader.load("16xnew.tmx");

        //Load our tmx and set our scale
        renderer = new OrthogonalTiledMapRenderer(map, 1/16f);

        //Grab our viewport values
        float width = viewport.getWorldWidth();
        float height = viewport.getWorldHeight();
        float gameScale = 2f;
        float tileSize = 16f;

        //Reposition our camera
        // '/2' centers our game , '/16' scales to unit scale no we can get our tiles :)
        //camera.position.set((width / 2) / tileSize, (height / 2) / tileSize, 0);
        camera.setToOrtho(false, width / tileSize, height / tileSize);
        camera.setToOrtho(false, (width / tileSize) / gameScale, (height / tileSize) / gameScale);

        //Zoom in our camera by the scaled amount multiplied by 2
        camera.zoom /= gameScale * tileSize; //This is what we use to zoom our game
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
        //batch.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(hud.stage.getCamera().combined);

        update(delta);
        renderer.render();

        //Draw hud
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void handleInput(float delta){

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            count[1] += 1;
            System.out.print(count[1] + ":" +viewport.getWorldWidth() + " : " + viewport.getWorldHeight() + "\n");
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            camera.zoom += .1 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.zoom -= .1 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            camera.position.x -= 10 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            camera.position.x += 10 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            camera.position.y += 10 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            camera.position.y -= 10 * delta;
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            count[0] += 1;
            //Gets coordinates click on screen(relative to pixels)
            Vector3 input = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

            //Unproject input coordinates from screen to our game
            Vector3 u = camera.unproject(input);

            //Display
            //Reminder: 'Math.floor' rounds down (eg 99.99999 == 99)
            System.out.print(count[0] + ":Clicked tile: " + Math.floor(u.x) + " : " + Math.floor(u.y) + "\n");

        }
    }

}
