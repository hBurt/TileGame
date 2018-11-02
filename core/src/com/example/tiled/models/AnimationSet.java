package com.example.tiled.models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by: Harrison on 02 Nov 2018
 */
public class AnimationSet {

    private Map<Direction, Animation<TextureRegion>> walking;
    private Map<Direction, TextureRegion> standing;

    public AnimationSet(Animation<TextureRegion> walkNorth,
                        Animation<TextureRegion> walkSouth,
                        Animation<TextureRegion> walkEast,
                        Animation<TextureRegion> walkWest,
                        TextureRegion standNorth,
                        TextureRegion standSouth,
                        TextureRegion standEast,
                        TextureRegion standWest ) {
        walking = new HashMap<Direction, Animation<TextureRegion>>();
        walking.put(Direction.NORTH, walkNorth);
        walking.put(Direction.SOUTH, walkSouth);
        walking.put(Direction.EAST, walkEast);
        walking.put(Direction.WEST, walkWest);
        standing = new HashMap<Direction, TextureRegion>();
        standing.put(Direction.NORTH, standNorth);
        standing.put(Direction.SOUTH, standSouth);
        standing.put(Direction.EAST, standEast);
        standing.put(Direction.WEST, standWest);
    }

    public Animation<TextureRegion> getWalking(Direction dir){
        return walking.get(dir);
    }

    public TextureRegion getStanding(Direction dir){
        return standing.get(dir);
    }
    
}
