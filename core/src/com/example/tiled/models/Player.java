package com.example.tiled.models;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;

/**
 * Created by: Harrison on 26 Oct 2018
 */
public class Player {

    //Our players x and y coods
    private int x, y;

    private int colideX, colideY;
    TiledMap map;

    int mapWidth, mapHeight;

    private float worldX, worldY;
    int srcX, srcY;
    int desX, desY;
    float animTimer;
    private float ANIM_TIME = .4f;
    private boolean[][] collision;

    private float walkTimer;
    private boolean moveRequestThisFrame;

    private ACTOR_STATE state;

    //Construct the player class
    public Player(TiledMap map, int posX, int posY) {
        this.map = map;
        this.x = posX;
        this.y = posY;
        this.worldX = x;
        this.worldY = y;
        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);
        this.state = ACTOR_STATE.STANDING;
        collision = new boolean[mapWidth][mapHeight];
        setColide();
    }
    //Retrieve our maps layers
    //MapLayers ml = map.getLayers();

    //Get the layer related to collisoion
    //layer = (TiledMapTileLayer) ml.get("walls");

    //If this == null, then no collision.
    //System.out.print("This: " + layer.getCell(1,1).getTile().getId());
    private void setColide(){
        MapLayers mapLayers = map.getLayers();
        TiledMapTileLayer tmtl = (TiledMapTileLayer) mapLayers.get("walls");
        for(int x = 0; x < mapWidth; x ++){
            for(int y = 0; y < mapHeight; y ++) {
                collision[x][y] = true;
                //Grab everytile in 'walls' layer.
                //Note: if tile is null, it is walkable
                if(tmtl.getCell(x, y) != null) {
                     collision[x][y] = false;
                    //System.out.print(tmtl.getCell(x, y).getTile().getId());
                }
            }
        }
    }

    public void update(float delta){
        if( state == ACTOR_STATE.WALKING){
            animTimer += delta;
            worldX = Interpolation.linear.apply(srcX, desX, animTimer / ANIM_TIME);   //World x not greater than desX
            worldY = Interpolation.linear.apply(srcY, desY, animTimer / ANIM_TIME);   //World x not greater than desX
            if(animTimer > ANIM_TIME){
                finishMove();
                state = ACTOR_STATE.STANDING;
            }
        }
    }

    //Players simple move method
    public boolean move(Direction direction){
        if(state != ACTOR_STATE.STANDING){
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
        state = ACTOR_STATE.WALKING;
    }

    private void finishMove() {
        state = ACTOR_STATE.STANDING;
        this.worldX = desX;
        this.worldY = desY;
        this.srcX = 0;
        this.srcY = 0;
        this.desX = 0;
        this.desY = 0;
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
