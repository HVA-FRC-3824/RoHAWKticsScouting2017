package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import frc3824.rohawkticsscouting2017.Firebase.DataModels.Qualitative;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 *         Created: 8/24/16
 */
public class LVA_Qualitative extends ArrayAdapter<Qualitative>{

    private final static String TAG = "LVA_Qualitative";

    private ArrayList<Qualitative> mTeams;

    public LVA_Qualitative(Context context, ArrayList<Qualitative> objects) {
        super(context, R.layout.list_item_qualitative, objects);
        mTeams = objects;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_qualitative, null);
        }

        final Qualitative q = mTeams.get(position);
        if(position < mTeams.size() - 1){
            Qualitative qn = mTeams.get(position + 1);
            if(qn.tied || q.tied){
                ((TextView)convertView.findViewById(R.id.rank)).setText(String.format("T%d) ", q.rank));
            } else {
                ((TextView)convertView.findViewById(R.id.rank)).setText(String.format("%d) ", q.rank));
            }
        } else {
            if(q.tied){
                ((TextView)convertView.findViewById(R.id.rank)).setText(String.format("T%d) ", q.rank));
            } else {
                ((TextView)convertView.findViewById(R.id.rank)).setText(String.format("%d) ", q.rank));
            }
        }


        ((TextView) convertView.findViewById(R.id.team_number)).setText(String.format("%d", q.team_number));

        Button tie = (Button)convertView.findViewById(R.id.tie);

        if(position == 0){

            tie.setVisibility(View.GONE);
        } else {
            tie.setVisibility(View.VISIBLE);
            tie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    q.tied = !q.tied;
                    if(q.tied){
                        q.rank = mTeams.get(position - 1).rank;
                    } else {
                        q.rank = position + 1;
                    }
                    Qualitative qp = q;

                    for(int i = position + 1; i < mTeams.size(); i++){
                        Qualitative q2 = mTeams.get(i);
                        if(mTeams.get(i).tied){
                            q2.rank = qp.rank;
                        } else {
                            q2.rank = i + 1;
                        }
                        mTeams.set(i, q2);
                        qp = q2;

                    }

                    notifyDataSetChanged();
                }
            });
        }



        return convertView;
    }

}
