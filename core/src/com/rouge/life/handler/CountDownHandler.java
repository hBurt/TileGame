package com.rouge.life.handler;

import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * Created by: Harrison on 08 Nov 2018
 */
public class CountDownHandler extends Action {

    private int count;

    @Override
    public boolean act(float delta) {
        --count;
        return true;
    }

    public void setCount(int value) { count = value; }

    public int getCount() {
        return count;
    }

    public boolean isComplete() { return count == 0; }
}
