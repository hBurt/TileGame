package com.rouge.life.models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;
import com.rouge.life.RougeLife;
import com.rouge.life.pathfinding.SimpleNode;


/**
 * Created by: Harrison on 26 Oct 2018
 */
public class Player {

    //Our players x and y coods
    private int x, y;

    private int colideX, colideY;
    private TiledMap map;

    SimpleNode node;
    private boolean isWalking;

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

    public AnimationSet getAnimations() {
        return animations;
    }

    private AnimationSet animations;

    private boolean moveRequestThisFrame;

    private float walkTimer;

    private Array<SimpleNode> simpleNodes;

    //Construct the player class
    public Player(TiledMap map, int posX, int posY, RougeLife app, Array<SimpleNode> simpleNodes) {
        this.map = map;
        this.x = posX;
        this.y = posY;
        this.worldX = x;
        this.worldY = y;
        this.isWalking = false;
        currentState = ACTOR_STATE.STANDING;
        facingDir = Direction.SOUTH;

        this.simpleNodes = simpleNodes;

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
            isWalking = true;
            animTimer += delta;
            walkTimer += delta;
            System.out.println("1:" + walkTimer);
            worldX = Interpolation.linear.apply(srcX, desX, animTimer / ANIM_TIME);   //WorldLoader x not greater than desX
            worldY = Interpolation.linear.apply(srcY, desY, animTimer / ANIM_TIME);   //WorldLoader x not greater than desX
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
                    walkTimer -= leftOverTime;
                    //TODO this need to be set to 0 when player is done with the path
                    //System.out.println("LeftOvers:" + walkTimer);
                }
            }
        }
        isWalking = false;
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
        //Edit: Below collision should not be necessary with nodes but I will keep it here as reference for the future.
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


    //Had to reverse them??
    public void walkTo(int x, int y){
        if(getWorldX() == x + 1 && getWorldY() == y){   //Right (east)
            move(Direction.WEST);
            System.out.println("Do walk west");
        }
        if(getWorldX() == x - 1 && getWorldY() == y){   //Left (west)
            move(Direction.EAST);
            System.out.println("Do walk east");
        }
        if(getWorldX() == x && getWorldY() == y + 1){   //Up (north)
            move(Direction.SOUTH);
            System.out.println("Do walk south");
        }
        if(getWorldX() == x && getWorldY() == y - 1){   //Down (south)
            move(Direction.NORTH);
            System.out.println("Do walk north");
        }
    }

    public Direction walkToDir(int x, int y){
        if(getWorldX() == x + 1 && getWorldY() == y){   //Right (east)
            return Direction.WEST;
        }
        if(getWorldX() == x - 1 && getWorldY() == y){   //Left (west)
            return Direction.EAST;
        }
        if(getWorldX() == x && getWorldY() == y + 1){   //Up (north)
            return Direction.SOUTH;
        }
        if(getWorldX() == x && getWorldY() == y - 1){   //Down (south)
            return Direction.NORTH;
        }
        return  null;
    }

    private void initalizeMove(int oldX, int oldY, int dx, int dy){
        this.srcX = x;
        this.srcY = y;
        this.desX = oldX + dx;
        this.desY = oldY + dy;
        this.worldX = x;
        this.worldY = y;
        animTimer = 0f;
        isWalking = true;
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
        isWalking = false;
    }

    public TextureRegion getTexture(){
        if(currentState == ACTOR_STATE.STANDING){
            return animations.getStanding(facingDir);
        } else if(currentState == ACTOR_STATE.WALKING){
            return animations.getWalking(facingDir).getKeyFrame(walkTimer);
        }
        return animations.getStanding(facingDir);
    }

    public int getID(){
        for (SimpleNode s : simpleNodes) {
            //If coords of s node == player x & y
            if(s.peek((int) Math.floor(getWorldX()), (int) Math.floor(getWorldY()))){
                return s.getIndex();
            }
        }
        return -1;
    }

    SimpleNode gottenNode = null;

    public SimpleNode getNode(){
        for (SimpleNode s : simpleNodes) {
            //If coords of s node == player x & y
            if(s.peek((int) Math.floor(getWorldX()), (int) Math.floor(getWorldY()))){
                gottenNode = null;
                return s;
            }
        }

        return gottenNode;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
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
