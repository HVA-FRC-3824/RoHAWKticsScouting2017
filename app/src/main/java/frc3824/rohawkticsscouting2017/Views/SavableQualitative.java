package frc3824.rohawkticsscouting2017.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_Qualitative;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.ScoutMap;
import frc3824.rohawkticsscouting2017.Utilities.ScoutValue;
import frc3824.rohawkticsscouting2017.Views.DragSortListView.DragSortListView;

/**
 * @author frc3824
 * Created: 8/24/16
 *
 *
 */
public class SavableQualitative extends SavableView implements DragSortListView.DropListener {

    private final static String TAG = "SavableQualitative";

    private DragSortListView mListView;
    private LVA_Qualitative mLva;
    private String mKey;
    private Context mContext;
    private ArrayList<Integer> mTeams;

    public SavableQualitative(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.savable_qualitative, this);

        // Setup label and get key
        TextView label = (TextView) findViewById(R.id.label);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SavableView);
        label.setText(typedArray.getString(R.styleable.SavableView_label));
        mKey = typedArray.getString(R.styleable.SavableView_key);
        typedArray.recycle();

        mListView = (DragSortListView)findViewById(R.id.listview);
        mListView.setDropListener(this);
        //mListView.setFloatViewManager(new SimpleFloatViewManager(mListView));
    }

    public void setTeams(ArrayList<Integer> teams) {
        mTeams = teams;
        mLva = new LVA_Qualitative(mContext, teams);
        mListView.setAdapter(mLva);
    }

    @Override
    public void drop(int from, int to) {
        int team_number = mTeams.get(from);
        mTeams.remove(from);
        mTeams.add(to, team_number);
        mLva.notifyDataSetChanged();
    }

    @Override
    public String writeToMap(ScoutMap map) {
        map.put(mKey, mTeams);
        return "";
    }

    @Override
    public String restoreFromMap(ScoutMap map) {
        if(map.contains(mKey)) {
            try {
                mTeams = (ArrayList<Integer>)map.getObject(mKey);
            } catch (ScoutValue.TypeException e) {
                Log.e(TAG, e.getMessage());
                return e.getMessage();
            }
        }

        return "";
    }
}
