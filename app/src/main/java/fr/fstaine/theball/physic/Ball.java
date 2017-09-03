package fr.fstaine.theball.physic;

import android.util.Log;

public class Ball {
	private final int radius = 30;
	private final float dimi = 5f;
	private final float maxSpeed = 20f;
	private final float bounceReduction = 0.2f;

	private Pt screenSize;
	private Pt pos;
	private Pt speed;

    private int score = 0;

	public Ball(int sizeX, int sizeY)
	{
		screenSize = new Pt(sizeX, sizeY);
		pos = new Pt(sizeX/2, sizeY/2);
		speed = new Pt(0f, 0f);
	}

	public Pt getPosition() {
		return pos;
	}

	public float getPosX() {
		return pos.x;
	}

	public void setPosX(float posX) {
		if(posX < radius) {
			this.pos.x = radius;
			this.speed.x *= -bounceReduction;
		}
		else if(posX > screenSize.x - radius) {
			this.pos.x = screenSize.x - radius;
			this.speed.x *= -bounceReduction;
		}
		else {
			this.pos.x = posX;
		}
	}

	public float getPosY() {
		return pos.y;
	}

	public void setPosY(float posY) {
		if(posY < radius)
		{
			this.pos.y = radius;
			this.speed.y *= -bounceReduction;
		}
		else if(posY > screenSize.y - radius)
		{
			this.pos.y = screenSize.y - radius;
			this.speed.y *= -bounceReduction;
		}
		else
		{
			this.pos.y = posY;
		}
	}

	public void setHeight(float y)
	{
		this.screenSize.y = y;
	}

	public void setWidth(float x)
	{
		this.screenSize.x = x;
	}

	public int getRadius()
	{
		return this.radius;
	}

	public void setAcceleration(float pX, float pY) {
		speed.x -= pX / dimi;
		if(speed.x > maxSpeed)
			speed.x = maxSpeed;
		if(speed.x < -maxSpeed)
			speed.x = -maxSpeed;

		speed.y += pY / dimi;
		if(speed.y > maxSpeed)
			speed.y = maxSpeed;
		if(speed.y < -maxSpeed)
			speed.y = -maxSpeed;

		setPosX(pos.x + speed.x);
		setPosY(pos.y + speed.y);
	}

    /**
     * Called when the player caught a ball
     */
    public void catchBall() {
        // TODO: set 10 as a param
        score += 10;
        Log.d("Score", "Got a ball");
    }

    public int getScore() {
        return score;
    }
}
