import javafx.scene.control.Label;

public class Message extends Label
{
	/**
	 * 012
	 * 7 3
	 * 654
	 */
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

	public void reverseDirection()
	{
		int reversedDirection = getDirection();
		reversedDirection = ((reversedDirection * 2) + 8) / 2;
		if (reversedDirection > 7)
			reversedDirection -= 8;

		setDirection(reversedDirection);
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
}
