package frc3824.rohawkticsscouting2017.Comms;

import android.bluetooth.BluetoothSocket;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Arrays;

import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/21/16
 *
 * This thread runs during a connection with a remote device.
 * It handles all incoming and outgoing transmissions.
 */
public class ConnectedThread extends Thread {

    private final static String TAG = "ConnectedThread";

    private BluetoothSocket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private int mState;
    private MessageHandler mHandler;
    private boolean mMessageReceived;
    private boolean running;

    public ConnectedThread(BluetoothSocket socket, MessageHandler handler) {
        Log.d(TAG, "create ConnectedThread");
        mSocket = socket;
        mHandler = handler;

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the BluetoothSocket input and output streams
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "temp sockets not created", e);
        }

        mMessageReceived = false;
        mInputStream = tmpIn;
        mOutputStream = tmpOut;
        mState = Constants.Bluetooth.RECEIVING;
    }

    public void run() {
        Log.i(TAG, "BEGIN ConnectedThread");
        running = true;
        try {
            while (running) {
                if (mInputStream.available() > 0 && mState == Constants.Bluetooth.RECEIVING) {
                    boolean waitingForHeader = true;
                    ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
                    byte[] headerBytes = new byte[22];
                    byte[] digest = new byte[16];
                    int headerIndex = 0;
                    byte header;
                    int totalSize, remainingSize = -1;

                    while (running) {
                        if (waitingForHeader) {
                            header = (byte) mInputStream.read();
                            headerBytes[headerIndex++] = header;

                            if(headerIndex == 22) {
                                if ((headerBytes[0] == Constants.Bluetooth.HEADER_MSB) && (headerBytes[1] == Constants.Bluetooth.HEADER_LSB)) {
                                    Log.v(TAG, "Header Received.  Now obtaining length");
                                    byte[] dataSizeBuffer = Arrays.copyOfRange(headerBytes, 2, 6);
                                    totalSize = byteArrayToInt(dataSizeBuffer);
                                    remainingSize = totalSize;
                                    Log.v(TAG, "Data size: " + totalSize);
                                    digest = Arrays.copyOfRange(headerBytes, 6, 22);
                                    waitingForHeader = false;
                                } else {
                                    Log.e(TAG, "Did not receive correct header.  Closing socket");
                                    mSocket.close();
                                    mHandler.sendEmptyMessage(Constants.Bluetooth.Message_Type.INVALID_HEADER);
                                    break;
                                }
                            }
                        } else {
                            // Read the data from the stream in chunks
                            byte[] buffer = new byte[Constants.Bluetooth.CHUNK_SIZE];
                            Log.v(TAG, String.format("Waiting for data.  Expecting %d more bytes.", remainingSize));
                            int bytesRead = mInputStream.read(buffer);
                            Log.v(TAG, "Read " + bytesRead + " bytes into buffer");
                            dataOutputStream.write(buffer, 0, bytesRead);
                            remainingSize -= bytesRead;

                            if (remainingSize <= 0) {
                                Log.v(TAG, "Expected data has been received.");
                                break;
                            }
                        }
                    }
                    // check the integrity of the data
                    byte[] data = dataOutputStream.toByteArray();

                    if (digestMatch(data, digest)) {
                        Log.v(TAG, "Digest matches OK.");
                        Message message = new Message();
                        message.obj = data;
                        message.what = Constants.Bluetooth.Message_Type.DATA_RECEIVED;
                        mHandler.sendMessage(message);

                        // Send the digest back to the client as a confirmation
                        Log.v(TAG, "Sending back digest for confirmation");
                        mOutputStream.write(digest);
                        mMessageReceived = true;
                    } else {
                        Log.e(TAG, "Digest did not match.  Corrupt transfer?");
                        mHandler.sendEmptyMessage(Constants.Bluetooth.Message_Type.DIGEST_DID_NOT_MATCH);
                        mOutputStream.write(digest);
                    }

                }
                while (mInputStream.available() == 0 && mState != Constants.Bluetooth.RECEIVING && running);
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            cancel();
        }
        Log.i(TAG, "END ConnectedThread");
    }

    public boolean messageReceived(){
        return mMessageReceived;
    }

    /**
     * Write to the connected OutStream.
     *
     * @param buffer The bytes to write
     */
    public boolean write(byte[] buffer) {
        String tempBuffer = new String(buffer);
        if(buffer.length > 30) {
            Log.d(TAG, String.format("Sending: %s ... %s",tempBuffer.substring(0,15),tempBuffer.substring(tempBuffer.length()-15)));
        } else {
            Log.d(TAG, String.format("Sending: %s",tempBuffer));
        }
        mState = Constants.Bluetooth.SENDING;
        try {
            mHandler.sendEmptyMessage(Constants.Bluetooth.Message_Type.SENDING_DATA);

            // Send the header control first
            mOutputStream.write(Constants.Bluetooth.HEADER_MSB);
            mOutputStream.write(Constants.Bluetooth.HEADER_LSB);

            // write size
            mOutputStream.write(intToByteArray(buffer.length));

            // write digest
            byte[] digest = getDigest(buffer);
            mOutputStream.write(digest);

            // now write the data
            mOutputStream.write(buffer);
            mOutputStream.flush();

            Log.v(TAG, "Data sent.  Waiting for return digest as confirmation");

            byte[] incomingDigest = new byte[16];
            int incomingIndex = 0;

            while (true) {
                byte header = (byte)mInputStream.read();
                incomingDigest[incomingIndex++] = header;
                if (incomingIndex == 16) {
                    if (digestMatch(buffer, incomingDigest)) {
                        Log.v(TAG, "Digest matched OK.  Data was received OK.");
                        mHandler.sendEmptyMessage(Constants.Bluetooth.Message_Type.DATA_SENT_OK);
                        mState = Constants.Bluetooth.RECEIVING;
                        return true;
                    } else {
                        Log.e(TAG, "Digest did not match.  Might want to resend.");
                        mHandler.sendEmptyMessage(Constants.Bluetooth.Message_Type.DIGEST_DID_NOT_MATCH);
                        mState = Constants.Bluetooth.RECEIVING;
                        return false;
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            cancel();
        }
        mState = Constants.Bluetooth.RECEIVING;
        return false;
    }

    public void cancel() {
        running = false;
        try {
            // Hack to break the pipe and get the other side to close
            mOutputStream.write("x".getBytes());
        } catch (IOException e) {
            Log.d(TAG,e.getMessage());
        }

        try{
            mSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "close() of connect socket failed", e);
        }
    }

    public String getRemoveDeviceName() {
        return mSocket.getRemoteDevice().getName();
    }

    public boolean isConnected() {
        return mSocket.isConnected();
    }

    public byte[] intToByteArray(int a) {
        byte[] ret = new byte[4];
        ret[3] = (byte) (a & 0xFF);
        ret[2] = (byte) ((a >> 8) & 0xFF);
        ret[1] = (byte) ((a >> 16) & 0xFF);
        ret[0] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }

    public int byteArrayToInt(byte[] b) {
        return (b[3] & 0xFF) + ((b[2] & 0xFF) << 8) + ((b[1] & 0xFF) << 16) + ((b[0] & 0xFF) << 24);
    }

    public boolean digestMatch(byte[] imageData, byte[] digestData) {
        return Arrays.equals(getDigest(imageData), digestData);
    }

    public byte[] getDigest(byte[] imageData) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            return messageDigest.digest(imageData);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            throw new UnsupportedOperationException("MD5 algorithm not available on this device.");
        }
    }

}
