package Main;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarListEntry;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Controller
{

    @FXML private TableView<Project>           tableProjects;
    @FXML private TableColumn<Project, String> colProject;
    @FXML private TableColumn<Project, String> colDays;
    @FXML private DatePicker                   dateFrom;
    @FXML private DatePicker                   dateTo;
    @FXML private CheckBox                     checkAllTime;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize()
    {
        // set start dates
        Calendar  cal  = Calendar.getInstance();
        LocalDate from = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        cal.add(Calendar.MONTH, 1);
        LocalDate to = cal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        dateFrom.setValue(from);
        dateTo.setValue(to);
        // init the columns
        colProject.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        colDays.setCellValueFactory(cellData -> cellData.getValue().getDaysProperty());
        // Listen for selection changes and show the person details when changed.
        //tableProjects.getSelectionModel().selectedItemProperty().addListener(
        //        (observable, oldValue, newValue) -> showPersonDetails(newValue));
        // trigger one time
        this.onAllTimeChecked(null);
    }

    /**
     * Get the data
     */
    private void getData()
    {
        try
        {
            ObservableList<Project> projectData = FXCollections.observableArrayList();
            List<CalendarListEntry> cals        = CalendarTool.getCalendarList();
            for(CalendarListEntry s : cals)
            {
                if(s.getSummary().startsWith("#"))
                {
                    DateTime from = null;
                    DateTime to   = null;
                    if(!checkAllTime.isSelected())
                    {
                        from = new DateTime(Date.from(dateFrom.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                        to = new DateTime(Date.from(dateTo.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
                    }

                    float duration = CalendarTool.getEventCount(s.getId(), from, to);
                    projectData.add(new Project(s.getSummary(), "" + duration));
                }
            }
            tableProjects.setItems(projectData);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Button GET was clicked
     */
    public void onGetClick(ActionEvent actionEvent)
    {
        this.getData();
    }

    /**
     * Check box all time checked
     */
    public void onAllTimeChecked(ActionEvent actionEvent)
    {
        dateFrom.setDisable(checkAllTime.isSelected());
        dateTo.setDisable(checkAllTime.isSelected());
    }
}
