package com.example.tiled.handler;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.example.tiled.ExampleTiledGame;

/**
 * Created by: Harrison on 03 Nov 2018
 */
public class WorldHandler {

    private int lvlTileWidth;
    private int lvlTileHeight;
    private static int staticLvlTileWidth;
    private static int staticLvlTileHeight;
    private int lvlPixelWidth;
    private int lvlPixelHeight;
    private int tilePixelWidth;
    private int tilePixelHeight;
    private TiledMap tiledMap;

    private static GraphHandler graph;

    public WorldHandler(String tmxName, ExampleTiledGame app){
        tiledMap = app.getAssetManager().get(tmxName, TiledMap.class);
        loadLevel();
    }

    public void loadLevel(){
        MapProperties properties = tiledMap.getProperties();
        lvlTileWidth = properties.get("width", Integer.class);
        lvlTileHeight = properties.get("height", Integer.class);
        staticLvlTileWidth = properties.get("width", Integer.class);
        staticLvlTileHeight = properties.get("height", Integer.class);
        tilePixelWidth = properties.get("tilewidth", Integer.class);
        tilePixelHeight = properties.get("tileheight", Integer.class);
        lvlPixelWidth = lvlTileWidth * tilePixelWidth;
        lvlPixelHeight = lvlTileHeight * tilePixelHeight;

        //Graph from tilemap
        graph = new GraphHandler(tiledMap);
    }

    public int getLvlTileWidth() {
        return lvlTileWidth;
    }

    public int getLvlTileHeight() {
        return lvlTileHeight;
    }

    public static int getStaticTileLvlWidth() { return staticLvlTileWidth; }

    public static int getStaticTileLvlHeight() { return staticLvlTileHeight; }

    public int getLvlPixelWidth() {
        return lvlPixelWidth;
    }

    public int getLvlPixelHeight() {
        return lvlPixelHeight;
    }

    public int getTilePixelWidth() {
        return tilePixelWidth;
    }

    public int getTilePixelHeight() {
        return tilePixelHeight;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public GraphHandler getGraph() {
        return graph;
    }
}
