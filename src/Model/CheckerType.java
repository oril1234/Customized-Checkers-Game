package Model;

/**
 * This enum is for type of checker
 * @author user sharks
 *
 */
public enum CheckerType
{
	BLACK(1), WHITE(-1);

	/**
	 * Direction to which checker move  
	 */
	final int moveDir;

	CheckerType(int moveDir)
	{
		this.moveDir = moveDir;
	}


}
