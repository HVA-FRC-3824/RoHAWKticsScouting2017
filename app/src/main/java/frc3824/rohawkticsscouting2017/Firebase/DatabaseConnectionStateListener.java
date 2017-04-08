package frc3824.rohawkticsscouting2017.Firebase;

/**
 * @author frc3824
 * Created: 4/8/2017.
 */

public interface DatabaseConnectionStateListener {
    void databaseConnected();

    void databaseDisconnected();
}
