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
    private TextureAtlas atlas;
    private OrthographicCamera camera;
    private FitViewport viewport;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public GameScreen(TileGame app) {
        super(app);
        batch = new SpriteBatch();
        atlas = getApp().getAssetManager().get("textures.atlas", TextureAtlas.class);

        camera = new OrthographicCamera();
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, camera);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("16x16.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/16f);

        float width = viewport.getWorldWidth();
        float height = viewport.getWorldHeight();

        camera.position.set(width / 2, height / 2, 0);
        camera.setToOrtho(false, (width / 2) / 16, (height / 2) / 16);
        camera.zoom /= 16;
    }

    public void update(float delta){
        //Check for any input first
        handleInput(delta);
        //Update our camera after inputs
        camera.update();
        renderer.setView(camera);
    }

    @Override
    public void render(float delta) {
        update(delta);

        batch.setProjectionMatrix(camera.combined);

        //Draw stuff
        batch.begin();
        //When we Load our map, the width and height will be our tile maps x and y length(eg 100x100 tiles)
        //batch.draw(atlas.findRegion("badlogic"), 0,0,256, 256);
        batch.end();

        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private void handleInput(float delta){
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            camera.position.x -= 200 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            camera.position.x += 200 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            camera.position.y += 200 * delta;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            camera.position.y -= 200 * delta;
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            Vector3 input = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 u = camera.unproject(input);
            System.out.print(u.x + " : " + u.y + "\n");
        }
    }

}
