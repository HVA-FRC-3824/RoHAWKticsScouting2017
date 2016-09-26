package frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.ListItemModels.CloudImage;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.TPD;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.Firebase.Storage;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author Andrew Messing
 * Created: 8/15/16
 *
 *
 */
public class LVA_CloudImage extends ArrayAdapter<CloudImage> {

    private final static String TAG = "LVA_CloudImage";

    private ArrayList<CloudImage> mCloudFiles;
    private Context mContext;
    private Storage mStorage;
    private Database mDatabase;
    private int mImageType;

    public LVA_CloudImage(Context context, ArrayList<CloudImage> objects, int imageType) {
        super(context, R.layout.list_item_cloud_image, objects);
        mCloudFiles = objects;
        mContext = context;
        mStorage = Storage.getInstance();
        mDatabase = Database.getInstance();
        mImageType = imageType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_cloud_image, null);
        }

        final CloudImage ci = mCloudFiles.get(position);
        Button upload = (Button) convertView.findViewById(R.id.upload);
        Button download = (Button) convertView.findViewById(R.id.download);
        final ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView filepath = (TextView) convertView.findViewById(R.id.filename);
        String filename;
        if (ci.filepath != null) {
            filename = ci.filepath.substring(ci.filepath.lastIndexOf('/') + 1);
        } else {

            if (ci.remote) {
                filename = String.format("%s: Download required", ci.extra);
            } else {
                filename = String.format("%s: No Image", ci.extra);
            }
        }
        filepath.setText(filename);

        final TextView message = (TextView) convertView.findViewById(R.id.message);
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar);

        if (ci.local) {
            if (ci.internet) {
                upload.setEnabled(true);
                upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UploadTask uploadTask = null;
                        switch(mImageType)
                        {
                            case Constants.Cloud.ROBOT_PICTURE:
                                uploadTask = mStorage.uploadRobotPicture(ci.filepath);
                                break;
                            case Constants.Cloud.STRATEGY:
                                uploadTask = mStorage.uploadStrategyPicture(ci.filepath);
                                break;
                        }

                        progressBar.setVisibility(View.VISIBLE);
                        message.setVisibility(View.GONE);
                        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (double) taskSnapshot.getBytesTransferred() * 100 / (double) taskSnapshot.getTotalByteCount();
                                int progress_int = (int) progress;
                                progressBar.setProgress(progress_int);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Upload failure");
                                message.setText("Upload failure");
                                message.setTextColor(Color.RED);
                                message.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d(TAG, "Upload success");
                                message.setText("Upload success");
                                message.setTextColor(Color.GREEN);
                                message.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);

                                ci.remote = true;
                                ci.url = taskSnapshot.getDownloadUrl().getPath();

                                switch (mImageType) {
                                    case Constants.Cloud.ROBOT_PICTURE:
                                        TPD t = mDatabase.getTPD(Integer.parseInt(ci.extra));
                                        t.robot_image_url = ci.url;
                                        mDatabase.setTPD(t);
                                        break;
                                    case Constants.Cloud.STRATEGY:
                                        Strategy s = mDatabase.getStrategy(ci.extra);
                                        s.url = ci.url;
                                        mDatabase.setStrategy(s);
                                        break;
                                }

                                Log.d(TAG, ci.url);
                                notifyDataSetChanged();
                            }
                        });
                    }
                });
            } else {
                upload.setEnabled(false);
            }
            displayPicture(image, ci.filepath);
        } else {
            upload.setEnabled(false);
            image.setImageResource(android.R.color.transparent);
        }

        if (ci.remote && ci.internet) {
            download.setEnabled(true);
            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FileDownloadTask fileDownloadTask = null;
                    switch (mImageType)
                    {
                        case Constants.Cloud.ROBOT_PICTURE:
                            fileDownloadTask = mStorage.downloadRobotPicture(Integer.parseInt(ci.extra), ci.filepath);
                            break;
                        case Constants.Cloud.STRATEGY:
                            fileDownloadTask = mStorage.downloadStrategyPicture(ci.extra, ci.filepath);
                            break;
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    message.setVisibility(View.GONE);
                    fileDownloadTask.addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            double progress = (double) taskSnapshot.getBytesTransferred() * 100 / (double) taskSnapshot.getTotalByteCount();
                            int progress_int = (int) progress;
                            progressBar.setProgress(progress_int);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Download failure");
                            Log.e(TAG, e.getMessage());
                            message.setText("Download failure");
                            message.setTextColor(Color.RED);
                            message.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "Download success");
                            message.setText("Download success");
                            message.setTextColor(Color.GREEN);
                            message.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);

                            ci.local = true;
                            // The local filepath should have been set by the database

                            notifyDataSetChanged();
                        }
                    });
                }
            });
        } else {
            download.setEnabled(false);
        }

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
