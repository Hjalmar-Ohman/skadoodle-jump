package game.actors;

import javafx.scene.image.Image;
import states.GameModel;
import states.HighScoreState;
/**
 * Kills the player if not shielded. See player.java for animation and logic
 */

public class PlatformFatal extends Platform {

	private GameModel model;

	public PlatformFatal(Image image, int yAdjust, GameModel model) {
		super(image, yAdjust);
		this.model = model;
	}

	@Override
	public void intersect(Player player) {
		if (!player.getShielded()) {
			model.updateHC(player.getScore());
			model.switchState(new HighScoreState(model, player.getScore()));
		} else {
			player.setShielded(false);
		}		

		player.setJump(true);
	}

}
