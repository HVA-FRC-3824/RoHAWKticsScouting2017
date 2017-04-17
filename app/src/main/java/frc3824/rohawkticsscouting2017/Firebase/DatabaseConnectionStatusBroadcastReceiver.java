package frc3824.rohawkticsscouting2017.Firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * @author frc3824
 * Created: 4/8/2017
 */

public class DatabaseConnectionStatusBroadcastReceiver extends BroadcastReceiver{

    public static final String ACTION_DATABASE_CONNECTED = "action_database_connected";
    public static final String ACTION_DATABASE_DISCONNECTED = "action_database_disconnected";

    private DatabaseConnectionStateListener m_listener;

    public DatabaseConnectionStatusBroadcastReceiver(Context context, DatabaseConnectionStateListener listener)
    {
        m_listener = listener;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DATABASE_CONNECTED);
        intentFilter.addAction(ACTION_DATABASE_DISCONNECTED);
        context.registerReceiver(this, intentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(ACTION_DATABASE_CONNECTED.equals(intent.getAction())) {
            m_listener.databaseConnected();
        } else if(ACTION_DATABASE_DISCONNECTED.equals(intent.getAction())){
            m_listener.databaseDisconnected();
        }
    }
}
