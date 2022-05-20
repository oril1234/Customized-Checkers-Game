package View;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import org.json.simple.parser.ParseException;

import Control.SysData;
import Model.Question;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controller of questions displayed to user
 * @author user Sharks
 *
 */
public class QuestionsToPlayersController implements Initializable
{

	@FXML
	private AnchorPane questionsDetails;

	@FXML
	private Label questionLabel;

	@FXML
	private ToggleGroup answerGroup;

	@FXML
	private RadioButton answer1;

	@FXML
	private RadioButton answer2;

	@FXML
	private RadioButton answer3;

	@FXML
	private RadioButton answer4;

	private List<Question> questions;

	private Question question;
	
	private SysData sd=SysData.getInstance();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		answerGroup.selectToggle(answer1);
		try
		{
			sd.loadQuestionsDetails();
		} catch (IOException | ParseException e)
		{
			e.printStackTrace();
		}
		questions = sd.getQuestions();
		choseRandomQuestion();
	}

	private void choseRandomQuestion()
	{
		Random random = new Random();
		int randomIndex = random.nextInt(questions.size());
		question = questions.get(randomIndex);

		questionLabel.setText(question.getContent());
		answer1.setText(question.getAnswers().get(0));
		answer2.setText(question.getAnswers().get(1));
		answer3.setText(question.getAnswers().get(2));
		answer4.setText(question.getAnswers().get(3));

	}

	@FXML
	void answerQuestion(ActionEvent event)
	{
		// RadioButton selectedAnswer= (RadioButton) answerGroup.getSelectedToggle();
		int selectedIndex = answerGroup.getToggles().indexOf(answerGroup.getSelectedToggle());

		Alert alert = new Alert(AlertType.NONE);
		alert.setTitle("Correct Answer");
		alert.setContentText("Your answer is correct!");
		alert.setHeaderText(null);
		alert.getButtonTypes().add(ButtonType.OK);
        sd.playCorrectAnswerSound();

		boolean iscorrect = selectedIndex == question.getCorrectAnswerIdx();
		if (!iscorrect)
		{
			
			int correctAnswerNumber = question.getCorrectAnswerIdx() + 1;
			alert.setAlertType(AlertType.ERROR);
			alert.setTitle("Wrong Answer");
			alert.setContentText("You were wrong! the correct answer is answer number " + correctAnswerNumber);
            sd.playWrongAnswerSound();
		}
		alert.showAndWait();

		int addedPoints = 0;
		switch (question.getQuestionLevel()) {
		case 1:
			addedPoints = iscorrect ? 100 : -250;
			break;
		case 2:
			addedPoints = iscorrect ? 200 : -100;
			break;
		case 3:
			addedPoints = iscorrect ? 500 : -50;
			break;

		}
		
		sd.updatePlayerScore(addedPoints);
		Stage thisWindow = (Stage) questionsDetails.getScene().getWindow();
		thisWindow.close();

	}

}
