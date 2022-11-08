package game.actors;

import static constants.Constants.GAME_SPEED;
import static constants.Constants.SCREEN_HEIGHT;
import static constants.Constants.SCREEN_WIDTH;

import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import states.GameModel;
import states.HighScoreState;
/**
 * This class represents the player, a.k.a. the Mushroom. When the the game 
 * ends (i.e. the player dies), HighScoreState.java is instantiated and 
 * becomes the new "current state" via the GameModel that the constructor
 * for Player.java takes in. The next game, a new player will be instantiated.
 * 
 * In the class, several booleans are defined. 
 * They define the players moving direction, if she is jumping or falling,
 * if she is shielded, boosted, accelerated, etc.
 * 
 * The class also defines several integers and doubles:
 * player speed (imported from Constants.java), player position, gravity, 
 * left and right acceleration, the players score, and so on. 
 * 
 * The class is responsible for drawing itself and it stores all 
 * player images in an arrayList that is imported from GameModel.java. 
 * It also draws a square around the player (playerRect) to check 
 * if the player is intersecting with any platforms.
 * 
 * Finally, the class contains an update method which gets called on from
 * the update method in PlayState.java every animation cycle (the length
 * of an animation cycle is defined in the animation timer in Main.java).
 *
 */

public class Player {
	public class Point {
		double x;
		double y;

		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

	}
	private GameModel model;
	private Image playerImage;
	private Image playerShieldImage;
	private Point positionPlayer;
	private Rectangle2D playerRect;
	private int size;

	private boolean movingRight = false;
	private boolean movingLeft = false;
	private boolean jumping = true;
	private boolean boosted = false;
	private boolean hitHalfway = false;
	private boolean shielded = false;
	private boolean latestDirRight = true;
	private boolean onSamePlatform;
	private boolean rightIsAccDown;
	private boolean leftIsAccDown;
	private boolean powerFall = false;

	private int pixelsJumped;
	private double speed = GAME_SPEED;
	private double rightAcc = 0;
	private double leftAcc = 0;

	private double gravity = 0;

	private int score = 0;
	private double boosterAngle = 0;

	private ArrayList<Image> playerImages;


	public Player(GameModel model) {
		this.model = model;
		this.size = 70;

		positionPlayer = new Point(SCREEN_WIDTH/2-50, SCREEN_HEIGHT-90);
		playerRect = new Rectangle2D(positionPlayer.x, positionPlayer.y + size/4, size/2, size/10); 
		score = 0;

		playerImages = model.getPlayerImages();

		playerShieldImage = playerImages.get(6);

		setPlayerImage("right_jumping");
	}
	
	/**
	 * Runs every update of the game.
	 * Checks player death, updates player acceleration, speed and sets player image.
	 */
	public void update(GameModel model, ArrayList<Platform> platforms) {

		this.jump();

		if (this.getPosY() <= SCREEN_WIDTH/2 || hitHalfway) {
			for (Platform platform : platforms) {
				platform.lower(speed);
			}
			hitHalfway = true;
		}

		//if jumping vs not jumping
		if (speed<=0) {
			this.jumping = false;
			this.boosted=false;
			
			this.hitHalfway = false;
			positionPlayer.y+=1;
		} else {
			pixelsJumped += speed;
		}

		//player dies or leaves screen
		if (this.getPosY() >= SCREEN_HEIGHT - size) {
			model.updateHC(this.getScore());
			model.switchState(new HighScoreState(model, score));
		} else if (positionPlayer.x >= SCREEN_WIDTH) {
			positionPlayer.x = -size;
		} else if (positionPlayer.x <= -size) {
			positionPlayer.x = SCREEN_WIDTH;
		}

		if(this.getMovingLeft()) {
			this.moveLeft();
		}else if(this.getMovingRight()) {
			this.moveRight();
		}else if(latestDirRight) {
			if (getJumping()) {
				setPlayerImage("right_jumping");
			}else if (getSuperJumping()) {
				setPlayerImage("right_super");
			}else if (isPowerFall()) {
				setPowerFallImage();
			}else{
				setPlayerImage("right_falling");
			}
		}else if(!latestDirRight) {
			if (getJumping()) {
				setPlayerImage("left_jumping");
			}else if (getSuperJumping()) {
				setPlayerImage("left_super");
			}else if (isPowerFall()) {
				setPowerFallImage();
			}else{
				setPlayerImage("left_falling");
			}

		}

		if(this.getSuperJumping()){
			this.incAngle();
		}

		//HORIZONTAL ACC-CONTROLL
		if(rightIsAccDown) {
			positionPlayer.x += rightAcc;
			rightAcc-= GAME_SPEED/80;
			if (rightAcc <= 0) {
				stopRightAcc();
			}
		}else if(leftIsAccDown) {
			positionPlayer.x -= leftAcc;
			leftAcc -= GAME_SPEED/80;
			if (leftAcc <= 0) {
				stopLeftAcc();
			}
		}

	}
	
