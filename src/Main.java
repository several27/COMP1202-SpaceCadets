import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleLongProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

public class Main extends FXApplet
{
	Button addMessageButton, clearButton, randomMessageButton;
	HBox topBox;

	Pane canvas;

	VBox mainBox;

	List<Label> labels = new ArrayList<>();

	AnimationTimer timer;

	private final FrameStats frameStats = new FrameStats();

	@Override
	public void initApplet()
	{
		// Buttons
		addMessageButton = new Button("Add Message");
		clearButton = new Button("Clear");
		randomMessageButton = new Button("Add Random Messages");

		addMessageButton.setOnAction(e -> displayAddMessageWindow());
		clearButton.setOnAction(e -> clearCanvas());
		randomMessageButton.setOnAction(e -> addRandomMessages());

		// Stats
		Label stats = new Label();
		stats.textProperty().bind(frameStats.textProperty());

		// Top horizontal box
		topBox = new HBox();
		topBox.setSpacing(5);
		topBox.getChildren().addAll(addMessageButton, clearButton, randomMessageButton, stats);

		// Bottom canvas (pane)
		canvas = new Pane();
//		canvas.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		canvas.getChildren().addAll(labels);
		canvas.widthProperty().addListener((observable, oldValue, newValue) ->
		{
			canvas.setMinHeight(mainBox.getHeight() - topBox.getHeight() - 10); // adjust height to 100% on width change
		});

		// Main vertical box
		mainBox = new VBox();
		mainBox.setPadding(new Insets(5, 5, 5, 5));
		mainBox.setSpacing(5);
		mainBox.getChildren().addAll(topBox, canvas);

		scene.setRoot(mainBox);

		// Start animation
		timer = new MessagesAnimationTimer();
		timer.start();
	}

	private class MessagesAnimationTimer extends AnimationTimer
	{
		final LongProperty lastUpdateTime = new SimpleLongProperty(0);

		@Override
		public void handle(long now)
		{
			if (lastUpdateTime.get() > 0)
			{
				long elapsedTime = now - lastUpdateTime.get();

				for (Node node : canvas.getChildren())
				{
					if (!(node instanceof Message))
						break;

//				double x = node.getTranslateX(),
//						y = node.getTranslateY();
//
//				if (newX - 100 > canvas.getWidth() || newX + 100 < 0 || newY - 100 > canvas.getHeight() || newY + 100 < 0)
//				{
//					System.out.println("newX: " + newX + ", newY: " + newY);
//					canvas.getChildren().remove(node);
//				}

					double newX, newY, newX1, newY1;

					// Detect border collision
					do
					{
						newX = ((Message) node).getNewX();
						newY = ((Message) node).getNewY();
						newX1 = newX + ((Message) node).getWidth();
						newY1 = newY + ((Message) node).getHeight();

						if (newX < 0 || newY < 0 || newX > canvas.getWidth() || newY > canvas.getHeight() || newX1 < 0 || newY1 < 0 || newX1 > canvas.getWidth() || newY1 > canvas.getHeight())
						{
							((Message) node).setDirection(getRandomInteger(0, 360));
						}

					}
					while (newX < 0 || newY < 0 || newX > canvas.getWidth() || newY > canvas.getHeight() || newX1 < 0 || newY1 < 0 || newX1 > canvas.getWidth() || newY1 > canvas.getHeight());

//				else
//				{
					// Detect collision between messages
//					for (Node node2 : canvas.getChildren())
//					{
//						if (!(node2 instanceof Message))
//							break;
//
//						if (node2.getTranslateX() == newX && node2.getTranslateY() == newY)
//						{
//							((Message) node).reverseDirection();
//						}
//					}
//				}

					node.setTranslateX(((Message) node).getNewX());
					node.setTranslateY(((Message) node).getNewY());
				}

				frameStats.addFrame(elapsedTime);
			}
			lastUpdateTime.set(now);
		}
	}

	private void displayAddMessageWindow()
	{
		String message = newMessageBox.display();
		addMessage(
				message,
				getRandomInteger(10, 30),
				Color.rgb(
						getRandomInteger(0, 255),
						getRandomInteger(0, 255),
						getRandomInteger(0, 255)
				         )
		          );
	}

	private void addMessage(String message, double fontSize, Color color)
	{
		Message label = new Message(message, getRandomInteger(0, 360));
		label.setFont(new Font("Arial", fontSize));
		label.setTextFill(color);

		label.setTranslateZ(labels.size() + 1);
		// sometimes outside of border
		// so not exactly what I would want
//		label.setTranslateX(getRandomInteger(0, (int) canvas.getWidth()));
//		label.setTranslateY(getRandomInteger(0, (int) canvas.getHeight()));
		// so the better solution would be to just use static position
		label.setTranslateX(canvas.getWidth() / 2);
		label.setTranslateY(canvas.getHeight() / 2);

		labels.add(label);
		canvas.getChildren().add(label);
	}

	private void clearCanvas()
	{
		canvas.getChildren().clear();
	}

	private void addRandomMessages()
	{
		timer.stop();

		for (int i = 0; i < 500; i++)
		{
			addMessage("Random text", getRandomInteger(10, 30), Color.rgb(getRandomInteger(0, 255), getRandomInteger(0, 255), getRandomInteger(0, 255)));
		}

		System.out.println(canvas.getChildren().size());

		timer.start();
	}

	private static int getRandomInteger(int min, int max)
	{
		return new Random().nextInt((max - min) + 1) + min;
	}

	private static class FrameStats {
		private long   frameCount;
		private double meanFrameInterval; // millis
		private final ReadOnlyStringWrapper text = new ReadOnlyStringWrapper(this, "text", "Frame count: 0 Average frame interval: N/A");

		public long getFrameCount()
		{
			return frameCount;
		}

		public double getMeanFrameInterval()
		{
			return meanFrameInterval;
		}

		public void addFrame(long frameDurationNanos)
		{
			meanFrameInterval = (meanFrameInterval * frameCount + frameDurationNanos / 1_000_000.0) / (frameCount + 1);
			frameCount++;
			text.set(toString());
		}

		public String getText()
		{
			return text.get();
		}

		public ReadOnlyStringProperty textProperty()
		{
			return text.getReadOnlyProperty();
		}

		@Override
		public String toString()
		{
			return String.format("Frame count: %,d Average frame interval: %.3f milliseconds", getFrameCount(), getMeanFrameInterval());
		}
	}
}