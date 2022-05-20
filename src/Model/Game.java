package Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javafx.scene.paint.Color;

/**
 * This class represents the game of Hamka
 **/

public class Game
{

	/**
	 * The board on which the game is played
	 */
	public Tile[][] board;

	/**
	 * White Player
	 */
	public static Player whitePlayer;

	/**
	 * Black Player
	 */
	public static Player blackPlayer;

	/**
	 * The player whom its current turn
	 */
	public static Player currentTurn;

	/**
	 * Variable that represents the time since the beginning of the game
	 */
	public int milSecGeneralDuration = 0;
	
	/**
	 * Variable that represents the time since the beginning of the current turn
	 */
	public int milSecCurrentDuration = 0;


	/**
	 * String that represents the duration of the game in the format of HH:MM:SS
	 */
	private String formattedDuration;

	/**
	 * Reachable tiles through normal move
	 */
	private Set<Tile> normalMoveTiles;

	/**
	 * Reachable tiles through eating a rival checker
	 */
	private Set<Tile> eatMoveTiles;

	/**
	 * Tiles with no checkers
	 */
	private Set<Tile> emptyTiles;

	/**
	 * Checkers that are able to make a normal move
	 */
	private Set<Checker> normalMoveCheckers;

	/**
	 * Checkers that are able to eat rival checkers
	 */
	private Set<Checker> eaters;

	/**
	 * The ID of the last checker that was removed for not eating rival checker
	 */
	private Integer punishedCheckerID;

	/**
	 * The ID of the last checker that was eaten
	 */
	private Integer eatenCheckerId;

	/**
	 * True if in current turn a checker was placed on a red checker and is able to
	 * make another move
	 */
	private boolean hasRedTilePrivilege = false;

	/**
	 * True if in current turn a checker ate a rival checker and can eat another
	 */
	private boolean hasEatingSequence;

	/**
	 * The last checker that could make another consecutive turn
	 */
	private Checker priviledged;

	/**
	 * String representation of finish date of game
	 */
	public String finishDate;

	/**
	 * The name of the winner
	 */
	private String winner;

	/**
	 * The score of the winner
	 */
	private String winnerScore;

	/**
	 * The name of the loser
	 */
	private String loser;

	/**
	 * The score of the loser
	 */
	private String loserScore;

	public Game(Player whitePlayer, Player blackPlayer, List<String> loadedGameDetails)
	{
		board = new Tile[Constants.WIDTH][Constants.HEIGHT];
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
		if (loadedGameDetails == null)
			currentTurn = this.whitePlayer;
		
		
		else
		{
			boolean isWhite=loadedGameDetails.get(Constants.VALID_LOADED_FILE_LENGTH-1).equals("W");
			currentTurn=isWhite?Game.whitePlayer:Game.blackPlayer;
		}
		
		instantiateFields();
		if (loadedGameDetails == null)
			instantiateBoard();
		else
			loadGame(loadedGameDetails);
	}

	/**
	 * Instantiating all the data structures used in the game
	 */
	private void instantiateFields()
	{
		normalMoveTiles = new HashSet<>();
		eatMoveTiles = new HashSet<>();
		emptyTiles = new HashSet<>();
		normalMoveCheckers = new HashSet<>();
		eaters = new HashSet<>();
	}

	/**
	 * Instantiating board when game starts from the initial state
	 */
	private void instantiateBoard()
	{
		for (int y = 0; y < Constants.HEIGHT; y++)
		{

			for (int x = 0; x < Constants.HEIGHT; x++)
			{
				boolean isLightTile = (x + y) % 2 == 0;
				Tile tile = new Tile(isLightTile, x, y);
				board[x][y] = tile;
				Checker checker = null;

				if (y > 2 && y < 5 && !isLightTile)
					emptyTiles.add(tile);

				if (y <= 2 && !isLightTile)
				{
					checker = new Checker(CheckerType.BLACK, x, y, false);
				}

				if (y >= 5 && (x + y) % 2 != 0)
				{
					checker = new Checker(CheckerType.WHITE, x, y, false);
				}

				if (checker != null)
				{
					board[x][y].setChecker(checker);
				}

			}
		}

	}

