package Main;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class CalendarTool
{
    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";

    /**
     * Directory to store user credentials for this application.
     */
    private static final File DATA_STORE_DIR = new File(System.getProperty("user.home"), ".credentials/calendar-java-quickstart");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY);

    static
    {
        try
        {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     *
     * @throws IOException
     */
    static Credential authorize() throws IOException
    {
        // Load client secrets.
        InputStream         in            = CalendarTool.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
                                                                                   JSON_FACTORY,
                                                                                   clientSecrets,
                                                                                   SCOPES).setDataStoreFactory(DATA_STORE_FACTORY)
                                                                                          .setAccessType("offline")
                                                                                          .build();
        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     *
     * @return an authorized Calendar client service
     *
     * @throws IOException
     */
    static Calendar getCalendarService() throws IOException
    {
        Credential credential = authorize();
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    static void run() throws IOException
    {
        /*
        // Build a new authorized API client service.
        // Note: Do not confuse this class with the
        //   com.google.api.services.calendar.model.Calendar class.
        Calendar service = getCalendarService();

        // List the next 10 events from the primary calendar.
        DateTime              now = new DateTime(System.currentTimeMillis());
        Events events = service.events()
                               .list("primary")
                               .setMaxResults(10)
                               .setTimeMin(now)
                               .setOrderBy("startTime")
                               .setSingleEvents(true)
                               .execute();
        List<Event> items = events.getItems();
        if(items.size() == 0)
        {
            System.out.println("No upcoming events found.");
        }
        else
        {
            System.out.println("Upcoming events");
            for(Event event : items)
            {
                DateTime start = event.getStart().getDateTime();
                if(start == null)
                {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }*/
    }

    /**
     * Get a calendar lsit
     */
    static List<CalendarListEntry> getCalendarList() throws IOException
    {
        // Build a new authorized API client service.
        // Note: Do not confuse this class with the
        //   com.google.api.services.calendar.model.Calendar class.
        Calendar service = getCalendarService();

        // List the next 10 events from the primary calendar.
        DateTime                now  = new DateTime(System.currentTimeMillis());
        CalendarList            feed = service.calendarList().list().execute();
        List<CalendarListEntry> cals = feed.getItems();
        return cals;
    }

    /**
     * Get event count for calendar
     */
    static float getEventCount(String calendar) throws IOException
    {
        // Build a new authorized API client service.
        // Note: Do not confuse this class with the
        //   com.google.api.services.calendar.model.Calendar class.
        Calendar service = getCalendarService();

        // List the next 10 events from the primary calendar.
        DateTime              now = new DateTime(System.currentTimeMillis());
        Events events = service.events()
                               .list(calendar)
                               //.setMaxResults(10)
                               //.setTimeMin(now)
                               .setOrderBy("startTime")
                               .setSingleEvents(true)
                               .execute();
        long duration = 0;
        List<Event> items = events.getItems();
        for(Event event : items)
        {
            long v1;
            if(event.getStart().getDateTime() != null)
            {
                // skip non full day events
                // v1 = event.getStart().getDateTime().getValue();
                continue;
            }
            else
            {
                v1 = event.getStart().getDate().getValue();
            }
            long v2;
            if(event.getEnd().getDateTime() != null)
            {
                // skip non full day events
                // v2 = event.getEnd().getDateTime().getValue();
                continue;
            }
            else
            {
                v2 = event.getEnd().getDate().getValue();
            }

            duration += getDateDiff(v1, v2, TimeUnit.HOURS);
        }
        return duration / 24.0f;
    }

    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(long date1, long date2, TimeUnit timeUnit) {
        long diffInMillies = date2 - date1;
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
}