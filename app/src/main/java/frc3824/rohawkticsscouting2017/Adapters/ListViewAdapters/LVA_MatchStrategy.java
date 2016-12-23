package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Activities.MatchPlanning;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 12/19/16
 */
public class LVA_MatchStrategy extends ArrayAdapter<Pair<Integer, Boolean>> implements View.OnClickListener {

    private final static String TAG = "LVA_MatchStrategy";

    private ArrayList<Pair<Integer, Boolean>> mMatches;
    private Context mContext;

    public LVA_MatchStrategy(Context context, ArrayList<Pair<Integer, Boolean>> objects) {
        super(context, R.layout.list_item_match_strategy, objects);
        mMatches = objects;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_match_strategy, null);
        }

        ((TextView)convertView.findViewById(R.id.filename)).setText(String.valueOf(mMatches.get(position).first));
        if(mMatches.get(position).second)
        {
            Database.getInstance().getMatchStrategy(mMatches.get(position).first).create(mContext);
            convertView.setBackgroundColor(Color.GREEN);
        }
        else
        {
            convertView.setBackgroundColor(Color.RED);
        }

        convertView.setOnClickListener(this);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        int match_number = Integer.parseInt(((TextView)v.findViewById(R.id.filename)).getText().toString());
        Intent intent = new Intent(mContext, MatchPlanning.class);
        intent.putExtra(Constants.Intent_Extras.MATCH_NUMBER, match_number);
        mContext.startActivity(intent);
    }
}
