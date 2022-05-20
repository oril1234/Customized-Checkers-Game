package View;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import Control.SysData;
import Model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class GameController implements Initializable
{
	/**
	 * Main pane of scene
	 */
	@FXML
	private AnchorPane gamePane;

	/**
	 * Pane that contains the game board and checkers
	 */
	@FXML
	private AnchorPane boardPane;

	/**
	 * Pane that contains game details
	 */
	@FXML
	private AnchorPane detailsPane;

	/**
	 * A group that holds the tiles elements to be placed on board
	 */
	@FXML
	private Group tilesGroup;

	/**
	 * A group that holds the checkers elements to be placed on tiles
	 */
	@FXML
	private Group checkersGroup;

	/**
	 * A label with the white player name
	 */
	@FXML
	private Label whitePLabel;

	/**
	 * A label with the white player score
	 */
	@FXML
	private Label whitePLabelScore;

	/**
	 * A label with the black player name
	 */
	@FXML
	private Label blackPLabel;

	/**
	 * A label with the black player score
	 */
	@FXML
	private Label blackPLabelScore;

	/**
	 * A label that contains the time passed since the game started
	 */
	@FXML
	private Label generalTimerLabel;

	/**
	 * A label that contains the time passed since the beginning of current turn
	 */
	@FXML
	private Label currentTimerLabel;

	/**
	 * Timer object for the time passed since the game started
	 */
	private Timeline generalTimer;

	/**
	 * Timer object for the time passed since the beginning of current turn
	 */
	private Timeline currentTimer;

	/**
	 * Object of message timer
	 */
	private Timeline messageTimer;

	/**
	 * A visual rectangle that is placed around the score of white player
	 */
	@FXML
	private Rectangle whiteRec;

	/**
	 * A visual rectangle that is placed around the score of black player
	 */
	@FXML
	private Rectangle blackRec;

	/**
	 * Milliseconds since the game started
	 */
	private int generalElapsedTime = 0;

	/**
	 * Milliseconds since the beginning of current turn
	 */
	private int currentElapseTime = 0;

	private int messageTimeElapse = 0;
	private boolean gameStarted;

	/**
	 * True when a question pop-up is open so tiles won't be painted with
	 * green/orange
	 */
	private boolean preventPainting = false;

	/**
	 * True if a player stepped on a blue tile
	 */
	private boolean didStepOnBlue = false;

	/**
	 * A pane that shows a message
	 */
	@FXML
	private StackPane messagePane;

	/**
	 * A message that appears tp [layer
	 */
	@FXML
	private Text messageText;

	private SysData sd = SysData.getInstance();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{

		gameStarted = true;
		SetLayOutPreferences();
		setScoreLablesStrokes();
		String whiteP = sd.getPlayerName("WHITE");
		String blackP = sd.getPlayerName("BLACK");
		whitePLabel.setText(whiteP);
		blackPLabel.setText(blackP);
		Tile[][] board = sd.getGameBoard();

		for (int y = 0; y < Constants.HEIGHT; y++)
		{

			for (int x = 0; x < Constants.HEIGHT; x++)
			{

				Tile tile = board[x][y];

				tilesGroup.getChildren().add(tile);
				if (tile.hasChecker())
				{

					Checker checker = tile.getChecker();
					checkersGroup.getChildren().add(checker);
					checker.setOnMouseDragEntered(e ->
					{
						messagePane.setVisible(false);
					});
					setHandlerForReleaseChecker(checker);

				}

			}
		}

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("");
		alert.setContentText(whiteP + ", you start first. Goodluck!");
		alert.setHeaderText(null);
		alert.showAndWait();

		prepareToNextTurn();
				
		initializeTimers();
		generalElapsedTime=sd.getGeneralTimeDuration();
		currentElapseTime=sd.getCurrentTurnDuration();


	}

	/**
	 * Initializing timers
	 */
	private void initializeTimers()
	{
		// Initializing the general timer of the game
		generalTimer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				sd.updateGameDuration();
				generalElapsedTime = generalElapsedTime + 1000;
				int hours = (generalElapsedTime / 3600000);
				int minutes = (generalElapsedTime / 60000) % 60;
				int seconds = (generalElapsedTime / 1000) % 60;
				String seconds_string = String.format("%02d", seconds);
				String minutes_string = String.format("%02d", minutes);
				String hours_string = String.format("%02d", hours);
				generalTimerLabel.setText(hours_string + ":" + minutes_string + ":" + seconds_string);

			}
		}));
		generalTimer.setCycleCount(Timeline.INDEFINITE);

		// Initializing timer of current player
		currentTimer = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				sd.updateGameCurrentDuration(currentElapseTime);
				currentElapseTime = currentElapseTime + 1000;
				int hours = (currentElapseTime / 3600000);
				int minutes = (currentElapseTime / 60000) % 60;
				int seconds = (currentElapseTime / 1000) % 60;
				String seconds_string = String.format("%02d", seconds);
				String minutes_string = String.format("%02d", minutes);
				String hours_string = String.format("%02d", hours);
				currentTimerLabel.setText(hours_string + ":" + minutes_string + ":" + seconds_string);

				if (currentElapseTime == 30000 && !preventPainting)
					sd.paintTileInGame(Color.GREEN);
				if (currentElapseTime == 90000 && !preventPainting)
					sd.paintTileInGame(Color.ORANGE);

			}

		}));
		currentTimer.setCycleCount(Timeline.INDEFINITE);
		resetGeneralTimer();
		resetCurrentTimer();
		
		
		generalTimer.play();
		currentTimer.play();
	}

	/**
	 * Add mouse release event handler to checker
	 * 
	 * @param checker checker
	 */
	public void setHandlerForReleaseChecker(Checker checker)
	{
		checker.setOnMouseReleased(e ->
		{
			// Get source and destination coordinates of checker
			int oldX = sd.getCoordinate(checker.getOldX());
			int oldY = sd.getCoordinate(checker.getOldY());
			int newX = sd.getCoordinate(checker.getLayoutX());
			int newY = sd.getCoordinate(checker.getLayoutY());

			// Checking first if movement is possible
			MoveResult result = sd.tryMove(checker, newX, newY);

			// Making the move if possible
			if (!result.getType().equals(MoveType.NONE))
			{
				sd.moveCheckerOnBoard(checker, result, oldX, oldY, newX, newY);
				handleAfterMove(checker, result, oldX, oldY, newX, newY);
			} else
				sd.cancelMove(checker);
		});
	}

	/**
	 * Setting the layout of details on board
	 */
	private void SetLayOutPreferences()
	{
		AnchorPane.setTopAnchor(boardPane, (double) Constants.TILE_SIZE);
		AnchorPane.setTopAnchor(detailsPane, (double) Constants.TILE_SIZE);
		double boardLeftAnchor = AnchorPane.getLeftAnchor(boardPane);
		double boardPixelWidth = Constants.WIDTH * Constants.TILE_SIZE;
		AnchorPane.setLeftAnchor(detailsPane, boardLeftAnchor + boardPixelWidth);
		detailsPane.setPrefHeight(boardPixelWidth);

	}

	/**
	 * This method moves the checker to a new position and other actions afterwards
	 * 
	 * @param checker The checker to move
	 * @param result  The move the checker does
	 * @param x0      The source x
	 * @param y0      The source y
	 * @param x1      The destination x
	 * @param y1      The destination y
	 */
	public void handleAfterMove(Checker checker, MoveResult result, int x0, int y0, int x1, int y1)
	{
		// Occurs when eat was made
		if (sd.getEatenCheckerId() != null)
		{
			checkersGroup.getChildren().remove(new Checker(sd.getEatenCheckerId()));
			sd.deleteEatenCheckerID();

			showMessage(
					"Checker Was Just eaten." + Game.currentTurn.color + " earned " + Constants.EATPOINTS + " points",
					MessageType.CHECKER_EATEN);

		}

		// Occurs when a checker was excluded for not eating
		if (sd.getPunishedCheckerID() != null)
		{
			showMessage(Game.currentTurn.color + " Checker lost for not eating", MessageType.CHECKER_NOT_EATEN);
			checkersGroup.getChildren().remove(new Checker(sd.getPunishedCheckerID()));
			sd.deletePunishedCheckerID();
			prepareToNextTurn();
			return;
		}

		if (checker.isQueen())
		{
			if (result instanceof QueenMoveResult)
			{
				Integer modX = ((QueenMoveResult) result).getModifiedX();
				Integer modY = ((QueenMoveResult) result).getModifiedY();

				x1 = modX != null ? modX : x1;
				y1 = modY != null ? modY : y1;
			}

			else
			{
				showMessage(Game.currentTurn.color + " checker has become queen", MessageType.BECAME_QUEEN);
			}
		}

		// Occurs if a pkayer can move the same checker twice for stepping on red tile
		if (sd.isTileColor(x1, y1, Color.RED) && checker.canDrag())
		{
			showMessage(Game.currentTurn.color + " checker has another turn", MessageType.HAS_ANOTHER_TURN);
			sd.decorateCheckerForStepOnRed(checker);
		}

		// Updating result according to time of movement
		int seconds = currentElapseTime / 1000;
		sd.updatePlayerScore(60 - seconds);

		// check privileges of
		sd.handlePrivileges();

		if (sd.isTileColor(x1, y1, Color.BLUE))
		{
			preventPainting = true;
			stepOnBlueTile();
		}

		else if (sd.isTileColor(x1, y1, Color.YELLOW))
		{
			preventPainting = true;
			askQuestions();
		} else
		{
			prepareToNextTurn();
		}

	}

	/**
	 * This method is for updating the score in the labels
	 */
	private void updateScoreLabels()
	{
		
		whitePLabelScore.setText(sd.getPlayerScore("WHITE") + "");
		blackPLabelScore.setText(sd.getPlayerScore("BLACK") + "");

	}

	/**
	 * Creates a question pop up when player steps on yellow tile
	 */
	private void askQuestions()
	{
		try
		{
			int beforeQuestion = sd.getPlayerScore(Game.currentTurn.color);
			sd.playQuestionsPopupSound();
			Stage window = new Stage();
			window.initModality(Modality.WINDOW_MODAL);
			window.initOwner(gamePane.getScene().getWindow());
			FXMLLoader louder = new FXMLLoader();
			louder.setLocation(getClass().getResource("/View/QuestionsToPlayers.fxml"));
			louder.load();
			Parent parent = louder.getRoot();
			Scene scene = new Scene(parent, 772, 529);

			window.setTitle("Answer Question");
			window.setScene(scene);

			window.setOnHidden(e ->
			{
				int difference = sd.getPlayerScore(Game.currentTurn.color) - beforeQuestion;
				if (difference >= 0)
					showMessage(Game.currentTurn.color + " player earned " + difference + " points for correct answer",
							MessageType.QUESTION_ANSWERED);
				else
					showMessage(Game.currentTurn.color + " player lost " + (-difference) + " points for wrong answer",
							MessageType.QUESTION_ANSWERED);
				prepareToNextTurn();

			});
			window.show();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Opening pop up for stepping on blue tile
	 */
	public void stepOnBlueTile()
	{
		try
		{
			sd.playMenuSound();

			Stage window = new Stage();
			window.initModality(Modality.WINDOW_MODAL);
			window.initOwner(gamePane.getScene().getWindow());
			FXMLLoader louder = new FXMLLoader();
			louder.setLocation(getClass().getResource("/View/PickTileAfterStepOnBlue.fxml"));
			louder.load();
			Parent parent = louder.getRoot();
			Scene scene = new Scene(parent);
			window.setScene(scene);
			window.initStyle(StageStyle.UTILITY);
			window.setOnHidden(e ->
			{
				didStepOnBlue = true;

				Tile chosenTile = sd.getChosenTileAfterStepOnBlue();
				if (chosenTile != null)
				{
					restoreChecker(chosenTile.getXBoard(), chosenTile.getYBoard());
				}
				prepareToNextTurn();

			});

			window.show();

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Restore checker on tile
	 * 
	 * @param x x of tile
	 * @param y y of tile
	 */
	private void restoreChecker(int x, int y)
	{
		Checker restoredChecker = null;
		String currentColor = Game.currentTurn.color;
		if (currentColor.equals("WHITE"))
		{
			restoredChecker = new Checker(CheckerType.WHITE, x, y, false);
			if (y == 0)
				restoredChecker.setQueen(true);
		} else
		{
			restoredChecker = new Checker(CheckerType.BLACK, x, y, false);
			if (y == Constants.HEIGHT - 1)
				restoredChecker.setQueen(true);
		}

		checkersGroup.getChildren().add(restoredChecker);
		setHandlerForReleaseChecker(restoredChecker);
		sd.setChecker(x, y, restoredChecker);

	}

	/**
	 * set the rectangle around the current player's score
	 */
	private void setScoreLablesStrokes()
	{
		if (sd.isTurn("WHITE"))
		{
			whiteRec.setStrokeWidth(3);
			blackRec.setStrokeWidth(0);
		} else
		{
			whiteRec.setStrokeWidth(0);
			blackRec.setStrokeWidth(3);
		}

	}

	/**
	 * Making preparations to next turn
	 */
	private void prepareToNextTurn()
	{

		// Allow painting of green and orange tiles
		preventPainting = false;

		// Reseting timer of current turn
		resetCurrentTimer();

		// Updating the labels that render the score
		updateScoreLabels();

		// Check if there's a checker that is allowed to have a consecutive turn or
		// stepped on blue tile
		if (!sd.checkPriviliges() || didStepOnBlue)
		{
			didStepOnBlue = false;
			if (!gameStarted)
			{
				sd.changeTurn();
			}
			sd.updateGamePossibleMoves();
			setScoreLablesStrokes();
			sd.resetAllCheckersState();
		}
		if (!gameStarted)
			checkGameEnded();

		gameStarted = false;

		// Painting Yellow and Red tiles for next turn
		sd.prepareTilesToNexTurn();
	}

	/**
	 * Exiting game if it's finished
	 */
	public void checkGameEnded()
	{
		if (sd.isGameEnded())
		{
			generalTimer.stop();
			currentTimer.stop();
			messageTimer.stop();
			generalTimer.getKeyFrames().clear();
			currentTimer.getKeyFrames().clear();
			messageTimer.getKeyFrames().clear();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("");
			alert.setContentText("Game has just ended");

			alert.setHeaderText(null);
			alert.showAndWait();
			try
			{
				sd.playMenuSound();
				Stage primaryStage = (Stage) gamePane.getScene().getWindow();
				AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/View/GameSummary.fxml"));
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/View/application.css").toExternalForm());

				if (primaryStage == null)
				{
					primaryStage = (Stage) gamePane.getScene().getWindow();
				}
				primaryStage.setScene(scene);
				primaryStage.setTitle("Home");
				primaryStage.show();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reset the timer of the current player
	 */
	public void resetCurrentTimer()
	{

		currentElapseTime = 0;
		int hours = (currentElapseTime / 3600000);
		int minutes = (currentElapseTime / 60000) % 60;
		int seconds = (currentElapseTime / 1000) % 60;
		String seconds_string = String.format("%02d", seconds);
		String minutes_string = String.format("%02d", minutes);
		String hours_string = String.format("%02d", hours);
		currentTimerLabel.setText(hours_string + ":" + minutes_string + ":" + seconds_string);

	}

	/**
	 * Reset the general timer of the game;
	 */
	public void resetGeneralTimer()
	{

		generalElapsedTime = 0;
		int hours = (generalElapsedTime / 3600000);
		int minutes = (generalElapsedTime / 60000) % 60;
		int seconds = (generalElapsedTime / 1000) % 60;
		String seconds_string = String.format("%02d", seconds);
		String minutes_string = String.format("%02d", minutes);
		String hours_string = String.format("%02d", hours);
		generalTimerLabel.setText(hours_string + ":" + minutes_string + ":" + seconds_string);

	}

	private void showMessage(String message, MessageType mType)
	{
		messagePane.setVisible(true);
		messageTimeElapse = 0;
		
		
		if(messageTimer!=null)
		{
			messageTimer.stop();
			messageTimer.getKeyFrames().clear();

		}

		// Initializing the general timer of the game
		messageTimer = new Timeline(new KeyFrame(Duration.seconds(0.5), new EventHandler<ActionEvent>()
		{

			@Override
			public void handle(ActionEvent event)
			{
				messageTimeElapse += 500;
				if (messageTimeElapse == 5000)
				{
					messageTimer.stop();
					messageTimer.getKeyFrames().clear();
					messageTimeElapse = 0;
					messagePane.setVisible(false);
				}

			}
		}));
		messageTimer.setCycleCount(Timeline.INDEFINITE);
		messageText.setText(message);

		switch (mType) {
		case MUST_EAT:
			messageText.setStyle("-fx-fill: black");
			break;
		case CHECKER_EATEN:
			messageText.setStyle("-fx-fill: #FFFFFF");
			break;
		case BECAME_QUEEN:
			messageText.setStyle("-fx-fill: #FFFFFF");
			break;
		case CHECKER_NOT_EATEN:
			messageText.setStyle("-fx-fill: #FFFFFF");
			break;
		case HAS_ANOTHER_TURN:
			messageText.setStyle("-fx-fill: #FFFFFF");
			break;

		}

		messageTimer.play();

	}

	@FXML
	void exitFromGame(ActionEvent event)
	{
		generalTimer.pause();
		currentTimer.pause();
		sd.playMenuSound();
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Exit Game");
		alert.setContentText("Would you like to save game");
		alert.setHeaderText(null);
		alert.getButtonTypes().remove(ButtonType.OK);
		alert.getButtonTypes().add(ButtonType.YES);
		alert.getButtonTypes().add(ButtonType.NO);
		alert.getButtonTypes().add(ButtonType.CANCEL);

		alert.showAndWait();

		if (alert.getResult() != ButtonType.CANCEL)
		{
			try
			{
				sd.playMenuSound();
				generalTimer.stop();
				generalTimer.getKeyFrames().clear();

				currentTimer.stop();
				currentTimer.getKeyFrames().clear();

				if (messageTimer != null)
				{
					messageTimer.stop();
					messageTimer.getKeyFrames().clear();
				}

				if (alert.getResult() == ButtonType.YES)
				{
					saveGameBeforeExit();
				}
				Stage primaryStage = (Stage) gamePane.getScene().getWindow();
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
		} else
		{
			generalTimer.play();
			currentTimer.play();

		}

	}

	private void saveGameBeforeExit()
	{
		FileChooser chooser = new FileChooser();
		String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
		chooser.setInitialDirectory(new File(currentPath));

		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		chooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = chooser.showSaveDialog(new Stage());

		if (file != null)
		{
			sd.saveGameToFile(file);
		}

	}

	@FXML
	void onPause(ActionEvent event)
	{
		generalTimer.pause();
		currentTimer.pause();
		sd.playMenuSound();
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Game Paused");
		alert.setContentText("Game was paused");
		alert.setHeaderText(null);
		alert.getButtonTypes().remove(ButtonType.OK);
		ButtonType cont = new ButtonType("Continue", ButtonData.OK_DONE);
		alert.getButtonTypes().add(cont);

		alert.showAndWait();

		if (alert.getResult().getButtonData() == ButtonData.OK_DONE)
		{
			try
			{
				sd.playMenuSound();
				generalTimer.play();
				currentTimer.play();

			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

}
