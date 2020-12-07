import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JComboBox.KeySelectionManager;

public class GamePanel extends JPanel implements KeyListener {

	private enum GameState {
		Title, Playing, GameOver
	}

	ArrayList<Point2D.Float> snowflakePositions = new ArrayList<>();
	Color backgroundColor = new Color(208, 244, 247);
	Image tileableMountainBackground, tileableTreelineImage, titleImage, snowflake_8_Image, cottageImage, smokeImage, sleighImage, giftboxImage, coronaImage;
	float[] backgroundOffsets = new float[3];
	Image[] backgroundImages = new Image[3];
	Random rnd = new Random();
	int points = 0;
	MovingSprite sleigh;
	ArrayList<MovingSprite> coronaCreatures = new ArrayList<>();
	int numberOfCottagesOnScreen = 2;
	private int updatesPerSecond = 30;
	private int millisecDelay = 1000 / updatesPerSecond;
	private double nextUpdate;
	private double lastUpdate;
	private boolean upkeyPressed, downkeyPressed, leftkeyPressed, rightkeyPressed;
	ArrayList<Point2D.Float> cottagePositions = new ArrayList<>();
	ArrayList<MovingSprite> giftboxes = new ArrayList<>();
	Font font = new Font("Impact", Font.PLAIN, 32);

	private GameState gameState = GameState.Title;
	public GameState getGameState() {return gameState;}
	public void setGameState(GameState gameState) {this.gameState = gameState;}

	public GamePanel() {
		loadContent();
		addKeyListener(this);
	}
	
	public void loadContent() {
		tileableMountainBackground = loadImage("/gradient_tileable_mountain_.png");
		tileableTreelineImage = loadImage("/tileable_treeline.png");
		titleImage = loadImage("/title_640.png");
		snowflake_8_Image = loadImage("/snowflake_8.png");
		cottageImage = loadImage("/cottage_128px.png");
		smokeImage = loadImage("/smoke.png");
		sleighImage = loadImage("/sleigh_128.png");
		giftboxImage= loadImage("/giftbox_32.png");
		coronaImage= loadImage("/corona_64px.png");
		sleigh = new MovingSprite(new Point2D.Float(200, 200), new Point2D.Float(), sleighImage);
		backgroundImages[0] = tileableMountainBackground;
		backgroundImages[1] = tileableTreelineImage;
		backgroundImages[2] = tileableTreelineImage;
	}

	private void createSnow() {
		for (int i = 0; i < 360; i++) {
			snowflakePositions.add(new Point2D.Float(rnd.nextInt(getWidth()), rnd.nextInt(getHeight())));
		}
	}
	
	private void positionCottages() {

		int cottageSpacing = 600;
		for (int i = 0; i < numberOfCottagesOnScreen; i++) {
			int x = getWidth();
			x += cottageSpacing * i + rnd.nextInt(cottageSpacing / 3);
			Point2D.Float position = new Point2D.Float(x, getHeight() - 160 - rnd.nextInt(40));
			cottagePositions.add(position);
		}
	}
	
	


	public void run() {

		createSnow();
		positionCottages();


		lastUpdate = nextUpdate = System.currentTimeMillis();

		while (true) {
			repaint();

			double currentTimeMillis = System.currentTimeMillis();
			if (nextUpdate <= currentTimeMillis) {
				
				double msBeforeNextUpdate = millisecDelay - (currentTimeMillis - nextUpdate);

				double msElapsedSinceLastUpdate = currentTimeMillis - lastUpdate;
				lastUpdate = System.currentTimeMillis();
				nextUpdate = lastUpdate + msBeforeNextUpdate;
				
				update(msElapsedSinceLastUpdate);
				repaint();

				System.out.println(msElapsedSinceLastUpdate);
			}
		}
	}
	

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2d.setColor(backgroundColor);
//		g2d.fillRect(0, 0, g2d.getClipBounds().width, g2d.getClipBounds().height);

