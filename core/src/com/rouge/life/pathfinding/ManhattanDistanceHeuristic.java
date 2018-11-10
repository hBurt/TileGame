package com.rouge.life.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

/**
 * Created by: Harrison on 06 Nov 2018
 */
public class ManhattanDistanceHeuristic implements Heuristic<SimpleNode> {

    @Override
    public float estimate(SimpleNode node, SimpleNode endNode) {
        return Math.abs(endNode.getX() - node.getX()) + Math.abs(endNode.getX() - node.getY());
    }
}
