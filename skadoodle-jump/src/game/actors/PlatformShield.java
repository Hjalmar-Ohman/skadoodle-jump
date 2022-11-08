package game.actors;

import javafx.scene.image.Image;
/**
 * Shields the player from fatal platforms. See player.java for animation and logic
 */
public class PlatformShield extends Platform {


	public PlatformShield(Image image, int yAdjust) {
		super(image, yAdjust);
	}

	@Override
	public void intersect(Player player) {
		player.setJump(true);
		player.setShielded(true);
	}

}
