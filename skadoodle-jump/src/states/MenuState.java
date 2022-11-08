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
 * This state represents the menu of the Game The main responsibility of this
 * class is to allow the user to swap state to the PlayState
 */
public class MenuState extends GameState {
	/*
	 * The following three variables are just used to show that a change of state
	 * can be made. The same variables also exist in the PlayState, can you think of
	 * a way to make this more general and not duplicate variables?
	 */
	private String informationText1;
	private String informationText2;
	private Color fontColor;

	public MenuState(GameModel model) {
		super(model);

		informationText1 = "High score: " + model.getHC();
		informationText2 = "Move: L/R arrow\nPowerdive: down arrow";

		fontColor = Color.LIME;
	}

	/**
	 * Draws information text to the screen
	 */
	@Override
	public void draw(GraphicsContext g) {

		drawBg(g);

		g.setFill(fontColor);
		g.setFont(new Font(30)); // Small letters
		g.fillText(informationText1, SCREEN_WIDTH / 4, SCREEN_HEIGHT -40);
		g.setFont(new Font(15)); // Big letters
		g.fillText(informationText2, SCREEN_WIDTH / 3, SCREEN_HEIGHT -250);

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
				model.setLatestWasPS1(true);
				model.setLatestWasPS2(false);
				model.switchState(new PlayStateMap1(model));
			} else if (click.getY() >= 255 && click.getY() <= 300) {
				model.setLatestWasPS1(false);
				model.setLatestWasPS2(true);
				model.switchState(new PlayStateMap2(model));
			}

		}
	}

	@Override
	public void keyPressed(KeyEvent key) {
		if (key.getCode() == KeyCode.ENTER) {
						model.switchState(new PlayStateMap1(model));
		} else if (key.getCode() == KeyCode.TAB) {
						model.switchState(new PlayStateMap2(model));
		} else if (key.getCode() == KeyCode.ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent key) {
		;
	}

	/**
	 * We have nothing to update in the menu, no moving objects etc. so we leave the
	 * update method empty.
	 */
	@Override
	public void update() {
	}

	@Override
	public void loadBgImg (){
		try {
			setBgImg(new Image(new FileInputStream("menu_background.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
