package frc3824.rohawkticsscouting2017.Fragments.PickList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_PickList;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPA;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Views.DragSortListView.DragSortListView;

/**
 * @author Andrew Messing
 * Created: 8/23/16
 */
public class ScoutPickFragment extends Fragment implements View.OnClickListener, DragSortListView.DropListener {

    private final static String TAG = "ScoutPickFragment";

    protected ArrayList<TPA> mTeams;
    protected Database mDatabase;
    private LVA_PickList mAdapter;
    private Comparator<TPA> mComparator;
    private DragSortListView mList;

    public ScoutPickFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_scout_pick, container, false);

        mList = (DragSortListView)view.findViewById(R.id.pick_list);

        mDatabase = Database.getInstance();

        mTeams = setupTeamList();

        // Remove dnp pick teams
        for(int i = 0; i < mTeams.size(); i++)
        {
            if(mTeams.get(i).dnp)
            {
                mTeams.remove(i);
                i--;
            }
        }

        mComparator = new Comparator<TPA>() {
            @Override
            public int compare(TPA t1, TPA t2) {
                if(t1.picked && t2.picked)
                {
                    return 0;
                }
                else if(t1.picked)
                {
                    return 1;
                }
                else if(t2.picked)
                {
                    return -1;
                }
                else if(t1.manual_ranking == -1)
                {
                    return Double.compare(t2.pick_ability, t1.pick_ability);
                }
                else
                {
                    return Integer.compare(t1.manual_ranking, t2.manual_ranking);
                }

            }
        };
        Collections.sort(mTeams, mComparator);

        mAdapter = new LVA_PickList(getContext(), mTeams);
        mList.setAdapter(mAdapter);
        mList.setDropListener(this);

        view.findViewById(R.id.reset).setOnClickListener(this);

        return view;
    }

    // Should be Overridden by child class
    public ArrayList<TPA> setupTeamList()
    {
        return new ArrayList<>();
    }

    // Should be Overridden by child class
    public void save() {}


    // Save button or always save?
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                for(TPA team: mTeams)
                {
                    team.manual_ranking = -1;
                }
                Collections.sort(mTeams, mComparator);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void drop(int from, int to) {

        TPA team = mTeams.get(from);
        mTeams.remove(team);
        mTeams.add(to, team);

        /*
        if (from < to) {
            for (int i = from; i < to; i++) {
                team = mTeams.get(i);
                team.manual_ranking = i + 1;
            }
        } else if (from > to) {
            for (int i = to + 1; i <= from; i++) {
                team = mTeams.get(i);
                team.manual_ranking = i + 1;
            }
        }
        */

        for(int i = 0; i < mTeams.size(); i++)
        {
            TPA t = mTeams.get(i);
            t.manual_ranking = i + 1;
        }

        mAdapter.notifyDataSetChanged();
        save();
    }

    public void update()
    {
        mTeams = setupTeamList();
        // Remove dnp pick teams
        for(int i = 0; i < mTeams.size(); i++)
        {
            if(mTeams.get(i).dnp)
            {
                mTeams.remove(i);
                i--;
            }
        }
        Collections.sort(mTeams, mComparator);
        mAdapter = new LVA_PickList(getContext(), mTeams);
        mList.setAdapter(mAdapter);
    }
}