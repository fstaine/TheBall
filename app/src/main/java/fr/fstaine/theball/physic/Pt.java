package fr.fstaine.theball.physic;

public class Pt{
	float x;
	float y;

	public Pt(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public float distance(Pt position) {
		return (float) Math.sqrt(Math.pow(this.x - position.x, 2) + Math.pow(this.y - position.y, 2));
	}
}