	/**
	 * A method to instantiate a board and checker according to a text file
	 * 
	 * @param gameDetals The list of game details loaded from text
	 */
	public void loadGame(List<String> gameDetals)
	{
		Iterator<String> detailsIterator = gameDetals.iterator();
		for (int y = 0; y < Constants.HEIGHT; y++)
		{

			for (int x = 0; x < Constants.WIDTH; x++)
			{
				boolean isLightTile = (x + y) % 2 == 0;
				Tile tile = new Tile(isLightTile, x, y);
				board[x][y] = tile;
				Checker checker = null;
				if (!isLightTile && detailsIterator.hasNext())
				{
					String item = detailsIterator.next();
					switch (item) {
					case "1":
						checker = new Checker(CheckerType.WHITE, x, y, false);
						tile.setChecker(checker);
						if(y==0)
							checker.setQueen(true);
						break;
					case "11":
						checker = new Checker(CheckerType.WHITE, x, y, false);
						checker.setQueen(true);
						tile.setChecker(checker);
						break;
					case "2":
						checker = new Checker(CheckerType.BLACK, x, y, false);
						tile.setChecker(checker);
						if(y==Constants.HEIGHT-1)
							checker.setQueen(true);
						break;
					case "22":
						checker = new Checker(CheckerType.BLACK, x, y, false);
						checker.setQueen(true);
						tile.setChecker(checker);
					case "0":
						emptyTiles.add(tile);
					}
				}

			}
		}
		if(detailsIterator.hasNext())
			detailsIterator.next();
		
		if(detailsIterator.hasNext())
			Game.whitePlayer.setNickName(detailsIterator.next());
		if(detailsIterator.hasNext())
			Game.whitePlayer.updateScore(Integer.parseInt(detailsIterator.next()));
		
		if(detailsIterator.hasNext())
			Game.blackPlayer.setNickName(detailsIterator.next());
		if(detailsIterator.hasNext())
			Game.blackPlayer.updateScore(Integer.parseInt(detailsIterator.next()));
		
		if(detailsIterator.hasNext())
			milSecGeneralDuration=Integer.parseInt(detailsIterator.next());
		
		if(detailsIterator.hasNext())
			milSecCurrentDuration=Integer.parseInt(detailsIterator.next());

		
	}

	public void setTile(Tile tile)
	{
		board[tile.getXBoard()][tile.getYBoard()] = tile;
	}

	public Tile getTile(int x, int y)
	{

		return isInBoard(x, y) ? board[x][y] : null;
	}

	public void setCheckerOnTile(Checker checker, int x, int y)
	{
		if (isInBoard(x, y))
		{
			emptyTiles.remove(new Tile(x, y));
			board[x][y].setChecker(checker);
		}

	}

	/**
	 * Method to check if one of the of the players is out of checkers
	 * @return True if one of the players ran out of checker
	 */
	public boolean hasZeroCheckers()
	{
		int countWhiteCheckers = 0, countBlackCheckers = 0;
		for (int i = 0; i < Constants.WIDTH; i++)
		{
			for (int j = 0; j < Constants.HEIGHT; j++)
			{
				if (board[i][j].hasChecker())
				{
					if (board[i][j].getChecker().getType() == CheckerType.WHITE)
						countWhiteCheckers++;
					else
						countBlackCheckers++;
				}
			}
		}
		return countWhiteCheckers == 0 || countBlackCheckers == 0;

	}

	/**
	 * A method that checks if game ended according to number of checker and ability
	 * of players to move
	 * 
	 * @return True if game has to end
	 */
	public boolean isGameEnded()
	{
		return hasZeroCheckers() || (normalMoveCheckers.size() == 0 && eaters.size() == 0);

	}

	/**
	 * Delivering the current turn to the the other player
	 */
	public static void changeTurn()
	{
		if (currentTurn.color == "WHITE")
		{
			currentTurn=blackPlayer;
		} else if (currentTurn.color == "BLACK")
		{
			currentTurn=whitePlayer;

		}
		
		
	}