		drawBackground(g2d);
		drawCottages(g2d);
		switch (getGameState()) {
		case Title:
			drawLogo(g);

			break;
		case Playing:
			drawScore(g);
			for (MovingSprite box : giftboxes) {
				box.draw(g2d);
			}
			drawCoronaCreatures(g2d);
			sleigh.draw(g2d);
			break;
		case GameOver:
			drawScore(g);
			break;
		default:
			break;
		}
		drawCoronaCreatures(g2d);
		drawSnow(g);

	}

	private void drawCoronaCreatures(Graphics2D g2d) {
		for (MovingSprite coronaCreature : coronaCreatures) {
			coronaCreature.draw(g2d);
		}
	}
	
	private void drawBackground(Graphics2D g2d) {
		for (int backgroundCounter = 0; backgroundCounter < 4; backgroundCounter++) {
			g2d.drawImage(tileableMountainBackground,
					(backgroundCounter * tileableMountainBackground.getWidth(null)) - (int) backgroundOffsets[0], 0,
					null);
		}

		for (int backgroundCounter = 0; backgroundCounter < 4; backgroundCounter++) {
			g2d.drawImage(tileableTreelineImage,
					(backgroundCounter * tileableTreelineImage.getWidth(null)) - (int) backgroundOffsets[1], getHeight() / 2,
					null);
		}

		for (int backgroundCounter = 0; backgroundCounter < 4; backgroundCounter++) {
			g2d.drawImage(tileableTreelineImage,
					(backgroundCounter * tileableTreelineImage.getWidth(null)) - (int) backgroundOffsets[2],
					getHeight() / 3 * 2 - 72, null);
		}
	}
	
	private void drawCottages(Graphics2D g2d) {
		for (Point2D.Float point : cottagePositions) {
			g2d.drawImage(cottageImage, (int)(point.x - cottageImage.getWidth(null) / 2), (int)(point.y - cottageImage.getHeight(null) / 2), null);
		}
	}

	private void drawSnow(Graphics g) {
		for (int i = 0; i < snowflakePositions.size(); i++) {
			Point2D.Float position = snowflakePositions.get(i);
			if (i % 7 != 0) {
				g.drawImage(snowflake_8_Image, (int)position.x,(int) position.y, (int)position.x + snowflake_8_Image.getWidth(null) / 2,
						(int)position.y + snowflake_8_Image.getWidth(null) / 2, 0, 0, 8, 8, null);
			} else {
				g.drawImage(snowflake_8_Image, (int)position.x, (int)position.y, null);
			}
		}

	}

	private void drawScore(Graphics g) {
		g.setFont(font);
		g.setColor(Color.black);
		g.drawString("Score: " + points, 18, 42);
		g.setColor(Color.white);
		g.drawString("Score: " + points, 22, 38);
	}

	private void drawLogo(Graphics g) {
		double yOffset = Math.sin(System.currentTimeMillis() / 300) * 8;
		g.drawImage(titleImage, (getWidth() - titleImage.getWidth(null)) / 2,
				(getHeight() - titleImage.getHeight(null)) / 2 + (int) yOffset * 0 - titleImage.getHeight(null) / 7,
				titleImage.getWidth(null), titleImage.getHeight(null), null);
	}
	

	public void update(double msElapsedSinceLastUpdate) {

		moveBackground(msElapsedSinceLastUpdate);
		moveSnow();
		moveCottages(msElapsedSinceLastUpdate);
		
		switch (getGameState()) {
		case Title:

			break;
		case Playing:
			moveSleighBasedOnKeypresses(msElapsedSinceLastUpdate);
			moveGiftboxes(msElapsedSinceLastUpdate);
			moveCoronaCreatures(msElapsedSinceLastUpdate);
			performCollisionDetection();
			break;
		case GameOver:
			break;
		default:
			break;
		}
	}

	private void performCollisionDetection() {

		for (int packageCounter = giftboxes.size() - 1; packageCounter >= 0; packageCounter--) {
			MovingSprite giftBox = giftboxes.get(packageCounter);
			Point2D.Float giftPosition = giftBox.getPosition();

			if (giftPosition.y < getHeight() / 2) {
				for (int creatureCounter = coronaCreatures.size() - 1; creatureCounter >= 0; creatureCounter--) {
					MovingSprite creature = coronaCreatures.get(creatureCounter);
					Point2D.Float creaturePosition = creature.getPosition();
					if (Point2D.distance(creaturePosition.x, creaturePosition.y, giftPosition.x, giftPosition.y) < (giftboxImage.getWidth(null) + coronaImage.getWidth(null)) / 2) {
						giftboxes.remove(giftBox);
						moveCoronaCreatureRightOfScreen(creature);
						points += 1;
						break;
					}
				}
			}
			else {
				for (int cottageCounter = cottagePositions.size() - 1; cottageCounter >= 0; cottageCounter--) {
					Point2D.Float cottagePosition = cottagePositions.get(cottageCounter);
					if (Point2D.distance(cottagePosition.x - cottageImage.getWidth(null) / 4,
							cottagePosition.y - cottageImage.getHeight(null) / 3, giftPosition.x,
							giftPosition.y) < (giftboxImage.getWidth(null) + cottageImage.getWidth(null)) / 6) {
						giftboxes.remove(giftBox);
						points += 3;
						break;
					}
				}
			}
		}
	}

	private void moveGiftboxes(double msElapsedSinceLastUpdate) {
		for (int i = giftboxes.size()-1; i >= 0; i--) {
			MovingSprite box = giftboxes.get(i);
			box.update(msElapsedSinceLastUpdate);
			if(box.getPosition().y > getHeight() + box.getHeight()) {
				giftboxes.remove(box);
				if(points > 0) {points--;}
			}
		}
	}
	
	private void addCoronaCreatures()
	{
		for (int i = 0; i < 4; i++) {
			MovingSprite creature = new MovingSprite(new Point2D.Float(0,0), new Point2D.Float(-.25f, 0), coronaImage);
			coronaCreatures.add(creature);
			moveCoronaCreatureRightOfScreen(creature);
		}
	}
	private void moveSleighBasedOnKeypresses(double msElapsedSinceLastUpdate) {
		float deltaX = 0, deltaY = 0;
		float speed = .3f;
		deltaY += upkeyPressed ? -1 : 0;
		deltaY += downkeyPressed ? 1 : 0;
		deltaX += leftkeyPressed ? -1 : 0;
		deltaX += rightkeyPressed ? 1 : 0;

		sleigh.setMovement(new Point2D.Float(deltaX * speed, deltaY * speed));
		sleigh.update(msElapsedSinceLastUpdate);
		float minX = sleigh.getWidth()/2;
		float maxX = getWidth() - sleigh.getWidth()/2;
		float minY = sleigh.getHeight()/2 + 20;
		float maxY = getHeight()/2 - sleigh.getHeight()/2;
		
		sleigh.getPosition().x = Math.max(sleigh.getPosition().x, minX);
		sleigh.getPosition().x = Math.min(sleigh.getPosition().x, maxX);
		sleigh.getPosition().y = Math.max(sleigh.getPosition().y, minY);
		sleigh.getPosition().y = Math.min(sleigh.getPosition().y, maxY);
	}

	private void moveCottages(double msElapsedSinceLastUpdate) {
		for (Point2D.Float p : cottagePositions) {
			double amountToMove = .04 * 3.5 * msElapsedSinceLastUpdate;
			p.x -= amountToMove;

			if (p.x < -200) {
				moveCottage(p);
			}
		}
	}
	
	private void moveCoronaCreatures(double msElapsedSinceLastUpdate) {
		for (MovingSprite coronaCreature : coronaCreatures) {
			coronaCreature.update(msElapsedSinceLastUpdate);
			if (coronaCreature.getPosition().x < -200) {
				moveCoronaCreatureRightOfScreen(coronaCreature);
			}
		}
	}

	private void moveCoronaCreatureRightOfScreen(MovingSprite coronaCreature) {
		
		int coronaCreatureSpacing = getWidth() / 2;
		float furthestPositionRight = getWidth();
		for (int i = 0; i < coronaCreatures.size(); i++) {
			if (coronaCreatures.get(i).getPosition().x > furthestPositionRight) {
				furthestPositionRight = coronaCreatures.get(i).getPosition().x;
			}
		    coronaCreature.getPosition().x = furthestPositionRight + coronaCreatureSpacing / 2  + rnd.nextInt(coronaCreatureSpacing / 2);
		    coronaCreature.getPosition().y =  rnd.nextInt(getHeight()/3)+80 + sleigh.getHeight();
System.out.println(coronaCreature.getPosition());
		}		
	}
	
	private void moveSnow() {
		
		float maxAmountMovedOnXAxis = 2;
		for (int i = 0; i < snowflakePositions.size(); i++) {
			Point2D.Float snowFlakePosition = snowflakePositions.get(i);
			snowFlakePosition.y++;
			snowFlakePosition.y %= getHeight();
			snowFlakePosition.x -= rnd.nextFloat()*maxAmountMovedOnXAxis;

			if (rightkeyPressed) {snowFlakePosition.x -= 2;}
			if (snowFlakePosition.x < 0) {
				snowFlakePosition.x += getWidth();
			}
		}
	}

	private void moveBackground(double msElapsedSinceLastUpdate) {
		for (int i = 0; i < backgroundOffsets.length; i++) {
			backgroundOffsets[i] += .04 * (i + 1) * msElapsedSinceLastUpdate;
			backgroundOffsets[i] %= backgroundImages[i].getWidth(null);
		}
	}

	private void moveCottage(Point2D.Float cottagePosition) {
		int cottageSpacing = getWidth() / 2;
		float furthestPositionRight = getWidth();
		for (int i = 0; i < cottagePositions.size(); i++) {
			if (cottagePositions.get(i).x > furthestPositionRight) {
				furthestPositionRight = cottagePositions.get(i).x;
			}
			cottagePosition.x = furthestPositionRight + cottageSpacing / 3 + rnd.nextInt(cottageSpacing / 3);
			cottagePosition.y = getHeight() - (60 + rnd.nextInt(80));

		}
	}

	private Image loadImage(String imagePathOrUrl) {
		Image image = null;
		try {
			image = ImageIO.read(this.getClass().getResource(imagePathOrUrl));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return image;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (getGameState() == gameState.Title) {
			setGameState(GameState.Playing);
			addCoronaCreatures();
		} else {

			switch (arg0.getKeyCode()) {
			case KeyEvent.VK_UP:
				upkeyPressed = true;
				break;
			case KeyEvent.VK_DOWN:
				downkeyPressed = true;
				break;
			case KeyEvent.VK_LEFT:
				leftkeyPressed = true;
				break;
			case KeyEvent.VK_RIGHT:
				rightkeyPressed = true;
				break;
			case KeyEvent.VK_SPACE:
				giftboxes.add(new MovingSprite(
						new Point2D.Float(sleigh.getPosition().x, sleigh.getPosition().y + sleigh.getHeight() / 2),
						new Point2D.Float(0, .3f), giftboxImage));
				break;
			default:

			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_UP:
			upkeyPressed = false;
			break;
		case KeyEvent.VK_DOWN:
			downkeyPressed = false;
			break;
		case KeyEvent.VK_LEFT:
			leftkeyPressed = false;
			break;
		case KeyEvent.VK_RIGHT:
			rightkeyPressed = false;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}