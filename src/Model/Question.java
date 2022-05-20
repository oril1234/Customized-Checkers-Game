package Model;


import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * A class of question
 * @author user Sharks
 *
 */
public class Question
{

	/**
	 * Level of question
	 */
	private int questionLevel;

	/**
	 * Content of question
	 */
	private SimpleStringProperty content;

	/**
	 * List of question
	 */
	private List<String> answers;

	/**
	 * Index of correct answer in list
	 */
	private int correctAnswerIdx;
	
	/**
	 * The team of developers
	 */
	private String team;
	public Question(int questionLevel, String content, List<String> answers, int correctAnswerIdx, String team)
	{
		this.questionLevel = questionLevel;
		this.content = new SimpleStringProperty(content);
		this.answers = answers;
		this.correctAnswerIdx = correctAnswerIdx;
		this.team = team;
	}

	/**
	 *  Add a new answer to answers' set
	 * @param answer answer to add
	 */
	public void addAnswer(String answer)
	{
		answers.add(answer);
	}

	/**
	 *  Remove an answer from answers' set
	 * @param answer answer to remove
	 */
	public void deleteAnswer(String answer)
	{
		answers.remove(answer);
	}

	public StringProperty answerProperty(int index)
	{
		StringProperty answer = new SimpleStringProperty(answers.get(index));
		return answer;
	}

	/**
	 *  Checking if a given answer is the correct one
	 * @param answer answer to check
	 * @return True if answer is correct
	 */
	public boolean checkCorrectAnswer(String answer)
	{
		for (int i = 0; i < answers.size(); i++)
		{
			if (answers.get(i).equals(answer))
			{
				if (i == correctAnswerIdx)
					return true;
			}
		}
		return false;
	}

	
	public void setAnswers(List<String> answers)
	{
		this.answers = answers;
	}

	public void setCorrectanswerIdx(int correctanswerIdx)
	{
		this.correctAnswerIdx = correctanswerIdx;
	}

	public int getQuestionLevel()
	{
		return questionLevel;
	}

	public void setQuestionLevel(int questionLevel)
	{
		this.questionLevel = questionLevel;
	}

	public String getContent()
	{
		return content.get();
	}

	public SimpleStringProperty getMyContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = new SimpleStringProperty(content);
	}

	public List<String> getAnswers()
	{
		return answers;
	}

	public int getCorrectAnswerIdx()
	{
		return correctAnswerIdx;
	}

	public SimpleStringProperty correctAnswerIdxIntegerProperty()
	{
		return new SimpleStringProperty((correctAnswerIdx + 1) + "");

	}

	public void setCorrectanswer(int correctAnswerIdx)
	{
		this.correctAnswerIdx = correctAnswerIdx;
	}

	//

	@Override
	public String toString()
	{
		return "Question [questionLevel=" + questionLevel + ", content=" + content + ", answers=" + answers
				+ ", correctanswerIdx=" + correctAnswerIdx + ", team=" + team + "]";
	}

	public String getTeam()
	{
		return team;
	}

	public void setTeam(String team)
	{
		this.team = team;
	}

}
