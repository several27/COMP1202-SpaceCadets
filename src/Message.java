import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class Message extends Label
{
	private int direction;

	public Message(String text, int direction)
	{
		super(text);

		this.direction = direction;
	}

	public int getDirection()
	{
		return direction;
	}

	public void setDirection(int direction)
	{
		this.direction = direction;
	}

	public double getNewX()
	{
		double speed = getFont().getSize() / 10;

		return getTranslateX() + speed * Math.sin(Math.toRadians(direction));
	}

	public double getNewY()
	{
		double speed = getFont().getSize() / 10;

		return getTranslateY() + speed * Math.cos(Math.toRadians(direction));
	}

	public boolean detectCollisionWithCanvas(Pane canvas)
	{
		double newX, newY, newX1, newY1;

		newX = getNewX();
		newY = getNewY();
		newX1 = newX + getWidth();
		newY1 = newY + getHeight();

		return (
				newX < 0 || newY < 0
				||
				newX > canvas.getWidth() || newY > canvas.getHeight()
				||
				newX1 < 0 || newY1 < 0
				||
				newX1 > canvas.getWidth() || newY1 > canvas.getHeight()
		);
	}
}