	/**
	 * This method updates a set of possible coordinates the player can move to
	 * 
	 * @param cType checker type
	 */
	public void updatePossibleMove(CheckerType cType)
	{
		normalMoveTiles = new HashSet<>();
		normalMoveCheckers = new HashSet<>();
		eatMoveTiles = new HashSet<>();
		eaters = new HashSet<>();
		emptyTiles = new HashSet<>();
		for (int x = 0; x < Constants.WIDTH; x++)
		{
			for (int y = 0; y < Constants.HEIGHT; y++)
			{
				Checker checker = board[x][y].getChecker();

				if (checker == null && (x + y) % 2 != 0)
				{
					emptyTiles.add(board[x][y]);
				}

				int i = 1;
				while (checker != null && checker.getType().equals(cType) && i <= Constants.WIDTH)
				{
					
					int xLeft = x - i, xRight = x + i;
					int yUp = y - i, yDown = y + i;

					MoveResult result = tryCheckerMove(checker, xLeft, yUp);
					updateSpecificCheckerAndRelativeTiles(checker, result, xLeft, yUp);

					result = tryCheckerMove(checker, xLeft, yDown);
					updateSpecificCheckerAndRelativeTiles(checker, result, xLeft, yDown);

					result = tryCheckerMove(checker, xRight, yUp);
					updateSpecificCheckerAndRelativeTiles(checker, result, xRight, yUp);

					result = tryCheckerMove(checker, xRight, yDown);
					updateSpecificCheckerAndRelativeTiles(checker, result, xRight, yDown);

					i++;
				}

			}
		}
	}

	/**
	 * Adding a checker to sets of checkers that can move and adding a tile to sets
	 * of reachable tiles
	 * 
	 * @param checker The checker to update
	 * @param mResult The type of movement that is possible for the checker
	 * @param x       The x coordinate of a reachable tile
	 * @param y       The y coordinate of a reachable tile
	 */
	public void updateSpecificCheckerAndRelativeTiles(Checker checker, MoveResult mResult, int x, int y)
	{

		if (mResult.getType().equals(MoveType.NONE))
			return;

		if (mResult instanceof QueenMoveResult)
		{
			Integer modX = ((QueenMoveResult) mResult).getModifiedX();
			Integer modY = ((QueenMoveResult) mResult).getModifiedY();
			x = modX != null ? modX : x;
			y = modY != null ? modY : y;

		}

		if (mResult.getType().equals(MoveType.NORMAL))
		{
			normalMoveCheckers.add(checker);
			normalMoveTiles.add(board[x][y]);
		}

		if (mResult.getType().equals(MoveType.EAT))
		{

			eaters.add(checker);
			eatMoveTiles.add(board[x][y]);
		}
		emptyTiles.add(board[x][y]);

	}

	public Set<Tile> getNormalMovetiles()
	{
		return normalMoveTiles;
	}

	public Set<Tile> getEatMoveTiles()
	{
		return eatMoveTiles;
	}

	public Set<Checker> getEaters()
	{
		return eaters;
	}

	/**
	 * This method returns the result of an attempted movement of a checker
	 * @param checker The checker to be moved
	 * @param newX    The new x coordinate of checker
	 * @param newY    The new y coordinate of checker
	 * @return An enum result of the movement
	 */
	public MoveResult tryCheckerMove(Checker checker, int newX, int newY)
	{


		if ((newX + newY) % 2 == 0 || (!isInBoard(newX, newY) && !checker.isQueen())
				|| (isInBoard(newX, newY) && board[newX][newY].hasChecker()))
		{
			return new CommonMoveResult();
		}


		int x0 = toBoard(checker.getOldX());
		int y0 = toBoard(checker.getOldY());

		if (checker.isQueen())
		{
			return new QueenMoveResult(checker, board, x0, y0, newX, newY);
		}

		return new CommonMoveResult(checker, board, x0, y0, newX, newY);

	}

