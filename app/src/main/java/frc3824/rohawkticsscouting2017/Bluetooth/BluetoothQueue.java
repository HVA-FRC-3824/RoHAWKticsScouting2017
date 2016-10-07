package frc3824.rohawkticsscouting2017.Bluetooth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.SMD;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TDTF;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TMD;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/22/16
 *
 * Queue to hold data that fails to send.
 */
public class BluetoothQueue {

    private final static String TAG = "BluetoothQueue";

    private ArrayList<TMD> mTMDs;
    private ArrayList<SMD> mSMDs;
    private ArrayList<TDTF> mTDTFs;

    private static BluetoothQueue mQueue;

    public static BluetoothQueue getInstance() {
        if(mQueue == null) {
            mQueue = new BluetoothQueue();
        }
        return mQueue;
    }

    private BluetoothQueue() {
        mTMDs = new ArrayList<>();
        mSMDs = new ArrayList<>();
        mTDTFs = new ArrayList<>();
    }

    public void add(TMD tmd)
    {
        mTMDs.add(tmd);
    }

    public void add(SMD smd)
    {
        mSMDs.add(smd);
    }

    public void add(TDTF tdtf) {
        mTDTFs.add(tdtf);
    }

    public List<String> getQueueList() {
        List<String> rv = new ArrayList<>();
        Gson mGson = new GsonBuilder().create();
        for(TMD tmd: mTMDs) {
            rv.add(String.format("%c%s", Constants.Bluetooth.Message_Headers.MATCH_HEADER, mGson.toJson(tmd)));
        }

        for(SMD smd: mSMDs) {
            rv.add(String.format("%c%s", Constants.Bluetooth.Message_Headers.SUPER_HEADER, mGson.toJson(smd)));
        }

        for(TDTF tdtf: mTDTFs) {
            rv.add(String.format("%c%s", Constants.Bluetooth.Message_Headers.FEEDBACK_HEADER, mGson.toJson(tdtf)));
        }

        return rv;
    }

    public void clear() {
        mTMDs.clear();
        mSMDs.clear();
        mTDTFs.clear();
    }
}
