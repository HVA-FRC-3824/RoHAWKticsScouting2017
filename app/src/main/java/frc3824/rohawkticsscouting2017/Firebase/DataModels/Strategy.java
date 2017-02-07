package frc3824.rohawkticsscouting2017.Firebase.DataModels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import frc3824.rohawkticsscouting2017.R;

/**
 * @author frc3824
 * Created: 8/20/16
 *
 * Class to hold the info for a strategy image
 */
@IgnoreExtraProperties
public class Strategy {

    private final static String TAG = "Strategy";

    public long last_modified;
    public int width;
    public int height;
    public String name;
    public String filepath;
    public String url;
    public String notes;
    public String path_json; // For easy transfer during competition

    public Strategy() {}

    public void create(Context context){
        File file = new File(filepath);
        if(!file.exists() || file.lastModified() < last_modified){
            Bitmap fieldBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.field_top_down);
            Bitmap backgroundBitmap = Bitmap.createScaledBitmap(fieldBitmap, width, height, false);
            Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas drawCanvas = new Canvas(canvasBitmap);
            Paint drawPaint = new Paint();
            drawPaint.setAntiAlias(true);
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setStrokeJoin(Paint.Join.ROUND);
            drawPaint.setStrokeCap(Paint.Cap.ROUND);
            Path drawPath = new Path();
            try {
                JSONArray paths = new JSONArray(path_json);
                for(int i = 0; i < paths.length(); i++){
                    JSONObject colorPath = paths.getJSONObject(i);
                    float brushSize = (float)colorPath.getDouble("brush_size");
                    drawPaint.setStrokeWidth(brushSize);
                    int paintColor = colorPath.getInt("paint_color");
                    drawPaint.setColor(paintColor);
                    Boolean isErase = colorPath.getBoolean("erase");
                    if(isErase) {
                        drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    } else {
                        drawPaint.setXfermode(null);
                    }
                    JSONArray path = colorPath.getJSONArray("path");
                    for(int j = 0; j < path.length(); j++)
                    {
                        JSONObject point = path.getJSONObject(j);
                        float x = (float)point.getDouble("x");
                        float y = (float)point.getDouble("y");
                        drawPath.moveTo(x, y);
                    }
                    drawPaint.setColor(paintColor);
                    drawCanvas.drawPath(drawPath, drawPaint);
                    drawPath.reset();
                }
                Bitmap saveImage = Bitmap.createBitmap(backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(saveImage);
                Paint canvasPaint = new Paint(Paint.DITHER_FLAG);
                canvas.drawBitmap(backgroundBitmap, 0, 0, canvasPaint);
                canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);

                String imageName = name + ".png";
                FileOutputStream fos = null;
                try {
                    fos = context.openFileOutput(imageName, Context.MODE_WORLD_WRITEABLE);
                    saveImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (FileNotFoundException e) {
                    Log.d(TAG, e.getMessage());
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        Log.d(TAG, e.getMessage());
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