	/**
	 * Resets gravity and gives a boost in speed
	 */
	public void intersect() {
		gravity = 0;
		if (isPowerFall()) {
			if (boosted) {
				speed = 3*GAME_SPEED;
			}else {
				speed = 2*GAME_SPEED;
			}
			this.setPowerFall(false);
		}else {
			if (boosted) {
				speed = 2*GAME_SPEED;
			}else {
				speed = GAME_SPEED;
			}
		}

	}

	public void jump() {
		gravity+= GAME_SPEED/1500;
		
		if (powerFall) {
			speed-=GAME_SPEED/4; 
		} else {
			speed-=gravity;
		}
		
		if (!hitHalfway) {
			this.positionPlayer.y -= speed;
		}
	}


	public void keyPressed(KeyEvent key) {
		if(key.getCode() == KeyCode.RIGHT) {
			this.setRight(true);
		}else if(key.getCode() == KeyCode.LEFT) {
			this.setLeft(true);
		}else if(key.getCode() == KeyCode.DOWN) {
			this.setPowerFall(true);
			this.speed = 0;
		}
	}

	public void keyReleased(KeyEvent key) {
		if(key.getCode() == KeyCode.RIGHT) {

			this.setRight(false);
			latestDirRight = true;
			rightIsAccDown = true;
		}else if(key.getCode() == KeyCode.LEFT) {

			this.setLeft(false);
			latestDirRight = false;
			leftIsAccDown = true;
		}
	}

	public void moveRight() { 
		stopLeftAcc();
		positionPlayer.x += GAME_SPEED + rightAcc;
		if (getJumping()) {
			setPlayerImage("right_jumping");
		}else if (getSuperJumping()) {
			setPlayerImage("right_super");
		}else if (isPowerFall()) {
			setPowerFallImage();
		}else{
			setPlayerImage("right_falling");
		}
		//makes the player acc. to the right
		if (rightAcc < GAME_SPEED) {
			rightAcc += GAME_SPEED/20;
		}
	}

	public void moveLeft() {
		stopRightAcc();
		positionPlayer.x += -GAME_SPEED - leftAcc;
		if (getJumping()) {
			setPlayerImage("left_jumping");
		}else if (getSuperJumping()) {
			setPlayerImage("left_super");
		}else if (isPowerFall()) {
			setPowerFallImage();
		}else{
			setPlayerImage("left_falling");
		}
		//makes the player acc. to the left
		if (leftAcc < GAME_SPEED) {
			leftAcc += GAME_SPEED/20;
		}		
	}

	public void stopRightAcc() {
		rightIsAccDown = false;
		rightAcc = 0;
	}

	public void stopLeftAcc() {
		leftIsAccDown = false;
		leftAcc = 0;
	}

	public void incScore(int scoreInc) {
		score += scoreInc;
	}
	public int getScore() {
		return score;
	}

	private void rotate(GraphicsContext gc, double angle, double px, double py) {
		Rotate r = new Rotate(angle, px, py);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
	}

	private void drawRotatedImage(GraphicsContext g, Image image, double angle, double topLeftX, double topLeftY) {
		g.save(); // saves the current state on stack, including the current transform
		rotate(g, angle, topLeftX, topLeftY);
		g.drawImage(image, topLeftX, topLeftY, size, size);
		g.restore(); // back to original state (before rotation)
	}

