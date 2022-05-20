import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import Model.Checker;
import Model.CheckerType;
import Model.Constants;
import Model.Game;
import Model.MoveResult;
import Model.MoveType;
import Model.Player;
import Model.Question;

import Model.Tile;
import View.GameController;
import javafx.scene.paint.Color;


class TestClass
{
	private List<String> answers;
	private Player Ptester1;
	private Player Ptester2;
	
	@Before
	public List<String> initializeAnswers()
	{
		answers = new ArrayList<>(Arrays.asList("yes", "no", "Don't know", "sometimes"));
		return answers;
	}

	@Test
	public void QuestionTest() throws IOException
	{
		Question Qtester = new Question(1, "do you like this game?", initializeAnswers(), 1, "sharks");

		assertNotNull("Question is not null value", Qtester.getContent());
		assertEquals(1, Qtester.getQuestionLevel());
		assertTrue(Qtester.checkCorrectAnswer("no"));
		
		Qtester.deleteAnswer("sometimes");
		assertEquals(3, answers.size());
		Qtester.addAnswer("maybe");
		assertEquals(4, answers.size());
		

		/* To fail the test */
		// assertEquals(2, Qtester.getQuestionLevel());

	}

	@Before
	public void initializePlayers()
	{
		Ptester1 = new Player("WHITE","jon");
		Ptester2 = new Player("BLACK","shon");
	}

	
	@Test
	public void CheckerAndPlayerTest()
	{
		initializePlayers();
		assertNotNull("Player nickName is not null value", Ptester1.getNickName());
		assertNotEquals("player1 and player 2 are not equals", Ptester1, Ptester2);
		
		Checker checker1 = new Checker(CheckerType.BLACK, 3,4,false);
		Checker checker2 = new Checker(CheckerType.WHITE, 2,5,false);
		assertNotEquals("checker1 and checker2 are not equals",checker1,checker2);	
	}
	
	
	@Test
	public void GameTest()
	{
		initializePlayers();
		Game Gtester = new Game(Ptester1, Ptester2,null);
		Player p = Gtester.getCurrentTurn();
		assertEquals(Ptester1, Gtester.getCurrentTurn());
		Game.changeTurn();
		assertEquals("BLACK", Game.currentTurn.color);
	}



	@Test
	public void TileTest()
	{
		Tile tile = new Tile(false,2,3);
		assertEquals(Color.valueOf(Constants.DARK_TILE),tile.getFill());
		tile.setColor(Constants.YELLOW_TILE);
		assertEquals(Color.valueOf(Constants.YELLOW_TILE),tile.getFill());
	}
	
	
	
}
