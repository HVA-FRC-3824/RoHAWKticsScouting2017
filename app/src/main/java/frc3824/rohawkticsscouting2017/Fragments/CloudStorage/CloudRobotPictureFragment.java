package frc3824.rohawkticsscouting2017.Fragments.CloudStorage;

import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_CloudImage;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.CloudImage;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TeamPitData;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.UploadableImage;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/15/16
 *
 * Fragment that handles uploading/downloading robot images to google cloud storage
 */
public class CloudRobotPictureFragment extends CloudImageFragment{

    private final static String TAG = "CloudRobotPicture";

    public void setupLva(ListView listView, boolean internet){
        ArrayList<Integer> teams = mDatabase.getTeamNumbers();
        getCloudImages(teams, internet);

        mPictureType = Constants.Cloud.ROBOT_PICTURE;

        LVA_CloudImage lva = new LVA_CloudImage(mContext, mCIs, mPictureType);
        listView.setAdapter(lva);
    }

    protected void getCloudImages(ArrayList<Integer> teams, boolean internet) {
        mCIs = new ArrayList<>();
        for(int team_number: teams) {
            TeamPitData teamPitData = mDatabase.getTeamPitData(team_number);
            if(teamPitData == null)
                continue;
            for(UploadableImage image: teamPitData.robot_pictures) {
                CloudImage ci = new CloudImage();

                ci.extra = String.valueOf(team_number);
                ci.internet = internet;

                if (new File(image.filepath).exists()) {
                    ci.local = true;
                }
                ci.filepath = image.filepath;

                ci.remote = image.remote;

                mCIs.add(ci);
            }
        }
    }

    protected void setupNotifications(){
        mUploadNotificationId = Constants.Notifications.UPLOAD_ROBOT_PICTURES;
        mDownloadNotificationId = Constants.Notifications.DOWNLOAD_ROBOT_PICTURES;

        mUploadNotificationBuilder.setContentTitle("Uploading Robot Pictures");
        mDownloadNotificationBuilder.setContentTitle("Downloading Robot Pictures");
    }
}
