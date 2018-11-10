package com.rouge.life.models;


import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rouge.life.RougeLife;
import com.rouge.life.Settings;
import com.rouge.life.handler.WorldHandler;
import com.rouge.life.pathfinding.ManhattanDistanceHeuristic;
import com.rouge.life.pathfinding.NodeGraph;
import com.rouge.life.pathfinding.SimpleNode;


/**
 * Created by: Harrison on 02 Nov 2018
 */
public class WorldLoader {

    private TmxMapLoader mapLoader;
    private TiledMap map;

    private OrthographicCamera camera;
    private Viewport viewport;

    private OrthogonalTiledMapRenderer renderer;

    private Player player;

    private WorldHandler worldHadler;

    private Array<SimpleNode> simpleNodes;

    BitmapFont font = new BitmapFont();

    //Handle delta time
    float delta;

    //Node Handling
    IndexedAStarPathFinder<SimpleNode> mPathFinder;

    NodeGraph mGraph;

    /** This is where the solution will end up. */
    DefaultGraphPath<SimpleNode> mPath;

    /** This is a Heuristic function that will estimate
     * how close the current node is to the end. */
    ManhattanDistanceHeuristic mHeuristic;


    public WorldLoader(String tmxName, RougeLife app){

        //Init a new map loader for our tmx
        mapLoader = new TmxMapLoader();

        //Set the tmx into the TiledMap
        map = mapLoader.load(tmxName);

        //Load our tmx and set our scale
        renderer = new OrthogonalTiledMapRenderer(getMap(), 1/16f);

        //Create a Orthographic cam
        camera = new OrthographicCamera();

        //Create a new viewport ser to fixed size.
        viewport = new FitViewport(RougeLife.V_WIDTH, RougeLife.V_HEIGHT, camera);

        //Properly scale our camera
        camera.setToOrtho(false,
                (viewport.getWorldWidth() / Settings.tileSize) / Settings.gameScale,
                (viewport.getWorldHeight() / Settings.tileSize) / Settings.gameScale);

        //Zoom in our camera by the scaled amount
        camera.zoom /= Settings.gameScale * Settings.tileSize; //This is what we use to zoom our game

        worldHadler = new WorldHandler(tmxName, app);

        //Get our nodes
        simpleNodes = worldHadler.getGraph().getSimpleNodes();

        //We have our nodes at this point
        mGraph = new NodeGraph(simpleNodes);
        mPath = new DefaultGraphPath<SimpleNode>();
        mHeuristic = new ManhattanDistanceHeuristic();

        //System.out.print(simpleNodes.size);
        initNodes();
        //testPath();
    }

    /**
     * Add connections to the node at aX aY. If there is no node present at those
     * coordinates no connection will be created.
     * @param aNode The node to connect from.
     * @param aX x coordinate of connecting node.
     * @param aY y coordinate of connecting node.
     */
    private void addNodeNeighbour(SimpleNode aNode, int aX, int aY) {
        // Make sure that we are within our array bounds
        if (aX >= 0 && aX < 25 && aY >=0 && aY < 25) {

            //For each node in the simple nodes array
            for(int i = 0; i < simpleNodes.size; i++){

                //Does node = node?
                if (simpleNodes.get(i).getX() == aX && simpleNodes.get(i).getY() == aY) {

                    //Quick Debug
                    //System.out.print("Adding " + simpleNodes.get(i).getIndex() + " to " + aNode.getIndex() + "\n");

                    //Add node with coordinates (aX, aY)
                    aNode.addNeighbour(simpleNodes.get(i));
                }
            }
        }
    }

    //Warning: Make sure the nodes that get added are not in on the collision layer
    private void initNodes(){

        //For each node in our array, add it's connected nodes
        for(int ii = 0; ii < simpleNodes.size; ii++){
            //Add connection between ( node(eg. aNode), &
            addNodeNeighbour(simpleNodes.get(ii), simpleNodes.get(ii).getX() - 1, simpleNodes.get(ii).getY());
            addNodeNeighbour(simpleNodes.get(ii), simpleNodes.get(ii).getX() + 1, simpleNodes.get(ii).getY());
            addNodeNeighbour(simpleNodes.get(ii), simpleNodes.get(ii).getX(), simpleNodes.get(ii).getY() + 1);
            addNodeNeighbour(simpleNodes.get(ii), simpleNodes.get(ii).getX(), simpleNodes.get(ii).getY() - 1);
            //System.out.println(simpleNodes.get(ii).toString() + " : Init size = " + simpleNodes.get(ii).getConnections().size + "\n");
        }

        mPathFinder = new IndexedAStarPathFinder<SimpleNode>(mGraph, true);
    }

