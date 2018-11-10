package com.rouge.life.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;

/**
 * Created by: Harrison on 06 Nov 2018
 */
public class NodeGraph implements IndexedGraph<SimpleNode> {

    Array<SimpleNode> nodes;

    public NodeGraph(Array<SimpleNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public int getIndex(SimpleNode node) {
        return node.getIndex();
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    @Override
    public Array<Connection<SimpleNode>> getConnections(SimpleNode fromNode) {
        return fromNode.getConnections();
    }

    public Array<SimpleNode> getNodes() {
        return nodes;
    }
}
