package com.tilegame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tilegame.game.TileGame;
import com.tilegame.game.models.Player;

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
    private TiledMapTileLayer layer;
    private OrthogonalTiledMapRenderer renderer;

    private TextureAtlas textureAtlas;

    //Create new texture for our player
    private Texture texture;

    private Hud hud;
    private int[] count = {0, 0};  //Count output #

    private Player player;




    public GameScreen(TileGame app) {
        super(app);

        //Create a new SpriteBatch
        batch = new SpriteBatch();

        //Load Texture as player
        texture = new Texture(Gdx.files.internal("character/player_stand_south.png"));

        player = new Player(new Vector2(2, 2));

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
        //camera.setToOrtho(false, width / tileSize, height / tileSize);
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

        float worldStartX = viewport.getWorldWidth() / (float) 2 - camera.position.x * 32;
        float worldStartY = viewport.getWorldHeight() / (float) 2 - camera.position.y * 32;

        //Start our batch
        batch.begin();
        batch.draw(texture,
                worldStartX + player.getX() * 32, //Players x
                worldStartY + player.getY() * 32, //Players Y
                32, //tile size
                32  //tile size
        );
        batch.end();


        //Draw hud
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void handleInput(float delta){

        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)){
            count[1] += 1;
            System.out.print(count[1] + ":" + viewport.getWorldWidth() + " : " + viewport.getWorldHeight() + "\n");
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_2)){
            camera.zoom += .1 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_3)){
            camera.zoom -= .1 * delta;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            player.move(new Vector2(1, 0));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            player.move(new Vector2(0, -1)); //player.move(new Vector2(0, 1));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            player.move(new Vector2(-1, 0));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            player.move(new Vector2(0, 1)); //player.move(new Vector2(-1, 0))
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

    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
    }
}
