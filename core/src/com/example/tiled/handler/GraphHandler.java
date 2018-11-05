package com.example.tiled.handler;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.example.tiled.pathfinding.SimpleNode;

/**
 * Created by: Harrison on 03 Nov 2018
 */
public class GraphHandler {

    Array<SimpleNode> simpleNodes;

    public GraphHandler(TiledMap map) {
        simpleNodes = new Array<SimpleNode>();

        TiledMapTileLayer tiles = (TiledMapTileLayer)map.getLayers().get("walls");
        int mapWidth = WorldHandler.getStaticTileLvlWidth();
        int mapHeight = WorldHandler.getStaticTileLvlHeight();

        //Create graph of node on map
        for(int x = 0; x < mapWidth; x++){
            System.out.print("\n");
            for(int y = 0; y < mapHeight; y++){
                //Can walk
                if(tiles.getCell(x, y) == null){
                    System.out.print(/*"" + x + ", " + y + */"  ");
                    //Add new node to specified x & y tile.
                    simpleNodes.add(new SimpleNode(x, y, 0));
                } else {
                    //Show Collision(in console)
                    System.out.print(/*"" + x + ", " + */"x "/* + x + ":" + y*/);
                }
            }
        }

    }

    public Array<SimpleNode> getSimpleNodes() {
        return simpleNodes;
    }

    public void show(){
        for (SimpleNode node: simpleNodes) {
            System.out.println(node.getX()+ ", " + node.getY() + "\n");
        }
    }
}
