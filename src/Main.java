import javafx.animation.AnimationTimer;
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
	Label stats;
	HBox topBox;

	Pane canvas;

	VBox mainBox;

	List<Label> labels = new ArrayList<>();

	AnimationTimer timer;

	private Benchmark benchmark = new Benchmark();

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
		stats = new Label();

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
		@Override
		public void handle(long now)
		{
			for (Node node : canvas.getChildren())
			{
				if (!(node instanceof Message))
					break;

				// Detect border collision
				while (((Message) node).detectCollisionWithCanvas(canvas))
				{
					((Message) node).setDirection(getRandomInteger(0, 360));
				}

				node.setTranslateX(((Message) node).getNewX());
				node.setTranslateY(((Message) node).getNewY());
			}

			benchmark.addFrame();
			stats.setText(benchmark.getText() + ", Number of messages: " + canvas.getChildren().size());
		}
	}

	private void displayAddMessageWindow()
	{
		String message = NewMessageBox.display();
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

		timer.start();
	}

	private static int getRandomInteger(int min, int max)
	{
		return new Random().nextInt((max - min) + 1) + min;
	}
}