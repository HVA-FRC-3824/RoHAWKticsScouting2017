package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 10/7/16
 *
 *
 */
public class LVA_TeamListBuilderDialog extends ArrayAdapter<Integer> {

    private final static String TAG = "LVA_TeamListBuilderDialog";

    private ArrayList<Integer> mMatchNumbers;
    LayoutInflater mLayoutInflator;

    public LVA_TeamListBuilderDialog(Context context, ArrayList<Integer> objects) {
        super(context, R.layout.list_item_team_list_builder_dialog_match_numbers, objects);
        mLayoutInflator = LayoutInflater.from(context);
        mMatchNumbers = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflator.inflate(R.layout.list_item_team_list_builder_dialog_match_numbers, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.match_number);
        textView.setText(String.valueOf(mMatchNumbers.get(position)));

        convertView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMatchNumbers.remove(position);
            }
        });

        return convertView;
    }
}
