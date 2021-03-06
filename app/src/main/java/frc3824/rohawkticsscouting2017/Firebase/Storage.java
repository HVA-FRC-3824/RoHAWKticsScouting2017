package frc3824.rohawkticsscouting2017.Firebase;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

/**
 * @author frc3824
 * Created: 8/15/16
 *
 * Class to handle uploading/downloading to google cloud storage via firebase
 */
public class Storage {

    private final static String TAG = "Storage";

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mRootRef;
    private StorageReference mEventRef;
    private StorageReference mImagesRef;
    private StorageReference mRobotImagesRef;
    private StorageReference mStrategyImagesRef;

    private String mEventKey;

    private static Storage mSingleton;

    public static Storage getInstance(String eventKey) {
        if (mSingleton == null) {
            mSingleton = new Storage();
        }

        mSingleton.setEventKey(eventKey);

        return mSingleton;
    }

    public static Storage getInstance() {
        if (mSingleton == null) {
            mSingleton = new Storage();
        }

        return mSingleton;
    }

    private Storage() {
        mFirebaseStorage = FirebaseStorage.getInstance();
        mRootRef = mFirebaseStorage.getReferenceFromUrl("gs://rohawktics-scouting-2017.appspot.com");
    }

    private void setEventKey(String eventKey) {
        if (eventKey == "" || mEventKey == eventKey)
            return;

        mEventKey = eventKey;
        mEventRef = mRootRef.child(eventKey);
        mImagesRef = mEventRef.child("images");
        mRobotImagesRef = mImagesRef.child("robots");
        mStrategyImagesRef = mImagesRef.child("strategies");
    }

    //region Robot Picture
    public UploadTask uploadRobotPicture(String filepath) {
        return uploadRobotPicture(new File(filepath));
    }

    public UploadTask uploadRobotPicture(File file) {
        Uri uri = Uri.fromFile(file);
        StorageReference fileRef = mRobotImagesRef.child(uri.getLastPathSegment());
        return fileRef.putFile(uri);
    }

    public FileDownloadTask downloadRobotPicture(String filepath) {
        return downloadRobotPicture(new File(filepath));
    }

    public FileDownloadTask downloadRobotPicture(File file) {
        File parent = file.getParentFile();
        if (!parent.exists()) {
            boolean madeParent = parent.mkdirs();
            Log.d(TAG, String.valueOf(madeParent));
        }

        Uri uri = Uri.fromFile(file);
        StorageReference fileRef = mRobotImagesRef.child(uri.getLastPathSegment());

        return fileRef.getFile(uri);
    }
    //endregion

    //region Strategy Picture
    public UploadTask uploadStrategyPicture(String filepath) {
        return uploadStrategyPicture(new File(filepath));
    }

    public UploadTask uploadStrategyPicture(File file) {
        Uri uri = Uri.fromFile(file);
        StorageReference fileRef = mStrategyImagesRef.child(uri.getLastPathSegment());
        return fileRef.putFile(uri);
    }

    public FileDownloadTask downloadStrategyPicture(String strategy_name, String filepath) {
        return downloadStrategyPicture(strategy_name, new File(filepath));
    }

    public FileDownloadTask downloadStrategyPicture(String strategy_name, File file) {
        File parent = file.getParentFile();
        if (!parent.exists()) {
            boolean madeParent = parent.mkdirs();
            Log.d(TAG, String.valueOf(madeParent));
        }

        return mStrategyImagesRef.child(String.format("%s.png", strategy_name)).getFile(file);
    }
    //endregion Strategy Picture
}
