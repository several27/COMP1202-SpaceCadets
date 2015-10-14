import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class newMessageBox
{
	static String _message;

	public static String getMessage()
	{
		return _message;
	}

	public static String display()
	{
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL); // block input events with other windows
		stage.setTitle("Add new message to canvas");
		stage.setMinWidth(250);

		TextField message = new TextField();
		message.setPromptText("Type message");

		Button addButton = new Button("Add");
		addButton.setOnAction(e ->
		                      {
			                      _message = message.getText();

			                      if (_message.length() > 0)
			                      {
				                      stage.close();
			                      }
		                      });

		VBox layout = new VBox(5);
		layout.setPadding(new Insets(5, 5, 5, 5));
		layout.getChildren().addAll(message, addButton);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		stage.setScene(scene);
		stage.showAndWait();

		return _message;
	}
}
