package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.NotificationCompat;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import frc3824.rohawkticsscouting2017.Comms.AcceptThread;
import frc3824.rohawkticsscouting2017.Comms.MessageHandler;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.SuperMatchData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamMatchData;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Statistics.Aggregate;
import frc3824.rohawkticsscouting2017.Utilities.CircularBuffer;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/21/16
 *
 * Activity to receive data from the other tablets
 */
public class Server extends Activity {

    //region variables
    private final static String TAG = "Server";

    private AcceptThread mAcceptThread;
    private BluetoothAdapter mAdapter;
    private CircularBuffer mCircularBuffer;
    private TextView mLogView;
    private Map<String, TextView> mLabels;

    private NotificationManager mNotificationManager;

    private int mMatchNotificationId;
    private NotificationCompat.Builder mMatchNotificationBuilder;

    private int mSuperNotificationId;
    private NotificationCompat.Builder mSuperNotificationBuilder;

    private Intent mNotificationIntent;
    private PendingIntent mNotificationPendingIntent;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        findViewById(android.R.id.content).setKeepScreenOn(true);

        mLogView = (TextView) findViewById(R.id.log);
        mLogView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mCircularBuffer = new CircularBuffer(50);

        mLabels = new HashMap<>();
        mLabels.put(Constants.Bluetooth.Device_Names.BLUE1, (TextView)findViewById(R.id.blue1));
        mLabels.put(Constants.Bluetooth.Device_Names.BLUE2, (TextView)findViewById(R.id.blue2));
        mLabels.put(Constants.Bluetooth.Device_Names.BLUE3, (TextView)findViewById(R.id.blue3));
        mLabels.put(Constants.Bluetooth.Device_Names.RED1, (TextView)findViewById(R.id.red1));
        mLabels.put(Constants.Bluetooth.Device_Names.RED2, (TextView)findViewById(R.id.red2));
        mLabels.put(Constants.Bluetooth.Device_Names.RED3, (TextView)findViewById(R.id.red3));
        mLabels.put(Constants.Bluetooth.Device_Names.SUPER, (TextView)findViewById(R.id.super1));
        mLabels.put(Constants.Bluetooth.Device_Names.SERVER, (TextView)findViewById(R.id.server));
        mLabels.put(Constants.Bluetooth.Device_Names.STRATEGY, (TextView)findViewById(R.id.strategy));
        mLabels.put(Constants.Bluetooth.Device_Names.DRIVETEAM, (TextView)findViewById(R.id.driveteam));
        for(TextView tv: mLabels.values()) {
            tv.setBackgroundColor(Color.RED);
        }

        mAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mAdapter == null) {
            Log.e(TAG, "This device does not have bluetooth");
            mCircularBuffer.insert("This device does not support bluetooth.", Constants.Server_Log_Colors.RED);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mLogView.setText(Html.fromHtml(mCircularBuffer.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                mLogView.setText(Html.fromHtml(mCircularBuffer.toString()));
            }
            return;
        }

        if(!mAdapter.isEnabled()) {
            Log.d(TAG, "Bluetooth is not enabled");
            mCircularBuffer.insert("Bluetooth is not enabled", Constants.Server_Log_Colors.RED);
            mCircularBuffer.insert("Enabling bluetooth...", Constants.Server_Log_Colors.YELLOW);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mLogView.setText(Html.fromHtml(mCircularBuffer.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                mLogView.setText(Html.fromHtml(mCircularBuffer.toString()));
            }
            mAdapter.enable();
            while (!mAdapter.isEnabled());
            mCircularBuffer.insert("Bluetooth is now enabled", Constants.Server_Log_Colors.GREEN);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mLogView.setText(Html.fromHtml(mCircularBuffer.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                mLogView.setText(Html.fromHtml(mCircularBuffer.toString()));
            }
        }

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotificationIntent = new Intent(this, Server.class);
        mNotificationPendingIntent = PendingIntent.getActivity(this, 0, mNotificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mMatchNotificationId = Constants.Notifications.MATCH_RECIEVED;
        mMatchNotificationBuilder = new NotificationCompat.Builder(this);
        mMatchNotificationBuilder.setContentTitle("Received Match Data");
        mMatchNotificationBuilder.setSmallIcon(R.drawable.logo);
        //mMatchNotificationBuilder.setContentIntent(mNotificationPendingIntent);

        mSuperNotificationId = Constants.Notifications.SUPER_RECIEVED;
        mSuperNotificationBuilder = new NotificationCompat.Builder(this);
        mSuperNotificationBuilder.setContentTitle("Received Super Data");
        mSuperNotificationBuilder.setSmallIcon(R.drawable.logo);
        //mSuperNotificationBuilder.setContentIntent(mNotificationPendingIntent);


        if(Looper.myLooper() == null) {
            Looper.prepare();
        }
        mAcceptThread = new AcceptThread(mAdapter, new SyncHandler(), true);
        mAcceptThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAcceptThread.cancel();
    }

    private class SyncHandler extends MessageHandler {
        @Override
        public void displayText(String text) {
            Log.d(TAG, text);
            mCircularBuffer.insert(text);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mLogView.setText(Html.fromHtml(mCircularBuffer.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                mLogView.setText(Html.fromHtml(mCircularBuffer.toString()));
            }
        }

        @Override
        public void displayText(String text, String color) {
            Log.d(TAG, text);
            mCircularBuffer.insert(text, color);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                mLogView.setText(Html.fromHtml(mCircularBuffer.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                mLogView.setText(Html.fromHtml(mCircularBuffer.toString()));
            }
        }

        @Override
        public void connectionAdded(String name) {
            mLabels.get(name).setBackgroundColor(Color.GREEN);
        }

        @Override
        public void connectionLost(String name)
        {
            mLabels.get(name).setBackgroundColor(Color.RED);
        }

        @Override
        public void dataReceived(TeamMatchData teamMatchData) {
            mNotificationManager.notify(mMatchNotificationId, mMatchNotificationBuilder.build());
            Aggregate.aggregateForTeam(teamMatchData.team_number);
        }

        @Override
        public void dataReceived(SuperMatchData superMatchData) {
            mNotificationManager.notify(mSuperNotificationId, mSuperNotificationBuilder.build());
            Aggregate.aggregateForSuper();
        }
    }
}
