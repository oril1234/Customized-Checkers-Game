package View;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import Control.SysData;
import Model.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * Controller for loading agame from text
 * @author user Sharks
 *
 */
public class LoadGameFromTextController implements Initializable
{

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button chooseNotSavedBtn;

    @FXML
    private Button deleteUploadBtn;

    @FXML
    private Text fileName;

    @FXML
    private Button uploadBtn;

    @FXML
    private Button chooseSavedBtn;

	private File file;

	private List<String> reloadedGame;

	
	private SysData sd = SysData.getInstance();

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		reloadedGame = new ArrayList<>();
	}

	@SuppressWarnings("resource")
	@FXML
	void chooseFile(ActionEvent event) throws IOException
	{
		FileChooser fc = new FileChooser();
		String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
		fc.setInitialDirectory(new File(currentPath));
		fc.setTitle("Choose A File To Load");
		Stage stage = (Stage) mainPane.getScene().getWindow();
		file = fc.showOpenDialog(stage);


		if (file!=null)
		{
			boolean isValidFile =file.getName().length() >= Constants.MIN_FILE_NAME_LENGTH
					&& file.getName().substring(file.getName().length() - 3).equals("txt");
			if(!isValidFile)
				handleError("File is not valid!");
			
			else
			{
				chooseSavedBtn.setDisable(true);
				chooseNotSavedBtn.setDisable(true);
				deleteUploadBtn.setVisible(true);
				uploadBtn.setVisible(true);
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line = null;
				reloadedGame = new ArrayList<>();

				while ((line = br.readLine()) != null)
				{
					// Ignore white space
					String[] values = line.replaceAll("\\s", "").split(",");
					reloadedGame.addAll(Arrays.asList(values));

				}
				
				if(event.getSource().equals(chooseSavedBtn) && 
						reloadedGame.size()!=Constants.VALID_SAVED_FILE_LENGTH)
				{
					handleError("Files is not in a valid length for saved game");
					br.close();
					return;
				}
				
				else if(event.getSource().equals(chooseNotSavedBtn) && 
						reloadedGame.size()!=Constants.VALID_LOADED_FILE_LENGTH)
				{
					handleError("Files is not in a valid length for loaded game");
					br.close();
					return;
				}
				
				br.close();
				fileName.setVisible(true);
				fileName.setText(file.getName());
			}
		} 
	}

	private void handleError(String content)
	{
		resetControls();
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setContentText(content);
		alert.setHeaderText(null);
		alert.showAndWait();

		
	}

	private void resetControls()
	{
		uploadBtn.setVisible(false);
		deleteUploadBtn.setVisible(false);
		fileName.setVisible(false);
		chooseNotSavedBtn.setDisable(false);
		chooseSavedBtn.setDisable(false);
	}

	@FXML
	void upload(ActionEvent event)
	{
		sd.instantiatePlayer("WHITE", "W");
		sd.instantiatePlayer("BLACK", "B");
		sd.loadGame(reloadedGame);
		startGame();

	}

	private void startGame()
	{
		try
		{

			Stage primaryStage = (Stage) mainPane.getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("GameView.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			Scene scene = new Scene(root);

			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Game");
			primaryStage.show();
			GameController gc=loader.getController();
			gc.checkGameEnded();

			SysData.playStartGameSound();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@FXML
	void deletUpload(ActionEvent event)
	{
		resetControls();
	}

	@FXML
	void returnToHomePage(ActionEvent event)
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

}
