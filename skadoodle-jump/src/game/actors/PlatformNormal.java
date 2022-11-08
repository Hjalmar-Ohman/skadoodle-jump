package game.actors;

import javafx.scene.image.Image;
/**
 * Platform with no extra feature.
 */
public class PlatformNormal extends Platform {

	public PlatformNormal(Image image, int yAdjust) {
		super(image, yAdjust);
	}

	@Override
	public void intersect(Player player) {
		player.setJump(true); 
	}

}
