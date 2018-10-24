package com.tilegame.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.tilegame.game.TileGame;

/**
 * Created by: Harrison on 24 Oct 2018
 */
public class GameScreen extends AbstractScreen {

    int tilesize = 16;

    int worldX = 8;
    int worldY = 8;

    private float camSpeed = .5f;

    private SpriteBatch batch;
    private TextureAtlas atlas;
    private OrthographicCamera camera;

    public GameScreen(TileGame app) {
        super(app);
        batch = new SpriteBatch();
        atlas = getApp().getAssetManager().get("textures.atlas", TextureAtlas.class);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(256, 256 );
        camera.position.set(0,0,0);
    }


    @Override
    public void render(float delta) {
        //Quick easy visual to confirm screen switch
        Gdx.gl.glClearColor(0, 1, 0, 1);
        handleInput();
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        //Draw stuff
        batch.begin();
        batch.draw(atlas.findRegion("badlogic"), 0,0,100, 100);
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.02;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(3, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.rotate(-camSpeed, 0, 0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            camera.rotate(camSpeed, 0, 0, 1);
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            Vector3 input = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 u = camera.unproject(input);
            System.out.print(u.x + " : " + u.y + "\n");
        }

        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100/camera.viewportWidth);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
    }
}
