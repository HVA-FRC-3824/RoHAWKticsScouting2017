package frc3824.rohawkticsscouting2017.Fragments.CloudStorage;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_CloudImage;
import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.CloudImage;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Firebase.Storage;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 10/8/16
 */
public class CloudImageFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "CloudImageFragment";

    //region variables
    protected Context mContext;
    protected ArrayList<CloudImage> mCIs;
    private Storage mStorage;
    protected Database mDatabase;

    private ProgressBar mUploadAllProgressBar;
    private ProgressBar mDownloadAllProgressBar;

    private TextView mUploadAllMessage;
    private TextView mDownloadAllMessage;

    private NotificationManager mNotificationManager;
    protected Notification.Builder mUploadNotificationBuilder;
    protected int mUploadNotificationId;
    protected Notification.Builder mDownloadNotificationBuilder;
    protected int mDownloadNotificationId;

    protected int mPictureType;

    private int green;
    private int red;
    //endregion

    public  CloudImageFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cloud_robot_picture, container, false);

        mContext = getContext();

        green = Color.rgb(0, 153, 0);
        red = Color.RED;

        ListView listView = (ListView)view.findViewById(R.id.file_list);

        mDatabase = Database.getInstance();
        mStorage = Storage.getInstance();

        boolean internet = true;
        if(isNetworkAvailable()) {
            view.findViewById(R.id.upload_all).setOnClickListener(this);
            view.findViewById(R.id.download_all).setOnClickListener(this);
        } else {
            view.findViewById(R.id.upload_all).setEnabled(false);
            view.findViewById(R.id.download_all).setEnabled(false);
            internet = false;
        }

        setupLva(listView, internet);

        view.findViewById(R.id.upload_all).setOnClickListener(this);
        view.findViewById(R.id.download_all).setOnClickListener(this);

        mUploadAllProgressBar = (ProgressBar)view.findViewById(R.id.upload_all_progress_bar);
        mDownloadAllProgressBar = (ProgressBar)view.findViewById(R.id.download_all_progress_bar);
        mUploadAllMessage = (TextView)view.findViewById(R.id.upload_all_message);
        mDownloadAllMessage = (TextView)view.findViewById(R.id.download_all_message);


        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mUploadNotificationBuilder = new Notification.Builder(mContext);
        mUploadNotificationBuilder.setSmallIcon(R.drawable.logo);

        mDownloadNotificationBuilder = new Notification.Builder(mContext);
        mDownloadNotificationBuilder.setSmallIcon(R.drawable.logo);

        setupNotifications();


        return view;
    }

    protected void setupNotifications(){}

    protected void setupLva(ListView listView, boolean internet){}

    private void upload_next(final int i) {
        if(i == mCIs.size()) {
            mUploadAllProgressBar.setVisibility(View.GONE);
            mUploadAllProgressBar.setProgress(0);
            mUploadAllMessage.setText("Upload All Success");
            mUploadAllMessage.setTextColor(green);
            mUploadAllMessage.setVisibility(View.VISIBLE);
            mUploadNotificationBuilder.setContentText("Upload Complete");
            mUploadNotificationBuilder.setProgress(0, 0, false);
            mNotificationManager.notify(mUploadNotificationId, mUploadNotificationBuilder.build());
            return;
        }

        CloudImage cloudImage = mCIs.get(i);
        if(cloudImage.local && cloudImage.internet) {

            UploadTask uploadTask;
            if(mPictureType == Constants.Cloud.STRATEGY) {
                uploadTask = mStorage.uploadStrategyPicture(cloudImage.filepath);
            } else if(mPictureType == Constants.Cloud.ROBOT_PICTURE){
                uploadTask = mStorage.uploadRobotPicture(cloudImage.filepath);
            } else {
                Log.e(TAG, "Unknown image type");
                return;
            }
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "Upload Success");
                    mUploadAllProgressBar.setProgress(i + 1);
                    mUploadNotificationBuilder.setProgress(mCIs.size(), i + 1, false);
                    mNotificationManager.notify(mUploadNotificationId, mUploadNotificationBuilder.build());
                    upload_next(i + 1);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Upload Failure");
                    mUploadAllMessage.setText("Upload Failure");
                    mUploadAllMessage.setTextColor(red);
                    mUploadAllMessage.setVisibility(View.VISIBLE);
                }
            });
        } else {
            mUploadAllProgressBar.setProgress(i + 1);
            mUploadNotificationBuilder.setProgress(mCIs.size(), i + 1, false);
            mNotificationManager.notify(mUploadNotificationId, mUploadNotificationBuilder.build());
            upload_next(i + 1);
        }
    }

    private void download_next(final int i) {
        if(i == mCIs.size()) {
            mDownloadAllProgressBar.setVisibility(View.GONE);
            mDownloadAllProgressBar.setProgress(0);
            mDownloadAllMessage.setText("Download All Success");
            mDownloadAllMessage.setTextColor(green);
            mDownloadAllMessage.setVisibility(View.VISIBLE);
            mDownloadNotificationBuilder.setContentText("Download Complete");
            mDownloadNotificationBuilder.setProgress(0, 0, false);
            mNotificationManager.notify(mDownloadNotificationId, mDownloadNotificationBuilder.build());
            return;
        }

        CloudImage cloudImage = mCIs.get(i);
        if(cloudImage.remote && cloudImage.internet) {

            FileDownloadTask fileDownloadTask;
            if(mPictureType == Constants.Cloud.STRATEGY) {
                fileDownloadTask = mStorage.downloadStrategyPicture(cloudImage.extra, cloudImage.filepath);
            } else if(mPictureType == Constants.Cloud.ROBOT_PICTURE){
                fileDownloadTask = mStorage.downloadRobotPicture(cloudImage.filepath);
            } else {
                Log.e(TAG, "Unknown image type");
                return;
            }
            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d(TAG, "Download Success");
                    mDownloadAllProgressBar.setProgress(i + 1);
                    mDownloadNotificationBuilder.setProgress(mCIs.size(), i + 1, false);
                    mNotificationManager.notify(mDownloadNotificationId, mDownloadNotificationBuilder.build());
                    download_next(i + 1);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Download Failure");
                    mDownloadAllMessage.setText("Download Failure");
                    mDownloadAllMessage.setTextColor(red);
                    mDownloadAllMessage.setVisibility(View.VISIBLE);
                }
            });
        } else {
            mDownloadAllProgressBar.setProgress(i + 1);
            mDownloadNotificationBuilder.setProgress(mCIs.size(), i + 1, false);
            mNotificationManager.notify(mDownloadNotificationId, mDownloadNotificationBuilder.build());
            download_next(i + 1);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.upload_all:
                mUploadAllMessage.setVisibility(View.GONE);
                mUploadAllProgressBar.setProgress(0);
                mUploadAllProgressBar.setVisibility(View.VISIBLE);
                mUploadAllProgressBar.setMax(mCIs.size());
                mUploadNotificationBuilder.setContentText("Upload in progress");
                mUploadNotificationBuilder.setProgress(mCIs.size(), 0, true);
                mNotificationManager.notify(mUploadNotificationId, mUploadNotificationBuilder.build());
                upload_next(0);
                break;
            case R.id.download_all:
                mDownloadAllMessage.setVisibility(View.GONE);
                mDownloadAllProgressBar.setProgress(0);
                mDownloadAllProgressBar.setVisibility(View.VISIBLE);
                mDownloadAllProgressBar.setMax(mCIs.size());
                mDownloadNotificationBuilder.setContentText("Download in progress");
                mDownloadNotificationBuilder.setProgress(mCIs.size(), 0, true);
                mNotificationManager.notify(mDownloadNotificationId, mDownloadNotificationBuilder.build());
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
