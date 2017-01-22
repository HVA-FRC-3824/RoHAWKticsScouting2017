package frc3824.rohawkticsscouting2017.Comms;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.ScoutAccuracy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.SuperMatchData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamCalculatedData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamDTFeedback;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamMatchData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPickAbility;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPitData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamRankingData;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/21/16
 *
 *
 */
public class MessageHandler extends Handler {

    private final static String TAG = "MessageHandler";

    private Gson mGson;
    private Database mDatabase;

    public MessageHandler() {
        mGson = new GsonBuilder().create();
        mDatabase = Database.getInstance();
    }

    @Override
    public void handleMessage(Message msg) {
        String message;
        switch (msg.what) {
            case Constants.Bluetooth.Message_Type.COULD_NOT_CONNECT:
                displayText("Could not connect", Constants.Server_Log_Colors.RED);
                break;
            case Constants.Bluetooth.Message_Type.DATA_SENT_OK:
                displayText("Data sent ok", Constants.Server_Log_Colors.GREEN);
                break;
            case Constants.Bluetooth.Message_Type.SENDING_DATA:
                displayText("Sending data", Constants.Server_Log_Colors.BLUE);
                break;
            case Constants.Bluetooth.Message_Type.DIGEST_DID_NOT_MATCH:
                displayText("Digest did not match", Constants.Server_Log_Colors.RED);
                break;
            case Constants.Bluetooth.Message_Type.INVALID_HEADER:
                displayText("Invalid header", Constants.Server_Log_Colors.RED);
                break;
            case Constants.Bluetooth.Message_Type.CONNECTION_LOST:
                message = (String)msg.obj;
                displayText("Connection Lost: " + message, Constants.Server_Log_Colors.RED);
                connectionLost(message);
                break;
            case Constants.Bluetooth.Message_Type.NEW_CONNECTION:
                message = (String)msg.obj;
                displayText("Connection Added: " + message, Constants.Server_Log_Colors.GREEN);
                connectionAdded(message);
                break;
            case Constants.Bluetooth.Message_Type.DATA_RECEIVED:
                message = new String((byte[]) msg.obj);
                if (message.length() > 30) {
                    displayText(String.format("Received: %s ... %s", message.substring(0, 15), message.substring(message.length() - 15)));
                } else {
                    displayText(String.format("Received: %s", message));
                }

                if (message.length() == 0)
                    return;

                switch (message.charAt(0)) {
                    case Constants.Bluetooth.Message_Headers.MATCH_HEADER:
                        TeamMatchData teamMatchData = mGson.fromJson(message.substring(1), TeamMatchData.class);
                        mDatabase.setTeamMatchData(teamMatchData);
                        dataReceived(teamMatchData);
                        displayText("Match Data Received", Constants.Server_Log_Colors.YELLOW);
                        break;
                    case Constants.Bluetooth.Message_Headers.PIT_HEADER:
                        mDatabase.setTeamPitData(mGson.fromJson(message.substring(1), TeamPitData.class));
                        displayText("Pit Data Received", Constants.Server_Log_Colors.YELLOW);
                        break;
                    case Constants.Bluetooth.Message_Headers.SUPER_HEADER:
                        SuperMatchData superMatchData = mGson.fromJson(message.substring(1), SuperMatchData.class);
                        mDatabase.setSuperMatchData(superMatchData);
                        dataReceived(superMatchData);
                        displayText("Super Data Received", Constants.Server_Log_Colors.YELLOW);
                        break;
                    case Constants.Bluetooth.Message_Headers.FEEDBACK_HEADER:
                        TeamDTFeedback teamDTFeedback = mGson.fromJson(message.substring(1), TeamDTFeedback.class);
                        mDatabase.setTeamDTFeedback(teamDTFeedback);
                        displayText("Feedback Data Received", Constants.Server_Log_Colors.YELLOW);
                        break;
                    case Constants.Bluetooth.Message_Headers.CALC_HEADER:
                        mDatabase.setTeamCalculatedData(mGson.fromJson(message.substring(1), TeamCalculatedData.class));
                        displayText("Calculated Data Received", Constants.Server_Log_Colors.YELLOW);
                        break;
                    case Constants.Bluetooth.Message_Headers.SYNC_HEADER:
                        try {
                            JSONObject response = new JSONObject(message.substring(1));

                            JSONArray jsonArray = response.getJSONArray("match");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject match = jsonArray.getJSONObject(i);
                                mDatabase.setTeamMatchData(mGson.fromJson(match.toString(), TeamMatchData.class));
                            }

                            jsonArray = response.getJSONArray("pit");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject pit = jsonArray.getJSONObject(i);
                                mDatabase.setTeamPitData(mGson.fromJson(pit.toString(), TeamPitData.class));
                            }

                            jsonArray = response.getJSONArray("super");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject super_ = jsonArray.getJSONObject(i);
                                mDatabase.setSuperMatchData(mGson.fromJson(super_.toString(), SuperMatchData.class));
                            }

                            jsonArray = response.getJSONArray("calc");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject calc = jsonArray.getJSONObject(i);
                                mDatabase.setTeamCalculatedData(mGson.fromJson(calc.toString(), TeamCalculatedData.class));
                            }

                            jsonArray = response.getJSONArray("1st");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject tpa = jsonArray.getJSONObject(i);
                                mDatabase.setFirstTPA(mGson.fromJson(tpa.toString(), TeamPickAbility.class));
                            }

                            jsonArray = response.getJSONArray("2nd");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject tpa = jsonArray.getJSONObject(i);
                                mDatabase.setSecondTPA(mGson.fromJson(tpa.toString(), TeamPickAbility.class));
                            }

                            jsonArray = response.getJSONArray("3rd");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject tpa = jsonArray.getJSONObject(i);
                                mDatabase.setThirdTPA(mGson.fromJson(tpa.toString(), TeamPickAbility.class));
                            }

                            jsonArray = response.getJSONArray("current");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject trd = jsonArray.getJSONObject(i);
                                mDatabase.setCurrentTRD(mGson.fromJson(trd.toString(), TeamRankingData.class));
                            }

                            jsonArray = response.getJSONArray("predicted");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject trd = jsonArray.getJSONObject(i);
                                mDatabase.setPredictedTRD(mGson.fromJson(trd.toString(), TeamRankingData.class));
                            }

                            jsonArray = response.getJSONArray("accuracy");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject sa = jsonArray.getJSONObject(i);
                                mDatabase.setScoutAccuracy(mGson.fromJson(sa.toString(), ScoutAccuracy.class));
                            }

                            jsonArray = response.getJSONArray("strategy");
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject s = jsonArray.getJSONObject(i);
                                mDatabase.setStrategy(mGson.fromJson(s.toString(), Strategy.class));
                            }

                        } catch (JSONException e) {
                           displayText("Error is parsing the response", Constants.Server_Log_Colors.RED);
                        }
                        displayText("Sync Response Received", Constants.Server_Log_Colors.YELLOW);
                        break;
                }
                break;
        }
    }

    public void displayText(String message){

    }

    public void displayText(String message, String color){

    }

    public void connectionAdded(String message){

    }

    public void connectionLost(String message){

    }

    public void dataReceived(TeamMatchData teamMatchData){

    }

    public void dataReceived(SuperMatchData superMatchData){

    }
}