	/**
	 * This method moves the checker to a new position and other actions afterwards
	 * @param checker The checker to move
	 * @param result  The move the checker does
	 * @param x0      The source x
	 * @param y0      The source y
	 * @param x1      The destination x
	 * @param y1      The destination y
	 */
	public void executeMove(Checker checker, MoveResult result, int x0, int y0, int x1, int y1)
	{
		resetPriviledges();
		board[x0][y0].setChecker(null);

		if (checker.isQueen())
		{

			Integer modX = ((QueenMoveResult) result).getModifiedX();
			Integer modY = ((QueenMoveResult) result).getModifiedY();

			x1 = modX != null ? modX : x1;
			y1 = modY != null ? modY : y1;

		}

		//setting checker on new tile
		board[x1][y1].setChecker(checker);

		//Executed when checker ate opponent
		if (result.getType() == MoveType.EAT)
		{

			Game.currentTurn.updateScore(Constants.EATPOINTS);
			Checker eatenChecker = result.getEatenChecker();
			eatenCheckerId = eatenChecker.getCheckerID();
			int xEaten = toBoard(eatenChecker.getOldX());
			int yEaten = toBoard(eatenChecker.getOldY());
			board[xEaten][yEaten].setChecker(null);
			emptyTiles.add(new Tile(xEaten, yEaten));
			hasEatingSequence = true;
			priviledged = checker;
			checker.setInEatingSequence(true);
		}

		//Updating the new location of checker
		checker.move(x1, y1);

		// Remove the current coordinate from set of empty tiles
		emptyTiles.remove(board[x1][y1]);

		// Add the previous tile to set of tiles without checkers
		emptyTiles.add(board[x0][y0]);

		/*
		 * If a player can eat rival checker and doesn't do that he will be punished
		 * with a loss of a checker that was able to eat
		 */
		if (eaters.size() > 0 && result.getType() == MoveType.NORMAL)
		{
			punishForNotEating();
			return;
		}

		//Granting a player with 50 points after stepping on a green tile
		if (board[x1][y1].getFill().equals(Color.GREEN))
		{
			Game.currentTurn.updateScore(Constants.GREEN_SCORE);
		}

		//Mark the checker as one that can maybe make another movement in the same turn
		if (board[x1][y1].getFill().equals(Color.RED))
		{
			priviledged = checker;
			hasRedTilePrivilege = true;
		}

		// Turning the checker to a queen
		if (!checker.isQueen() && y1 == Constants.HEIGHT - 1 || y1 == 0)
			checker.setQueen(true);

	}

	/**
	 * Removing a random checker that could be used to eat another checker and did no do so
	 */
	private void punishForNotEating()
	{
		int randomIndex = new Random().nextInt(eaters.size());
		int i = 0;

		for (Checker c : eaters)
		{

			if (i == randomIndex)
			{

				punishedCheckerID = c.getCheckerID();
				int x = toBoard(c.getOldX());
				int y = toBoard(c.getOldY());
				board[x][y].setChecker(null);
				emptyTiles.add(board[x][y]);
			}
			i++;
		}

	}

	/**
	 * This method returns the x or y coordinates of tile on which a queen can land on exiting the board
	 * @param coordinate coordinate
	 * @return Modified coordinate
	 */

	public int getModifiedCoordinate(int coordinate)
	{
		if (coordinate == -1)
			return Constants.WIDTH - 1;
		return coordinate == Constants.WIDTH ? 0 : coordinate;
	}

	/**
	 * Returns the x or y coordinate of tile according to pixel inserted
	 * @param pixel the inserted pixel
	 * @return the x or y coordinate
	 */
	public int toBoard(double pixel)
	{

		int retVal = (int) (pixel + (pixel < 0 ? -1 : 1) * Constants.TILE_SIZE / 2) / Constants.TILE_SIZE;
		return retVal;
	}

	
	
	public void paintTile(Tile tile, Color color)
	{

		if (tile != null && tile.hasChecker())
			return;

		if (tile != null)
			board[tile.getXBoard()][tile.getYBoard()].setFill(color);

		
		//If color is green a random reachable tile will be painted with green
		else if (color.equals(Color.GREEN))
		{
			Set<Tile> possibleMoves = new HashSet<>();
			possibleMoves.addAll(normalMoveTiles);
			possibleMoves.addAll(eatMoveTiles);

			int randomIndex = 0;

			if (possibleMoves.size() > 0)
				randomIndex = new Random().nextInt(possibleMoves.size());

			int i = 0;
			for (Tile t : possibleMoves)
			{
				
				if (i == randomIndex)
				{

					int x = t.getXBoard();
					int y = t.getYBoard();
					board[x][y].setFill(color);
				}
				i++;
			}
		} else
		{
		}

	}


	public Tile[][] getBoard()
	{
		return board;
	}

	public Player getWhitePlayer()
	{
		return whitePlayer;
	}

	public void setWhitePlayer(Player whitePlayer)
	{
		Game.whitePlayer = whitePlayer;
	}

	public Player getBlackPlayer()
	{
		return blackPlayer;
	}

	public void setBlackPlayer(Player blackPlayer)
	{
		Game.blackPlayer = blackPlayer;
	}

	public Player getCurrentTurn()
	{
		return currentTurn;
	}

	public void setCurrentTurn(Player currentTurn)
	{
		Game.currentTurn = currentTurn;
	}

