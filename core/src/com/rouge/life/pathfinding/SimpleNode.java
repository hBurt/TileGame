package com.rouge.life.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.utils.Array;

/**
 * Created by: Harrison on 03 Nov 2018
 */
public class SimpleNode {

    //Node x pos
    int x;

    //Node y pos
    int y;

    //Node index
    int index;

    //Is node part of larger path?

    private boolean isSelected;
    /** The neighbours of this node. i.e to which node can we travel to from here. */
    Array<Connection<SimpleNode>> connections = new Array<Connection<SimpleNode>>();

    public SimpleNode(int x, int y, int index) {
        this.x = x;
        this.y = y;
        this.index = index;
        this.isSelected = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getIndex() {
        return index;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void select(){
        this.isSelected = true;
    }

    public void unSelect(){
        this.isSelected = true;
    }

    public String toString() {
        return "Index:" + index + " x:" + x + " y:" + y;
    }

    public Array<Connection<SimpleNode>> getConnections() {
        return connections;
    }

    public boolean peek(int x, int y){
        //if int x and int y = this x and this y then return true
        return this.x == x && this.y == y;
    }

    public void addNeighbour(SimpleNode aNode) {
        if (null != aNode) {
            //Sets cost to move connection to 1f
            connections.add(new DefaultConnection<SimpleNode>(this, aNode));
        }
    }

}
