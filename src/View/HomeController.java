package View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;

import Control.SysData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Home controller
 * @author user Sharks
 *
 */
public class HomeController implements Initializable
{
	@FXML
	private AnchorPane homeAnchorPane;
	SysData sd = SysData.getInstance();

	@FXML
	void OpenInstructions(ActionEvent event) throws IOException
	{
		sd.playMenuSound();

		FXMLLoader louder = new FXMLLoader();
		louder.setLocation(getClass().getResource("/View/GameInstructions.fxml"));
		louder.load();
		Parent parent = louder.getRoot();
		Scene tableViewScene = new Scene(parent);

		// This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setTitle("Question Management");

		window.setScene(tableViewScene);
		window.show();

	}

	@FXML
	public void startGame(ActionEvent event)
	{
		sd.playMenuSound();

		FXMLLoader louder = new FXMLLoader();
		louder.setLocation(getClass().getResource("/View/PickNicknames.fxml"));
		try
		{
			louder.load();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Parent parent = louder.getRoot();
		Scene tableViewScene = new Scene(parent);

		// This line gets the Stage information
		Stage window = (Stage) homeAnchorPane.getScene().getWindow();
		window.setTitle("Question Management");

		window.setScene(tableViewScene);
		window.show();
	}

	public void changeToQuestionsManagementView(ActionEvent event) throws IOException
	{
		sd.playMenuSound();

		FXMLLoader louder = new FXMLLoader();
		louder.setLocation(getClass().getResource("/View/QuestionsView.fxml"));
		louder.load();
		Parent parent = louder.getRoot();
		Scene tableViewScene = new Scene(parent);

		// This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setTitle("Question Management");

		window.setScene(tableViewScene);
		window.show();
	}

	@FXML
	void goToHistory(ActionEvent event) throws IOException
	{
		sd.playMenuSound();

		Stage window = (Stage) homeAnchorPane.getScene().getWindow();
		FXMLLoader louder = new FXMLLoader();
		louder.setLocation(getClass().getResource("/View/gameHistoryView.fxml"));
		louder.load();
		Parent parent = louder.getRoot();
		Scene scene = new Scene(parent);
		window.setScene(scene);
		window.show();

	}

	@FXML
	void loadAStateOfGame(ActionEvent event)
	{
		try
		{
			sd.playMenuSound();

			Stage window = (Stage) homeAnchorPane.getScene().getWindow();
			FXMLLoader louder = new FXMLLoader();
			louder.setLocation(getClass().getResource("/View/LoadGameFromText.fxml"));
			louder.load();
			Parent parent = louder.getRoot();
			Scene scene = new Scene(parent);
			window.setScene(scene);
			window.show();
		} catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	@FXML
	void exitGame(ActionEvent event)
	{
		sd.playMenuSound();
		((Stage)homeAnchorPane.getScene().getWindow()).close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{

	}

}
