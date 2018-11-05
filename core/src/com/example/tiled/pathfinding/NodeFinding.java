package com.example.tiled.pathfinding;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by: Harrison on 04 Nov 2018
 */
public class NodeFinding {

    Array<SimpleNode> simpleNodes;

    private int startX;
    private int startY;
    private int endX;
    private int endY;

    HashMap<SimpleNode, Boolean> mapping;

    NodeFinding(Array<SimpleNode> simpleNodes, int startX, int startY, int endX, int endY){
        this.simpleNodes = simpleNodes;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        for (SimpleNode s : simpleNodes) {
            if(s.getX() == startX && s.getY() == startY){
                mapping.put(s, false);
            } else if (s.getX() == endX && s.getY() == endY) {
                mapping.put(s, false);
            } else {
                mapping.put(s, true);
            }
        }
    }

    private void checking(int startX, int startY){
            for(SimpleNode s : mapping.keySet()){

            }
    }




}
