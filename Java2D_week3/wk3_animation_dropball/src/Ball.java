import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class Ball {
	private Point2D position;
	private Point2D speed;
	private double angleInDegrees;

	private float size = 100;

	public Ball(Point2D position, Point2D speed, float angleInDegree)
	{
		this.position = position;
		this.speed = new Point2D.Double(speed.getX()*Math.cos(Math.toRadians(angleInDegree)),
				speed.getY()*Math.cos(Math.toRadians(angleInDegree)));
		this.angleInDegrees = angleInDegree;
	}


	public void update(Point2D maxSize) {

		// Get the ball's bounds, offset by the radius of the ball
		double ballMinX = 0 + size/2;
		double ballMinY = 0 + size/2;
		double ballMaxX = maxSize.getX() - size/2;
		double ballMaxY = maxSize.getY() - size/2;

		// Calculate the ball's new position
		Point2D oldPos = this.position;
		this.position = new Point2D.Double(this.position.getX()+this.speed.getX(),
												 this.position.getY()+this.speed.getY());
		// Check if the ball moves over the bounds. If so, adjust the position and speed.
		if (this.position.getX() < ballMinX) {
			this.speed.setLocation(-this.speed.getX(), this.speed.getY()); // Reflect along normal
			this.position.setLocation(ballMinX, this.position.getY());
		} else if (this.position.getX() > ballMaxX) {
			this.speed.setLocation(-this.speed.getX(), this.speed.getY()); // Reflect along normal
			this.position.setLocation(ballMaxX, this.position.getY());
		}
		// May cross both x and y bounds
		if (this.position.getY() < ballMinY) {
			this.speed.setLocation(this.speed.getX(), -this.speed.getY()); // Reflect along normal
			this.position.setLocation(this.position.getX(), ballMinY);
		} else if (this.position.getY() > ballMaxY) {
			this.speed.setLocation(this.speed.getX(), -this.speed.getY()); // Reflect along normal
			this.position.setLocation(this.position.getX(), ballMaxY);
		}
	}

	public void draw(FXGraphics2D g2d) {
		float[] fractions = { 0.0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};
		Color[] colors = new Color[fractions.length];
		for(int i = 0; i < colors.length; i++)
			colors[i] = Color.getHSBColor(fractions[i], 1.0f, 1.0f);

		g2d.setPaint(new LinearGradientPaint(
				(float)position.getX()-size/2, (float)position.getY()-size/2,
				(float)position.getX()+size/2, (float)position.getY()+size/2,
				fractions, colors));
		g2d.fill(new Ellipse2D.Double(position.getX()-size/2, position.getY()-size/2, size, size));
		g2d.setPaint(Color.black);
	}

	/** Return the magnitude of speed. */
	public double getSpeed() {
		return Math.sqrt(this.speed.getX() * this.speed.getX() + this.speed.getY() * this.speed.getY());
	}

	/** Return the direction of movement in degrees (counter-clockwise). */
	public double getMoveAngle() {
		return Math.toDegrees(Math.atan2(-this.speed.getY(), this.speed.getX()));
	}

	/** Return mass */
	public double getMass() {
		double radius = size/2;
		return radius * radius * radius / 1000.0;  // Normalize by a factor
	}

	/** Return the kinetic energy (0.5mv^2) */
	public double getKineticEnergy() {
		return 0.5 * getMass() * (this.speed.getX() * this.speed.getX() + this.speed.getY() * this.speed.getY());
	}

	/** Describe itself. */
	public String toString() {
		return String.format("@(%3.0f,%3.0f) r=%3.0f V=(%2.0f,%2.0f) " +
						"S=%4.1f \u0398=%4.0f KE=%3.0f",
				this.position.getX(), this.position.getY(), this.size/2,
				this.speed.getX(), this.speed.getY(), getSpeed(), getMoveAngle(),
				getKineticEnergy());  // \u0398 is theta
	}


}
