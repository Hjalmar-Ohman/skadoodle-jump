package game.actors;

import static constants.Constants.SCREEN_WIDTH;

import java.util.Random;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
/**
 * The super class for all platforms.
 *
 * Responsible for drawing and keeping track of the platform's position.
 */
public abstract class Platform {
	private Point position;
	private Image image;
	private double gameSizeX;
	private double gameSizeY;
	private Rectangle2D platformRect;
	private boolean interactable;

	public class Point {
		double x;
		double y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	public Platform(Image image, int yPos) {
		this.image = image;
		double PNGtoSizeRatio = 15;
		gameSizeX = image.getWidth()/PNGtoSizeRatio;
		gameSizeY = image.getHeight()/PNGtoSizeRatio;

		this.setInteractable(true);

		Random randX = new Random();
		this.position = new Point(randX.nextInt(SCREEN_WIDTH - (int) gameSizeX), yPos - gameSizeY);
	}

	public void delegate(GraphicsContext g) {
		if (g != null) {
			draw(g);
		}	
	}
	private void draw(GraphicsContext g) {
		g.drawImage(image, position.x , position.y, gameSizeX, gameSizeY);

		platformRect = new Rectangle2D(position.x, position.y, gameSizeX*0.75, 30);
	}

	public void lower(double speed) {
		position.y += speed;
	}

	public Rectangle2D getRect() {
		return platformRect;
	}


	public double getPositionY() {
		return position.y;
	}

	public abstract void intersect(Player player);


	public boolean isInteractable() {
		return interactable;
	}



	public void setInteractable(boolean interactable) {
		this.interactable = interactable;
	}
}
