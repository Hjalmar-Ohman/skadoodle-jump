package game.actors;

import javafx.scene.image.Image;
/**
 * Uninteractable (teractable?) platform. See player.java for animation and logic
 */
public class PlatformTransparent extends Platform {

	public PlatformTransparent(Image image, int yAdjust) {
		super(image, yAdjust);
		setInteractable(false);
	}

	@Override
	public void intersect(Player player) {

	}

}
