package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import frc3824.rohawkticsscouting2017.Activities.TeamView;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPickAbility;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 *         Created: 8/23/16
 */
public class LVA_PickList extends ArrayAdapter<TeamPickAbility>{

    private final static String TAG = "LVA_PickList";

    private ArrayList<TeamPickAbility> mTeams;
    private Context mContext;

    public LVA_PickList(Context context, ArrayList<TeamPickAbility> objects) {
        super(context, R.layout.list_item_pick, objects);
        mTeams = objects;
        mContext = context;
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_pick, null);
        }

        final TeamPickAbility team = mTeams.get(position);

        if(team.picked)
        {
            convertView.setBackgroundColor(Color.RED);
        }
        else
        {
            convertView.setBackgroundColor(Color.GREEN);
        }

        ((TextView)convertView.findViewById(R.id.team_number)).setText(String.valueOf(team.team_number));
        ((TextView)convertView.findViewById(R.id.nickname)).setText(String.valueOf(team.nickname));

        if(team.robot_picture_filepath != null && team.robot_picture_filepath != "") {
            displayPicture((ImageView) convertView.findViewById(R.id.robot_picture), team.robot_picture_filepath);
        }

        ((TextView)convertView.findViewById(R.id.top_line)).setText(team.top_line);
        ((TextView)convertView.findViewById(R.id.second_line)).setText(team.second_line);
        ((TextView)convertView.findViewById(R.id.third_line)).setText(team.third_line);

        if(team.red_card)
        {
            convertView.findViewById(R.id.red_card).setVisibility(View.VISIBLE);
        }
        else
        {
            convertView.findViewById(R.id.red_card).setVisibility(View.GONE);
        }

        if(team.yellow_card)
        {
            convertView.findViewById(R.id.yellow_card).setVisibility(View.VISIBLE);
        }
        else
        {
            convertView.findViewById(R.id.yellow_card).setVisibility(View.GONE);
        }

        if(team.stopped_moving)
        {
            convertView.findViewById(R.id.stopped_moving).setVisibility(View.VISIBLE);
        }
        else
        {
            convertView.findViewById(R.id.stopped_moving).setVisibility(View.GONE);
        }

        Button pickedButton = (Button)convertView.findViewById(R.id.picked_button);
        if(team.picked)
        {
            pickedButton.setText("Unpicked");
        }
        else
        {
            pickedButton.setText("Picked");
        }

        pickedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Database.getInstance().setPicked(team.team_number, !team.picked);
                team.picked = !team.picked;
                Collections.sort(mTeams, new Comparator<TeamPickAbility>() {
                    @Override
                    public int compare(TeamPickAbility t1, TeamPickAbility t2) {
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
                });
                notifyDataSetChanged();
            }
        });


        convertView.findViewById(R.id.view_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TeamView.class);
                intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, team.team_number);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    /**
     * Sets the Image view to display the image
     *
     * @return
     */
    private void displayPicture(ImageView imageView, String filepath) {
        // Get the dimensions of the View
        int targetW = 100;
        int targetH = 100;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filepath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filepath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }
}
