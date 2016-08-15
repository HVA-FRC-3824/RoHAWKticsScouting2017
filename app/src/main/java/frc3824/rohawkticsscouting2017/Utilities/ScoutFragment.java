package frc3824.rohawkticsscouting2017.Utilities;

import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import frc3824.rohawkticsscouting2017.Views.CustomScoutView;

/**
 * @author Andrew Messing
 * Created: 8/11/16
 *
 * Abstract base class for each of the fragments that need to save
 */
public abstract class ScoutFragment extends Fragment {

    private final static String TAG = "ScoutFragment";

    // protected means that it can be accessed by its children class
    // a private variable cannot
    protected ScoutMap mValueMap;

    public ScoutFragment() {
        mValueMap = new ScoutMap();
    }

    public void setValueMap(ScoutMap map)
    {
        mValueMap = map;
    }

    /**
     * Writes the contents of all the CustomScoutViews contained within to a map through recursively
     * searching through the views
     *
     * @param map the ScoutMap to write all the values to
     * @return any error messages
     */
    public String writeContentsToMap(ScoutMap map)
    {
        // Get the ViewGroup holding all of the widgets
        ViewGroup vg = (ViewGroup) getView();

        if(vg == null)
        {
            Log.d(TAG, "Null View");
            // If the view has been destroyed, state should already be saved to the parent activity
            return "";
        }

        String error = "";
        int childCount = vg.getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            View view = vg.getChildAt(i);
            if(view instanceof CustomScoutView)
            {
                error += ((CustomScoutView)view).writeToMap(map);
            }
            else if(view instanceof ViewGroup)
            {
                error += writeContentsToMap(map, (ViewGroup)view);
            }
        }
        return error;
    }

    /**
     * Writes the contents of all the CustomScoutViews contained within to a map through recursively
     * searching through the views contained in the provided ViewGroup
     *
     * @param map the ScoutMap to write all the values to
     * @param vg the viewGroup to search through
     * @return any error messages
     */
    protected String writeContentsToMap(ScoutMap map, ViewGroup vg)
    {
        String error = "";
        int childCount = vg.getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            View view = vg.getChildAt(i);
            if(view instanceof CustomScoutView)
            {
                error += ((CustomScoutView)view).writeToMap(map);
            }
            else if(view instanceof ViewGroup)
            {
                error += writeContentsToMap(map, (ViewGroup)view);
            }
        }
        return error;
    }

    /**
     * Displays all the values in the map to their respective CustomScoutViews by recursively
     * searching through all the views
     *
     * @param map the map in which the values are retrieved from
     */
    public String restoreContentsFromMap(ScoutMap map)
    {
        //Get the ViewGroup holding all of the widgets
        ViewGroup vg = (ViewGroup) getView();
        if(vg == null)
        {
            Log.d(TAG, "Null view");
            // If the view has been destroyed, state should already be saved to parent activity
            map.putAll(mValueMap);
            return "";
        }

        String error = "";
        int childCount = vg.getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            View view = vg.getChildAt(i);
            if(view instanceof CustomScoutView)
            {
                error += ((CustomScoutView)view).restoreFromMap(map);
            }
            else if(view instanceof ViewGroup)
            {
                error += restoreContentsFromMap(map, (ViewGroup)view);
            }
        }
        return error;
    }

    /**
     * Displays all the values in the map to their respective CustomScoutViews by recursively
     * searching through all the views
     *
     * @param map the map in which the values are retrieved from
     * @param vg the ViewGroup to search through
     */
    protected String restoreContentsFromMap(ScoutMap map, ViewGroup vg)
    {
        String error = "";
        int childCount = vg.getChildCount();
        for(int i = 0; i < childCount; i++)
        {
            View view = vg.getChildAt(i);
            if(view instanceof CustomScoutView)
            {
                error += ((CustomScoutView)view).restoreFromMap(map);
            }
            else if(view instanceof ViewGroup)
            {
                error += restoreContentsFromMap(map, (ViewGroup)view);
            }
        }

        return error;
    }

    @Override
    public void onDestroyView()
    {
        mValueMap.clear();
        writeContentsToMap(mValueMap);
        super.onDestroyView();
    }
}
