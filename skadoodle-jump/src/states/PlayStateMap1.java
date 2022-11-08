package states;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
/**
 * Map 1, with unique graphics from map 2.
 */
public class PlayStateMap1 extends PlayState {

	public PlayStateMap1(GameModel model) {
		super(model);
	}

	@Override
	public void loadBgImg (){
		try {
			setBgImg(new Image(new FileInputStream("background_1.jpeg")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void loadPlatformImg(){
		try {
			setPlatformFatalPNG(new Image(new FileInputStream("platformFatal.png")));
			setPlatformNormalPNG(new Image(new FileInputStream("platformNormal.png")));
			setPlatformShield(new Image(new FileInputStream("platformShield.png")));
			setPlatformBoost(new Image(new FileInputStream("platformBoost.png")));
			setPlatformTransparent(new Image(new FileInputStream("platformTransparent.png")));
		} catch (FileNotFoundException e) {
			System.out.println("Unable to find image-files!");
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent click) {
	}
}