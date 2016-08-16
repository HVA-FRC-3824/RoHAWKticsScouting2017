package frc3824.rohawkticsscouting2017.Fragments.CloudStorage;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
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

    private final static String TAG = "CloudRobotPicture";

    private Context mContext;
    private ArrayList<CloudImage> mCIs;
    private Storage mStorage;
    private Database mDatabase;

    private ProgressBar mUploadAllProgressBar;
    private ProgressBar mDownloadAllProgressBar;

    public CloudRobotPictureFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_cloud_robot_picture, container, false);

        mContext = getContext();

        ListView listView = (ListView)view.findViewById(R.id.file_list);

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.APP_DATA, Context.MODE_PRIVATE);
        String eventKey = sharedPreferences.getString(Constants.Settings.EVENT_KEY, "");
        mDatabase = Database.getInstance(eventKey);
        mStorage = Storage.getInstance(eventKey);
        ArrayList<Team> teams = mDatabase.getTeams();

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

        mCIs = new ArrayList<>();
        for(Team team: teams)
        {
            CloudImage ci = new CloudImage();

            ci.team_number = team.team_number;
            ci.internet = internet;

            if(team.robot_image_filepath != null && !team.robot_image_filepath.equals(""))
            {
                if(new File(team.robot_image_filepath).exists()) {
                    ci.local = true;
                }
                ci.filepath = team.robot_image_filepath;
            }

            if(team.robot_image_url != null && !team.robot_image_url.equals(""))
            {
                ci.remote = true;
                ci.url = team.robot_image_url;
            }

            mCIs.add(ci);
        }

        LVA_CloudImage lva = new LVA_CloudImage(mContext, R.layout.list_item_cloud_image, mCIs,
                mStorage, mDatabase, Constants.Cloud.ROBOT_PICTURE);
        listView.setAdapter(lva);

        view.findViewById(R.id.upload_all).setOnClickListener(this);
        view.findViewById(R.id.download_all).setOnClickListener(this);

        mUploadAllProgressBar = (ProgressBar)view.findViewById(R.id.upload_all_progress_bar);
        mDownloadAllProgressBar = (ProgressBar)view.findViewById(R.id.download_all_progress_bar);

        return view;
    }

    private void upload_next(final int i)
    {
        if(i == mCIs.size())
        {
            mUploadAllProgressBar.setVisibility(View.GONE);
            mUploadAllProgressBar.setProgress(0);
            return;
        }

        CloudImage cloudImage = mCIs.get(i);
        if(cloudImage.local && cloudImage.internet)
        {
            UploadTask uploadTask = mStorage.uploadRobotPicture(cloudImage.filepath);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "Upload Success");
                    mUploadAllProgressBar.setProgress(i + 1);
                    upload_next(i + 1);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Upload Failure");
                    // Don't want a infinite loop
                }
            });
        }
        else
        {
            mUploadAllProgressBar.setProgress(i + 1);
            upload_next(i + 1);
        }
    }

    private void download_next(final int i)
    {
        if(i == mCIs.size())
        {
            mDownloadAllProgressBar.setVisibility(View.GONE);
            mDownloadAllProgressBar.setProgress(0);
            return;
        }

        CloudImage cloudImage = mCIs.get(i);
        if(cloudImage.remote && cloudImage.internet)
        {
            FileDownloadTask fileDownloadTask = mStorage.downloadRobotPicture(cloudImage.team_number, cloudImage.filepath);
            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "Download Success");
                    mDownloadAllProgressBar.setProgress(i + 1);
                    download_next(i + 1);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Download Failure");

                }
            });
        }
        else
        {
            mDownloadAllProgressBar.setProgress(i + 1);
            download_next(i + 1);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.upload_all:
                mUploadAllProgressBar.setProgress(0);
                mUploadAllProgressBar.setVisibility(View.VISIBLE);
                mUploadAllProgressBar.setMax(mCIs.size());
                upload_next(0);
                break;
            case R.id.download_all:
                mDownloadAllProgressBar.setProgress(0);
                mDownloadAllProgressBar.setVisibility(View.VISIBLE);
                mDownloadAllProgressBar.setMax(mCIs.size());
                download_next(0);
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
