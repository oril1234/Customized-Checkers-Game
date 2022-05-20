package View;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.github.cliftonlabs.json_simple.Jsoner;

import Control.SysData;
import Model.Question;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

/**
 * This is a controller class to add, delete and edit questions 
 */
public class QuestionsController implements Initializable
{
	@FXML
	private AnchorPane questionsAnchorPane;

	@FXML
	private TableView<Question> questionsTable;

	@FXML
	private TableColumn questionNum = new TableColumn("#");

	@FXML
	private TableColumn<Question, Integer> questionLevel;

	@FXML
	private TableColumn<Question, String> questionContent;

	@FXML
	private TableColumn<Question, String> answer1;

	@FXML
	private TableColumn<Question, String> answer2;

	@FXML
	private TableColumn<Question, String> answer3;

	@FXML
	private TableColumn<Question, String> answer4;
	
    @FXML
    private TableColumn<Question, String> correctAnswer;

	ObservableList<Question> questions = FXCollections.observableArrayList();

	@FXML
	private Button addButton;

	@FXML
	private Button editButton;

	@FXML
	private Button deleteButton;
	
	SysData sd=SysData.getInstance();

	
	/*
	 * Reading questions data from json file 
	 */
	public void readJsonSimpleDemo() throws Exception
	{

		questions.addAll(sd.getQuestions());
	}

	
	/*
	 * Initializing the objects of table column of questions details such as 
	 * the content of the question, the answers, the level and the correct answer
	 * As well as the table object and observable list of questions objects
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		try
		{
			sd.loadQuestionsDetails();
			editButton.setDisable(true);
			deleteButton.setDisable(true);

			//Allowing edit and delete operations only if a row in the table is selected
			questionsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) ->
			{
				if (newSelection != null)
				{
					deleteButton.setDisable(false);
					editButton.setDisable(false);
				}
			});
			
			//Setting auto numbering for numbers of questions in the table
			questionNum.setCellValueFactory(new Callback<CellDataFeatures<Question, String>, ObservableValue<String>>()
			{
				@Override
				public ObservableValue<String> call(CellDataFeatures<Question, String> q)
				{
					return new ReadOnlyObjectWrapper((questionsTable.getItems().indexOf(q.getValue()) + 1) + "");
				}
			});
			questionNum.setSortable(false);

			//Bind the value of the column to the property of question level in Question class
			questionLevel.setCellValueFactory(new PropertyValueFactory<Question, Integer>("questionLevel"));

			//Configure question content column
			setCellFactoryForTableColumn(questionContent);
			questionContent.setCellValueFactory(new PropertyValueFactory<Question, String>("content"));

			//Configure answers content columns
			setCellFactoryForTableColumn(answer1);
			answer1.setCellValueFactory(
					new Callback<TableColumn.CellDataFeatures<Question, String>, ObservableValue<String>>()
					{

						@Override
						public ObservableValue<String> call(CellDataFeatures<Question, String> data)
						{
							return data.getValue().answerProperty(0);
						}
					});
			
			setCellFactoryForTableColumn(answer2);
			answer2.setCellValueFactory(
					new Callback<TableColumn.CellDataFeatures<Question, String>, ObservableValue<String>>()
					{

						@Override
						public ObservableValue<String> call(CellDataFeatures<Question, String> data)
						{
							return data.getValue().answerProperty(1);
						}
					});
			
			setCellFactoryForTableColumn(answer3);
			answer3.setCellValueFactory(
					new Callback<TableColumn.CellDataFeatures<Question, String>, ObservableValue<String>>()
					{

						@Override
						public ObservableValue<String> call(CellDataFeatures<Question, String> data)
						{
							return data.getValue().answerProperty(2);
						}
					});
			
			setCellFactoryForTableColumn(answer4);
			answer4.setCellValueFactory(
					new Callback<TableColumn.CellDataFeatures<Question, String>, ObservableValue<String>>()
					{

						@Override
						public ObservableValue<String> call(CellDataFeatures<Question, String> data)
						{
							return data.getValue().answerProperty(3);
						}
					});
			correctAnswer.setCellValueFactory(
					new Callback<TableColumn.CellDataFeatures<Question, String>, ObservableValue<String>>()
					{

						@Override
						public ObservableValue<String> call(CellDataFeatures<Question, String> data)
						{
							return data.getValue().correctAnswerIdxIntegerProperty();
						}
					});

			//Binding the observable list to the table
			questionsTable.setItems(questions);
			//Reading the questions details from json
			readJsonSimpleDemo();

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * This method is used for wrapping the text within every table cell
	 * @param tableCol
	 */
	private void setCellFactoryForTableColumn(TableColumn<Question, String> tableCol)
	{
		tableCol.setCellFactory(tc ->
		{
			TableCell<Question, String> cell = new TableCell<>();
			Text text = new Text();
			cell.setGraphic(text);
			cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
			text.wrappingWidthProperty().bind(tableCol.widthProperty());
			text.textProperty().bind(cell.itemProperty());
			return cell;
		});

	}

