package frc3824.rohawkticsscouting2017.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import frc3824.rohawkticsscouting2017.Adapters.ListViewAdapters.LVA_Strategies;
import frc3824.rohawkticsscouting2017.Firebase.DataModels.Strategy;
import frc3824.rohawkticsscouting2017.Firebase.Database;
import frc3824.rohawkticsscouting2017.R;
import frc3824.rohawkticsscouting2017.Utilities.Constants;
import frc3824.rohawkticsscouting2017.Views.StrategyDrawingView;

/**
 * @author frc3824
 * Created: 8/20/16
 *
 * Activity for drawing an individual plan
 */
public class StrategyPlanning extends Activity implements View.OnClickListener{

    // Shortened due to TAG length limit for logging
    private final static String TAG = "IndividualStrategyPlan";

    private StrategyDrawingView mDrawingView;
    private ImageButton mCurrentPaint;
    private float mExtraSmallBrush, mSmallBrush, mMediumBrush, mLargeBrush;
    private String mCurrentStrategyName;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        mDrawingView = (StrategyDrawingView) findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        mCurrentPaint = (ImageButton) paintLayout.getChildAt(5); //black
        mCurrentPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed, null));

        mExtraSmallBrush = 5;
        mSmallBrush = 10;
        mMediumBrush = 20;
        mLargeBrush = 30;

        findViewById(R.id.draw_btn).setOnClickListener(this);
        findViewById(R.id.erase_btn).setOnClickListener(this);
        findViewById(R.id.new_btn).setOnClickListener(this);
        findViewById(R.id.save_btn).setOnClickListener(this);
        findViewById(R.id.open_btn).setOnClickListener(this);

        mDrawingView.setBrushSize(mExtraSmallBrush);
    }

    /**
     *  Changes the color for the Draw View and sets the color button to pressed
     *
     * @param view
     */
    public void paintClicked(View view) {
        mDrawingView.setErase(false);
        mDrawingView.setBrushSize(mDrawingView.getLastBrushSize());
        if (view != mCurrentPaint) {
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();
            mDrawingView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed, null));
            mCurrentPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint, null));
            mCurrentPaint = imgView;
        }
    }

    /**
     *  Implements the actions for the various buttons
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.draw_btn:
                drawButtonClicked();
                break;
            case R.id.erase_btn:
                eraseButtonClicked();
                break;
            case R.id.new_btn:
                newButtonClicked();
                break;
            case R.id.save_btn:
                saveButtonClicked();
                break;
            case R.id.open_btn:
                openButtonClicked();
                break;

        }
    }

    /**
     *
     */
    private void drawButtonClicked() {
        mDrawingView.setErase(false);
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Brush size:");
        brushDialog.setContentView(R.layout.dialog_brush_chooser);

        // extra small button sets the size of the brush to extra small
        ImageButton extraSmallBtn = (ImageButton) brushDialog.findViewById(R.id.extra_small_brush);
        extraSmallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setBrushSize(mExtraSmallBrush);
                mDrawingView.setLastBrushSize(mExtraSmallBrush);
                brushDialog.dismiss();
            }
        });

        // small button sets the size of the brush to small
        ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setBrushSize(mSmallBrush);
                mDrawingView.setLastBrushSize(mSmallBrush);
                brushDialog.dismiss();
            }
        });

        // medium button sets the size of the brush to medium
        ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setBrushSize(mMediumBrush);
                mDrawingView.setLastBrushSize(mMediumBrush);
                brushDialog.dismiss();
            }
        });

        // large button sets the size of the brush to large
        ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setBrushSize(mLargeBrush);
                mDrawingView.setLastBrushSize(mLargeBrush);
                brushDialog.dismiss();
            }
        });
        brushDialog.show();
    }

    /**
     *
     */
    private void eraseButtonClicked() {
        final Dialog eraser_brushDialog = new Dialog(this);
        eraser_brushDialog.setTitle("Eraser size:");
        eraser_brushDialog.setContentView(R.layout.dialog_brush_chooser);

        // extra small button sets the size of the eraser brush to extra small
        ImageButton eraser_extraSmallBtn = (ImageButton) eraser_brushDialog.findViewById(R.id.extra_small_brush);
        eraser_extraSmallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(mExtraSmallBrush);
                eraser_brushDialog.dismiss();
            }
        });

        // small button sets the size of the eraser brush to small
        ImageButton eraser_smallBtn = (ImageButton) eraser_brushDialog.findViewById(R.id.small_brush);
        eraser_smallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(mSmallBrush);
                eraser_brushDialog.dismiss();
            }
        });

        // medium button sets the size of the eraser brush to medium
        ImageButton eraser_mediumBtn = (ImageButton) eraser_brushDialog.findViewById(R.id.medium_brush);
        eraser_mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(mMediumBrush);
                eraser_brushDialog.dismiss();
            }
        });

        // large button sets the size of the eraser brush to large
        ImageButton eraser_largeBtn = (ImageButton) eraser_brushDialog.findViewById(R.id.large_brush);
        eraser_largeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawingView.setErase(true);
                mDrawingView.setBrushSize(mLargeBrush);
                eraser_brushDialog.dismiss();
            }
        });
        eraser_brushDialog.show();
    }

    /**
     *
     */
    private void newButtonClicked() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("New drawing");
        newDialog.setMessage("Start new strategy (you will lose the current strategy)?");

        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mDrawingView.startNew();
                dialog.dismiss();
            }
        });

        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        newDialog.show();
    }

    /**
     *
     */
    private void saveButtonClicked() {
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save strategy");
        LayoutInflater inflater = this.getLayoutInflater();
        final View saveView = inflater.inflate(R.layout.dialog_save_strategy,null);
        if(mCurrentStrategyName != null && mCurrentStrategyName != "")
        {
            EditText editText = (EditText)saveView.findViewById(R.id.strategy_name);
            editText.setText(mCurrentStrategyName);
        }

        saveDialog.setView(saveView);
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mDrawingView.setDrawingCacheEnabled(true);
                Bitmap saveImage = mDrawingView.getDrawingCache();
                EditText editText = (EditText)saveView.findViewById(R.id.strategy_name);

                String imageName = editText.getText().toString();
                Strategy strategy = new Strategy();
                if(imageName == "")
                {
                    mDrawingView.destroyDrawingCache();
                    dialog.cancel();
                    return;
                }
                strategy.name = imageName;
                imageName += ".png";
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(imageName, Context.MODE_WORLD_WRITEABLE);
                    saveImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    strategy.last_modified = System.currentTimeMillis();
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

                File saveFile = new File(StrategyPlanning.this.getFilesDir(), imageName);
                if(saveFile.exists()) {
                    strategy.filepath = saveFile.getAbsolutePath();
                    Database.getInstance().setStrategy(strategy);
                }

                mDrawingView.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

    /**
     *
     */
    private void openButtonClicked() {
        AlertDialog.Builder openDialog = new AlertDialog.Builder(this);
        openDialog.setTitle("Load strategy");
        final ArrayList<Strategy> mStrategies = Database.getInstance().getAllStrategies();
        LVA_Strategies lva = new LVA_Strategies(this, mStrategies);
        openDialog.setAdapter(lva, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Strategy strategy = mStrategies.get(i);
                String imageName = strategy.name + ".png";
                Log.d(TAG, imageName);
                try {
                    Bitmap loadImage = BitmapFactory.decodeStream(openFileInput(imageName));
                    mDrawingView.load(loadImage.copy(Bitmap.Config.ARGB_8888, true));
                    mCurrentStrategyName = strategy.name;
                } catch (FileNotFoundException ex) {
                    Log.e(TAG, "Clicked on a strategy that is not local");
                }
                dialog.cancel();
            }
        });

        openDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        openDialog.show();
    }
}
