package Model;

/**
 * This class represents an attempted movement of a queen
 * @author user
 *
 */
public class QueenMoveResult implements MoveResult
{
	/**
	 * The type of move that is possible for the queen
	 */
	private MoveType type=MoveType.NONE;
	
	/**
	 * The checker that could be eaten if a queen ate
	 */
	private Checker eatenChecker;
	
	/**
	 * Modified x coordinate of attempted movement of a queen out of board 
	 */
	private Integer modifiedX=null;
	
	/**
	 * Modified y coordinate of attempted movement of a queen out of board 
	 */
	private Integer modifiedY=null;
	
	public QueenMoveResult()
	{
		
	}
	
	
	public QueenMoveResult(Checker queen,Tile[][]board,int x0, int y0,int newX,int newY)
	{
		tryCheckerMove(queen, board, x0, y0, newX, newY);
	}

	
	/**
	 * This method updates the result of an attempted movement of a queen from x0,y0 to newx,newY
	 */
	public void tryCheckerMove(Checker queen,Tile[][]board,int x0, int y0,int newX,int newY)
	{
		//Queen is allowed to exit board not more than one tile distance in each direction
		boolean inBounderiesOfQueen=newX>-2 && newX<=Constants.WIDTH && newY>-2 && newY<=Constants.HEIGHT;
		if (Math.abs(newX - x0) != Math.abs(newY - y0) || newX == x0 || newY == y0 || !inBounderiesOfQueen)
		{
			type=MoveType.NONE;
			return;
		}


		CheckerType checkerType = queen.getType();
		int sameTypeNum = 0;
		int rivalTypeNum = 0;
		int xEaten = -1;
		int yEaten = -1;
		boolean newXBigger = newX > x0;
		boolean newYBigger = newY > y0;

		x0 = newXBigger ? x0 + 1 : x0 - 1;
		y0 = newYBigger ? y0 + 1 : y0 - 1;

		//Counting number of checker between x0,y0 and newX,newY
		while (newX != x0|| newY != y0 && sameTypeNum == 0 && rivalTypeNum <= 1)
		{
			boolean xConstraints = x0 >= 0 && x0 < Constants.WIDTH;
			boolean yConstraints = y0 >= 0 && y0 < Constants.HEIGHT;

			if (xConstraints & yConstraints && board[x0][y0].hasChecker())
			{
				if (board[x0][y0].getChecker().getType().equals(checkerType))
					sameTypeNum++;

				else
				{
					xEaten = x0;
					yEaten = y0;
					rivalTypeNum++;
				}
			}

			x0 = newXBigger ? x0 + 1 : x0 - 1;
			y0 = newYBigger ? y0 + 1 : y0 - 1;
		}

		//Movement is not possible when there's checker of the same type between old and new coordinates
		if (sameTypeNum > 0)
		{
			type=MoveType.NONE;
			return;
		}
		boolean boardConstraints = newX < Constants.WIDTH && newX >= 0 && newY < Constants.HEIGHT && newY >= 0;

		// 1 rival was detected
		if (rivalTypeNum == 1)
		{
			// New Coordinate is out of borders
			if (!boardConstraints)
			{

				newX = getModifiedCoordinate(newX);
				newY = getModifiedCoordinate(newY);
				if(board[newX][newY].hasChecker())
				{
					type=MoveType.NONE;
					return;
				}
				
				type=MoveType.EAT;
				this.eatenChecker=board[xEaten][yEaten].getChecker();
				this.modifiedX=newX;
				this.modifiedY=newY;
				return;
			}

			type=MoveType.EAT;
			this.eatenChecker=board[xEaten][yEaten].getChecker();
			return;
		}

		// No checker detected on the path between source coordinate and destination one
		if (sameTypeNum + rivalTypeNum == 0)
		{

			// Destination x and y are not out of the board
			if (boardConstraints)
			{
				this.type=MoveType.NORMAL;
				return;
			}

			// Modifying new x and y if they're out of borders
			newX = getModifiedCoordinate(newX);
			newY = getModifiedCoordinate(newY);


			// If first tile beyond board borders is free checker will be located there
			if (!board[newX][newY].hasChecker())
			{
				this.type=MoveType.NORMAL;
				this.modifiedX=newX;
				this.modifiedY=newY;
				return;
			}

			// If first tile beyond board borders contains checker of the same type movement
			// is aborted
			else if (board[newX][newY].getChecker().getType().equals(checkerType))
			{
				this.type=MoveType.NONE;
				return;
			}

			/*
			 * This condition is met for sure when first tile beyond board borders contains
			 * checker of rival
			 */
			else
			{
				xEaten = newX;
				yEaten = newY;
				rivalTypeNum++;
			}

			// Movement towards new tile
			newX = newXBigger ? newX + 1 : newX - 1;
			newX = getModifiedCoordinate(newX);
			newY = newYBigger ? newY + 1 : newY - 1;
			newY = getModifiedCoordinate(newY);

			if (!board[newX][newY].hasChecker())
			{
				this.type=MoveType.EAT;
				this.eatenChecker=board[xEaten][yEaten].getChecker();
				this.modifiedX=newX;
				this.modifiedY=newY;
				return;
			}

		}

		this.type=MoveType.NONE;
	}

	private int getModifiedCoordinate(int coordinate)
	{
		if (coordinate == -1)
			return Constants.WIDTH - 1;
		return coordinate == Constants.WIDTH ? 0 : coordinate;

	}
	public QueenMoveResult(MoveType type, Checker checker)
	{
		this.type = type;
		this.eatenChecker = checker;
	}


	public Integer getModifiedX()
	{
		return modifiedX;
	}
	
	public Integer getModifiedY()
	{
		return modifiedY;
	}

	public MoveType getType()
	{
		return type;
	}

	public Checker getEatenChecker()
	{
		return eatenChecker;
	}


}
