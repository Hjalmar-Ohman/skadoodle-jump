package game.actors;

import javafx.scene.image.Image;
/**
 * Makes the player jump extra high. See player.java for animation and logic
 */
public class PlatformBoost extends Platform {

	public PlatformBoost(Image image, int yAdjust) {
		super(image, yAdjust);
	}

	@Override
	public void intersect(Player player) {
		player.setAngleZero();
		player.setBoosted(true);
	}

}
