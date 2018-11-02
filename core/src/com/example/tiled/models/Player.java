package com.example.tiled.models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.example.tiled.ExampleTiledGame;

/**
 * Created by: Harrison on 26 Oct 2018
 */
public class Player {

    //Our players x and y coods
    private int x, y;

    private int colideX, colideY;
    private TiledMap map;

    private int mapWidth, mapHeight;

    private float worldX, worldY;
    private int srcX, srcY;
    private int desX, desY;
    private float animTimer;
    //Player speed
    private float ANIM_TIME = .3f;
    private boolean[][] collision;

    private Direction facingDir;
    private ACTOR_STATE currentState;

    private AnimationSet animations;

    private boolean moveRequestThisFrame;

    private float walkTimer;

    //Construct the player class
    public Player(TiledMap map, int posX, int posY, ExampleTiledGame app) {
        this.map = map;
        this.x = posX;
        this.y = posY;
        this.worldX = x;
        this.worldY = y;
        currentState = ACTOR_STATE.STANDING;
        facingDir = Direction.SOUTH;


        MapLayers mapLayers = map.getLayers();

        TextureAtlas atlas = app.getAssetManager().get("character/textures.atlas", TextureAtlas.class);

        animations = new AnimationSet(
                new Animation<TextureRegion>(0.3f / 2f, atlas.findRegions("player_walk_north"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation<TextureRegion>(0.3f / 2f, atlas.findRegions("player_walk_south"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation<TextureRegion>(0.3f / 2f, atlas.findRegions("player_walk_east"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation<TextureRegion>(0.3f / 2f, atlas.findRegions("player_walk_west"), Animation.PlayMode.LOOP_PINGPONG),
                atlas.findRegion("player_stand_north"),
                atlas.findRegion("player_stand_south"),
                atlas.findRegion("player_stand_east"),
                atlas.findRegion("player_stand_west"));

        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);
        collision = new boolean[mapWidth][mapHeight];

        setColide((TiledMapTileLayer) mapLayers.get("walls"));
    }

    //Set collision tiles
    private void setColide(TiledMapTileLayer tiledMapTileLayer){
        for(int x = 0; x < mapWidth; x ++){
            for(int y = 0; y < mapHeight; y ++) {
                collision[x][y] = true;
                //Grab every tile in the 'walls' layer.
                //Note: if tile is null, it is walkable
                if(tiledMapTileLayer.getCell(x, y) != null) {
                     collision[x][y] = false;
                    //System.out.print(tmtl.getCell(x, y).getTile().getId());
                }
            }
        }
    }

    public void update(float delta){
        if( currentState == ACTOR_STATE.WALKING){
            animTimer += delta;
            walkTimer += delta;
            worldX = Interpolation.linear.apply(srcX, desX, animTimer / ANIM_TIME);   //World x not greater than desX
            worldY = Interpolation.linear.apply(srcY, desY, animTimer / ANIM_TIME);   //World x not greater than desX
            if(animTimer > ANIM_TIME){
                float leftOverTime = animTimer - ANIM_TIME;
                finishMove();
                if(moveRequestThisFrame){
                    if(move(facingDir)) {
                        animTimer += leftOverTime;
                        worldX = Interpolation.linear.apply(srcX, desX, animTimer / ANIM_TIME);
                        worldY = Interpolation.linear.apply(srcY, desY, animTimer / ANIM_TIME);
                    }
                } else {
                    walkTimer = 0f;
                }
            }
        }
        moveRequestThisFrame = false;
    }

    //Players simple move method
    public boolean move(Direction direction){
        if(currentState == ACTOR_STATE.WALKING){
            if(facingDir == direction){
                moveRequestThisFrame = true;
            }
            return false;
        }
        if(x + direction.getDx() >= mapWidth || x + direction.getDx() < 0 || y + direction.getDy() >= mapHeight || y + direction.getDy() < 0){
            return false;
        }
        //If player collides with a tile in the 'walls' layer, stop them.
        if(!collision[x + direction.getDx()][y + direction.getDy()]){
            colideX = x + direction.getDx();
            colideY = y + direction.getDy();
            return false;
        }
        //Set our players facing direction to use for animations
        facingDir = direction;

        initalizeMove(x, y, direction.getDx(), direction.getDy());
        //Set players x and y relative to the addition/subtraction from direction
        x += direction.getDx();
        y += direction.getDy();

        //Debug
        System.out.println("Player(x,y): " + getX() + ", " + getY() );
        return true;
    }

    private void initalizeMove(int oldX, int oldY, int dx, int dy){
        this.srcX = x;
        this.srcY = y;
        this.desX = oldX + dx;
        this.desY = oldY + dy;
        this.worldX = x;
        this.worldY = y;
        animTimer = 0f;
        currentState = ACTOR_STATE.WALKING;
    }

    private void finishMove() {
        currentState = ACTOR_STATE.STANDING;
        this.worldX = desX;
        this.worldY = desY;
        this.srcX = 0;
        this.srcY = 0;
        this.desX = 0;
        this.desY = 0;
    }

    public TextureRegion getTexture(){
        if(currentState == ACTOR_STATE.STANDING){
            return animations.getStanding(facingDir);
        } else if(currentState == ACTOR_STATE.WALKING){
            return animations.getWalking(facingDir).getKeyFrame(walkTimer);
        }
        return animations.getStanding(facingDir);
    }


    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }

    public int getColideX() {
        return colideX;
    }

    public int getColideY() {
        return colideY;
    }

    public enum ACTOR_STATE {
        WALKING,
        STANDING
    }

    //Return players x value
    public float getX() {
        return x;
    }

    //Return players y value
    public float getY() {
        return y;
    }



}