	/**
	 * True if x,y is within the board
	 * @param x x
	 * @param y y
	 * @return true if tile is in board
	 */
	public boolean isInBoard(int x, int y)
	{
		return x >= 0 && x < Constants.WIDTH && y >= 0 && y < Constants.HEIGHT;
	}

	
	/**
	 * A queen can't be far more than one unit from board in it's x,y coordinate
	 * @param x x
	 * @param y y
	 * @return true if checker is far less than 2 units from board
	 */
	public boolean isValidDeviationOfQueen(int x, int y)
	{
		return x >= -1 && x <= Constants.WIDTH && y >= -1 && y <= Constants.WIDTH;
	}

	public Integer getPunishedCheckerID()
	{
		return punishedCheckerID;

	}
	
	public void setPunishedCheckerId(Integer id)
	{
		this.punishedCheckerID = id;

	}


	/**
	 * Repaint dark tiles when turn changes
	 */
	public void repaintDarkTiles()
	{
		// Repaint dark tiles
		for (int x = 0; x < Constants.WIDTH; x++)
		{
			for (int y = 0; y < Constants.HEIGHT; y++)
			{
				if ((x + y) % 2 != 0)
					board[x][y].setFill(Color.valueOf(Constants.DARK_TILE));

			}
		}
	}


	
	public Integer getEatenCheckerId()
	{
		return eatenCheckerId;

	}

	public void setEatenCheckerId(Integer id)
	{
		this.eatenCheckerId = id;

	}

	/**
	 * Painting 3 random tiles with Yellow
	 */
	public void generateYellowTiles()
	{

		int[] indices = new Random().ints(0, emptyTiles.size()).distinct().limit(3).toArray();
		int random1 = indices[0];
		int random2 = indices[1];
		int random3 = indices[2];
		int i = 0;

		for (Tile tile : emptyTiles)
		{
			if (i == random1 || i == random2 || i == random3)
				paintTile(tile, Color.YELLOW);
			i++;
		}
	}

	/**
	 * Generating green tile randomly
	 */
	public void generateGreenTile()
	{

		paintTile(null, Color.GREEN);

	}

	/**
	 * Generating orange tiles
	 */
	public void generateOrangeTiles()
	{
		Set<Tile> possibleMoves = new HashSet<>();
		possibleMoves.addAll(normalMoveTiles);
		possibleMoves.addAll(eatMoveTiles);

		for (Tile tile : possibleMoves)
		{
			paintTile(tile, Color.ORANGE);
		}

	}

	/**
	 * Generating a red tile
	 */
	public void generateRedTile()
	{
		int randomIndex = 0, i = 0;

		if (normalMoveTiles.size() > 0)
			randomIndex = new Random().nextInt(normalMoveTiles.size());
		boolean paintedRed = false;

		// Red tile is generated if no checker can eat
		if (eaters.size() == 0)
		{

			Tile yellowTile = null;
			for (Tile tile : normalMoveTiles)
			{
				if (i == randomIndex)
				{
					if (!tile.getFill().equals(Color.YELLOW))
					{

						tile.setFill(Color.RED);
						paintedRed = true;
					} else
					{
						yellowTile = tile;
						randomIndex++;
					}
				}
				i++;
			}
			if (!paintedRed && yellowTile != null)
				yellowTile.setFill(Color.RED);

		}

	}

	/**
	 * Generating blue tile when a player has 2 checkers and 1 queen
	 */
	public void generateBlueTile()
	{
		int queensNum = 0, checkersNum = 0;
		String currentColor = Game.currentTurn.getColor();
		CheckerType currtype = CheckerType.valueOf(currentColor);
		for (int x = 0; x < Constants.WIDTH; x++)
		{
			for (int y = 0; y < Constants.WIDTH; y++)
			{
				if (board[x][y].hasChecker() && board[x][y].getChecker().isQueen()
						&& board[x][y].getChecker().getType().equals(currtype))
					queensNum++;
				else if (board[x][y].hasChecker() && !board[x][y].getChecker().isQueen()
						&& board[x][y].getChecker().getType().equals(currtype))
					checkersNum++;
			}
		}

		if (queensNum == 1 && checkersNum == 2)
		{
			int randIndex = new Random().nextInt(emptyTiles.size());
			int i = 0;

			Tile differentColorTile = null;
			for (Tile tile : emptyTiles)
			{
				if (i == randIndex)
				{
					if (!tile.getFill().equals(Color.YELLOW) && !tile.getFill().equals(Color.RED))
						board[tile.getXBoard()][tile.getYBoard()].setFill(Color.BLUE);
					else
						differentColorTile = tile;
				}

				i++;
			}
			if (differentColorTile != null)
			{
				board[differentColorTile.getXBoard()][differentColorTile.getYBoard()].setFill(Color.BLUE);
			}
		}
	}

