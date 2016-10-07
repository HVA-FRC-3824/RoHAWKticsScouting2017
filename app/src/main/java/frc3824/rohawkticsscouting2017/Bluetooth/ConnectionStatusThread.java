package frc3824.rohawkticsscouting2017.Bluetooth;

import android.os.Message;

import java.util.ArrayList;
import java.util.Stack;

import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/22/16
 *
 * This thread gets all the ConnectedThreads from the server and checks to make sure they
 * are still connected. If not them they are declared lost and removed.
 */
public class ConnectionStatusThread extends Thread {

    private final static String TAG = "ConnectionStatusThread";

    private ArrayList<ConnectedThread> mConnections;
    private Stack<ConnectedThread> mToAdd;
    private boolean mRunning;
    private BluetoothHandler mHandler;

    public ConnectionStatusThread(BluetoothHandler handler) {
        mConnections = new ArrayList<>();
        mToAdd = new Stack<>();
        mHandler = handler;
    }

    public void run() {
        mRunning = true;
        while (mRunning) {
            // Remove any disconnected connection threads
            for (int i = 0; i < mConnections.size(); i++) {
                ConnectedThread ct = mConnections.get(i);
                if(!ct.isConnected()) {
                    Message message = new Message();
                    message.obj = ct.getRemoveDeviceName();
                    message.what = Constants.Bluetooth.Message_Type.CONNECTION_LOST;
                    mHandler.sendMessage(message);
                    ct.cancel();
                    mConnections.remove(i);
                    i--;
                }
            }

            while (mToAdd.size() > 0) {
                mConnections.add(mToAdd.pop());
            }
        }
    }

    public void addCT(ConnectedThread ct) {
        mToAdd.add(ct);
    }

    public void cancel() {
        mRunning = false;
        for(ConnectedThread ct: mConnections) {
            ct.cancel();
        }
        for (ConnectedThread ct: mToAdd) {
            ct.cancel();
        }
    }

}
