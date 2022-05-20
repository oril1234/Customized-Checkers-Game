package View;

import java.net.URL;

import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import Control.SysData;
import Model.Checker;
import Model.Constants;
import Model.Tile;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Controller of scene of picking a tile to restore checker on
 * @author user sharks 
 *
 */
public class PickTileAfterStepOnBlueController implements Initializable
{
	/**
	 * Main pane of scene
	 */
	@FXML
	private AnchorPane mainPane;

	@FXML
	private Group tilesGroup;
	
	@FXML
	private Group checkersGroup;

	private Set<Tile> possibleTiles;

	@FXML
	private Button confirmBtn;
	
	private SysData sd=SysData.getInstance();
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		Tile[][] board=sd.getGameBoard();
		possibleTiles = new HashSet<>();
		
		for (int x = 0; x < Constants.WIDTH; x++)
		{

			for (int y = 0; y < Constants.HEIGHT; y++)
			{
				boolean isLightTile = (x + y) % 2 == 0;
				Tile tile = new Tile(x, y);
				tile.setWidth(Constants.MINI_TILE_SIZE);
				tile.setHeight(Constants.MINI_TILE_SIZE);

				tile.relocate(x * Constants.MINI_TILE_SIZE, y * Constants.MINI_TILE_SIZE );

				if (!isLightTile)
				{
					if(board[x][y].hasChecker())
					{
						Checker checker=new Checker(board[x][y].getChecker().getType(), x, y,true);
						checker.setCheckerID(board[x][y].getChecker().getCheckerID());
						checker.setCanDrag(false);
						checkersGroup.getChildren().add(checker);
					}
					tile.setFill(Color.valueOf(Constants.DARK_TILE));
					if (sd.checkValidityOfTileForRestoringCheckers(board[x][y]))
					{
						possibleTiles.add(tile);
				
						tile.setOnMouseClicked(e ->
						{
							confirmBtn.setDisable(false);
							for (Tile t : possibleTiles)
							{
								if (!t.equals(tile))
									t.setStrokeWidth(0);
							}
							tile.setStroke(Color.GOLD);
							tile.setStrokeWidth(5);
							sd.setChosenTileafterstepOnBlue(tile);

						});
					}
					else
						tile.setFill(Color.valueOf("#C70039"));
				} else
				{
					
					tile.setFill(Color.valueOf(Constants.LIGHT_TILE));
	

				}

				tilesGroup.getChildren().add(tile);

			}
		}

	}

	@FXML
	void confirmTileSelection()
	{
		((Stage) mainPane.getScene().getWindow()).close();
	}

}