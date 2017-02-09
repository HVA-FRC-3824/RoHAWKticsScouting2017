package frc3824.rohawkticsscouting2017.Comms;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/21/16
 *
 * Thread running during the connection process
 */
public class ConnectThread extends Thread {

    private final static String TAG = "ConnectThread";

    private BluetoothSocket mSocket;
    private BluetoothDevice mDevice;
    private String mSocketType;
    private UUID MY_UUID;
    private ConnectedThread mConnectedThread;

    public ConnectThread(BluetoothDevice device, boolean secure) {
        mDevice = device;
        mSocket = null;
        mSocketType = secure ? "Secure" : "Insecure";
        BluetoothSocket tmp = null;
        mConnectedThread = null;

        // Get a BluetoothSocket for a connection with the given BluetoothDevice
        try {
            if (secure) {
                if(MY_UUID == null) {
                    MY_UUID = UUID.fromString(Constants.Comms.UUID_SECURE);
                }
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } else {
                if(MY_UUID == null) {
                    MY_UUID = UUID.fromString(Constants.Comms.UUID_INSECURE);
                }
                tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            }
        } catch (IOException e) {
            Log.d(TAG, "", e);

        }
        mSocket = tmp;
    }

    public void run() {
        Log.i(TAG, "BEGIN mConnectThread");
        setName("ConnectThread" + mSocketType);

        // Make a connection to the BluetoothSocket
        try {
            // This is a blocking call and will only return on a successful connection or an exception
            mSocket.connect();
        } catch (IOException e) {
            // The above works for android to android bluetooth
            // The below works for android to rpi bluetooth
            Class<?> clazz = mSocket.getRemoteDevice().getClass();
            Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};
            Method m = null;
            try {
                m = clazz.getMethod("createRfcommSocket", paramTypes);
                Object[] params = new Object[] {Integer.valueOf(1)};
                mSocket = (BluetoothSocket) m.invoke(mSocket.getRemoteDevice(), params);
                mSocket.connect();

            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
                // Close the socket
                try {
                    mSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                return;
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
                // Close the socket
                try {
                    mSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                return;
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
                // Close the socket
                try {
                    mSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
                try {
                    mSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                return;
            }
        }

        if(Looper.myLooper() == null) {
            Looper.prepare();
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mSocket, new MessageHandler());
        mConnectedThread.start();
        while (isConnected());
        Log.i(TAG, "END mConnectThread");
    }

    public boolean isConnected()
    {
        return mSocket != null && mSocket.isConnected();
    }

    public boolean messageReceived(){
        return mConnectedThread != null && mConnectedThread.messageReceived();
    }

    public void cancel() {
        mConnectedThread.cancel();
        mConnectedThread = null;
    }

    public boolean write(String message) {
        if(mConnectedThread != null) {
            return mConnectedThread.write(message.getBytes());
        }
        return false;
    }
}
