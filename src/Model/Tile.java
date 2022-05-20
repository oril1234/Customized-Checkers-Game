package Model;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
/**
 * This class is of tile in Hamka represented as rectangle
 * @author user
 *
 */
public class Tile extends Rectangle
{
	/**
	 * Color of tile 
	 */
	private String color;
	
	/**
	 * x position of tile 
	 */
	private final int xBoard;
	
	/**
	 * y position of tile 
	 */
	private final int yBoard;
	
	/**
	 * Checker on tile
	 */
	private Checker checker;

	/**
	 * 
	 * @return whether or not this tile has checker on it
	 */
	public boolean hasChecker()
	{
		return checker != null;
	}

	public Checker getChecker()
	{
		return checker;
	}

	public void setChecker(Checker checker)
	{
		this.checker = checker;
	}

	public void setColor(String color)
	{
		this.color = color;
		setFill(Color.valueOf(color));
	}
	
	public Tile(int x, int y)
	{
		xBoard=x;
		yBoard=y;
	}

	public Tile(boolean light, int x, int y)
	{
		xBoard=x;
		yBoard=y;
		setWidth(Constants.TILE_SIZE);
		setHeight(Constants.TILE_SIZE);

		relocate(x * Constants.TILE_SIZE, y * Constants.TILE_SIZE);

		color = light ? Constants.LIGHT_TILE : Constants.DARK_TILE;

		setFill(Color.valueOf(color));
	}
	
	/**
	 * 
	 * @return The x coordinate of tile
	 */
	public int getXBoard()
	{
		return xBoard;
	}
	
	/**
	 * 
	 * @return The y coordinate of tile
	 */
	public int getYBoard()
	{
		return yBoard;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + xBoard;
		result = prime * result + yBoard;
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
		Tile other = (Tile) obj;
		return other.getXBoard()==xBoard && other.getYBoard()==yBoard;

	}
	
	
	

}
