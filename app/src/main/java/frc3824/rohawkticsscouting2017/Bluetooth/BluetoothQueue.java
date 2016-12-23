package frc3824.rohawkticsscouting2017.Bluetooth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.SuperMatchData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamDTFeedback;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamMatchData;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/22/16
 *
 * Queue to hold data that fails to send.
 */
public class BluetoothQueue {

    private final static String TAG = "BluetoothQueue";

    private ArrayList<TeamMatchData> mTeamMatchDatas;
    private ArrayList<SuperMatchData> mSuperMatchDatas;
    private ArrayList<TeamDTFeedback> mTeamDTFeedbacks;

    private static BluetoothQueue mQueue;

    public static BluetoothQueue getInstance() {
        if(mQueue == null) {
            mQueue = new BluetoothQueue();
        }
        return mQueue;
    }

    private BluetoothQueue() {
        mTeamMatchDatas = new ArrayList<>();
        mSuperMatchDatas = new ArrayList<>();
        mTeamDTFeedbacks = new ArrayList<>();
    }

    public void add(TeamMatchData teamMatchData)
    {
        mTeamMatchDatas.add(teamMatchData);
    }

    public void add(SuperMatchData superMatchData)
    {
        mSuperMatchDatas.add(superMatchData);
    }

    public void add(TeamDTFeedback teamDTFeedback) {
        mTeamDTFeedbacks.add(teamDTFeedback);
    }

    public List<String> getQueueList() {
        List<String> rv = new ArrayList<>();
        Gson mGson = new GsonBuilder().create();
        for(TeamMatchData teamMatchData : mTeamMatchDatas) {
            rv.add(String.format("%c%s", Constants.Bluetooth.Message_Headers.MATCH_HEADER, mGson.toJson(teamMatchData)));
        }

        for(SuperMatchData superMatchData : mSuperMatchDatas) {
            rv.add(String.format("%c%s", Constants.Bluetooth.Message_Headers.SUPER_HEADER, mGson.toJson(superMatchData)));
        }

        for(TeamDTFeedback teamDTFeedback : mTeamDTFeedbacks) {
            rv.add(String.format("%c%s", Constants.Bluetooth.Message_Headers.FEEDBACK_HEADER, mGson.toJson(teamDTFeedback)));
        }

        return rv;
    }

    public void clear() {
        mTeamMatchDatas.clear();
        mSuperMatchDatas.clear();
        mTeamDTFeedbacks.clear();
    }
}
