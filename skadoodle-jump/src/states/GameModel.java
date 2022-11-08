package states;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * This class represents the current state of the game.
 *
 * This implementation of a state machine keeps a reference to the current state
 * (see /src/states/GameState).
 *
 * To change state simply call the switchState(GameState nextState) function
 * passing a reference to some other gameState.
 *
 * Initial State: MenuState
 *
 */

public class GameModel {

	private GameState currentState;
	private int highScore;
	private boolean latestWasHC = false;
	private boolean latestWasPS1 = true; 
	private boolean latestWasPS2 = false;

	private ArrayList<Image> playerImages;

	public GameModel() {
		// We start out in the MenuState.
		loadPlayerImages();

		this.currentState = new MenuState(this);


	}


	/**
	 * Switch to a new state, stored in the 'state' reference.
	 *
	 * This will call 'deactivate' on the current state, then store the new state as
	 * the current state, and finally call 'activate' on the new current state.
	 */
	public void switchState(GameState nextState) {
		this.currentState.deactivate();
		this.currentState = nextState;
		this.currentState.activate();
	}

	/**
	 * Delegates the keyPress from GamePanel to the current state
	 * 
	 * @param key
	 */
	public void mouseClicked(MouseEvent click) {
		currentState.mouseClicked(click);
	}

	public void keyPressed(KeyEvent key) {
		currentState.keyPressed(key);
	}

	public void keyReleased(KeyEvent key) {
		currentState.keyReleased(key);
	}

	/**
	 * The update function is called every iteration of the game loop. it's usually
	 * used to update the games logic e.g. objects position, velocity, etc...
	 */
	public void update() {
		currentState.update();
	}

	/**
	 * @param g Graphics object passed from GamePanel This function delegates
	 *          drawing from the GamePanel to the current state
	 */
	public void draw(GraphicsContext g) {
		currentState.draw(g);
	}

	private void loadPlayerImages() {
		playerImages = new ArrayList<Image>();
		try {
			this.playerImages.add(new Image(new FileInputStream("right_jumping.png")));
			this.playerImages.add(new Image(new FileInputStream("right_falling.png")));
			this.playerImages.add(new Image(new FileInputStream("right_super.png")));
			this.playerImages.add(new Image(new FileInputStream("left_jumping.png")));
			this.playerImages.add(new Image(new FileInputStream("left_falling.png")));
			this.playerImages.add(new Image(new FileInputStream("left_super.png")));
			this.playerImages.add(new Image(new FileInputStream("playerShield.png")));
			this.playerImages.add(new Image(new FileInputStream("power_fall.png")));
			this.playerImages.add(new Image(new FileInputStream("power_fall2.png")));
			this.playerImages.add(new Image(new FileInputStream("power_fall3.png")));



		} catch (FileNotFoundException e) {
			System.out.println("Unable to find image-files!");
		}		
	}



	public void updateHC(int newHC) {

		ObjectOutputStream out;

		if (newHC > highScore) {
			latestWasHC = true;
			try {
				File fileHC = new File("high_score.txt");

				if (fileHC != null) {
					out = new ObjectOutputStream(
							new FileOutputStream(fileHC));
					out.writeObject(newHC);
					out.close();
				}} catch (IOException ioe) {
					ioe.printStackTrace();
				}
		}
	}

	public int getHC() {
		ObjectInputStream in;

		try {
			File fileHC = new File("high_score.txt");
			if (fileHC != null) {
				in = new ObjectInputStream(
						new FileInputStream(fileHC));
				try {

					highScore = (int)in.readObject();

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				in.close();
				return highScore;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return highScore;
	}

	public boolean getLatestWasHC() {
		return latestWasHC;
	}

	public void setFalseLatestWasHC() {
		latestWasHC = false;
	}

	public ArrayList<Image> getPlayerImages() {
		return playerImages;
	}


	public boolean isLatestWasPS1() {
		return latestWasPS1;
	}


	public void setLatestWasPS1(boolean latestWasPS1) {
		this.latestWasPS1 = latestWasPS1;
	}


	public boolean isLatestWasPS2() {
		return latestWasPS2;
	}


	public void setLatestWasPS2(boolean latestWasPS2) {
		this.latestWasPS2 = latestWasPS2;
	}
}
