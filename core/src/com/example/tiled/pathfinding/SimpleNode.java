
package com.example.tiled.pathfinding;
/**
 * Created by: Harrison on 03 Nov 2018
 */
public class SimpleNode {

    int x, y;

    private int g;
    private int h;

    private int f;

    int cost;

    private boolean open;

    public SimpleNode(int x, int y, int cost) {
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.open = true;
    }


    enum NodeOrientation {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getF() {
        return f;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public SimpleNode getNextOrient(NodeOrientation o){
        if(o == NodeOrientation.NORTH){
            return new SimpleNode(x, y + 1, cost);
        } else if (o == NodeOrientation.SOUTH){
            return new SimpleNode(x, y - 1, cost);
        } else if (o == NodeOrientation.EAST){
            return new SimpleNode(x + 1, y, cost);
        } else if (o == NodeOrientation.WEST){
            return new SimpleNode(x - 1, y, cost);
        }
        return null;
    }

    public int getCost() {
        return cost;
    }

}
