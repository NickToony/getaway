package com.nicktoony.getaway.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nicktoony.getaway.components.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Nick on 03/09/2015.
 */
public class PoliceManager extends Entity {

    private static final int DELAY_MIN = 20;
    private static final int DELAY_MAX = 100;
    private static final float SPACE_BETWEEN = 0.3f;

    private int lastPolice = 100;
    private Random random;
    private List<Police> policeList = new ArrayList<Police>();

    @Override
    public void create() {
        random = new Random();
    }

    @Override
    public void step() {
        if (lastPolice == 0) {
            lastPolice = random.nextInt(DELAY_MAX - DELAY_MIN) + DELAY_MIN;

            int spawns = 3;
            while (spawns > 0) {
                float x = random.nextFloat()*2 - 1;
                boolean okay = true;
                for (Police police : policeList) {
                    if (Math.abs(x - police.getX()) < SPACE_BETWEEN) {
                        okay = false;
                    }
                }

                if (Math.abs(x - game.getPlayer().getXPercentage()) < SPACE_BETWEEN) {
                    okay = false;
                }

                if (okay) {
                    Police police = new Police(x);
                    policeList.add(police);
                    game.addEntity(police);
                    spawns -= 1;
                }
            }

        } else if (policeList.isEmpty()) {
            lastPolice -= 1;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // doesn't render anything
    }

    @Override
    public void dispose() {

    }

    public void remove(Police police) {
        policeList.remove(police);
    }
}
