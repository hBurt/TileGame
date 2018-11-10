package com.rouge.life.handler;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rouge.life.models.Direction;
import com.rouge.life.models.Player;
import com.rouge.life.models.WorldLoader;
import com.rouge.life.pathfinding.SimpleNode;


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

    WorldLoader worldLoader;
    private boolean doWalk = false;

    Array<SimpleNode> nodePath;

    public InputHandler(TiledMap map, Player player, OrthographicCamera camera, Viewport viewport, WorldLoader worldLoader) {
        this.player = player;
        this.camera = camera;
        this.viewport = viewport;
        this.worldLoader = worldLoader;
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
        /*//Grab graphics
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
        }*/
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT){

            //Get touch input coordinates relative to device's screen
            Vector3 input = new Vector3(screenX, screenY, 0);

            //Unproject input coordinates from screen to our game to retrieve our world coordinates
            Vector3 u = camera.unproject(input,
                    viewport.getScreenX(),
                    viewport.getScreenY(),
                    viewport.getScreenWidth(),
                    viewport.getScreenHeight()
            );

            //Save our x and y tile clicks
            int touchTileX = (int) Math.floor(u.x);
            int touchTileY = (int) Math.floor(u.y);

            //Loop through each node in our world &
            // if our clicked tile's x & y coords are equal to a nodes x & y,
            //use that node ID
            for(int i = 0; i < worldLoader.getSimpleNodes().size; i++){
                SimpleNode node = worldLoader.getSimpleNodes().get(i);
                if(node.peek(touchTileX, touchTileY)){
                    //worldLoader.showPath(player.getID(), node.getIndex());
                    nodePath = worldLoader.getPath(player.getID(), node.getIndex());
                }
            }

            //Quick debug
            System.out.print("\nDesktop: x,y: " + Math.floor(u.x) + "," + Math.floor(u.y) + "\n");
        }
        return false;
    }

    private void walkToNode(SimpleNode nextNode, final SimpleNode nnextNode){
        player.walkTo(nextNode.getX(), nextNode.getY());
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                player.walkTo(nnextNode.getX(), nnextNode.getY());
            }
        }, .3f);
    }


    private boolean firstUsed = false;

    int simpleCounter = 1;

    //Update player movement
    public void update(final float delta) {
        //If we have a node path, continue.
        if(nodePath != null && nodePath.size > 1) {
            //If our player node does not equal the end node in the array
            if (player.getNode() != nodePath.get(nodePath.size - 1)) {
                //For each node in our array do something
                for (int i = 0; i < nodePath.size; i++) {
                    //Turn each node red for debug
                    nodePath.get(i).select();
                }

                SimpleNode playerNode = player.getNode();
                final SimpleNode nextNode = nodePath.get(1);

                playerNode.unSelect();

                if (playerNode != nextNode) {
                    if(!firstUsed) {
                        //lets add this counter so we can iterate over the different nodes in the path
                        simpleCounter++;
                        //What's it at?
                        System.out.println(simpleCounter);

                        //Make it not red.
                        playerNode.unSelect();

                        //Traverse
                        for (int i = 1; i < nodePath.size; i++) {
                            final int finalI = i;
                            int lastDIrection;
                            Timer.schedule(new Timer.Task() {
                                               @Override
                                               public void run() {
                                                   if(finalI == nodePath.size - 1){
                                                       System.out.println("\nnode#" + finalI + " ; path complete");
                                                   } else {
                                                       System.out.println("\nnode#" + finalI);
                                                   }
                                                   player.walkTo(nodePath.get(finalI).getX(), nodePath.get(finalI).getY());
                                                   if(finalI == nodePath.size - 1){
                                                       System.out.println("Path complete!");
                                                   }

                                                   Direction currentDir = player.walkToDir(nodePath.get(finalI).getX(), nodePath.get(finalI).getY());


                                                   if(currentDir == Direction.NORTH){
                                                       System.out.println("n");
                                                       updateDirection(Direction.NORTH, delta);
                                                       //After a move, set the camera on our player
                                                       //camera.position.set(player.getX(), player.getY(), 0);
                                                   } else if(currentDir == Direction.SOUTH){
                                                       System.out.println("s");
                                                       updateDirection(Direction.SOUTH, delta);
                                                       //After a move, set the camera on our player
                                                       //camera.position.set(player.getX(), player.getY(), 0);
                                                   } else if(currentDir == Direction.EAST){
                                                       System.out.println("e");
                                                       updateDirection(Direction.EAST, delta);
                                                       //After a move, set the camera on our player
                                                       //camera.position.set(player.getX(), player.getY(), 0);
                                                   } else if(currentDir == Direction.WEST){
                                                       System.out.println("w");
                                                       updateDirection(Direction.WEST, delta);
                                                       //After a move, set the camera on our player
                                                       //camera.position.set(player.getX(), player.getY(), 0);
                                                   }
                                               }
                                           }, delta + .34f * i);    //If this is not set high enough, our player will not move properly.
                        }
                        firstUsed = true;
                    }

                    //player.walkTo(nextNode.getX(), nextNode.getY());

                    //Start walking to the first node in the array
                    /*if (playerNode.getX() == nextNode.getX() && playerNode.getY() == nextNode.getY()) {
                        countDown.setCount(3);
                        player.walkTo(nextNode.getX(), nextNode.getY());
                        //buttonPress[Direction.SOUTH.ordinal()] = true;
                    } else if (playerNode.getX() == nextNode.getX() && playerNode.getY() == nextNode.getY() - 1) {
                        countDown.setCount(3);
                        player.walkTo(nextNode.getX(), nextNode.getY());
                        //buttonPress[Direction.NORTH.ordinal()] = true;
                    } else if (playerNode.getX() == nextNode.getX() + 1 && playerNode.getY() == nextNode.getY()) {
                        countDown.setCount(3);
                        player.walkTo(nextNode.getX(), nextNode.getY());
                        //buttonPress[Direction.WEST.ordinal()] = true;
                    } else if (playerNode.getX() == nextNode.getX() - 1 && playerNode.getY() == nextNode.getY()) {
                        countDown.setCount(3);
                        player.walkTo(nextNode.getX(), nextNode.getY());
                        //buttonPress[Direction.EAST.ordinal()] = true;
                    }*/

                }
                /*if(countDown.isComplete()){
                    walkToNode(nodePath.get(2));
                }*/

            } else {
                firstUsed = false;
            }
            /*if (nodePath.size > 2) {
                if (player.getNode() != nodePath.get(1)) {
                    walkToNode(nodePathOld.get(simpleCounter));
                }
            }*/
        }
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
}
