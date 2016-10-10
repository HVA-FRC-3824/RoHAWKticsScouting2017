package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.MatchNumberCheck;
import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 10/9/16
 *
 *
 */
public class LVA_MatchScoutDrawer extends ArrayAdapter<MatchNumberCheck> {

    private final static String TAG = "LVA_MatchScoutDrawer";

    private ArrayList<MatchNumberCheck> mMatchNumbers;
    private Context mContext;

    public LVA_MatchScoutDrawer(Context context, ArrayList<MatchNumberCheck> objects) {
        super(context, R.layout.list_item_match_scout_drawer, objects);
        mMatchNumbers = objects;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_match_scout_drawer, null);
        }

        MatchNumberCheck mnc = mMatchNumbers.get(position);

        TextView match_number = (TextView)convertView.findViewById(R.id.match_number);
        match_number.setText(String.format("Match %d", mnc.match_number));

        ImageView imageView = (ImageView)convertView.findViewById(R.id.icon);

        if(mnc.check){
            imageView.setImageResource(R.drawable.check_2_color);
        } else {
            imageView.setImageBitmap(null);
        }

        return convertView;
    }
}
