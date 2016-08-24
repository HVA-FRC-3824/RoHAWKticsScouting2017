package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 *         Created: 8/24/16
 */
public class LVA_Qualitative extends ArrayAdapter<Integer>{

    private final static String TAG = "LVA_Qualitative";

    private ArrayList<Integer> mTeams;

    public LVA_Qualitative(Context context, ArrayList<Integer> objects) {
        super(context, R.layout.list_item_qualitative, objects);
        mTeams = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_qualitative, null);
        }

        ((TextView)convertView.findViewById(R.id.rank)).setText(String.format("%d) ", position + 1));
        ((TextView)convertView.findViewById(R.id.team_number)).setText(String.format("%d", mTeams.get(position)));

        return convertView;
    }

}
