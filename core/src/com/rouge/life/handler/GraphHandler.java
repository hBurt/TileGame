package com.rouge.life.handler;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.rouge.life.pathfinding.SimpleNode;


/**
 * Created by: Harrison on 03 Nov 2018
 */
public class GraphHandler {

    Array<SimpleNode> simpleNodes;

    int[][] simpleNodesArr;

    public GraphHandler(TiledMap map) {
        simpleNodes = new Array<SimpleNode>();

        TiledMapTileLayer tiles = (TiledMapTileLayer)map.getLayers().get("walls");
        int mapWidth = WorldHandler.getStaticTileLvlWidth();
        int mapHeight = WorldHandler.getStaticTileLvlHeight();

        simpleNodesArr = new int[mapWidth][mapHeight];
        int nodeCount = 0;
        //Create graph of node on map
        for(int x = 0; x < mapWidth; x++){
            //System.out.print("\n");
            for(int y = 0; y < mapHeight; y++){
                //Can walk
                if(tiles.getCell(x, y) == null){
                    //System.out.print(/*"" + x + ", " + y + */"  ");
                    //Add new node to specified x & y tile.
                    simpleNodes.add(new SimpleNode(x, y, nodeCount));
                    nodeCount++;
                    simpleNodesArr[x][y] = 1;   //Set node
                } else {
                    //Show Collision(in console)
                    //System.out.print(/*"" + x + ", " + */"x "/* + x + ":" + y*/);
                    simpleNodesArr[x][y] = 0;   //Set no node
                }
            }
            //System.out.print("\n");
        }

    }

    public Array<SimpleNode> getSimpleNodes() {
        return simpleNodes;
    }

    public int[][] getSimpleNodesArrBinary() {
        return simpleNodesArr;
    }

    public void show(){
        for (SimpleNode node: simpleNodes) {
            System.out.println(node.getX()+ ", " + node.getY() + "\n");
        }
    }
}