	public void delegate(GraphicsContext g) {
		if (g != null) {
			draw(g);
		}
	}

	private void draw(GraphicsContext g) {
		playerRect = new Rectangle2D(positionPlayer.x, positionPlayer.y + size/4, size/2, size/10); 

		if (boosted) { 
			drawRotatedImage(g, playerImage, boosterAngle, positionPlayer.x, positionPlayer.y);
			if(shielded) {
				drawRotatedImage(g, playerShieldImage, boosterAngle, positionPlayer.x , positionPlayer.y); 
			}
		}else {
			g.drawImage(playerImage, positionPlayer.x , positionPlayer.y, size, size);
			if(shielded) {
				g.drawImage(playerShieldImage, positionPlayer.x , positionPlayer.y, size, size); 
			}
		}


		g.setFill(Color.RED);
		g.setFont(new Font(20)); // Big letters
		g.fillText("Highscore: " + model.getHC() + "\nScore: " + getScore(), 5, 30);

	}

	public void setPlayerImage(String imageName) {
		if(imageName.equals("right_jumping")){
			playerImage = playerImages.get(0);
		}else if(imageName.equals("right_falling")){
			playerImage = playerImages.get(1);
		}else if(imageName.equals("right_super")){
			playerImage = playerImages.get(2);
		}else if(imageName.equals("left_jumping")){
			playerImage = playerImages.get(3);
		}else if(imageName.equals("left_falling")){
			playerImage = playerImages.get(4);
		}else if(imageName.equals("left_super")){
			playerImage = playerImages.get(5);
		}else if(imageName.equals("power_fall")){
			playerImage = playerImages.get(7);
		}else if(imageName.equals("power_fall2")){
			playerImage = playerImages.get(8);
		}else if(imageName.equals("power_fall3")){
			playerImage = playerImages.get(9);
		}

	}		

	public void setRight(Boolean b) {
		this.movingRight = b;

	}

	public void setLeft(Boolean b) {
		this.movingLeft = b;

	}

	public void setJump(Boolean b) {
		this.jumping = b;
	}

	public Boolean getMovingRight() {
		return movingRight;
	}

	public Boolean getMovingLeft() {
		return movingLeft;
	}
	public Boolean getJumping() {
		return jumping;
	}

	public Boolean getSuperJumping() {
		return boosted;
	}

	public void setBoosted(Boolean boosted) {
		this.boosted = boosted;
	}

	public Rectangle2D getPlayerRect() {
		return playerRect;
	}

	public double getPosY() {
		return positionPlayer.y;
	}

	public void setPosY(double posY) {
		positionPlayer.y=posY;
	}

	public Boolean getShielded() {
		return shielded;
	}

	public void setShielded(Boolean shielded) {
		this.shielded = shielded;
	}


	public Boolean getHitHalfway() {
		return hitHalfway;
	}

	public void setHitHalfway(Boolean hitHalfway) {
		this.hitHalfway = hitHalfway;
	}

	public int getPixelsJumped() {
		return pixelsJumped; 
	}


	public void setPixelsJumped(int pixelsJumped) {
		this.pixelsJumped = pixelsJumped;
	}

	public boolean isOnSamePlatform() {
		return onSamePlatform;
	}

	public void setOnSamePlatform(boolean onSamePlatform) {
		this.onSamePlatform = onSamePlatform;
	}

	public void incAngle() {
		boosterAngle += GAME_SPEED *0.78;
	}
	
	public void setAngleZero() {
		boosterAngle = 0;
	}

	public double getSpeed() {
		return speed;

	}

	public boolean isPowerFall() {
		return powerFall;
	}

	public void setPowerFall(boolean powerFall) {
		this.powerFall = powerFall;
	}

	public void setPowerFallImage() {
		if (playerImage == playerImages.get(7)) {
			setPlayerImage("power_fall2");
		}else if(playerImage == playerImages.get(8)) {
			setPlayerImage("power_fall3");
		}else {
			setPlayerImage("power_fall");
		}
	}

}
