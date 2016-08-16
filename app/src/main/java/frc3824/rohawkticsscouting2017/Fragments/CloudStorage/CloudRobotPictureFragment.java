package frc3824.rohawkticsscouting2017.Fragments.CloudStorage;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_CloudImage;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.CloudImage;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Team;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Firebase.Storage;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/15/16
 *
 *
 */
public class CloudRobotPictureFragment extends Fragment implements View.OnClickListener{

    private final static String TAG = "CloudRobotPictureFragment";

    private Context mContext;

    public CloudRobotPictureFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_cloud_robot_picture, container, false);

        mContext = getContext();

        ListView listView = (ListView)view.findViewById(R.id.file_list);

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String eventKey = sharedPreferences.getString(Constants.Settings.EVENT_KEY, "");
        Database database = Database.getInstance(eventKey);
        Storage storage = Storage.getInstance(eventKey);
        ArrayList<Team> teams = database.getTeams();

        boolean internet = true;
        if(isNetworkAvailable()) {
            view.findViewById(R.id.upload_all).setOnClickListener(this);
            view.findViewById(R.id.download_all).setOnClickListener(this);
        }
        else
        {
            view.findViewById(R.id.upload_all).setEnabled(false);
            view.findViewById(R.id.download_all).setEnabled(false);
            internet = false;
        }

        ArrayList<CloudImage> cis = new ArrayList<>();
        for(Team team: teams)
        {
            CloudImage ci = new CloudImage();

            ci.team_number = team.team_number;
            ci.internet = internet;

            if(team.robot_image_filepath != null && !team.robot_image_filepath.equals(""))
            {
                ci.local = true;
                ci.filepath = team.robot_image_filepath;
            }

            if(team.robot_image_url != null && !team.robot_image_url.equals(""))
            {
                ci.remote = true;
                ci.url = team.robot_image_url;
            }

            cis.add(ci);
        }

        LVA_CloudImage lva = new LVA_CloudImage(mContext, R.layout.list_item_cloud_image, cis,
                storage, database, Constants.Cloud.ROBOT_PICTURE);
        listView.setAdapter(lva);

        //TODO: add click to upload and download

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.upload_all:
                break;
            case R.id.download_all:
                break;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
