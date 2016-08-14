package frc3824.rohawkticsscouting2017.TheBlueAlliance;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.TheBlueAlliance.TBA_models.TBA_Event;
import frc3824.rohawkticsscouting2017.TheBlueAlliance.TBA_models.TBA_Match;
import frc3824.rohawkticsscouting2017.TheBlueAlliance.TBA_models.TBA_Team;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 * Created: 8/14/16
 */
public class TheBlueAlliance {

    private final static String TAG = "BlueAlliance";

    private Gson mGson;

    private static TheBlueAlliance mSingleton;

    public static TheBlueAlliance getInstance()
    {
        if(mSingleton == null)
        {
            mSingleton = new TheBlueAlliance();
        }

        return  mSingleton;
    }

    private TheBlueAlliance()
    {
        mGson = new GsonBuilder().create();
    }

    private String getData(HttpURLConnection con) {
        try {
            con.setRequestMethod("GET");
            con.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }
        catch(Exception e) { e.printStackTrace(); }
        return null;
    }

    public TBA_Event getEvent(String eventKey) throws IOException {
        URL url = new URL("https://www.thebluealliance.com/api/v2/event/" + eventKey);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.addRequestProperty(Constants.The_Blue_Alliance.TBA_Header_NAME, Constants.The_Blue_Alliance.TBA_Header_VALUE);
        return mGson.fromJson(getData(con), TBA_Event.class);
    }

    public ArrayList<TBA_Team> getEventTeams(String eventKey) throws IOException {
        URL url = new URL(String.format("https://www.thebluealliance.com/api/v2/event/%s/teams", eventKey));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.addRequestProperty(Constants.The_Blue_Alliance.TBA_Header_NAME, Constants.The_Blue_Alliance.TBA_Header_VALUE);

        Type type = new TypeToken<ArrayList<TBA_Team>>(){}.getType();

        return mGson.fromJson(getData(con), type);
    }

    public TBA_Team getTeam(int team_number) throws IOException {
        URL url = new URL(String.format("https://www.thebluealliance.com/api/v2/team/%d", team_number));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.addRequestProperty(Constants.The_Blue_Alliance.TBA_Header_NAME, Constants.The_Blue_Alliance.TBA_Header_VALUE);
        return mGson.fromJson(getData(con), TBA_Team.class);
    }

    public ArrayList<TBA_Match> getEventMatches(String eventKey) throws IOException {
        URL url = new URL(String.format("https://www.thebluealliance.com/api/v2/event/%s/matches", eventKey));
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.addRequestProperty(Constants.The_Blue_Alliance.TBA_Header_NAME, Constants.The_Blue_Alliance.TBA_Header_VALUE);

        Type type = new TypeToken<ArrayList<TBA_Match>>(){}.getType();

        return mGson.fromJson(getData(con), type);
    }

    public TBA_Match getEventMatch(String eventKey, int match_number) throws IOException {
        ArrayList<TBA_Match> matches = getEventMatches(eventKey);
        return matches.get(match_number - 1);
    }
}
