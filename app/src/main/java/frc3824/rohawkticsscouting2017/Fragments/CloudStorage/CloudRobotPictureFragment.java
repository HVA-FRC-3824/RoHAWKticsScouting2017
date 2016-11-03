package frc3824.rohawkticsscouting2017.Fragments.CloudStorage;

import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_CloudImage;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.CloudImage;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPD;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

import static frc3824.rohawkticsscouting2017.Utilities.Constants.Notifications.DOWNLOAD_ROBOT_PICTURES;

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

        LVA_CloudImage lva = new LVA_CloudImage(mContext, mCIs, Constants.Cloud.ROBOT_PICTURE);
        listView.setAdapter(lva);
    }

    protected void getCloudImages(ArrayList<Integer> teams, boolean internet) {
        mCIs = new ArrayList<>();
        for(int team_number: teams) {
            TPD tpd = mDatabase.getTPD(team_number);
            if(tpd == null)
                continue;
            for(int i = 0; i < tpd.robot_image_filepaths.size(); i++) {
                CloudImage ci = new CloudImage();

                ci.extra = String.valueOf(team_number);
                ci.internet = internet;

                if (tpd.robot_image_filepaths != null && tpd.robot_image_filepaths.size() != 0) {

                    if (new File(tpd.robot_image_filepaths.get(i)).exists()) {
                        ci.local = true;
                    }
                    ci.filepath = tpd.robot_image_filepaths.get(i);
                }

                if (tpd.robot_image_urls != null && tpd.robot_image_urls.size() != 0) {
                    if(!tpd.robot_image_urls.get(i).equals("")) {
                        ci.remote = true;
                    }
                    ci.url = tpd.robot_image_urls.get(i);
                }

                mCIs.add(ci);
            }
        }
    }

    protected void setupNotifications(){
        mUploadNotificationId = Constants.Notifications.UPLOAD_ROBOT_PICTURES;
        mDownloadNotificationId = DOWNLOAD_ROBOT_PICTURES;

        mUploadNotificationBuilder.setContentTitle("Uploading Robot Pictures");
        mDownloadNotificationBuilder.setContentTitle("Downloading Robot Pictures");
    }
}
