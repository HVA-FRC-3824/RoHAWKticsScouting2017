package frc3824.rohawkticsscouting2017.Fragments.CloudStorage;

import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_CloudImage;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.CloudImage;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/15/16
 *
 * Fragment that handles uploading/downloading strategy images to google cloud storage
 */
public class CloudStrategyFragment extends CloudImageFragment{

    private final static String TAG = "CloudStrategyFragment";

    protected void setupLva(ListView listView, boolean internet){
        ArrayList<Strategy> strategies = mDatabase.getStrategies();
        getCloudImages(strategies, internet);

        LVA_CloudImage lva = new LVA_CloudImage(mContext, mCIs, Constants.Cloud.STRATEGY);
        listView.setAdapter(lva);
    }

    protected void getCloudImages(ArrayList<Strategy> strategies, boolean internet) {
        mCIs = new ArrayList<>();
        for(Strategy strategy: strategies) {
            CloudImage ci = new CloudImage();

            ci.extra = strategy.name;
            ci.internet = internet;

            if(strategy.filepath != null && !strategy.filepath.equals("")) {
                if(new File(strategy.filepath).exists()) {
                    ci.local = true;
                }
                ci.filepath = strategy.filepath;
            }

            if(strategy.url != null && !strategy.url.equals("")) {
                ci.remote = true;
                ci.url = strategy.url;
            }

            mCIs.add(ci);
        }
    }

    protected void setupNotifications(){
        mUploadNotificationId = Constants.Notifications.UPLOAD_STRATEGIES;
        mDownloadNotificationId = Constants.Notifications.DOWNLOAD_STRATEGIES;

        mUploadNotificationBuilder.setContentTitle("Uploading Strategies");
        mDownloadNotificationBuilder.setContentTitle("Downloading Strategies");
    }
}
