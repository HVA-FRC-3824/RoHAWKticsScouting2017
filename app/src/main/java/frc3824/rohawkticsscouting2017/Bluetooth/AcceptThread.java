package frc3824.rohawkticsscouting2017.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 * Created: 8/21/16
 *
 * This thread runs while listening for incoming connections. It behaves
 * like a server-side client. It runs collecting connections.
 */
public class AcceptThread extends Thread {

    private final static String TAG = "AcceptThread";

    private BluetoothServerSocket mServer;

    private String mSocketType;
    private static UUID MY_UUID;
    private BluetoothHandler mHandler;
    private boolean mRunning;
    private ConnectionStatusThread mConnectionStatusThread;

    public AcceptThread(BluetoothAdapter adapter, BluetoothHandler handler, boolean secure)
    {
        BluetoothServerSocket tmp = null;
        mSocketType = secure ? "Secure" : "Insecure";
        mHandler = handler;

        mConnectionStatusThread = new ConnectionStatusThread(handler);
        mConnectionStatusThread.start();

        // Create a new listener server socket
        try {
            if(secure)
            {
                if(MY_UUID == null)
                {
                    MY_UUID = UUID.fromString(Constants.Bluetooth.UUID_SECURE);
                }
                tmp = adapter.listenUsingRfcommWithServiceRecord(Constants.Bluetooth.NAME_SECURE, MY_UUID);
            }
            else
            {
                if(MY_UUID == null)
                {
                    MY_UUID = UUID.fromString(Constants.Bluetooth.UUID_INSECURE);
                }
                tmp = adapter.listenUsingRfcommWithServiceRecord(Constants.Bluetooth.NAME_INSECURE, MY_UUID);
            }
        } catch (IOException e) {
            Log.e(TAG, "Socket listen() failed", e);
        }

        mServer = tmp;
    }

    public void run()
    {
        Log.d(TAG, "BEGIN mAcceptThread" + this);
        setName("AcceptThread" + mSocketType);

        BluetoothSocket socket;

        mRunning = true;
        // Listen to the server socket and accept new connections
        while(mRunning)
        {
            try {
                socket = mServer.accept();
            } catch (IOException e) {
                break;
            }

            // If connection was accepted
            if(socket != null)
            {
                Message message = new Message();
                message.obj = socket.getRemoteDevice().getName();
                message.what = Constants.Bluetooth.Message_Type.NEW_CONNECTION;
                mHandler.sendMessage(message);
                ConnectedThread ct = new ConnectedThread(socket, mHandler);
                mConnectionStatusThread.addCT(ct);
                while(!ct.isConnected() && mRunning);
                ct.start();
            }
        }
    }

    public void cancel()
    {
        mRunning = false;
        mConnectionStatusThread.cancel();

        try {
            mServer.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
