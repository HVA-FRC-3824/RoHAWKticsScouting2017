package frc3824.rohawkticsscouting2017.Fragments.PickList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_DoNotPick;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 * Created: 8/23/16
 *
 * Fragment for putting teams on the do not pick list. These team will not
 * show up on the pick lists.
 */
public class DoNotPickFragment extends ScoutPickFragment implements View.OnClickListener {

    private final static String TAG = "DoNotPickFragment";

    private Spinner mSpinner;
    private ArrayAdapter<Integer> mSpinnerAdapter;
    private ListView mListView;
    private LVA_DoNotPick mListViewAdapter;
    private Database mDatabase;
    private ArrayList<Integer> mTeamNumbers;
    private ArrayList<Integer> mDnpTeams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_do_not_pick, container, false);

        mDatabase = Database.getInstance();
        mTeamNumbers = mDatabase.getTeamNumbers();
        mDnpTeams = mDatabase.getDnpList();

        mTeamNumbers.removeAll(mDnpTeams);
        Collections.sort(mTeamNumbers);
        Collections.sort(mDnpTeams);

        Context context = getContext();
        mSpinner = (Spinner)view.findViewById(R.id.team_list);
        mSpinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, mTeamNumbers);
        mSpinner.setAdapter(mSpinnerAdapter);

        view.findViewById(R.id.add_team).setOnClickListener(this);

        mListView = (ListView)view.findViewById(R.id.dnp_list);
        mListViewAdapter = new LVA_DoNotPick(context, mDnpTeams, mTeamNumbers, mSpinnerAdapter);
        mListView.setAdapter(mListViewAdapter);

        return view;
    }


    @Override
    public void onClick(View view) {
        int position = mSpinner.getSelectedItemPosition();
        int team_number = mTeamNumbers.get(position);
        mDatabase.setDNP(team_number, true);
        mTeamNumbers.remove(position);
        mDnpTeams.add(team_number);
        Collections.sort(mDnpTeams);
        mSpinnerAdapter.notifyDataSetChanged();
        mListViewAdapter.notifyDataSetChanged();
    }
}
