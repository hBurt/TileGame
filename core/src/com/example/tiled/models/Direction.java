package com.example.tiled.models;

import com.example.tiled.Settings;

/**
 * Relate which direction you would like to go n/s/e/w
 */
public enum Direction {
    NORTH(0, 1),
    SOUTH(0, -1),
    EAST(1, 0),
    WEST(-1, 0)
    ;

    //Values of direction
    private int dx, dy;

    //Set the n/s/e/w directions
    Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
    }


    //Return the first value of our direction
    public int getDx() {
        return dx;
    }

    //Return the second value of our direction
    public int getDy() {
        return dy;
    }
}
