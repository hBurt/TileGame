package com.tilegame.game.models;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by: Harrison on 26 Oct 2018
 */
public class Player {

    Vector2 position;

    public Player(Vector2 position) {
        this.position = position;
    }

    public void move(Vector2 destination){
        position.x += destination.x;
        position.y += destination.y;

    }

    public float getY() {
        return position.x;
    }

    public float getX() {
        return position.y;
    }
}
