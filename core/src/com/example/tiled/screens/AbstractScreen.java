package com.example.tiled.screens;

import com.badlogic.gdx.Screen;
import com.example.tiled.ExampleTiledGame;

/**
 * Created by: Harrison on 23 Oct 2018
 */

public class AbstractScreen implements Screen {

    private ExampleTiledGame app;

    public AbstractScreen(ExampleTiledGame app) {
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

    public ExampleTiledGame getApp() {
        return app;
    }
}
