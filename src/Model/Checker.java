package Model;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

/**
 * This class represents a checker to be played with in the game of Hamka
 **/

public class Checker extends StackPane
{
	/**
	 * Identifier of checker
	 */
	private int checkerId;
	private static int assignId = 0;
	/**
	 * Type of checker (Light or dark)
	 */
	private CheckerType type;
	
	/**
	 * Ellipse that represents the checker
	 */
	private Ellipse bg;

	/**
	 * True if this checker is queen
	 */
	private boolean isQueen = false;

	/**
	 * The current x location of the checker when dragged by mouse
	 */
	private double mouseX;

	/**
	 * The current y location of the checker when dragged by mouse
	 */
	private double mouseY;

	/**
	 * The current x location of the checker
	 */
	private double oldX;

	/**
	 * The current y location of the checker
	 */
	private double oldY;

	/**
	 * Ellipse that represents the checker
	 */
	private Ellipse ellipse;
	
	private int tileSize;

	/**
	 * True when this checker ate rival checker in current turn
	 */
	private boolean canDrag = true;
	
	private boolean inEatingSequence=false;

	public Checker(int id, int x, int y)
	{
		this.checkerId = id;
		oldX = x;
		oldY = y;
	}
	
	public void setCheckerID(int id)
	{
		this.checkerId=id;
	}
	
	public int getCheckerID()
	{
		return checkerId;
	}

	public Checker(int id)
	{
		this.checkerId=id;
	}
	public Checker(CheckerType type, int x, int y,boolean isMiniChecker)
	{
		if(!isMiniChecker)
			checkerId = assignId++;
		this.type = type;

		tileSize=!isMiniChecker?Constants.TILE_SIZE:Constants.MINI_TILE_SIZE;
		move(x, y);

		// Create an ellipse javafx element to represent a checker
		bg = new Ellipse(tileSize * 0.3125, tileSize * 0.26);
		bg.setFill(Color.BLACK);

		bg.setStroke(Color.BLACK);
		bg.setStrokeWidth(tileSize * 0.03);

		bg.setTranslateX((tileSize - tileSize * 0.3125 * 2) / 2);
		bg.setTranslateY((tileSize - tileSize * 0.26 * 2) / 2 + tileSize * 0.07);

		ellipse = new Ellipse(tileSize * 0.3125, tileSize * 0.26);
		ellipse.setFill(type == CheckerType.BLACK ? Color.valueOf("#212e13") : Color.valueOf("#fff9f4"));

		ellipse.setStroke(Color.BLACK);
		ellipse.setStrokeWidth(tileSize * 0.03);
		

		ellipse.setTranslateX((tileSize - tileSize * 0.3125 * 2) / 2);
		ellipse.setTranslateY((tileSize - tileSize * 0.26 * 2) / 2);

		getChildren().addAll(bg, ellipse);

		setOnMousePressed(e ->
		{
			mouseX = e.getSceneX();
			mouseY = e.getSceneY();

		});

		setOnMouseDragged(e ->
		{
			this.toFront();
			ellipse.toFront();
			if (Game.currentTurn.color == this.type.toString() && canDrag)
			{
				// boolean hasEatingSeq=Game.checkPlayerWithEatingSequence();
				// if((hasEatingSeq && this.isInEatingSequence()) || !hasEatingSeq )
				relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
			}
		});
	}

	public CheckerType getType()
	{
		return type;
	}
	
	public void setEllipseStroke(Color color)
	{
		bg.setStroke(color);
		bg.setStrokeWidth(tileSize * 0.07);
	}
	
	public void setEllipseStrokeWidth(double width)
	{
		bg.setStrokeWidth(width);
	}

	public double getOldX()
	{
		return oldX;
	}

	public double getOldY()
	{
		return oldY;
	}

	public boolean isQueen()
	{
		return isQueen;
	}
	
	public boolean canDrag()
	{
		return canDrag;
	}

	/**
	 * Allowing or preventing a checker from moving
	 * @param state True if checker can be draged
	 */
	public void setCanDrag(boolean state)
	{
		canDrag=state;
	}
	
	/**
	 * Turning the checker to queen
	 * @param isQueen If true checker is queen
	 */
	public void setQueen(boolean isQueen)
	{
		this.isQueen = isQueen;
		if(isQueen)
			this.setEllipseStroke(Color.GOLD);
	}

	public Ellipse getElipse()
	{
		return ellipse;
	}

	/**
	 * This method moves the checker to the the x and y provided
	 * 
	 * @param x new x coordinate
	 * @param y new y coordinate
	 */
	public void move(int x, int y)
	{
		if (Game.currentTurn.color == null)
		{
			Game.currentTurn.color = "WHITE";
		}

		oldX = x * tileSize;
		oldY = y * tileSize;
		relocate(oldX, oldY);

	}
	
	/**
	 *  Allowing checker that ate to move in the same turn in order to eat another
	 * @param state True if checker ate another, and still can eat at the same turn
	 */
	public void setInEatingSequence(boolean state)
	{
		inEatingSequence=state;
	}

	/**
	 * 
	 * @return True if checker ate another, and still can eat at the same turn
	 */
	public boolean isInEatingsequence()
	{
		return inEatingSequence;
	}
	
	/**
	 * Preventing attempted movement of checker
	 */
	public void abortMove()
	{
		relocate(oldX, oldY);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + checkerId;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Checker other = (Checker) obj;
		return checkerId == other.checkerId;
	}
	
	/**
	 * Decorate checker if placed on red tile
	 */
	public void decorateForStepOnRed()
	{
		
		if(!isQueen)
			setEllipseStroke(Color.RED);
		else
			setEllipseStroke(Color.PURPLE);
		
	}
	
	/**
	 * Reseting the original stroke around the checker
	 */
	public void restoreStroke()
	{
		if(!isQueen)
			bg.setStroke(Color.BLACK);
		else
			bg.setStroke(Color.GOLD);
		bg.setStrokeWidth(tileSize * 0.03);
	}

}
