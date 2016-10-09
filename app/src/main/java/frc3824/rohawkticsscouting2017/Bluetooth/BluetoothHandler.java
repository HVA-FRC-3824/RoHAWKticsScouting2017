package frc3824.rohawkticsscouting2017.Bluetooth;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.SMD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TCD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TDTF;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TMD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPD;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/21/16
 *
 *
 */
public class BluetoothHandler extends Handler {

    private final static String TAG = "BluetoothHandler";

    private Gson mGson;
    private Database mDatabase;

    public BluetoothHandler() {
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
                        TMD tmd = mGson.fromJson(message.substring(1), TMD.class);
                        mDatabase.setTMD(tmd);
                        dataReceived(tmd);
                        displayText("Match Data Received", Constants.Server_Log_Colors.YELLOW);
                        break;
                    case Constants.Bluetooth.Message_Headers.PIT_HEADER:
                        mDatabase.setTPD(mGson.fromJson(message.substring(1), TPD.class));
                        displayText("Pit Data Received", Constants.Server_Log_Colors.YELLOW);
                        break;
                    case Constants.Bluetooth.Message_Headers.SUPER_HEADER:
                        SMD smd = mGson.fromJson(message.substring(1), SMD.class);
                        mDatabase.setSMD(smd);
                        dataReceived(smd);
                        displayText("Super Data Received", Constants.Server_Log_Colors.YELLOW);
                        break;
                    case Constants.Bluetooth.Message_Headers.FEEDBACK_HEADER:
                        TDTF tdtf = mGson.fromJson(message.substring(1), TDTF.class);
                        mDatabase.setTDTF(tdtf);
                        displayText("Feedback Data Received", Constants.Server_Log_Colors.YELLOW);
                        break;
                    case Constants.Bluetooth.Message_Headers.CALC_HEADER:
                        mDatabase.setTCD(mGson.fromJson(message.substring(1), TCD.class));
                        displayText("Calculated Data Received", Constants.Server_Log_Colors.YELLOW);
                        break;
                }
                break;
        }
    }

    public void displayText(String message){}
    public void displayText(String message, String color){}
    public void connectionAdded(String message){}
    public void connectionLost(String message){}
    public void dataReceived(TMD tmd){}
    public void dataReceived(SMD smd){}
}
