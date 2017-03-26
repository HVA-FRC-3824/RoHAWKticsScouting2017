package frc3824.rohawkticsscouting2017.Fragments.PickList;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import frc3824.rohawkticsscouting2017.Activities.TeamView;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_PickList;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPickAbility;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Views.DragSortListView.DragSortListView;

/**
 * @author frc3824
 * Created: 8/23/16
 */
public class ScoutPickFragment extends Fragment implements View.OnClickListener, DragSortListView.DropListener {

    private final static String TAG = "ScoutPickFragment";

    protected ArrayList<TeamPickAbility> mTeams;
    protected Database mDatabase;
    protected TeamPickAbility mUs;
    private LVA_PickList mAdapter;
    private Comparator<TeamPickAbility> mComparator;
    private DragSortListView mList;

    public ScoutPickFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scout_pick, container, false);

        mList = (DragSortListView)view.findViewById(R.id.pick_list);

        mDatabase = Database.getInstance();

        mTeams = setupTeamList();

        // Remove dnp pick teams and us (Can't pick ourselves)
        for(int i = 0; i < mTeams.size(); i++) {
            if(mTeams.get(i).team_number == Constants.OUR_TEAM_NUMBER){
                mUs = mTeams.get(i);
                mTeams.remove(i);
                i--;
            }
            else if(mTeams.get(i).dnp) {
                mTeams.remove(i);
                i--;
            }
        }

        setupUs(view);

        mComparator = new Comparator<TeamPickAbility>() {
            @Override
            public int compare(TeamPickAbility t1, TeamPickAbility t2) {
                if(t1.picked && t2.picked) {
                    return 0;
                } else if(t1.picked) {
                    return 1;
                } else if(t2.picked) {
                    return -1;
                } else if(t1.manual_ranking == -1) {
                    return Double.compare(t2.pick_ability, t1.pick_ability);
                } else {
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

    /**
     *
     * @param view
     */
    public void setupUs(View view){
        View ourTeam = view.findViewById(R.id.our_team);

        if(mUs == null){
            ourTeam.setVisibility(View.GONE);
            return;
        }

        ((TextView)ourTeam.findViewById(R.id.team_number)).setText(String.valueOf(mUs.team_number));
        ((TextView)ourTeam.findViewById(R.id.nickname)).setText(String.valueOf(mUs.nickname));

        if(mUs.robot_picture_filepath != null && mUs.robot_picture_filepath != "") {
            displayPicture((ImageView) ourTeam.findViewById(R.id.robot_picture), mUs.robot_picture_filepath);
        }

        Pattern pattern = Pattern.compile("PA: \\d+\\.\\d+ (\\.+)");
        Matcher matcher = pattern.matcher(mUs.top_line);

        if(matcher.matches()) {
            String top_line_without_pa = matcher.group();
            ((TextView) ourTeam.findViewById(R.id.top_line)).setText(top_line_without_pa);
        } else {
            ((TextView) ourTeam.findViewById(R.id.top_line)).setText(mUs.top_line);
        }
        ((TextView)ourTeam.findViewById(R.id.second_line)).setText(mUs.second_line);
        ((TextView)ourTeam.findViewById(R.id.third_line)).setText(mUs.third_line);

        if(mUs.red_card)
        {
            ourTeam.findViewById(R.id.red_card).setVisibility(View.VISIBLE);
        }
        else
        {
            ourTeam.findViewById(R.id.red_card).setVisibility(View.GONE);
        }

        if(mUs.yellow_card)
        {
            ourTeam.findViewById(R.id.yellow_card).setVisibility(View.VISIBLE);
        }
        else
        {
            ourTeam.findViewById(R.id.yellow_card).setVisibility(View.GONE);
        }

        if(mUs.stopped_moving)
        {
            ourTeam.findViewById(R.id.stopped_moving).setVisibility(View.VISIBLE);
        }
        else
        {
            ourTeam.findViewById(R.id.stopped_moving).setVisibility(View.GONE);
        }

        ourTeam.findViewById(R.id.picked_button).setVisibility(View.GONE);


        ourTeam.findViewById(R.id.view_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), TeamView.class);
                intent.putExtra(Constants.Intent_Extras.TEAM_NUMBER, mUs.team_number);
                getContext().startActivity(intent);
            }
        });
    }

    // Should be Overridden by child class
    public ArrayList<TeamPickAbility> setupTeamList() {
        return new ArrayList<>();
    }

    // Should be Overridden by child class
    public void save() {}


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                for(TeamPickAbility team: mTeams) {
                    team.manual_ranking = -1;
                }
                Collections.sort(mTeams, mComparator);
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void drop(int from, int to) {

        TeamPickAbility team = mTeams.get(from);
        mTeams.remove(team);
        mTeams.add(to, team);

        for(int i = 0; i < mTeams.size(); i++)
        {
            TeamPickAbility t = mTeams.get(i);
            t.manual_ranking = i + 1;
        }

        mAdapter.notifyDataSetChanged();
        save();
    }

    public void update() {
        mTeams = setupTeamList();
        // Remove dnp pick teams
        for(int i = 0; i < mTeams.size(); i++) {
            if(mTeams.get(i).dnp) {
                mTeams.remove(i);
                i--;
            }
        }
        Collections.sort(mTeams, mComparator);
        mAdapter = new LVA_PickList(getContext(), mTeams);
        mList.setAdapter(mAdapter);
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