	/**
	 * 
	 * @return True if checker can move in the same turn after stepping on red tile
	 */
	public boolean hasRedtilePrivilege()
	{
		return hasRedTilePrivilege;
	}

	/**
	 * 
	 * @return True if checker can eat another rival in the same turn
	 */
	public boolean hasEatingSequence()
	{
		return hasEatingSequence;
	}

	/**
	 * Deleting all privileges of player for next turn
	 */
	public void resetPriviledges()
	{
		hasEatingSequence = false;
		hasRedTilePrivilege = false;
		priviledged = null;
		for (int x = 0; x < Constants.WIDTH; x++)
		{
			for (int y = 0; y < Constants.HEIGHT; y++)
			{
				if (board[x][y].hasChecker())
				{
					Checker checker = board[x][y].getChecker();
					board[x][y].getChecker().setCanDrag(true);
					board[x][y].getChecker().setInEatingSequence(false);
					checker.restoreStroke();
				}

			}
		}

	}

	/**
	 * Checking if a player can use his movement privilege for stepping
	 * on red tile or eating an opponent
	 */
	public void handlePrivilegesForChecker()
	{
		if (priviledged == null)
			return;
		updatePossibleMove(priviledged.getType());
		boolean inNormal = normalMoveCheckers.contains(priviledged);
		boolean inEaters = eaters.contains(priviledged);

		if (priviledged.isInEatingsequence() && inEaters || (hasRedTilePrivilege && (inNormal || inEaters)))
		{
			
			for (int x = 0; x < Constants.WIDTH; x++)
			{
				for (int y = 0; y < Constants.HEIGHT; y++)
				{
					if (board[x][y].hasChecker() && !board[x][y].getChecker().equals(priviledged))
						board[x][y].getChecker().setCanDrag(false);
					;
				}
			}
			priviledged.setCanDrag(true);
		}

		else
			resetPriviledges();

	}

	/**
	 *
	 * @param tile A tile to check
	 * @return True if A tile can be used to contains a restored checker after stepping on blue tile 
	 */
	public boolean canRestoreCheckerOnTile(Tile tile)
	{
		CheckerType cType = CheckerType.valueOf(Game.currentTurn.getColor());
		if (tile.hasChecker())
			return false;
		int x = tile.getXBoard();
		int y = tile.getYBoard();
		int i = 1;
		boolean enemyNotFound = true;
		while (enemyNotFound && i <= 2)
		{
			int xLeft = x - i, xRight = x + i;
			int yUp = y - i, yDown = y + i;
			enemyNotFound = enemyNotFound && noCloseChecker(cType, x, y, xLeft, yUp);
			enemyNotFound = enemyNotFound && noCloseChecker(cType, x, y, xLeft, yDown);
			enemyNotFound = enemyNotFound && noCloseChecker(cType, x, y, xRight, yUp);
			enemyNotFound = enemyNotFound && noCloseChecker(cType, x, y, xRight, yDown);
			i++;

		}

		return enemyNotFound;
	}

	/**
	 * This method checks if a coordinates x,y and x1,y1 don't contain opposite checkers
	 * 
	 * @param cType The of checker to check
	 * @param x x
	 * @param y y 
	 * @param x1 x1
	 * @param y1 y1
	 * @return True if a coordinates x,y and x1,y1 don't contain opposite checkers
	 */
	public boolean noCloseChecker(CheckerType cType, int x, int y, int x1, int y1)
	{

		if (!isValidDeviationOfQueen(x1, y1))
			return true;

		if (isInBoard(x1, y1) && (!board[x1][y1].hasChecker()
				|| (board[x1][y1].hasChecker() && board[x1][y1].getChecker().getType().equals(cType))))
		{
			return true;
		}

		// Check if there is a close queen right beyond borders of board
		else if (isValidDeviationOfQueen(x1, y1) && (x1 == -1 || x1 == Constants.WIDTH)
				|| (y1 == -1 || y1 == Constants.HEIGHT))
		{

			x1 = getModifiedCoordinate(x1);
			y1 = getModifiedCoordinate(y1);
			Checker checker = board[x1][y1].getChecker();
			if (!board[x1][y1].hasChecker() || (board[x1][y1].hasChecker() && !checker.isQueen())
					|| (board[x1][y1].hasChecker() && checker.getType().equals(cType)))
			{

				return true;
			}

		}

		return false;
	}

