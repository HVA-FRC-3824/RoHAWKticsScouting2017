package frc3824.rohawkticsscouting2017.Utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * @author frc3824
 * Created: 8/11/16
 *
 * Map wrapper that holds ScoutValues
 */
public class ScoutMap {

    private final static String TAG = "ScoutMap";

    private Map<String, ScoutValue> mMap;

    public ScoutMap()
    {
        mMap = new HashMap<>();
    }

    public void put(String key, ScoutValue value) { mMap.put(key, value); }

    public void put(String key, int value)
    {
        mMap.put(key, new ScoutValue(value));
    }

    public void put(String key, float value)
    {
        mMap.put(key, new ScoutValue(value));
    }

    public void put(String key, double value)
    {
        mMap.put(key, new ScoutValue(value));
    }

    public void put(String key, String value)
    {
        mMap.put(key, new ScoutValue(value));
    }

    public void put(String key, boolean value)
    {
        mMap.put(key, new ScoutValue(value));
    }

    public void put(String key, Object value) { mMap.put(key, new ScoutValue(value)); }

    public boolean contains(String key)
    {
        return mMap.containsKey(key);
    }

    public ScoutValue get(String key)
    {
        return mMap.get(key);
    }

    public int getInt(String key) throws ScoutValue.TypeException {
        return mMap.get(key).getInt();
    }

    public double getDouble(String key) throws ScoutValue.TypeException {
        return mMap.get(key).getDouble();
    }

    public String getString(String key) throws ScoutValue.TypeException {
        return mMap.get(key).getString();
    }

    public boolean getBoolean(String key) throws ScoutValue.TypeException {
        return mMap.get(key).getBoolean();
    }

    public Object getObject(String key) throws ScoutValue.TypeException {
        return mMap.get(key).getObject();
    }

    public void remove(String key) { mMap.remove(key); }

    public void clear()
    {
        mMap.clear();
    }

    private Map<String, ScoutValue> getMap()
    {
        return mMap;
    }

    public void putAll(ScoutMap map)
    {
        mMap.putAll(map.getMap());
    }
}
