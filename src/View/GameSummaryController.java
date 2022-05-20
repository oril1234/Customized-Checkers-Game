package View;

import java.net.URL;
import java.util.ResourceBundle;

import Control.SysData;
import Model.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller of game summary screen
 * @author user sharks 
 *
 */
public class GameSummaryController implements Initializable
{

	@FXML
	private AnchorPane mainPane;

	@FXML
	private Text winnerScoreLbl;

	@FXML
	private Text winnerLbl;

	@FXML
	private Text loserScoreLbl;

	@FXML
	private Text loserLbl;

	@FXML
	private Text durationLbl;

	private SysData sd = SysData.getInstance();
	private Game game;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		game = sd.getGame();

		String blackLbl = game.getBlackPlayer().getNickName();
		String whiteLbl = game.getWhitePlayer().getNickName();
		int blackScore = game.getBlackPlayer().getScore();
		int whiteScore = game.getWhitePlayer().getScore();

		if (whiteScore > blackScore)
		{
			winnerLbl.setText(whiteLbl + "");
			winnerScoreLbl.setText(whiteScore + "");

			loserLbl.setText(blackLbl + "");
			loserScoreLbl.setText(blackScore + "");

		} else
		{
			winnerLbl.setText(blackLbl + "");
			winnerScoreLbl.setText(blackScore + "");

			loserLbl.setText(whiteLbl + "");
			loserScoreLbl.setText(whiteScore + "");
		}
		durationLbl.setText(game.getFormattedDuration());

		sd.updateFinishDate();
		sd.updateWinnerAndLoser(winnerLbl.getText(), loserLbl.getText());
		sd.updateFinalScore(winnerScoreLbl.getText(), loserScoreLbl.getText());
		sd.loadGamesHistory();
		sd.addCurrentGameToJson();

	}

	@FXML
	void onRematch(ActionEvent event)
	{
		sd.instantiatePlayer("WHITE",game.getWhitePlayer().getNickName() );
		sd.instantiatePlayer("BLACK",game.getBlackPlayer().getNickName() );
		sd.instantiateGame();

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
			((GameController)loader.getController()).checkGameEnded();

			SysData.playStartGameSound();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
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
