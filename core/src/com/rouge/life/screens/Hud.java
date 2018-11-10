package com.rouge.life.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rouge.life.RougeLife;
import com.rouge.life.models.Player;


/**
 * Created by: Harrison on 26 Oct 2018
 */
public class Hud {
    public Stage stage;
    private Viewport viewport;

    private Vector2 v2;

    Label worldLabel, worldLabel2, worldLabel3, worldLabel4, worldLabel5;

    public Hud(SpriteBatch sb){

        viewport = new FitViewport(RougeLife.V_WIDTH, RougeLife.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top().left();
        table.setFillParent(true);

        worldLabel = new Label("Stage: World 1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel2 = new Label("Delta Time: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel3 = new Label("Position: X: , Y: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel4 = new Label("Screen Width: , Height: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel5 = new Label("Player is walking? :  ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(worldLabel).align(Align.left).padLeft(20).padTop(10);
        table.row();
        table.add(worldLabel2).align(Align.left).padLeft(20);
        table.row();
        table.add(worldLabel3).align(Align.left).padLeft(20);
        table.row();
        table.add(worldLabel4).align(Align.left).padLeft(20);
        table.row();
        table.add(worldLabel5).align(Align.left).padLeft(20);
        table.debug();

        stage.addActor(table);

        v2 = new Vector2();
    }

    public void update(float delta, Player player){
        worldLabel2.setText("Delta: " + delta);
        if(player != null) {
            worldLabel3.setText("Position: X: " + player.getX() + ", Y: " + player.getY());
            worldLabel4.setText("Last Collision: X: " + player.getColideX() + "Y: " + player.getColideY());
            worldLabel5.setText("Player is walking? :  " + player.isWalking());
        }
    }

    public void setV2(Vector2 v2) {
        this.v2 = v2;
    }

    public Vector2 getV2() {
        return v2;
    }
}
