package states;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
/**
 * Map 2, with unique graphics from map 1.
 */

public class PlayStateMap2 extends PlayState {

	public PlayStateMap2(GameModel model) {
		super(model);
	}
	
	@Override
	public void loadBgImg (){
		 try {
			setBgImg(new Image(new FileInputStream("background_2.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadPlatformImg() {
		try {
			setPlatformFatalPNG(new Image(new FileInputStream("platformFatal2.png")));
			setPlatformNormalPNG(new Image(new FileInputStream("platformNormal2.png")));
			setPlatformShield(new Image(new FileInputStream("platformShield2.png")));
			setPlatformBoost(new Image(new FileInputStream("platformBoost2.png")));
			setPlatformTransparent(new Image(new FileInputStream("platformTransparent2.png")));
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find image-files!");
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent click) {
	}
}
