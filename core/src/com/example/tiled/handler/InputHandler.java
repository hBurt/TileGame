package com.example.tiled.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.example.tiled.models.Direction;
import com.example.tiled.models.Player;

/**
 * Created by: Harrison on 26 Oct 2018
 */
public class InputHandler extends InputAdapter {

    Player player;
    OrthographicCamera camera;
    Viewport viewport;

    private boolean[] buttonPress;
    private float[] buttonTimer;

    private int sizeX, sizeY;

    public InputHandler(TiledMap map, Player player, OrthographicCamera camera, Viewport viewport) {
        this.player = player;
        this.camera = camera;
        this.viewport = viewport;
        sizeX = map.getProperties().get("width", Integer.class);
        sizeY = map.getProperties().get("height", Integer.class);
        buttonPress = new boolean[Direction.values().length];
        buttonPress[Direction.NORTH.ordinal()] = false;
        buttonPress[Direction.SOUTH.ordinal()] = false;
        buttonPress[Direction.EAST.ordinal()] = false;
        buttonPress[Direction.WEST.ordinal()] = false;
        buttonTimer = new float[Direction.values().length];
        buttonTimer[Direction.NORTH.ordinal()] = 0f;
        buttonTimer[Direction.SOUTH.ordinal()] = 0f;
        buttonTimer[Direction.EAST.ordinal()] = 0f;
        buttonTimer[Direction.WEST.ordinal()] = 0f;
    }

    //Update player movement
    public void update(float delta) {
        if(buttonPress[Direction.NORTH.ordinal()]) {
            updateDirection(Direction.NORTH, delta);

            //After a move, set the camera on our player
            camera.position.set(player.getX(), player.getY(), 0);
            return;
        }
        if(buttonPress[Direction.SOUTH.ordinal()]) {
            updateDirection(Direction.SOUTH, delta);

            //After a move, set the camera on our player
            camera.position.set(player.getX(), player.getY(), 0);
            return;
        }
        if(buttonPress[Direction.EAST.ordinal()]) {
            updateDirection(Direction.EAST, delta);

            //After a move, set the camera on our player
            camera.position.set(player.getX(), player.getY(), 0);
            return;
        }
        if(buttonPress[Direction.WEST.ordinal()]) {
            updateDirection(Direction.WEST, delta);

            //After a move, set the camera on our player
            camera.position.set(player.getX(), player.getY(), 0);
        }
    }

    /**
     * Runs every frame, for each direction whose key is pressed
     */
    private void updateDirection(Direction dir, float delta){
        buttonTimer[dir.ordinal()] += delta;
        considerMove(dir);
    }

    /**
     * Runs when a key is released
     */
    private void releaseDirection(Direction dir){
        buttonPress[dir.ordinal()] = false;
        considerReface(dir);
        buttonTimer[dir.ordinal()] = 0f;
    }

    private void considerMove(Direction dir){
        //if(buttonTimer[dir.ordinal()] > 0.07f){
            player.move(dir);
        //}
    }

    private void considerReface(Direction dir){
       // if(buttonTimer[dir.ordinal()] < 0.07f) {
            //player.reface(dir);
        //}
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.UP){
            if(player.getY() != sizeX - 1){
                buttonPress[Direction.NORTH.ordinal()] = true;
            }
        }
        if(keycode == Input.Keys.DOWN){
            if(player.getY() != 0) {
                buttonPress[Direction.SOUTH.ordinal()] = true;
            }
        }
        if(keycode == Input.Keys.LEFT){
            if(player.getX() !=0){
                buttonPress[Direction.WEST.ordinal()] = true;
            }
        }
        if(keycode == Input.Keys.RIGHT){
            if(player.getX() != sizeY - 1){
                buttonPress[Direction.EAST.ordinal()] = true;
            }
        }
        if(keycode == Input.Keys.L){
            System.out.print(camera.position);
        }
        if(keycode == Input.Keys.PLUS){
            camera.zoom += .01f;
        }
        if(keycode == Input.Keys.MINUS){
            camera.zoom -= .01f;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.UP){
            releaseDirection(Direction.NORTH);
        }
        if(keycode == Input.Keys.DOWN){
            releaseDirection(Direction.SOUTH);
        }
        if(keycode == Input.Keys.LEFT){
            releaseDirection(Direction.WEST);
        }
        if(keycode == Input.Keys.RIGHT){
            releaseDirection(Direction.EAST);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //Grab graphics
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        if(screenY >= 0 && screenY <= h /2){
            //up
            releaseDirection(Direction.NORTH);
        } else {
            if(screenX >= 0 && screenX <= 300){
                System.out.print("Button2");
                //left;
                releaseDirection(Direction.WEST);
            }else if(screenX <= w && screenX >= w - 300){
                System.out.print("Button2");
                //right
                releaseDirection(Direction.EAST);
            } else {
                //down
                releaseDirection(Direction.SOUTH);
            }
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        //Grab graphics
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        if(screenY >= 0 && screenY <= h /2){
            buttonPress[Direction.NORTH.ordinal()] = true;
        } else {
            if(screenX >= 0 && screenX <= 300){
                //left;
                buttonPress[Direction.WEST.ordinal()] = true;
            }else if(screenX <= w && screenX >= w - 300){
                //right
                buttonPress[Direction.EAST.ordinal()] = true;
            } else {
                //down
                buttonPress[Direction.SOUTH.ordinal()] = true;
            }
        }

        if(button == Input.Buttons.LEFT){

            //Get touch input coordinates relative to device's screen
            Vector3 input = new Vector3(screenX, screenY, 0);

            //Unproject input coordinates from screen to our game to retrieve our world coordinates
            Vector3 u = camera.unproject(input,viewport.getScreenX(),
                    viewport.getScreenY(),
                    viewport.getScreenWidth(),
                    viewport.getScreenHeight()
            );

            //Quick debug
            System.out.print("\nDesktop: x,y: " + Math.floor(u.x) + "," + Math.floor(u.y) + "\n");
        }
        return false;
    }
}
