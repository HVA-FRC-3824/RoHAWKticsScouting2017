package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamRankingData;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author Andrew Messing
 *         Created: 8/22/16
 */
public class LVA_PredictedRanking extends ArrayAdapter<TeamRankingData> {

    private final static String TAG = "LVA_PredictedRanking";

    private ArrayList<TeamRankingData> mRankings;

    public LVA_PredictedRanking(Context context, ArrayList<TeamRankingData> objects) {
        super(context, R.layout.list_item_predicted_ranking, objects);

        mRankings = objects;

        Collections.sort(objects, new Comparator<TeamRankingData>() {
            @Override
            public int compare(TeamRankingData teamRankingData, TeamRankingData t1) {
                return Integer.compare(teamRankingData.rank, t1.rank);
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_predicted_ranking, null);
        }

        TeamRankingData ranking = mRankings.get(position);

        ((TextView)convertView.findViewById(R.id.rank)).setText(String.valueOf(ranking.rank));
        ((TextView)convertView.findViewById(R.id.team_number)).setText(String.valueOf(ranking.team_number));
        ((TextView)convertView.findViewById(R.id.rps)).setText(String.valueOf(ranking.RPs));
        ((TextView)convertView.findViewById(R.id.auto)).setText(String.valueOf(ranking.auto));
        ((TextView)convertView.findViewById(R.id.scale_challenge)).setText(String.valueOf(ranking.scale_challenge));
        ((TextView)convertView.findViewById(R.id.record)).setText(String.format("%d-%d-%d", ranking.wins, ranking.ties, ranking.loses));
        ((TextView)convertView.findViewById(R.id.played)).setText(String.valueOf(ranking.played));


        return convertView;
    }
}
