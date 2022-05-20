package View;

import java.net.URL;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import Control.SysData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * This is a controller class for scene of picking nicknames for the player in the game
 */
public class PickNickNamesController implements Initializable
{

	@FXML
	private AnchorPane pickNicknamesAnchorPane;

	@FXML
	private TextField whitePlayerField;

	@FXML
	private TextField blackPlayerField;

	private SysData sd = SysData.getInstance();

    
    
	@FXML
	void startGame(ActionEvent event)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
      sd.playMenuSound();
		if (whitePlayerField.getText().equals("") || blackPlayerField.getText().equals("")
				|| whitePlayerField.getText().equals(blackPlayerField.getText()))
		{
			sd.playErrorSound();
			alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("You must pick different nicknames for both players");
			alert.setHeaderText(null);
			alert.showAndWait();
			return;
		}
		
		//Starting the game
		sd.instantiatePlayer("WHITE", whitePlayerField.getText());
		sd.instantiatePlayer("BLACK", blackPlayerField.getText());
		sd.instantiateGame();

		try
		{
			
			Stage primaryStage = (Stage) pickNicknamesAnchorPane.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("GameView.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			Scene scene = new Scene(root);

			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Game");
			primaryStage.show();
			((GameController)loader.getController()).checkGameEnded();

			SysData.playStartGameSound();

		} catch (Exception e)
		{
			e.printStackTrace();
		}


	}

	@FXML
	void ReturnToHomePage(ActionEvent event)
	{
		try
		{
			sd.playMenuSound();
			Stage primaryStage = (Stage) pickNicknamesAnchorPane.getScene().getWindow();
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/View/HomeView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/View/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Home");
			primaryStage.show();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		// Unfocus fields
		whitePlayerField.setFocusTraversable(false);
		blackPlayerField.setFocusTraversable(false);

	}

}
