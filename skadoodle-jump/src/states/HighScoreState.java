package states;

import static constants.Constants.SCREEN_HEIGHT;
import static constants.Constants.SCREEN_WIDTH;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
/**
 * Displays highscore
 */
public class HighScoreState extends GameState {

	private String informationText;
	private Color fontColor;

	public HighScoreState(GameModel model, Integer score) {
		super(model);

		if (model.getLatestWasHC()) {
			informationText = "New high score: " + model.getHC() + "\n\nPress Enter to play or\nescape to get to menu";;
			model.setFalseLatestWasHC();
		}else {
			if(score != null) {
				informationText = "Your score: " + score + " \nHigh score: " + model.getHC() + " \n\nPress Enter to play or\nescape to get to menu";

			}else {
				informationText = "High score: " + model.getHC() + "\n\nPress Enter to play or\nescape to get to menu";
			}
		}

		fontColor = Color.LIME;
	}

	/**
	 * Draws information text to the screen
	 */
	@Override
	public void draw(GraphicsContext g) {
		drawBg(g);

		g.setFill(fontColor);
		g.setFont(new Font(25)); // Big letters
		// Print the information text, centered on the canvas
		g.fillText(informationText, SCREEN_WIDTH / 4 + 20, SCREEN_HEIGHT -40);

	}

	/**
	 *
	 * @param key KeyEvent representing the pressed key
	 *
	 *            This function prints the pressed key to the console it's used to
	 *            show that a change of state has been made
	 *
	 *            For more information see GameState
	 */
	@Override
	public void mouseClicked(MouseEvent click) {
		if (click.getX() >= 175  && click.getX() <= 320) {
			if (click.getY() >= 170 && click.getY() <= 215) {
				if (model.isLatestWasPS1()) {
					model.switchState(new PlayStateMap1(model));
				}else if (model.isLatestWasPS2()) {
					model.switchState(new PlayStateMap2(model));
				}
			} else if (click.getY() >= 255 && click.getY() <= 300) {
				model.switchState(new MenuState(model));
			}
		}
	}


	@Override
	public void keyPressed(KeyEvent key) {
		if (key.getCode() == KeyCode.ENTER) {
			if (model.isLatestWasPS1()) {
				model.switchState(new PlayStateMap1(model));
			}else if (model.isLatestWasPS2()) {
				model.switchState(new PlayStateMap2(model));
			}		} else if (key.getCode() == KeyCode.ESCAPE) {
			model.switchState(new MenuState(model));
		}
	}

	@Override
	public void keyReleased(KeyEvent key) {
	}

	@Override
	public void loadBgImg (){
		try {
			setBgImg(new Image(new FileInputStream("HC_background.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * We have nothing to update in the menu, no moving objects etc. so we leave the
	 * update method empty.
	 */
	@Override
	public void update() {
	}

	/**
	 * We currently don't have anything to activate in the MenuState so we leave
	 * this method empty in this case.
	 */
	@Override
	public void activate() {

	}

	/**
	 * We currently don't have anything to deactivate in the MenuState so we leave
	 * this method empty in this case.
	 */

	@Override
	public void deactivate() {

	}


}
