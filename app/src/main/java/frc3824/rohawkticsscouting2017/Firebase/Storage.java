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
 *         Created: 8/15/16
 */
public class Storage {

    private final static String TAG = "Storage";

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mRootRef;
    private StorageReference mEventRef;
    private StorageReference mImagesRef;
    private StorageReference mRobotImagesRef;
    private StorageReference mStrategyImagesRef;
    private StorageReference mPickListRef;

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

        mEventRef = mRootRef.child(eventKey);
        mImagesRef = mEventRef.child("images");
        mRobotImagesRef = mImagesRef.child("robots");
        mStrategyImagesRef = mImagesRef.child("strategies");
        mPickListRef = mEventRef.child("pick_lists");
    }

    public UploadTask uploadRobotPicture(String filepath) {
        return uploadRobotPicture(new File(filepath));
    }

    public UploadTask uploadRobotPicture(File file) {
        Uri uri = Uri.fromFile(file);
        StorageReference fileRef = mRobotImagesRef.child(uri.getLastPathSegment());
        return fileRef.putFile(uri);
    }

    public FileDownloadTask downloadRobotPicture(int team_number, String filepath) {
        return downloadRobotPicture(team_number, new File(filepath));
    }

    public FileDownloadTask downloadRobotPicture(int team_number, File file) {
        File parent = file.getParentFile();
        if (!parent.exists()) {
            boolean madeParent = parent.mkdirs();
            Log.d(TAG, String.valueOf(madeParent));
        }

        return mRobotImagesRef.child(String.format("%d.jpg", team_number)).getFile(file);
    }


    public UploadTask uploadStrategyPicture(String filepath) {
        return uploadStrategyPicture(new File(filepath));
    }

    public UploadTask uploadStrategyPicture(File file) {
        Uri uri = Uri.fromFile(file);
        StorageReference fileRef = mStrategyImagesRef.child(uri.getLastPathSegment());
        return fileRef.putFile(uri);
    }

    public UploadTask uploadPickList(String filepath) {
        return uploadPickList(new File(filepath));
    }

    public UploadTask uploadPickList(File file) {
        Uri uri = Uri.fromFile(file);
        StorageReference fileRef = mPickListRef.child(uri.getLastPathSegment());
        return fileRef.putFile(uri);
    }

}
