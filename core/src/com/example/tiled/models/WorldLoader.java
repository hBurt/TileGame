package com.example.tiled.models;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.tiled.ExampleTiledGame;
import com.example.tiled.Settings;
import com.example.tiled.handler.WorldHandler;
import com.example.tiled.pathfinding.SimpleNode;

/**
 * Created by: Harrison on 02 Nov 2018
 */
public class WorldLoader {

    private TmxMapLoader mapLoader;
    private TiledMap map;

    private OrthographicCamera camera;
    private Viewport viewport;

    private OrthogonalTiledMapRenderer renderer;

    private Player player;

    private WorldHandler worldHadler;

    private Array<SimpleNode> simpleNodes;

    BitmapFont font = new BitmapFont();

    public WorldLoader(String tmxName, ExampleTiledGame app){

        //Init a new map loader for our tmx
        mapLoader = new TmxMapLoader();

        //Set the tmx into the TiledMap
        map = mapLoader.load(tmxName);

        //Load our tmx and set our scale
        renderer = new OrthogonalTiledMapRenderer(getMap(), 1/16f);

        //Create a Orthographic cam
        camera = new OrthographicCamera();

        //Create a new viewport ser to fixed size.
        viewport = new FitViewport(ExampleTiledGame.V_WIDTH, ExampleTiledGame.V_HEIGHT, camera);

        //Properly scale our camera
        camera.setToOrtho(false,
                (viewport.getWorldWidth() / Settings.tileSize) / Settings.gameScale,
                (viewport.getWorldHeight() / Settings.tileSize) / Settings.gameScale);

        //Zoom in our camera by the scaled amount
        camera.zoom /= Settings.gameScale * Settings.tileSize; //This is what we use to zoom our game

        worldHadler = new WorldHandler(tmxName, app);
        //Get our nodes
        simpleNodes = worldHadler.getGraph().getSimpleNodes();


    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void render(SpriteBatch batch){
        //Display the tileMap
        getRenderer().render();

        //Grab our world coordinates
        float worldStartX = getViewport().getWorldWidth() / 2f - getCamera().position.x * 32f;
        float worldStartY = getViewport().getWorldHeight() / 2f - getCamera().position.y * 32f;

        batch.begin();
        if(player != null) {
            batch.draw(player.getTexture(),
                    worldStartX + player.getWorldX() * 32f, //Players x
                    worldStartY + player.getWorldY() * 32f, //Players Y
                    Settings.gameScale * Settings.tileSize, //tile size
                    Settings.gameScale * Settings.tileSize  //tile size
            );
        }

        for (SimpleNode s: simpleNodes) {
            //Draw an example of our nodes
            font.draw(batch, s.getX() + ":" + s.getY(),
                    (worldStartX + s.getX() * 64f)
                            - (Settings.gameScale / 2 * Settings.tileSize)
                            - ((player.getWorldX() - 2) * 32) - 12,
                    (worldStartY + s.getY() * 64f) - (Settings.gameScale / 2 * Settings.tileSize) - ((player.getWorldY() - 2) * 32)
            );
        }
        batch.end();

    }

    public void update(float delta){

        //Interpolate(move/tween/etc..) the player
        player.update(delta);

        //Position our camera on player every frame
        getCamera().position.set(player.getWorldX(), player.getWorldY(), 0);

        //Update our camera after inputs
        getCamera().update();

        //update camera every frame
        getRenderer().setView(getCamera());


    }

    public void addPlayer(Player p){
        this.player = p;
    }
    public TiledMap getMap() {
        return map;
    }

    public OrthogonalTiledMapRenderer getRenderer(){
        return renderer;
    }

    public void set(String tmxName){
        this.map = mapLoader.load(tmxName);
    }
}
