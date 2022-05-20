package Model;

/**
 * This class represents the move a player can make with a normal checker that is not a queen
 * @author user
 *
 */
public class CommonMoveResult implements MoveResult
{

	private MoveType type=MoveType.NONE;
	
	/**
	 * If not null represented a checker that can be eaten in a specific move
	 */
	private Checker eatenChecker;
	
	public CommonMoveResult()
	{
		
	}
	
	public CommonMoveResult(Checker checker,Tile[][]board,int x0, int y0,int newX,int newY)
	{
		tryCheckerMove(checker, board, x0, y0, newX, newY);
	}

	/**
	 * In this method the result of an attempted movement is updated, though not actual movement is made yet
	 */
	public void tryCheckerMove(Checker checker,Tile[][]board,int x0, int y0,int newX,int newY)
	{
		boolean directionConstraint = newY - y0 == checker.getType().moveDir;
		boolean inEatingSeq=checker.isInEatingsequence() &&
				Math.abs(newY - y0) ==2;

		if (Math.abs(newX - x0) == 1 && directionConstraint)
		{
			type=MoveType.NORMAL;
			return;
		} else if (Math.abs(newX - x0) == 2 && 
				(newY - y0 == checker.getType().moveDir * 2 || inEatingSeq))
		{

			int x1 = x0 + (newX - x0) / 2;
			int y1 = y0 + (newY - y0) / 2;

			if (board[x1][y1].hasChecker() && board[x1][y1].getChecker().getType() != checker.getType())
			{
				type=MoveType.EAT;
				this.eatenChecker=board[x1][y1].getChecker();
				return;
			}
		}

		type=MoveType.NONE;
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