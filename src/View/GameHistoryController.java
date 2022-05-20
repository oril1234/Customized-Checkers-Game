package View;


import java.net.URL;
import java.util.ResourceBundle;


import Control.SysData;
import Model.Game;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Controller of game history screen
 * @author user sharks
 *
 */
public class GameHistoryController implements Initializable
{
	private ObservableList<Game> games = FXCollections.observableArrayList();

	@FXML
	private AnchorPane gamesAnchorPane;

	@FXML
	private TableView<Game> gamesTable;

	@FXML
	private TableColumn gameNum;

	@FXML
	private TableColumn<Game, String> date;

	@FXML
	private TableColumn<Game, String> winnerNicknameCol;

	@FXML
	private TableColumn<Game, String> winnerScoreCol;

	@FXML
	private TableColumn<Game, String> loserNicknameCol;

	@FXML
	private TableColumn<Game, String> loserScoreCol;

	@FXML
	private TableColumn<Game, String> gameDurationCol;

	private SysData sd = SysData.getInstance();

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		gameNum.setCellValueFactory(new Callback<CellDataFeatures<Game, String>, ObservableValue<String>>()
		{
			@Override
			public ObservableValue<String> call(CellDataFeatures<Game, String> q)
			{
				return new ReadOnlyObjectWrapper((gamesTable.getItems().indexOf(q.getValue()) + 1) + "");
			}
		});
		gameNum.setSortable(false);

		date.setCellValueFactory(new PropertyValueFactory<Game, String>("finishDate"));
		winnerNicknameCol.setCellValueFactory(new PropertyValueFactory<Game, String>("winner"));
		winnerScoreCol.setCellValueFactory(new PropertyValueFactory<Game, String>("winnerScore"));
		loserNicknameCol.setCellValueFactory(new PropertyValueFactory<Game, String>("loser"));
		loserScoreCol.setCellValueFactory(new PropertyValueFactory<Game, String>("loserScore"));
		gameDurationCol.setCellValueFactory(new PropertyValueFactory<Game, String>("formattedDuration"));

		gamesTable.setItems(games);

		sd.loadGamesHistory();
		games.removeAll();
		games.addAll(sd.getGames());

	}

	@FXML
	void returnToHomePage(ActionEvent event)
	{
		try
		{
			sd.playMenuSound();
			Stage primaryStage = (Stage) gamesAnchorPane.getScene().getWindow();
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