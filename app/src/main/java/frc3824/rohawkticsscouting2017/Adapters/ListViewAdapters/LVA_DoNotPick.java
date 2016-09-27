package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 8/23/16
 *
 * List View Adapter for showing teams on the do not pick list
 */
public class LVA_DoNotPick extends ArrayAdapter<Integer> implements View.OnClickListener {

    private final static String TAG = "LVA_DoNotPick";

    private ArrayList<Integer> mDnpTeams;
    private ArrayList<Integer> mOtherTeams;
    private ArrayAdapter<Integer> mOtherAdapter;
    private Database mDatabase;

    public LVA_DoNotPick(Context context, ArrayList<Integer> dnp, ArrayList<Integer> teams, ArrayAdapter<Integer> other) {
        super(context, R.layout.list_item_dnp, dnp);
        mDnpTeams = dnp;
        mOtherTeams = teams;
        mDatabase = Database.getInstance();
        mOtherAdapter = other;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_dnp, null);
        //}

        ((TextView)convertView.findViewById(R.id.team_number)).setText(mDnpTeams.get(position).toString());
        View delete = convertView.findViewById(R.id.delete);
        delete.setId(position);
        delete.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View view) {
        int team_number = mDnpTeams.get(view.getId());
        mDnpTeams.remove(view.getId());
        notifyDataSetChanged();
        mOtherTeams.add(team_number);
        Collections.sort(mOtherTeams);
        mOtherAdapter.notifyDataSetChanged();
        mDatabase.setDNP(team_number, false);
    }
}
