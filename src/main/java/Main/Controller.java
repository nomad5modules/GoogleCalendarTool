package Main;

import com.google.api.services.calendar.model.CalendarListEntry;

import java.io.IOException;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Controller
{

    @FXML private TableView<Project>           tableProjects;
    @FXML private TableColumn<Project, String> colProject;
    @FXML private TableColumn<Project, String> colDays;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize()
    {
        // Initialize the person table with the two columns.
        colProject.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        colDays.setCellValueFactory(cellData -> cellData.getValue().getDaysProperty());

        // Listen for selection changes and show the person details when changed.
        //tableProjects.getSelectionModel().selectedItemProperty().addListener(
        //        (observable, oldValue, newValue) -> showPersonDetails(newValue));

        /**
         * The data as an observable list of Persons.
         */
        ObservableList<Project> projectData = FXCollections.observableArrayList();


        // get the calendar list
        try
        {
            List<CalendarListEntry> cals = CalendarTool.getCalendarList();
            for(CalendarListEntry s : cals)
            {
                if(s.getSummary().startsWith("#"))
                {
                    float duration = CalendarTool.getEventCount(s.getId());
                    projectData.add(new Project(s.getSummary(), "" + duration));
                }
            }

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }


        tableProjects.setItems(projectData);

    }
}
