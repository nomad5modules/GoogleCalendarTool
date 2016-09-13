package Main;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by mlostek on 12.09.16.
 *
 * Project model
 */
class Project
{
    private final StringProperty name;
    private final StringProperty  days;

    /**
     * Default constructor.
     */
    Project()
    {
        this(null, null);
    }

    /**
     * Constructor with some initial data.
     */
    Project(String name, String days)
    {
        this.name = new SimpleStringProperty(name);
        this.days = new SimpleStringProperty(days);
    }

    String getName() { return name.get(); }

    String getDays() { return days.get(); }

    void setName(String name) { this.name.set(name);}

    void setDays(String days) { this.days.set(days); }

    StringProperty getNameProperty() { return this.name; }

    StringProperty getDaysProperty() { return this.days; }
}
