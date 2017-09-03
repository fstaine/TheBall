package fr.fstaine.theball.physic;

import java.util.Random;

public class Bonus {
    private Pt pos, screenSize;
    private float radius = 30f;
    private final Random rd;

    public Bonus(int sizeX, int sizeY)
    {
        screenSize = new Pt(sizeX, sizeY);
        rd = new Random();
        setRandomPosition();
    }

    public void setRandomPosition() {
        pos = new Pt(rd.nextFloat() * screenSize.x, rd.nextFloat() * screenSize.y);
    }

    public Pt getPosition() {
        return pos;
    }

    public float getPosX() {
        return pos.x;
    }

    public float getPosY() {
        return pos.y;
    }

    public float getRadius() {
        return radius;
    }

    public void setHeight(float y)
    {
        this.screenSize.y = y;
    }

    public void setWidth(float x)
    {
        this.screenSize.x = x;
    }
}
