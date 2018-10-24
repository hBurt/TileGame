package com.tilegame.game.screens;

import com.badlogic.gdx.Screen;
import com.tilegame.game.TileGame;

/**
 * Created by: Harrison on 23 Oct 2018
 */

public class AbstractScreen implements Screen {

    private TileGame app;

    public AbstractScreen(TileGame app) {
        this.app = app;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public TileGame getApp() {
        return app;
    }
}