    public Array<SimpleNode> getPath(int startNodeID, int endNodeID){
        SimpleNode startNode = simpleNodes.get(startNodeID);  //below
        SimpleNode endNode = simpleNodes.get(endNodeID);   //above

        clearSelects();

        mPath.clear();

        mPathFinder.searchNodePath(startNode, endNode, mHeuristic, mPath);

        return mPath.nodes;
    }

   public void showPath(int startNodeID, int endNodeID){
       SimpleNode startNode = simpleNodes.get(startNodeID);  //below
       SimpleNode endNode = simpleNodes.get(endNodeID);   //above

       clearSelects();

       mPath.clear();

       mPathFinder.searchNodePath(startNode, endNode, mHeuristic, mPath);

       if (mPath.nodes.size == 0) {
           System.out.println("-----No Custom path found-----");
       } else {
           System.out.println("-----Custom Found path-----");
       }

       //SimpleNode lastNode = new SimpleNode(-1,-1,-1);

       for (SimpleNode node : mPath.nodes) {
           node.select();
           System.out.println(node);
       }

        /*
       for(int i = 0; i < mPath.getCount(); i++){
           if(i < mPath.getCount() - 1) {
               SimpleNode node = mPath.get(i);
               player.walkTo(node.getX(), node.getY());
               countDown.setCount(30); // 3 seconds
               if(countDown.isComplete()){
                   SimpleNode node2 = mPath.get(i + 1);
                   player.walkTo(node2.getX(), node2.getY());
               }
           }
       }*/
   }

   private void clearSelects(){
       for (SimpleNode node : mPath.nodes) {
           node.setSelected(false);
       }
   }

    private void testPath(){

        SimpleNode startNode = simpleNodes.get(43);  //below
        SimpleNode endNode = simpleNodes.get(194);   //above

        mPath.clear();

        mPathFinder.searchNodePath(startNode, endNode, mHeuristic, mPath);

        if (mPath.nodes.size == 0) {
            System.out.println("-----No path found-----");
        } else {
            System.out.println("-----Found path-----");
        }

        for (SimpleNode node : mPath.nodes) {
            node.select();
            System.out.println(node);
        }

    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void render(SpriteBatch batch){
        //Display the tileMap
        getRenderer().render();


        //Grab our world coordinates
        float worldStartX = getViewport().getWorldWidth() / 2f - getCamera().position.x * 32f;
        float worldStartY = getViewport().getWorldHeight() / 2f - getCamera().position.y * 32f;

        batch.begin();
        if(player != null) {
            batch.draw(player.getTexture(),
                    worldStartX + player.getWorldX() * 32f, //Players x
                    worldStartY + player.getWorldY() * 32f, //Players Y
                    Settings.gameScale * Settings.tileSize, //tile size
                    Settings.gameScale * Settings.tileSize  //tile size
            );
        }

        font.setColor(Color.BLACK);
        for (SimpleNode s: simpleNodes) {
            //Draw an example of our nodes
            if(s.isSelected()){
                font.setColor(Color.RED);
            } else {
                font.setColor(Color.BLACK);
            }
            font.draw(batch, String.valueOf(s.getIndex()),
                    (worldStartX + s.getX() * 64f)
                            - (Settings.gameScale / 2 * Settings.tileSize)
                            - ((player.getWorldX() - 2) * 32) - 12,
                    (worldStartY + s.getY() * 64f) - (Settings.gameScale / 2 * Settings.tileSize) - ((player.getWorldY() - 2) * 32)
            );
        }
        batch.end();

    }

    public void update(float delta){

        this.delta = delta;

        //Interpolate(move/tween/etc..) the player
        player.update(delta);

        //Position our camera on player every frame
        getCamera().position.set(player.getWorldX(), player.getWorldY(), 0);

        //Update our camera after inputs
        getCamera().update();

        //update camera every frame
        getRenderer().setView(getCamera());


    }

    public Array<SimpleNode> getSimpleNodes() {
        return simpleNodes;
    }

    public void addPlayer(Player p){
        this.player = p;
    }
    public TiledMap getMap() {
        return map;
    }

    public OrthogonalTiledMapRenderer getRenderer(){
        return renderer;
    }

    public void set(String tmxName){
        this.map = mapLoader.load(tmxName);
    }
}
