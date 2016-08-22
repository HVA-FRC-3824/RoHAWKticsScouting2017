package frc3824.rohawkticsscouting2017.Bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 * Created: 8/21/16
 *
 *
 */
public class ConnectThread extends Thread {

    private final static String TAG = "ConnectThread";

    private BluetoothSocket mSocket;
    private BluetoothDevice mDevice;
    private String mSocketType;
    private UUID MY_UUID;
    private ConnectedThread mConnectedThread;

    public ConnectThread(BluetoothDevice device, boolean secure)
    {
        mDevice = device;
        mSocket = null;
        mSocketType = secure ? "Secure" : "Insecure";
        BluetoothSocket tmp = null;
        mConnectedThread = null;


        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            if (secure) {
                if(MY_UUID == null)
                {
                    MY_UUID = UUID.fromString(Constants.Bluetooth.UUID_SECURE);
                }
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } else {
                if(MY_UUID == null)
                {
                    MY_UUID = UUID.fromString(Constants.Bluetooth.UUID_INSECURE);
                }
                tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            }
        } catch (IOException e) {
            Log.e(TAG, "Socket create() failed", e);
        }
        mSocket = tmp;
    }

    public void run() {
        Log.i(TAG, "BEGIN mConnectThread");
        setName("ConnectThread" + mSocketType);

        // Make a connection to the BluetoothSocket
        try {
            // This is a blocking call and will only return on a
            // successful connection or an exception
            mSocket.connect();
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
            // Close the socket
            try {
                mSocket.close();
            } catch (IOException e2) {
                Log.e(TAG, "unable to close() socket during connection failure", e2);
            }
            return;
        }

        if(Looper.myLooper() == null) {
            Looper.prepare();
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mSocket, new BluetoothHandler());
        mConnectedThread.start();
        while (isConnected());
        Log.i(TAG, "END mConnectThread");
    }

    public boolean isConnected()
    {
        return mSocket != null && mSocket.isConnected();
    }

    public void cancel() {
        mConnectedThread.cancel();
        mConnectedThread = null;
    }

    public boolean write(String message)
    {
        if(mConnectedThread != null)
        {
            return mConnectedThread.write(message.getBytes());
        }
        return false;
    }
}
