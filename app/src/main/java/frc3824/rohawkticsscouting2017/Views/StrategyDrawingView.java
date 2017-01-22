package frc3824.rohawkticsscouting2017.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;

/**
 * @author frc3824
 * Created: 8/20/16
 *
 * Paint like view to draw on a background
 */
public class StrategyDrawingView extends View {

    private final static String TAG = "StrategyDrawingView";

    //region variables
    private int mScreenWidth, mScreenHeight;

    private Path mDrawPath;
    private Paint mDrawPaint, mCanvasPaint;
    private int mPaintColor = 0xFF000000;
    private Canvas mDrawCanvas;
    private Canvas mTeamsCanvas;
    private Bitmap mBackgroundBitmap;
    private Bitmap mCanvasBitmap;
    private Bitmap mTeamsBitmap;
    static private Bitmap mFieldBitmap;
    private float mBrushSize, mLastBrushSize;

    private boolean mErase;

    private JSONArray mPaths;
    private JSONObject mColorPath;
    private JSONArray mPath;

    private boolean mTeamsMarked;
    private Integer[] mTeamNumbers;
    private Point[] mTeamButtonPositions;
    private float mTeamButtonRadius;

    private boolean mColorSelected;
    //endregion

    public StrategyDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrawPath = new Path();
        mDrawPaint = new Paint();

        mBrushSize = getResources().getInteger(R.integer.extra_small_size);
        mLastBrushSize = mBrushSize;

        mDrawPaint.setColor(mPaintColor);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(mBrushSize);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        mCanvasPaint = new Paint(Paint.DITHER_FLAG);
        mFieldBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.field_top_down);

        mErase = false;
        mPaths = new JSONArray();

        mTeamsMarked = false;
        mColorSelected = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldH, oldW);

        mScreenWidth = w;
        mScreenHeight = h;

        mBackgroundBitmap = Bitmap.createScaledBitmap(mFieldBitmap, w, h, false);
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mDrawCanvas = new Canvas(mCanvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBackgroundBitmap, 0, 0, mCanvasPaint);
        canvas.drawBitmap(mCanvasBitmap, 0, 0, mCanvasPaint);
        if(mTeamsMarked) {
            canvas.drawBitmap(mTeamsBitmap, 0, 0, mCanvasPaint);
        }
        canvas.drawPath(mDrawPath, mDrawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mColorPath = new JSONObject();
                try {
                    mColorPath.put("paint_color", mPaintColor);
                    mColorPath.put("brush_size", mBrushSize);
                    mColorPath.put("mErase", mErase);
                    mPath = new JSONArray();
                    JSONObject point = new JSONObject();
                    point.put("x", touchX);
                    point.put("y", touchY);
                    mPath.put(point);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                mDrawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                if(mColorSelected){
                    return true;
                }

                try {
                    JSONObject point = new JSONObject();
                    point.put("x", touchX);
                    point.put("y", touchY);
                    mPath.put(point);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDrawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                if(mColorSelected){
                    mColorSelected = false;
                    return true;
                }

                try {
                    mColorPath.put("mPath", mPath);
                    mPaths.put(mColorPath);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDrawCanvas.drawPath(mDrawPath, mDrawPaint);
                mDrawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setColor(int newColor){
        invalidate();
        mPaintColor = newColor;
        mDrawPaint.setColor(mPaintColor);
    }

    public void setColor(String newColor) {
        invalidate();
        mPaintColor = Color.parseColor(newColor);
        mDrawPaint.setColor(mPaintColor);
    }

    public void setBrushSize(float newSize) {
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        mDrawPaint.setStrokeWidth(mBrushSize);
    }

    public void setLastBrushSize(float lastSize){
        mLastBrushSize = lastSize;
    }

    public float getLastBrushSize() {
        return mLastBrushSize;
    }

    public void setErase(boolean isErase) {
        mErase = isErase;
        if(isErase) {
            mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            mDrawPaint.setXfermode(null);
        }
    }

    public void startNew(){
        if(mScreenHeight == 0 || mScreenWidth == 0){
            return;
        }
        mCanvasBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mDrawCanvas = new Canvas(mCanvasBitmap);
        invalidate();
    }

    public void load(Bitmap bitmap) {
        mCanvasBitmap = bitmap;
        mDrawCanvas = new Canvas(mCanvasBitmap);
        invalidate();
    }

    public void load(String json){
        startNew();
        if(json.isEmpty()){
            mPaths = new JSONArray();
            return;
        }
        try {
            mPaths = new JSONArray(json);
            for(int i = 0; i < mPaths.length(); i++){
                mColorPath = mPaths.getJSONObject(i);
                mBrushSize = (float) mColorPath.getDouble("brush_size");
                mDrawPaint.setStrokeWidth(mBrushSize);
                mPaintColor = mColorPath.getInt("paint_color");
                mDrawPaint.setColor(mPaintColor);
                mPath = mColorPath.getJSONArray("mPath");
                for(int j = 0; j < mPath.length(); j++)
                {
                    JSONObject point = mPath.getJSONObject(j);
                    float x = (float)point.getDouble("x");
                    float y = (float)point.getDouble("y");
                    if(j == 0) {
                        mDrawPath.moveTo(x, y);
                    } else {
                        mDrawPath.lineTo(x, y);
                    }
                }
                mDrawCanvas.drawPath(mDrawPath, mDrawPaint);
                mDrawPath.reset();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toJson()
    {
        return mPaths.toString();
    }
}