	/**
	 * Updating the duration of the game
	 */
	public void updateDuration()
	{
		milSecGeneralDuration += 1000;
		int hours = (milSecGeneralDuration / 3600000);
		int minutes = (milSecGeneralDuration / 60000) % 60;
		int seconds = (milSecGeneralDuration / 1000) % 60;
		String seconds_string = String.format("%02d", seconds);
		String minutes_string = String.format("%02d", minutes);
		String hours_string = String.format("%02d", hours);
		this.formattedDuration = hours_string + ":" + minutes_string + ":" + seconds_string;

	}
	
	public int getGeneralDuration()
	{
		return milSecGeneralDuration;
	}
	
	/**
	 * Updating the duration of current turn
	 */
	public void updateCurrentDuration(int updatedTime)
	{
		milSecCurrentDuration=updatedTime;
	}

	public int getCurrentDuration()
	{
		return milSecCurrentDuration;
	}
	
	/**
	 * Set formatted game duration in the pattern of HH:MM:SS
	 * @param formattedDuration duration dtring
	 */
	public void setFormattedDuration(String formattedDuration)
	{
		this.formattedDuration = formattedDuration;
	}

	public String getFormattedDuration()
	{

		return formattedDuration;

	}

	/**
	 * Updating finish date of game 
	 */
	public void updateFinishDate()
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		this.finishDate = dtf.format(now);
	}

	/**
	 * Setting a specific finish date from text file
	 * @param date finish date
	 */
	public void setFinishDate(String date)
	{
		this.finishDate = date;
	}

	public String getFinishDate()
	{
		return finishDate;
	}

	/**
	 * Updating the final score 
	 * @param winnerScore winner score
	 * @param loserScore loser score
	 */
	public void updateFinalScore(String winnerScore, String loserScore)
	{
		this.winnerScore = winnerScore;
		this.loserScore = loserScore;

	}

	public String getWinner()
	{
		return winner;
	}

	public void setWinner(String winner)
	{
		this.winner = winner;
	}

	public String getLoser()
	{
		return loser;
	}

	public void setLoser(String loser)
	{
		this.loser = loser;
	}

	public String getWinnerScore()
	{
		return winnerScore;
	}

	public void setWinnerScore(String winnerScore)
	{
		this.winnerScore = winnerScore;
	}

	public String getLoserScore()
	{
		return loserScore;
	}

	public void setLoserScore(String loserScore)
	{
		this.loserScore = loserScore;
	}

	public void updateWinnerAndLoser(String winner, String loser)
	{
		this.winner = winner;
		this.loser = loser;

	}
	
	public String getSavedGameString()
	{
		String savedGame="";
		int counter=0;
		
		for(int i=0;i<Constants.TILES_NUMBER;i++)
		
		{
			int x=i%Constants.WIDTH;
			int y=i/Constants.HEIGHT;
			if((x+y)%2!=0)
			{

				Checker checker=board[x][y].getChecker();
				
				if(checker!=null)
				{
					switch (checker.getType()) {
					case WHITE:
						savedGame=!checker.isQueen()?savedGame+"1,":savedGame+"11,";
						break;
						
					case BLACK:
						savedGame=!checker.isQueen()?savedGame+"2,":savedGame+"22,";
						break;

					}
				}
				else
					savedGame=savedGame+"0,";
				if((++counter)%4==0)
					savedGame=savedGame+"\n";
			}
		}
		savedGame=Game.currentTurn.color.equals("WHITE")?(savedGame+"W"+"\n"):(savedGame+"B"+"\n");
		savedGame=savedGame+whitePlayer.getNickName()+"\n";
		savedGame=savedGame+whitePlayer.score+"\n";
		savedGame=savedGame+blackPlayer.getNickName()+"\n";
		savedGame=savedGame+blackPlayer.score+"\n";
		savedGame=savedGame+milSecGeneralDuration+"\n";
		savedGame=savedGame+milSecCurrentDuration+"";
		return savedGame;
	}
	
	

}
