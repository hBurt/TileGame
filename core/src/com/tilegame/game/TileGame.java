package com.tilegame.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.tilegame.game.screens.GameScreen;

public class TileGame extends Game {

	private GameScreen screen;

	private AssetManager assetManager;

	@Override
	public void create () {

		//To create textures use: TexturePacker
		assetManager = new AssetManager();
		assetManager.load("textures.atlas", TextureAtlas.class);
		assetManager.finishLoading();

		screen = new GameScreen(this); //must come after asset manager

		this.setScreen(screen);
	}

	@Override
	public void render () {
		//Clear all values in existing buffer
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Set background buffered bit to black
		Gdx.gl.glClearColor(0, 0, 0, 1);

		super.render();
	}


	public GameScreen getScreen() {
		return screen;
	}

	//Returns our assetManager
	public AssetManager getAssetManager() {
		return assetManager;
	}

}