	/**
	 * Making delete/edit buttons disabled if background outside of table was pressed
	 */
	@FXML
	void onPanePressed(MouseEvent event)
	{
		sd.playMenuSound();
		deleteButton.setDisable(true);
		editButton.setDisable(true);
		questionsTable.getSelectionModel().clearSelection();
	}

	/*
	 * Handle deletion of a question
	 */
	@FXML
	void onDeleteQuestion(ActionEvent event) throws IOException
	{
		sd.playMenuSound();
		int questionIndex = questionsTable.getSelectionModel().getSelectedIndex();
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Delete a question");
		alert.setContentText("Delete question number " + (questionIndex + 1) + "?");
		alert.setHeaderText(null);
		alert.getButtonTypes().remove(ButtonType.OK);
		alert.getButtonTypes().add(ButtonType.YES);
		alert.getButtonTypes().add(ButtonType.NO);

		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES)
		{
			questions.remove(questionIndex);
			if (questions.size() == 0)
			{
				deleteButton.setDisable(true);
				editButton.setDisable(true);

			}
			SysData.updateJson(questions);
		}
	}

	/*
	 * Handling addition of a new question
	 */
	@FXML
	void onAddButton(ActionEvent event) throws IOException
	{
		sd.playMenuSound();
		openAddEditModal("New Question Details", null);

	}

	/*
	 * Handling editing of a new question
	 */
	@FXML
	void onEditButton(ActionEvent event) throws IOException
	{
        sd.playMenuSound();
		int editedIndex = questionsTable.getSelectionModel().getSelectedIndex();
		openAddEditModal("Edit Question Details", editedIndex);

	}

	/**
	 * Open the pop-up window for adding a new question or editing an existing one
	 * @param title title
	 * @param editedIndex edited index
	 */
	public void openAddEditModal(String title, Integer editedIndex)
	{
		try
		{
			Stage window = new Stage();
			window.initModality(Modality.WINDOW_MODAL);
			window.initOwner(questionsAnchorPane.getScene().getWindow());
			FXMLLoader louder = new FXMLLoader();
			louder.setLocation(getClass().getResource("/View/AddEditQuestionModal.fxml"));
			louder.load();
			Parent parent = louder.getRoot();
			Scene scene = new Scene(parent, 452, 700);

			AddEditQuestionWindowController controller = louder.getController();
			controller.setStage(window);
			window.setTitle(title);
			window.setScene(scene);

			window.setOnHidden(e ->
			{
				questionsTable.getSelectionModel().clearSelection();
				deleteButton.setDisable(true);
				editButton.setDisable(true);
				Question updatedQuestion = controller.getUpdatedQuestion();
				if (updatedQuestion == null)
					return;
				if (editedIndex == null)
					questions.add(updatedQuestion);
				else
					questions.set(editedIndex, updatedQuestion);

				SysData.updateJson(questions);
			});
			window.show();

			Question questionToEdit = null;
			if (editedIndex != null)
				questionToEdit = questions.get(editedIndex);

			controller.setQuestionsDetails(questionToEdit,questions);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	@FXML
	void returnToHomePage(ActionEvent event)
	{
		try
		{
			sd.playMenuSound();
			Stage primaryStage = (Stage) questionsAnchorPane.getScene().getWindow();
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/View/HomeView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/View/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Home");
			primaryStage.show();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
