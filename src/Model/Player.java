package Model;


/**
 * A class of a player in Hamka game
 * @author user Sharks
 *
 */
public class Player
{
	/**
	 * Nickname of player
	 */
	private String nickname;

	
	/**
	 * Score of player in a game
	 */
	public int score=0;
	
	/**
	 * color of checkers of player
	 */
	public String color;
	
	private int activeCheckersNum;
	private int activeOrdinaryCheckersNum;
	private int activeQueensNum;

	public Player(String color, String nickname)
	{
		this.nickname = nickname;
		this.color=color;
		this.activeCheckersNum=Constants.INITIAL_CHECKERS_NUM;
		this.activeOrdinaryCheckersNum=Constants.INITIAL_CHECKERS_NUM;
		this.activeQueensNum=0;
	}


	public String getNickName()
	{
		return nickname;
	}

	public String getColor()
	{
		return color;
	}
	public void setColor(String color)
	{
		this.color = color;
	}
	public void setNickName(String nickname)
	{
		this.nickname = nickname;
	}

	public int getScore()
	{
		return score;
	}


	public void updateScore(int addedPoints)
	{
		this.score+=addedPoints;
		
	}
	
	public void updateOrdinaryCheckersNum(int ordinaryNum)
	{
		this.activeOrdinaryCheckersNum=ordinaryNum;
	}
	
	public int getOrdinaryCheckersNum()
	{
		return activeOrdinaryCheckersNum;
	}
	
	public void updateQueensNum(int queensNum)
	{
		activeQueensNum=queensNum;
	}
	
	public int getQueensNum()
	{
		return activeQueensNum;
	}
	
	
}
