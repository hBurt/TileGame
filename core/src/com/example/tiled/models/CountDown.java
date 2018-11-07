package com.example.tiled.models;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * Created by: Harrison on 07 Nov 2018
 */
public class CountDown extends Action {
    private int count;

    @Override
    public boolean act(float delta) {
        --count;
        return true;
    }

    public void setCount(int value) { count = value; }

    public boolean isComplete() { return count == 0; }
}
