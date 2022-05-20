package View;


import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import Model.Question;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import Control.*;

/**
 * This is a controller class of pop-up window for add/edit questions
 */
public class AddEditQuestionWindowController implements Initializable
{

	/**
	 * The window that contains the screen
	 */
	private Stage thisStage;

	/**
	 * All the questions
	 */
	private List<Question> questions;
	
	/**
	 * Not null only if a user edit a question
	 */
	private Question question;

	/**
	 * The added/edited question
	 */
	private Question questionToEdit = null;

	/**
	 * Text area in which the content is written
	 */
	@FXML
	private TextArea questionContent;

	/**
	 * The drop down to choose level
	 */
	@FXML
	private ChoiceBox<String> questionLevel;

	/**
	 * Text area of answer 1
	 */
	@FXML
	private TextArea answer1;

	/**
	 * Text area of answer 2
	 */
	@FXML
	private TextArea answer2;

	/**
	 * Text area of answer 3
	 */
	@FXML
	private TextArea answer3;

	/**
	 * Text area of answer 4
	 */
	@FXML
	private TextArea answer4;

	/**
	 * The dropdown of correct answer
	 */
	@FXML
	private ChoiceBox<String> correctanswerBox;

	/**
	 * Button to save question
	 */
	@FXML
	private Button saveButton;

	private SysData sd = SysData.getInstance();

	/**
	 * This method is invoked by the owner of the pop-up window to send the
	 * questions to edit or null pointer if new question is added
	 * @param question question
	 * @param questions  questions
	 */
	public void setQuestionsDetails(Question question, List<Question> questions)
	{
		this.questions = questions;
		questionLevel.getItems().add("1");
		questionLevel.getItems().add("2");
		questionLevel.getItems().add("3");

		correctanswerBox.getItems().add("1");
		correctanswerBox.getItems().add("2");
		correctanswerBox.getItems().add("3");
		correctanswerBox.getItems().add("4");

		// Default choices
		correctanswerBox.getSelectionModel().select("1");
		questionLevel.getSelectionModel().select("1");

		if (question == null)
			return;
		this.question=question;
		questionContent.setText(question.getContent());
		questionLevel.getSelectionModel().select(question.getQuestionLevel() - 1);
		answer1.setText(question.getAnswers().get(0));
		answer2.setText(question.getAnswers().get(1));
		answer3.setText(question.getAnswers().get(2));
		answer4.setText(question.getAnswers().get(3));
		correctanswerBox.getSelectionModel().select(question.getCorrectAnswerIdx());
	}

	/**
	 * * Saving the details of the updated question
	 * 
	 * @param event event
	 */
	@FXML
	public void onSave(ActionEvent event)
	{
		if (!validateFields())
		{
			SysData.playErrorSound();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("You must fill all the fields." + "\n" +
			"A question can't repeat more than once"+"\n"+
					"Answer can repeat only once for a question" );
			alert.setHeaderText(null);
			alert.showAndWait();

		} else
		{
			sd.playMenuSound();
			int level = Integer.parseInt(questionLevel.getSelectionModel().getSelectedItem() + "");
			String content = questionContent.getText();
			List<String> answers = new ArrayList<>();
			answers.add(answer1.getText());
			answers.add(answer2.getText());
			answers.add(answer3.getText());
			answers.add(answer4.getText());
			int correctAnswerIdx = correctanswerBox.getSelectionModel().getSelectedIndex();
			questionToEdit = new Question(level, content, answers, correctAnswerIdx, "Sharks");
			thisStage.close();
		}
	}

	/**
	 * Cancel add/edit
	 * 
	 * @param event event
	 */
	@FXML
	public void onCancel(ActionEvent event)
	{
		sd.playMenuSound();
		thisStage.close();
	}

	/**
	 * * Invoked by modal owner to get the object of the updated question
	 * 
	 * @return added/edited question
	 */
	public Question getUpdatedQuestion()
	{
		return questionToEdit;
	}

	/**
	 * Validating fields after add/edit
	 * 
	 * @return True if they are all valid
	 */
	private boolean validateFields()
	{
		boolean result = !questionContent.getText().equals("");
		result = result && !answer1.getText().equals("") && !answer2.getText().equals("");
		result = result && !answer3.getText().equals("") && !answer4.getText().equals("");
		
		boolean hasDuplicate = false;
		Iterator<Question> qIterator = questions.iterator();
		while (qIterator.hasNext() && !hasDuplicate)
		{
			Question currentQuestion=qIterator.next();
			if (!currentQuestion.equals(question) && currentQuestion.getContent().equals(questionContent.getText()))
				hasDuplicate = true;

		}
		
		List<String> tmpList = new ArrayList<>();
		tmpList.add(answer1.getText());
		tmpList.add(answer2.getText());
		tmpList.add(answer3.getText());
		tmpList.add(answer4.getText());
		
		for(int i=0;i<tmpList.size()&& !hasDuplicate;i++)
		{
			for(int j=0;j<4 && !hasDuplicate;j++)
			{
				if(i!=j && tmpList.get(j).equals(tmpList.get(i)))
				{
					hasDuplicate=true;

				}
			}
		}
		result=result && !hasDuplicate;

		return result;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{

	}

	public void setStage(Stage stage)
	{
		thisStage = stage;
	}

}
