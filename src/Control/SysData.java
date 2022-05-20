package Control;

import java.io.File;
import java.io.FileNotFoundException;

import java.io.FileReader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.cliftonlabs.json_simple.Jsoner;

import Model.Checker;
import Model.CheckerType;
import Model.Game;
import Model.MoveResult;
import Model.MoveType;
import Model.Player;
import Model.Question;
import Model.Tile;
import View.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/*
 * This class is a controller of home view
 */

public class SysData implements Initializable
{
	@FXML
	private AnchorPane homeAnchorPane;

	/**
	 * Current white
	 */
	private Player whitePlayer;

	/**
	 * Current black player
	 */
	private Player blackPlayer;

	/**
	 * Current game
	 */
	private Game game;

	private static SysData instance = null;

	private static List<Question> questions;
	static ObservableList<Question> observableQuestions;

	/**
	 * Chose tile to restore checker on after stepping on blue tile
	 */
	public Tile choseTileToRestoreChecker;

	private static List<Game> games;

	// static method to create instance of Singleton class
	public static SysData getInstance()
	{
		if (instance == null)
		{
			instance = new SysData();
			questions = new ArrayList<>();
			games = new ArrayList<>();
			observableQuestions = FXCollections.observableArrayList();

		}
		return instance;
	}

	/**
	 * This method is used to read data from json
	 * 
	 * @throws IOException    exception
	 * @throws ParseException exception
	 */
	public void loadQuestionsDetails() throws IOException, ParseException
	{
		questions = new ArrayList<>();
		String fileName = "Questions.json";
		FileReader reader;
		JSONObject jsO = new JSONObject();
		try
		{
			reader = new FileReader(fileName);
			JSONParser jsonParser = new JSONParser();
			jsO = (JSONObject) jsonParser.parse(reader);

			JSONArray jsAr = (JSONArray) jsO.get("questions");
			for (int i = 0; i < jsAr.size(); i++)
			{

				JSONObject currItem = ((JSONObject) jsAr.get(i));
				Question question = new Question(Integer.parseInt(currItem.get("level") + ""),
						(String) currItem.get("question"), (List<String>) currItem.get("answers"),
						Integer.parseInt(currItem.get("correct_ans") + ""), (String) currItem.get("team"));
				questions.add(question);
			}
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method is used for fetching the questions
	 * 
	 * @return questions
	 */
	public List<Question> getQuestions()
	{
		if (questions == null)
			try
			{
				loadQuestionsDetails();
			} catch (IOException | ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return questions;
	}

	/**
	 * Updating questions json
	 * 
	 * @param questionsToUpdate list to update json
	 */
	public static void updateJson(List<Question> questionsToUpdate)
	{
		questions = new ArrayList<>();
		questions.addAll(questionsToUpdate);
		JSONArray questionsArr = new JSONArray();

		for (Question q : questions)
		{
			JSONArray answers = new JSONArray();
			for (String ans : q.getAnswers())
			{
				answers.add(ans);
			}
			JSONObject inner = new JSONObject();

			inner.put("question", q.getContent());
			inner.put("answers", answers);
			inner.put("correct_ans", q.getCorrectAnswerIdx());
			inner.put("level", q.getQuestionLevel());
			inner.put("team", q.getTeam());
			questionsArr.add(inner);

		}
		JSONObject outer = new JSONObject();

		if (questions.size() > 0)
		{
			outer.put("questions", questionsArr);
		}

		@SuppressWarnings("resource")
		FileWriter writer;
		try
		{
			writer = new FileWriter("Questions.json");
			writer.write(Jsoner.prettyPrint(outer.toJSONString()));
			writer.flush();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	// Sounds
	public synchronized void playMenuSound()
	{
		new Thread(new Runnable()
		{
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run()
			{
				try
				{
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem
							.getAudioInputStream(Main.class.getResourceAsStream("/sounds/MenuButtons.wav"));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e)
				{
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public static synchronized void playStartGameSound()
	{
		new Thread(new Runnable()
		{
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run()
			{
				try
				{
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem
							.getAudioInputStream(Main.class.getResourceAsStream("/sounds/StartGame.wav"));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e)
				{
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public synchronized void playQuestionsPopupSound()
	{
		new Thread(new Runnable()
		{
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run()
			{
				try
				{
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem
							.getAudioInputStream(Main.class.getResourceAsStream("/sounds/QuestionPopup.wav"));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e)
				{
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public static synchronized void playErrorSound()
	{
		new Thread(new Runnable()
		{
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run()
			{
				try
				{
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem
							.getAudioInputStream(Main.class.getResourceAsStream("/sounds/Error.wav"));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e)
				{
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public static synchronized void playWrongAnswerSound()
	{
		new Thread(new Runnable()
		{
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run()
			{
				try
				{
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem
							.getAudioInputStream(Main.class.getResourceAsStream("/sounds/WrongAnswer.wav"));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e)
				{
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public static synchronized void playCorrectAnswerSound()
	{
		new Thread(new Runnable()
		{
			// The wrapper thread is unnecessary, unless it blocks on the
			// Clip finishing; see comments.
			public void run()
			{
				try
				{
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem
							.getAudioInputStream(Main.class.getResourceAsStream("/sounds/CorrectAnswer.wav"));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e)
				{
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		if (questions == null)
		{
			questions = new ArrayList<>();
			try
			{
				loadQuestionsDetails();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	////////////////////////////// History////////////////////////////////////

	public void loadGamesHistory()
	{
		try
		{
			games = new ArrayList<>();
			String fileName = "Games.json";
			FileReader reader;
			JSONObject jsO = new JSONObject();

			reader = new FileReader(fileName);
			JSONParser jsonParser = new JSONParser();
			jsO = (JSONObject) jsonParser.parse(reader);

			JSONArray jsAr = (JSONArray) jsO.get("games");
			for (int i = 0; i < jsAr.size(); i++)
			{

				JSONObject currItem = ((JSONObject) jsAr.get(i));
				Game game = new Game(new Player("WHITE", currItem.get("winner") + ""),
						new Player("BLACK", currItem.get("loser") + ""), null);
				game.setFinishDate(currItem.get("date") + "");
				game.setWinner(currItem.get("winner") + "");
				game.setWinnerScore(currItem.get("winner_score") + "");
				game.setLoser(currItem.get("loser") + "");
				game.setLoserScore(currItem.get("loser_score") + "");
				game.setFormattedDuration(currItem.get("duration") + "");

				games.add(game);
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void addCurrentGameToJson()
	{
		if (games.size() >= 10)
			games.remove(0);
		games.add(game);

		JSONArray gamesArr = new JSONArray();

		for (Game g : games)
		{

			JSONObject inner = new JSONObject();

			inner.put("date", g.getFinishDate());
			inner.put("winner", g.getWinner());
			inner.put("winner_score", g.getWinnerScore());
			inner.put("loser", g.getLoser());
			inner.put("loser_score", g.getLoserScore());
			inner.put("duration", g.getFormattedDuration());
			gamesArr.add(inner);

		}
		JSONObject outer = new JSONObject();

		if (games.size() > 0)
		{
			outer.put("games", gamesArr);
		}

		@SuppressWarnings("resource")
		FileWriter writer;
		try
		{
			writer = new FileWriter("Games.json");
			writer.write(Jsoner.prettyPrint(outer.toJSONString()));
			writer.flush();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public List<Game> getGames()
	{
		return games;
	}

	//////////////////////////// Game methods //////////////////////////////////////

	public void instantiateGame()
	{

		game = new Game(whitePlayer, blackPlayer, null);
	}

	public void loadGame(List<String> reloadedGame)
	{
		game = new Game(whitePlayer, blackPlayer, reloadedGame);

	}

	public void saveGameToFile(File file)
	{
		try
		{
			PrintWriter writer;
			writer = new PrintWriter(file);
			writer.println(game.getSavedGameString());
			writer.close();
		} catch (IOException ex)
		{
		}
	}

	public void instantiatePlayer(String color, String nickname)
	{
		if (color.equals("WHITE"))
			whitePlayer = new Player(color, nickname);
		else
			blackPlayer = new Player(color, nickname);
	}

	public Game getGame()
	{
		return game;
	}

	public String getPlayerName(String color)
	{
		if (color.equals("WHITE"))
			return whitePlayer.getNickName();

		return blackPlayer.getNickName();
	}

	public void updatePlayerScore(int addedPoints)
	{
		Game.currentTurn.updateScore(addedPoints);

	}

	public int getPlayerScore(String color)
	{
		if(color.equals("WHITE"))
			return Game.whitePlayer.score;
		
		return Game.blackPlayer.score;

	}

	public void setChecker(int x, int y, Checker checker)
	{

		game.setCheckerOnTile(checker, x, y);
	}

	public Tile getTile(int x, int y)
	{
		return game.getTile(x, y);
	}

	public Tile[][] getGameBoard()
	{
		return game.getBoard();
	}

	public void addTileToBoard(Tile tile)
	{
		game.setTile(tile);

	}

	public int getCoordinate(double pixel)
	{
		return game.toBoard(pixel);
	}

	public boolean hasChecker(int x, int y)
	{
		Tile returnedValue = game.getTile(x, y);
		return returnedValue != null ? returnedValue.hasChecker() : false;
	}

	public MoveResult tryMove(Checker checker, int newX, int newY)
	{
		return game.tryCheckerMove(checker, newX, newY);
	}

	public void cancelMove(Checker checker)
	{
		checker.abortMove();

	}

	public boolean isTurn(String color)
	{
		return Game.currentTurn.getColor().equals(color);
	}

	public void relocateChecker(Checker checker, int newX, int newY)
	{
		checker.move(newX, newY);

	}

	public boolean isTileColor(int x1, int y1, Color color)
	{
		return color.equals(game.getTile(x1, y1).getFill());
	}

	public void changeTurn()
	{
		Game.changeTurn();

	}

	public void changeToQueen(Checker checker)
	{
		checker.setQueen(true);

	}

	public void updateGamePossibleMoves()
	{
		String currentColor = Game.currentTurn.color;
		game.updatePossibleMove(CheckerType.valueOf(currentColor));

	}

	public Set<Tile> getGamePossibleMoves(MoveType mType)
	{
		return mType.equals(MoveType.NORMAL) ? game.getNormalMovetiles() : game.getEatMoveTiles();
	}

	public Set<Checker> getPossibleEaters()
	{
		return game.getEaters();
	}

	public void paintBoardTile(Tile tile, Color color)
	{
		game.paintTile(tile, color);

	}

	public boolean isCurrentColor()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isGameEnded()
	{
		return game.isGameEnded();
	}

	public int getCurrPlayerOrdinaryCheckersNum()
	{
		if (Game.currentTurn.getColor() == "WHITE")
			return whitePlayer.getOrdinaryCheckersNum();
		return blackPlayer.getOrdinaryCheckersNum();
	}

	public void setChosenTileafterstepOnBlue(Tile tile)
	{
		choseTileToRestoreChecker = tile;
	}

	public Tile getChosenTileAfterStepOnBlue()
	{
		return choseTileToRestoreChecker;
	}

	public void changeCheckerPossition(Checker checker, MoveResult result, int x0, int y0, int x1, int y1)
	{
		game.executeMove(checker, result, x0, y0, x1, y1);

	}

	public Integer getPunishedCheckerID()
	{
		return game.getPunishedCheckerID();
	}

	public void deletePunishedCheckerID()
	{
		game.setPunishedCheckerId(null);

	}

	public Integer getEatenCheckerId()
	{
		return game.getEatenCheckerId();
	}

	public void deleteEatenCheckerID()
	{
		game.setEatenCheckerId(null);

	}

	public void paintTileInGame(Color color)
	{

		if (color.equals(Color.GREEN))
		{
			game.generateGreenTile();
		}

		if (color.equals(Color.ORANGE))
		{
			game.generateOrangeTiles();
		}

	}

	public void moveCheckerOnBoard(Checker checker, MoveResult result, int oldX, int oldY, int newX, int newY)
	{
		game.executeMove(checker, result, oldX, oldY, newX, newY);

	}

	public boolean checkPriviliges()
	{
		return game.hasRedtilePrivilege() || game.hasEatingSequence();
	}

	public void handlePrivileges()
	{
		game.handlePrivilegesForChecker();
	}

	public void resetAllCheckersState()
	{
		game.resetPriviledges();

	}

	public void prepareTilesToNexTurn()
	{
		game.repaintDarkTiles();
		game.generateYellowTiles();
		game.generateRedTile();
		game.generateBlueTile();

	}

	public void decorateQueen(Checker checker)
	{
		checker.setEllipseStroke(Color.GOLD);
		checker.setEllipseStrokeWidth(3);
	}

	public boolean checkValidityOfTileForRestoringCheckers(Tile tile)
	{
		return game.canRestoreCheckerOnTile(tile);
	}

	public void updateGameDuration()
	{
		game.updateDuration();
	}

	public void updateGameCurrentDuration(int updatedTime)
	{
		game.updateCurrentDuration(updatedTime);

	}

	public void updateFinishDate()
	{
		game.updateFinishDate();
	}

	public void updateFinalScore(String winnerScore, String loserScore)
	{
		game.updateFinalScore(winnerScore, loserScore);

	}

	public void updateWinnerAndLoser(String winner, String loser)
	{
		game.updateWinnerAndLoser(winner, loser);

	}

	public void decorateCheckerForStepOnRed(Checker checker)
	{
		checker.decorateForStepOnRed();

	}
	
	public int getGeneralTimeDuration()
	{
		return game.getGeneralDuration();
	}
	
	public int getCurrentTurnDuration()
	{
		return game.getCurrentDuration();
	}

}
