package states;

import static constants.Constants.GAME_SPEED;
import static constants.Constants.SCREEN_HEIGHT;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import game.actors.Platform;
import game.actors.PlatformBoost;
import game.actors.PlatformFatal;
import game.actors.PlatformNormal;
import game.actors.PlatformShield;
import game.actors.PlatformTransparent;
import game.actors.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * This state represents the Playing State of the Game The main responsibility
 * of this class is to; - create Game Objects - update Game Objects - draw Game
 * Objects Game Objects are for instance; players, enemies, npc's, etc...
 *
 * The PlayState can also be thought off as a blue print where data is loaded
 * into some container from a file or some other type of data storage.
 *
 * It can also be created by some class responsible for object creation and then
 * passed to the PlayState as a parameter. This means all the PlayState has to
 * do is receive a list of objects, store them in some container and then for
 * every object in that container update and render that object.
 *
 * This way you can let the user define different Levels based on what
 * parameters are passed into the PlayState.
 */
public abstract class PlayState extends GameState {

	private Player player;
	private Platform latestJumped;


	private ArrayList<Platform> platforms; 
	private ArrayList<Platform> platformsToRemove; 

	private int		nextPlatformIndex = 0;

	private Image platformFatalPNG;
	private Image platformNormalPNG;
	private Image platformShieldPNG;
	private Image platformBoostPNG;
	private Image platformTransparentPNG;

	public PlayState(GameModel model) {
		super(model);
		loadPlatformImg();

		platforms = new ArrayList<Platform>();
		platformsToRemove = new ArrayList<Platform>();
		player = new Player(model); 
	}

	/**
	 * Draws information text to the screen.
	 */
	@Override
	public void draw(GraphicsContext g) {
		drawBg(g);

		for (Platform platform : platforms) {
			platform.delegate(g);
		}
		player.delegate(g);
	}


	@Override
	public void keyPressed(KeyEvent key) {
		if (key.getCode() == KeyCode.ESCAPE) {
			model.switchState(new MenuState(model));
		}else {
			player.keyPressed(key);
		}
	}

	@Override
	public void keyReleased(KeyEvent key) {
		player.keyReleased(key);
	}

	@Override
	public void update() {	


		player.update(model, platforms);

		
		for (Platform platform : platforms) {
			//check for platforms that left screen
			if (platform.getPositionY()>= SCREEN_HEIGHT) {
				platformsToRemove.add(platform);
			}

			//player platform intersect
			if (player.getPlayerRect().intersects(platform.getRect()) && !player.getJumping() && !player.getSuperJumping()){
				
				if (platform == latestJumped) {
					player.setOnSamePlatform(true);
				} else {
					player.setOnSamePlatform(false);
				}

				setLatestJumped(platform);


				platform.intersect(player);
				
				if (platform.isInteractable()) {
					player.intersect();
					break;
				}



			}
		}

		//removes platforms that have left the screen
		platforms.removeAll(platformsToRemove);

		if (!player.isOnSamePlatform()) {
			//increases score based on high you have traveled.
			if((int)player.getSpeed()>0) {
				player.incScore((int)player.getSpeed());
			}
			//
			if (player.getPixelsJumped() >= GAME_SPEED*30) {
				player.setPixelsJumped(0);
				addPlatforms(GAME_SPEED*30, 0);
			}
		}
	}
	/**
	 * Adds new platforms "above" that will fall down. 
	 * <p>
	 * First parameter "pixelsTraveled" adjusts amount of new platforms to be spawned. 
	 * <p>
	 * Second parameter adjusts platform to spawn further down on the screen.
	 * <p>
	 * Platforms are spawned in a set order, where "nice" platforms (shield, normal and boost) are guaranteed to spawn in a player jumping distance. Inbetween two "nice" platforms there is an "evil" platform (fatal or transparent) randomly spawned.
	 */
	public void addPlatforms(double pixelsTraveled, int yAdjust) {
		int amountOfPlatforms = 5;
		double distanceBetweenPlatforms = GAME_SPEED*15;

		for (int i = 0; i < pixelsTraveled/distanceBetweenPlatforms; i++) {
			if (nextPlatformIndex%amountOfPlatforms == 0) {
				Random rand = new Random();
				platforms.add(new PlatformTransparent(platformTransparentPNG, yAdjust - rand.nextInt((int) distanceBetweenPlatforms)));
			} else if (nextPlatformIndex%amountOfPlatforms==1) {
				platforms.add(new PlatformNormal(platformNormalPNG, yAdjust));
			} else if(nextPlatformIndex%amountOfPlatforms==2){
				platforms.add(new PlatformShield(platformShieldPNG, yAdjust));
			} else if(nextPlatformIndex%amountOfPlatforms==3){
				Random rand = new Random();
				platforms.add(new PlatformFatal(platformFatalPNG, yAdjust - rand.nextInt((int) distanceBetweenPlatforms), model));
			} else {
				platforms.add(new PlatformBoost(platformBoostPNG, yAdjust));
			}

			yAdjust -= distanceBetweenPlatforms;
			nextPlatformIndex++;
		}
		//remembers what platform is next to be spawned.
		nextPlatformIndex%=amountOfPlatforms;
	}



	@Override
	public void activate() {
		addPlatforms(SCREEN_HEIGHT, SCREEN_HEIGHT);
	}

	@Override
	public void deactivate() {
		platforms.clear();
	}

	@Override
	public void loadBgImg (){
		try {
			setBgImg(new Image(new FileInputStream("background_2.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void setLatestJumped(Platform platform) {
		latestJumped = platform;
	}

	public void setPlatformFatalPNG(Image platformFatal) {
		this.platformFatalPNG = platformFatal;
	}

	public void setPlatformNormalPNG(Image platformNormal) {
		this.platformNormalPNG = platformNormal;
	}

	public void setPlatformShield(Image platformShield) {
		this.platformShieldPNG = platformShield;
	}

	public void setPlatformBoost(Image platformBoost) {
		this.platformBoostPNG = platformBoost;
	}
	public void setPlatformTransparent(Image platformTransparent) {
		this.platformTransparentPNG = platformTransparent;
	}

	public abstract void loadPlatformImg();


}




