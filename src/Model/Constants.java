package Model;
/**
 * This is a class of constant values
 * @author user
 *
 */
public class Constants
{

	public static final int INITIAL_CHECKERS_NUM=12;
	
	/**
	 * Number of tiles vertically
	 */
	public static final int HEIGHT=8;
	
	/**
	 * Number of tiles horizontally
	 */
	public static final int WIDTH=8;
	
	public static final int TILES_NUMBER=64;
	
	/**
	 * Size in pixel of tile
	 */
	public static final int TILE_SIZE = 80;
	
	
	public static final int MINI_TILE_SIZE = 48;

	/**
	 * Color of light tile 
	 */
	public static final String LIGHT_TILE= "#7dfefe";
	
	/**
	 * Color of dark tile 
	 */
	public static final String DARK_TILE="#01a2a2";
	
	/**
	 * The score a player is given when stepping on green tile
	 */
	public static final int GREEN_SCORE = 50;

	/**
	 * Yellpw color
	 */
	public static final String YELLOW_TILE = "#FFFF00";

	/**
	 * Points earned for eating a checker
	 */
	public static final int EATPOINTS = 100;

	/**
	 * The minimum length of a file that is loaded from local computer
	 */
	public static final int MIN_FILE_NAME_LENGTH = 4;

	/**
	 * The color in which queen is painted when stepping on red tile
	 */
	public static final String QUEEN_ON_RED = "#F0A778";
	
	/**
	 * Valid length of a file of a saved game
	 */
	public static final int VALID_SAVED_FILE_LENGTH=39;
	
	/**
	 * Valid length of a file of a loaded game
	 */
	public static final int VALID_LOADED_FILE_LENGTH=33;
	
	/**
	 * The index of string that tells whose turn it is in a loaded game 
	 */
	public static final int LOADED_GAME_TURN_INDEX=32;
	
}
