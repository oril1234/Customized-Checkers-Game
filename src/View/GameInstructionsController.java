package View;


import java.net.URL;
import java.util.ResourceBundle;

import Control.SysData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Game instructions controller
 * @author user Sharks
 *
 */
public class GameInstructionsController implements Initializable
{

	@FXML
	private AnchorPane mainPane;
	
	private SysData sd=SysData.getInstance();

	@FXML
	void returnToHomePage()
	{
		try
		{
			sd.playMenuSound();
			Stage primaryStage = (Stage) mainPane.getScene().getWindow();
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
		// TODO Auto-generated method stub
		
	}

}
