package Model;

/**
 *  Interface representing a possible movement of a checker
 * @author user Sharks
 *
 */
public interface MoveResult
{
	public void tryCheckerMove(Checker queen,Tile[][]board,int x0, int y0,int newX,int newY);
	public MoveType getType();
	public Checker getEatenChecker();
}
