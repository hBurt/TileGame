package com.example.tiled.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.example.tiled.ExampleTiledGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.allowSoftwareMode = true;
		config.vSyncEnabled = true;
		new LwjglApplication(new ExampleTiledGame(), config);
	}
}
