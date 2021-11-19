import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class MovingSprite  {
	
	private Point2D.Float position, movement;
	private BufferedImage image;
	
	public int getWidth() {
		return getImage().getWidth(null);
	}

	public int getHeight() {
		return getImage().getHeight(null);
	}
	
	public Point2D.Float getPosition() {
		return position;
	}

	public void setPosition(Point2D.Float position) {
		this.position = position;
	}

	public Point2D.Float getMovement() {
		return movement;
	}

	public void setMovement(Point2D.Float movement) {
		this.movement = movement;
	}
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public MovingSprite(Point2D.Float position, Point2D.Float movement, BufferedImage image) {
		this.setPosition(position);
		this.setMovement(movement);
		this.setImage(image);
		
	}
	
	public void update(double msElapsedSinceLastUpdate) {
		Point2D.Float newPosition = new Point2D.Float((int)(getPosition().x + getMovement().x * msElapsedSinceLastUpdate), (int)(getPosition().y + getMovement().y* msElapsedSinceLastUpdate));
		this.setPosition(newPosition);
	}
	
	public void draw(Graphics g){
		g.drawImage(getImage(), (int)(getPosition().x - getWidth()/2), (int)(getPosition().y- getHeight()/2), null);
	}

}