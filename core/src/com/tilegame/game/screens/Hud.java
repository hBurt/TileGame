package com.tilegame.game.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tilegame.game.TileGame;

/**
 * Created by: Harrison on 26 Oct 2018
 */
public class Hud {
    public Stage stage;
    private Viewport viewport;

    private Vector2 v2;
    private int worldTimer;
    private float timeCount;
    private int score;

    Label worldLabel;

    public Hud(SpriteBatch sb){
        worldTimer = 0;
        timeCount = 0;

        viewport = new FitViewport(TileGame.V_WIDTH, TileGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        worldLabel = new Label("Words", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(worldLabel).expandX().padTop(10);
        table.debug();

        stage.addActor(table);

        v2 = new Vector2();
    }

    public void update(float delta){

    }

    public void setV2(Vector2 v2) {
        this.v2 = v2;
    }

    public Vector2 getV2() {
        return v2;
    }
}
